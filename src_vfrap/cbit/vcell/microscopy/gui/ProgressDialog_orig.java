/*
 * Copyright (C) 1999-2011 University of Connecticut Health Center
 *
 * Licensed under the MIT License (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *  http://www.opensource.org/licenses/mit-license.php
 */

package cbit.vcell.microscopy.gui;
import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import org.vcell.util.gui.ProgressDialogListener;
import org.vcell.util.gui.ProgressDialogListenerEventMulticaster;
/**
 * Insert the type's description here.
 * Creation date: (5/18/2004 1:14:29 AM)
 * @author: Ion Moraru
 */
@SuppressWarnings("serial")
public class ProgressDialog_orig extends JDialog {
	private JPanel ivjJDialogContentPane = null;
	private JLabel ivjJLabel1 = null;
	private JProgressBar ivjJProgressBar1 = null;
	private JButton ivjCancelButton = null;
	protected transient ProgressDialogListener fieldProgressDialogListenerEventMulticaster = null;
	IvjEventHandler ivjEventHandler = new IvjEventHandler();

class IvjEventHandler implements java.awt.event.ActionListener {
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if (e.getSource() == ProgressDialog_orig.this.getCancelButton()) 
				connEtoC1(e);
		};
	};

public ProgressDialog_orig() {
	super();
	initialize();
}

/**
 * Insert the method's description here.
 * Creation date: (5/19/2004 6:08:36 PM)
 * @param owner java.awt.Frame
 */
public ProgressDialog_orig(Frame owner) {
	super(owner);
	initialize();
}


/**
 * 
 * @param newListener cbit.util.ProgressDialogListener
 */
public void addProgressDialogListener(ProgressDialogListener newListener) {
	fieldProgressDialogListenerEventMulticaster = ProgressDialogListenerEventMulticaster.add(fieldProgressDialogListenerEventMulticaster, newListener);
	return;
}


/**
 * connEtoC1:  (CancelButton.action.actionPerformed(java.awt.event.ActionEvent) --> ProgressDialog.fireCancelButton_actionPerformed(Ljava.util.EventObject;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void connEtoC1(java.awt.event.ActionEvent arg1) {
	try {
		// user code begin {1}
		// user code end
		this.fireCancelButton_actionPerformed(new java.util.EventObject(this));
		// user code begin {2}
		// user code end
	} catch (java.lang.Throwable ivjExc) {
		// user code begin {3}
		// user code end
		handleException(ivjExc);
	}
}


/**
 * Method to support listener events.
 * @param newEvent java.util.EventObject
 */
protected void fireCancelButton_actionPerformed(java.util.EventObject newEvent) {
	if (fieldProgressDialogListenerEventMulticaster == null) {
		return;
	};
	fieldProgressDialogListenerEventMulticaster.cancelButton_actionPerformed(newEvent);
}


/**
 * Return the CancelButton property value.
 * @return javax.swing.JButton
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JButton getCancelButton() {
	if (ivjCancelButton == null) {
		try {
			ivjCancelButton = new javax.swing.JButton();
			ivjCancelButton.setName("CancelButton");
			ivjCancelButton.setText("cancel");
			ivjCancelButton.setVisible(true);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjCancelButton;
}

/**
 * Method generated to support the promotion of the cancelButtonVisible attribute.
 * @return boolean
 */
public boolean getCancelButtonVisible() {
	return getCancelButton().isVisible();
}


/**
 * Return the JDialogContentPane property value.
 * @return javax.swing.JPanel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JPanel getJDialogContentPane() {
	if (ivjJDialogContentPane == null) {
		try {
			ivjJDialogContentPane = new javax.swing.JPanel();
			ivjJDialogContentPane.setName("JDialogContentPane");
			ivjJDialogContentPane.setLayout(new java.awt.GridBagLayout());

			java.awt.GridBagConstraints constraintsJLabel1 = new java.awt.GridBagConstraints();
			constraintsJLabel1.gridx = 0; constraintsJLabel1.gridy = 0;
			constraintsJLabel1.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsJLabel1.weighty = 1.0;
			constraintsJLabel1.ipady = 15;
			getJDialogContentPane().add(getJLabel1(), constraintsJLabel1);

			java.awt.GridBagConstraints constraintsCancelButton = new java.awt.GridBagConstraints();
			constraintsCancelButton.gridx = 0; constraintsCancelButton.gridy = 2;
			constraintsCancelButton.insets = new java.awt.Insets(4, 0, 4, 0);
			getJDialogContentPane().add(getCancelButton(), constraintsCancelButton);

			java.awt.GridBagConstraints constraintsJProgressBar1 = new java.awt.GridBagConstraints();
			constraintsJProgressBar1.gridx = 0; constraintsJProgressBar1.gridy = 1;
			constraintsJProgressBar1.fill = java.awt.GridBagConstraints.HORIZONTAL;
			constraintsJProgressBar1.weightx = 1.0;
			constraintsJProgressBar1.weighty = 1.0;
			constraintsJProgressBar1.ipady = 25;
			getJDialogContentPane().add(getJProgressBar1(), constraintsJProgressBar1);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJDialogContentPane;
}

/**
 * Return the JLabel1 property value.
 * @return javax.swing.JLabel
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JLabel getJLabel1() {
	if (ivjJLabel1 == null) {
		try {
			ivjJLabel1 = new javax.swing.JLabel();
			ivjJLabel1.setName("JLabel1");
			ivjJLabel1.setText("We are doing something...");
			ivjJLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
			ivjJLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJLabel1;
}

/**
 * Return the JProgressBar1 property value.
 * @return javax.swing.JProgressBar
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private javax.swing.JProgressBar getJProgressBar1() {
	if (ivjJProgressBar1 == null) {
		try {
			ivjJProgressBar1 = new javax.swing.JProgressBar();
			ivjJProgressBar1.setName("JProgressBar1");
			ivjJProgressBar1.setStringPainted(true);
			// user code begin {1}
			// user code end
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {2}
			// user code end
			handleException(ivjExc);
		}
	}
	return ivjJProgressBar1;
}

/**
 * Called whenever the part throws an exception.
 * @param exception java.lang.Throwable
 */
private void handleException(java.lang.Throwable exception) {

	/* Uncomment the following lines to print uncaught exceptions to stdout */
	System.out.println("--------- UNCAUGHT EXCEPTION ---------");
	exception.printStackTrace(System.out);
}


/**
 * Initializes connections
 * @exception java.lang.Exception The exception description.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initConnections() throws java.lang.Exception {
	// user code begin {1}
	// user code end
	getCancelButton().addActionListener(ivjEventHandler);
}


/**
 * Initialize the class.
 */
/* WARNING: THIS METHOD WILL BE REGENERATED. */
private void initialize() {
	try {
		// user code begin {1}
		// user code end
		setName("DocumentLoader");
		setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		setSize(341, 116);
		setTitle("Please wait:");
		setContentPane(getJDialogContentPane());
		initConnections();
	} catch (java.lang.Throwable ivjExc) {
		handleException(ivjExc);
	}
	// user code begin {2}
	// user code end
}

/**
 * main entrypoint - starts the part when it is run as an application
 * @param args java.lang.String[]
 */
public static void main(java.lang.String[] args) {
	try {
		ProgressDialog_orig aProgressDialog;
		aProgressDialog = new ProgressDialog_orig();
		aProgressDialog.setModal(true);
		aProgressDialog.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				System.exit(0);
			};
		});
		aProgressDialog.setVisible(true);
		java.awt.Insets insets = aProgressDialog.getInsets();
		aProgressDialog.setSize(aProgressDialog.getWidth() + insets.left + insets.right, aProgressDialog.getHeight() + insets.top + insets.bottom);
		aProgressDialog.setVisible(true);
	} catch (Throwable exception) {
		System.err.println("Exception occurred in main() of javax.swing.JDialog");
		exception.printStackTrace(System.out);
	}
}


/**
 * 
 * @param newListener cbit.util.ProgressDialogListener
 */
public void removeProgressDialogListener(ProgressDialogListener newListener) {
	fieldProgressDialogListenerEventMulticaster = ProgressDialogListenerEventMulticaster.remove(fieldProgressDialogListenerEventMulticaster, newListener);
	return;
}


/**
 * Method generated to support the promotion of the cancelButtonVisible attribute.
 * @param arg1 boolean
 */
public void setCancelButtonVisible(boolean arg1) {
	getCancelButton().setVisible(arg1);
}


/**
 * Insert the method's description here.
 * Creation date: (5/19/2004 1:06:46 PM)
 * @param message java.lang.String
 */
void setMessage(String message) {
	getJLabel1().setText(message);
}


/**
 * Insert the method's description here.
 * Creation date: (5/19/2004 1:06:18 PM)
 * @param progress int
 */
void setProgress(int progress) {
	getJProgressBar1().setValue(progress);
}


/**
 * Insert the method's description here.
 * Creation date: (5/19/2004 1:06:18 PM)
 * @param progress int
 */
void setProgressBarString(String progressString) {
	getJProgressBar1().setString(progressString);
}
}
