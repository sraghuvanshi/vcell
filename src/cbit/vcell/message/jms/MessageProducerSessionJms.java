package cbit.vcell.message.jms;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TemporaryQueue;

import org.vcell.util.BeanUtils;
import org.vcell.util.PropertyLoader;

import cbit.vcell.message.VCMessage;
import cbit.vcell.message.VCMessageSession;
import cbit.vcell.message.VCMessagingException;
import cbit.vcell.message.VCMessagingInvocationTargetException;
import cbit.vcell.message.VCRpcRequest;
import cbit.vcell.message.VCellQueue;
import cbit.vcell.message.VCellTopic;
import cbit.vcell.message.messages.MessageConstants;
import cbit.vcell.mongodb.VCMongoMessage;

public class MessageProducerSessionJms implements VCMessageSession {
		
		private TemporaryQueue commonTemporaryQueue = null;
		private Connection connection = null;
		private Session session = null;
		protected boolean bIndependent;
		
		public MessageProducerSessionJms(VCMessagingServiceJms vcMessagingServiceJms) throws JMSException {
			this.connection = vcMessagingServiceJms.createConnectionFactory().createConnection();
			this.connection.setExceptionListener(new ExceptionListener() {
				public void onException(JMSException arg0) {
					MessageProducerSessionJms.this.onException(arg0);
				}
			});
			this.connection.start();
			boolean bTransacted = true;
			this.session = connection.createSession(bTransacted, Session.AUTO_ACKNOWLEDGE);
			this.commonTemporaryQueue = session.createTemporaryQueue();
			this.bIndependent = true;
		}

		public MessageProducerSessionJms(Session session) {
			this.session = session;
			this.bIndependent = false;
		}

		public Object sendRpcMessage(VCellQueue queue, VCRpcRequest vcRpcRequest, boolean returnRequired, long timeoutMS, String[] specialProperties, Object[] specialValues) throws VCMessagingException, VCMessagingInvocationTargetException {
			MessageProducer messageProducer = null;
			try {
				if (!bIndependent){
					throw new VCMessagingException("cannot invoke RpcMessage from within another transaction, create an independent message producer");
				}
				Destination destination = session.createQueue(queue.getName());
				messageProducer = session.createProducer(destination);				
				ObjectMessage rpcMessage = session.createObjectMessage(vcRpcRequest);
				rpcMessage.setStringProperty(MessageConstants.MESSAGE_TYPE_PROPERTY,MessageConstants.MESSAGE_TYPE_RPC_SERVICE_VALUE);
				rpcMessage.setStringProperty(MessageConstants.SERVICE_TYPE_PROPERTY,vcRpcRequest.getRequestedServiceType().getName());
				if (specialValues != null) {
					for (int i = 0; i < specialValues.length; i ++) {
						rpcMessage.setObjectProperty(specialProperties[i], specialValues[i]);
					}
				}

				if (returnRequired) {
					rpcMessage.setJMSReplyTo(commonTemporaryQueue);
					messageProducer.setTimeToLive(timeoutMS);
					messageProducer.send(rpcMessage);
					session.commit();
System.out.println("rpcMessage sent: id='"+rpcMessage.getJMSMessageID()+"'");
					String filter = MessageConstants.JMSCORRELATIONID_PROPERTY + "='" + rpcMessage.getJMSMessageID() + "'";
					MessageConsumer replyConsumer = session.createConsumer(commonTemporaryQueue,filter);
					Message replyMessage = replyConsumer.receive(timeoutMS);
					replyConsumer.close();
					if (replyMessage == null) {
						System.out.println("Request timed out");
					}

					if (replyMessage == null || !(replyMessage instanceof ObjectMessage)) {
						throw new JMSException("Server is temporarily not responding, please try again later. If problem persists, contact VCell_Support@uchc.edu." +
								" (server=" + vcRpcRequest.getRequestedServiceType().getName() + ", method=" + vcRpcRequest.getMethodName() +")");
					} else {
						VCMessageJms vcReplyMessage = new VCMessageJms(replyMessage);
						vcReplyMessage.loadBlobFile();
						Object returnValue = vcReplyMessage.getObjectContent();
						vcReplyMessage.removeBlobFile();
						if (returnValue instanceof Exception){
							throw new VCMessagingInvocationTargetException((Exception)returnValue);
						} else {
							return returnValue;
						}
					} 
				} else {
					rpcMessage.setJMSReplyTo(commonTemporaryQueue);
					messageProducer.setTimeToLive(timeoutMS);
					messageProducer.send(rpcMessage);
					commit();
					return null;
				}
			} catch (JMSException e){
				onException(e);
				throw new VCMessagingException(e.getMessage(),e);
			} finally {
				try {
					if (messageProducer!=null){
						messageProducer.close();
					}
				} catch (JMSException e) {
					onException(e);
				}
			}
		}

		public void sendQueueMessage(VCellQueue queue, VCMessage message) throws VCMessagingException {
			if (message instanceof VCMessageJms){
				try {
					Destination destination = session.createQueue(queue.getName());
					MessageProducer messageProducer = session.createProducer(destination);
					messageProducer.send(((VCMessageJms)message).getJmsMessage());
					if (bIndependent){
						session.commit();
					}
					VCMongoMessage.sendJmsMessageSent(message,queue);
				} catch (JMSException e) {
					onException(e);
				}
			}else{
				throw new RuntimeException("expected JMS message for JMS message service");
			}
		}
		
		public void sendTopicMessage(VCellTopic topic, VCMessage message) throws VCMessagingException {
			if (message instanceof VCMessageJms){
				VCMessageJms jmsMessage = (VCMessageJms)message;
				try {
					MessageProducer producer = session.createProducer(session.createTopic(topic.getName()));
					producer.send(jmsMessage.getJmsMessage());
					if (bIndependent){
						session.commit();
					}
					VCMongoMessage.sendJmsMessageSent(message,topic);
				} catch (JMSException e) {
					e.printStackTrace(System.out);
					onException(e);
				}				
			}else{
				throw new RuntimeException("must send a JMS message to a JMS messaging service");
			}
		}

		public void rollback() {
			try {
				session.rollback();
			}catch (JMSException e){
				onException(e);
			}
		}

		public void commit() {
			try {
				session.commit();
			}catch (JMSException e){
				onException(e);
			}
		}

		public VCMessage createTextMessage(String text) {
			try {
				Message jmsMessage = session.createTextMessage(text);
				return new VCMessageJms(jmsMessage);
			} catch (JMSException e) {
				e.printStackTrace(System.out);
				onException(e);
				throw new RuntimeException("unable to create text message");
			}
		}
		public VCMessage createObjectMessage(Serializable object) {
			try {
				// if the serialized object is very large, send it as a BlobMessage (ActiveMQ specific).
				byte[] serializedBytes = BeanUtils.toSerialized(object);
				
				long blobMessageSizeThreshold = Long.parseLong(PropertyLoader.getRequiredProperty(PropertyLoader.jmsBlobMessageMinSize));
				
				if (serializedBytes.length > blobMessageSizeThreshold){
					//
					// get (or create) directory to store Message BLOBs
					//
					File tempdir = new File(PropertyLoader.getRequiredProperty(PropertyLoader.jmsBlobMessageTempDir));
					if (!tempdir.exists()){
						tempdir.mkdirs();
					}
					
					//
					// write serialized message to "temp" file.
					//
					File blobFile = File.createTempFile("BlobMessage",".data",tempdir);
					FileOutputStream fileOutputStream = new FileOutputStream(blobFile);
					FileChannel channel = fileOutputStream.getChannel();
					channel.write(ByteBuffer.wrap(serializedBytes));
					channel.close();
					fileOutputStream.close();

					ObjectMessage objectMessage = session.createObjectMessage("emptyObject");
					objectMessage.setStringProperty(VCMessageJms.BLOB_MESSAGE_PRODUCER_TEMPDIR, tempdir.getAbsolutePath());
					objectMessage.setStringProperty(VCMessageJms.BLOB_MESSAGE_FILE_NAME, blobFile.getName());
					objectMessage.setStringProperty(VCMessageJms.BLOB_MESSAGE_OBJECT_TYPE, object.getClass().getName());
					objectMessage.setIntProperty(VCMessageJms.BLOB_MESSAGE_OBJECT_SIZE, serializedBytes.length);
					return new VCMessageJms(objectMessage,object);
				}else{
					ObjectMessage objectMessage = (ObjectMessage)session.createObjectMessage(serializedBytes);
					return new VCMessageJms(objectMessage);
				}
			} catch (JMSException e) {
				e.printStackTrace(System.out);
				onException(e);
				throw new RuntimeException("unable to create object message");
			} catch (Exception e){
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());				
			}
		}
		
		public VCMessage createMessage() {
			try {
				Message jmsMessage = session.createMessage();
				return new VCMessageJms(jmsMessage);
			} catch (JMSException e) {
				e.printStackTrace(System.out);
				onException(e);
				throw new RuntimeException("unable to create message");
			}
		}

		private void onException(JMSException e){
			e.printStackTrace(System.out);
		}

		public void close() {
			try {
				if (session!=null){
					session.close();
				}
				if (commonTemporaryQueue!=null){
					commonTemporaryQueue.delete();
				}
				if (connection!=null){
					connection.close();
				}
			}catch (JMSException e){
				onException(e);
			}
		}
	}