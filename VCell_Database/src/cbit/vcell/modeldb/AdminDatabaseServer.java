package cbit.vcell.modeldb;

/*�
 * (C) Copyright University of Connecticut Health Center 2001.
 * All rights reserved.
�*/
import java.rmi.RemoteException;

import cbit.rmi.event.SimulationJobStatus;
import cbit.util.DataAccessException;
import cbit.util.document.KeyValue;
import cbit.util.document.User;
import cbit.util.document.UserInfo;

/**
 * This type was created in VisualAge.
 */
public interface AdminDatabaseServer extends java.rmi.Remote {
/**
 * Insert the method's description here.
 * Creation date: (1/31/2003 2:33:54 PM)
 * @return cbit.vcell.solvers.SimulationJobStatus[]
 * @param bActiveOnly boolean
 * @param userOnly cbit.vcell.server.User
 * @exception java.rmi.RemoteException The exception description.
 */
SimulationJobStatus getSimulationJobStatus(KeyValue simKey, int jobIndex) throws RemoteException, DataAccessException;
	public java.util.List getSimulationJobStatus(String conditions) throws RemoteException, DataAccessException;
/**
 * Insert the method's description here.
 * Creation date: (1/31/2003 2:33:54 PM)
 * @return cbit.vcell.solvers.SimulationJobStatus[]
 * @param bActiveOnly boolean
 * @param userOnly cbit.vcell.server.User
 * @exception java.rmi.RemoteException The exception description.
 */
SimulationJobStatus[] getSimulationJobStatus(boolean bActiveOnly, User userOnly) throws RemoteException, DataAccessException;
/**
 * This method was created in VisualAge.
 * @return cbit.vcell.server.User
 * @param userid java.lang.String
 * @param password java.lang.String
 */
User getUser(String userid) throws RemoteException, DataAccessException;
/**
 * This method was created in VisualAge.
 * @return cbit.vcell.server.User
 * @param userid java.lang.String
 * @param password java.lang.String
 */
User getUser(String userid, String password) throws RemoteException, DataAccessException;
/**
 * This method was created in VisualAge.
 * @return cbit.vcell.server.User
 * @param userid java.lang.String
 * @param password java.lang.String
 */
User getUserFromSimulationKey(KeyValue simKey) throws RemoteException, DataAccessException;
/**
 * This method was created in VisualAge.
 * @return cbit.vcell.server.User
 * @param userid java.lang.String
 * @param password java.lang.String
 */
UserInfo getUserInfo(KeyValue userKey) throws RemoteException, DataAccessException;
/**
 * This method was created in VisualAge.
 * @return cbit.vcell.server.User
 * @param userid java.lang.String
 * @param password java.lang.String
 */
UserInfo[] getUserInfos() throws RemoteException, DataAccessException;
/**
 * Insert the method's description here.
 * Creation date: (1/31/2003 2:30:21 PM)
 * @return cbit.vcell.solvers.SimulationJobStatus
 * @param simulationJobStatus cbit.vcell.solvers.SimulationJobStatus
 * @exception java.rmi.RemoteException The exception description.
 */
SimulationJobStatus insertSimulationJobStatus(SimulationJobStatus simulationJobStatus) throws RemoteException, DataAccessException;
/**
 * This method was created in VisualAge.
 * @return cbit.vcell.server.User
 * @param userid java.lang.String
 * @param password java.lang.String
 */
cbit.util.document.UserInfo insertUserInfo(cbit.util.document.UserInfo newUserInfo) throws RemoteException, DataAccessException;
/**
 * Insert the method's description here.
 * Creation date: (1/31/2003 2:30:21 PM)
 * @return cbit.vcell.solvers.SimulationJobStatus
 * @param simulationJobStatus cbit.vcell.solvers.SimulationJobStatus
 * @exception java.rmi.RemoteException The exception description.
 */
SimulationJobStatus updateSimulationJobStatus(SimulationJobStatus oldSimulationJobStatus, SimulationJobStatus newSimulationJobStatus) throws RemoteException, DataAccessException;
/**
 * This method was created in VisualAge.
 * @return cbit.vcell.server.User
 * @param userid java.lang.String
 * @param password java.lang.String
 */
cbit.util.document.UserInfo updateUserInfo(cbit.util.document.UserInfo newUserInfo) throws RemoteException, DataAccessException;
}
