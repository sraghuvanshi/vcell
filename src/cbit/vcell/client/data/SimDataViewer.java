package cbit.vcell.client.data;
import cbit.rmi.event.DataJobEvent;
import cbit.vcell.simdata.ClientPDEDataContext;
import cbit.vcell.math.Constant;
import cbit.vcell.solver.ode.ODESolverResultSet;
import cbit.vcell.client.server.PDEDataManager;
import cbit.vcell.client.server.ODEDataManager;
import cbit.vcell.server.VCDataIdentifier;
import cbit.vcell.solver.VCSimulationDataIdentifier;
import cbit.vcell.server.DataAccessException;
import cbit.vcell.client.server.VCDataManager;
import javax.swing.*;
import cbit.vcell.solver.Simulation;
/**
 * Insert the type's description here.
 * Creation date: (10/17/2005 11:22:58 PM)
 * @author: Ion Moraru
 */
public class SimDataViewer extends DataViewer {
	private Simulation simulation = null;
	private VCDataManager vcDataManager = null;
	private DataViewer mainViewer = null;
	private JPanel paramChoicesPanel = null;
	private ODEDataViewer odeDataViewer = null;
	private PDEDataViewer pdeDataViewer = null;
	private boolean isODEData;
	private java.util.Hashtable choicesHash = new java.util.Hashtable();

/**
 * Insert the method's description here.
 * Creation date: (10/17/2005 11:30:45 PM)
 * @param simulation cbit.vcell.solver.Simulation
 * @param vcDataManager cbit.vcell.client.server.VCDataManager
 */
public SimDataViewer(Simulation simulation, VCDataManager vcDataManager, boolean isODEData) throws DataAccessException {
	super();
	setSimulation(simulation);
	setVcDataManager(vcDataManager);
	this.isODEData = isODEData;
	initialize();
}


/**
 * Insert the method's description here.
 * Creation date: (6/11/2004 2:33:44 PM)
 * @return javax.swing.JPanel
 */
private DataViewer createODEDataViewer(int jobIndex) {
	VCDataIdentifier vcdid = new VCSimulationDataIdentifier(getSimulation().getSimulationInfo().getAuthoritativeVCSimulationIdentifier(), jobIndex);
	ODEDataManager odeDatamanager = new ODEDataManager(vcDataManager, vcdid);
	odeDataViewer = new ODEDataViewer();
	odeDataViewer.setSimulation(getSimulation());
	try {
		odeDataViewer.setOdeSolverResultSet(odeDatamanager.getODESolverResultSet());
	} catch (cbit.vcell.server.DataAccessException exc) {
		cbit.gui.DialogUtils.showErrorDialog(odeDataViewer, "Could not fetch data for requested parameter choices\nJob may have failed or not yet started\n" + exc.getMessage());
		exc.printStackTrace();
	}
	odeDataViewer.setVcDataIdentifier(vcdid);
	return odeDataViewer;
}


/**
 * Insert the method's description here.
 * Creation date: (6/11/2004 2:33:44 PM)
 * @return javax.swing.JPanel
 */
private DataViewer createPDEDataViewer(int jobIndex) throws DataAccessException {
	VCDataIdentifier vcdid = new VCSimulationDataIdentifier(getSimulation().getSimulationInfo().getAuthoritativeVCSimulationIdentifier(), jobIndex);
	PDEDataManager pdeDatamanager = new PDEDataManager(vcDataManager, vcdid);
	pdeDataViewer = new PDEDataViewer();
	pdeDataViewer.setSimulation(getSimulation());
	pdeDataViewer.setPdeDataContext(pdeDatamanager.getPDEDataContext());
	return pdeDataViewer;
}

public void dataJobMessage(DataJobEvent dje) {
	getMainViewer().dataJobMessage(dje);
}

/**
 * Method generated to support the promotion of the exportMonitorPanel attribute.
 * @return cbit.vcell.export.ExportMonitorPanel
 */
public cbit.vcell.export.ExportMonitorPanel getExportMonitorPanel() {
	return getMainViewer().getExportMonitorPanel();
}


/**
 * Insert the method's description here.
 * Creation date: (10/17/2005 11:36:17 PM)
 * @return cbit.vcell.client.data.DataViewer
 */
private DataViewer getMainViewer() {
	return mainViewer;
}


/**
 * Insert the method's description here.
 * Creation date: (10/17/2005 11:36:17 PM)
 * @return javax.swing.JPanel
 */
private javax.swing.JPanel getParamChoicesPanel() {
	return paramChoicesPanel;
}


/**
 * Insert the method's description here.
 * Creation date: (10/17/2005 11:36:17 PM)
 * @return cbit.vcell.solver.Simulation
 */
private cbit.vcell.solver.Simulation getSimulation() {
	return simulation;
}


/**
 * Insert the method's description here.
 * Creation date: (10/17/2005 11:36:17 PM)
 * @return cbit.vcell.client.server.VCDataManager
 */
private cbit.vcell.client.server.VCDataManager getVcDataManager() {
	return vcDataManager;
}


/**
 * Insert the method's description here.
 * Creation date: (10/17/2005 11:37:52 PM)
 * @exception cbit.vcell.server.DataAccessException The exception description.
 */
private void initialize() throws cbit.vcell.server.DataAccessException {
	
	// create main viewer for jobIndex 0 and wire it up
	if (isODEData) {
		setMainViewer(createODEDataViewer(0));
	} else {
		setMainViewer(createPDEDataViewer(0));
	}
	java.beans.PropertyChangeListener pcl = new java.beans.PropertyChangeListener() {
		public void propertyChange(java.beans.PropertyChangeEvent evt) {
			if (evt.getSource() == SimDataViewer.this && (evt.getPropertyName().equals("dataViewerManager"))) {
				try {
					getMainViewer().setDataViewerManager(getDataViewerManager());
				} catch (java.beans.PropertyVetoException exc) {
					exc.printStackTrace();
				}
			}
			if (evt.getSource() == SimDataViewer.this && (evt.getPropertyName().equals("simulationModelInfo"))) {
				getMainViewer().setSimulationModelInfo(getSimulationModelInfo());
			}
		}
	};
	addPropertyChangeListener(pcl);
		
	
	// if necessarry, create parameter choices panel and wire it up
	if (getSimulation().getScanCount() > 1) {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		cbit.gui.EmptyBorderBean ebb = new cbit.gui.EmptyBorderBean();
		ebb.setInsets(new java.awt.Insets(4,2,4,2));
		JLabel label = new JLabel("View data for parameter values: ");
		label.setBorder(ebb);
		panel.add(label);
		String[] scanParams = getSimulation().getMathOverrides().getScannedConstantNames();
		javax.swing.event.ListSelectionListener lsl = new javax.swing.event.ListSelectionListener() {
			public void valueChanged(javax.swing.event.ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					updateScanParamChoices();
				}
			}
		};
		for (int i = 0; i < scanParams.length; i++){
			Constant[] scanConstants = getSimulation().getMathOverrides().getConstantArraySpec(scanParams[i]).getConstants();
			String[][] values = new String[scanConstants.length][1]; 
			for (int j = 0; j < scanConstants.length; j++){
				values[j][0] = scanConstants[j].getExpression().infix();
			}
			class ScanChoicesTableModel extends javax.swing.table.AbstractTableModel {
				String[] columnNames;
				Object[][] rowData;
				ScanChoicesTableModel(Object[][] argData, String[] argNames) {
					columnNames = argNames;
					rowData = argData;
				}
				public String getColumnName(int column) { return columnNames[column].toString(); }
				public int getRowCount() { return rowData.length; }
				public int getColumnCount() { return columnNames.length; }
				public Object getValueAt(int row, int col) { return rowData[row][col]; }
				public boolean isCellEditable(int row, int column) { return false; }
				public void setValueAt(Object value, int row, int col) {
					rowData[row][col] = value;
					fireTableCellUpdated(row, col);
				}
			};
			ScanChoicesTableModel tm = new ScanChoicesTableModel(values, new String[] {scanParams[i]});
			JTable table = new JTable(tm);
			choicesHash.put(scanParams[i], table);
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			table.getSelectionModel().setSelectionInterval(0,0);
			table.getSelectionModel().addListSelectionListener(lsl);
			JScrollPane scr = new JScrollPane(table);
			JPanel p = new JPanel();
			scr.setPreferredSize(new java.awt.Dimension (100, Math.min(150, table.getPreferredSize().height + table.getTableHeader().getPreferredSize().height + 5)));
			ebb = new cbit.gui.EmptyBorderBean();
			ebb.setInsets(new java.awt.Insets(4,2,4,2));
			p.setBorder(ebb);
			p.setLayout(new java.awt.BorderLayout());
			p.add(scr, java.awt.BorderLayout.CENTER);
			panel.add(p);
		}
		setParamChoicesPanel(panel);
	}

	// put things together
	setLayout(new java.awt.BorderLayout());
	add(getMainViewer(), java.awt.BorderLayout.CENTER);
	if (getSimulation().getScanCount() > 1) {
		add(getParamChoicesPanel(), java.awt.BorderLayout.SOUTH);
	}
}

/**
 * Insert the method's description here.
 * Creation date: (6/11/2004 2:43:49 PM)
 * @exception cbit.vcell.server.DataAccessException The exception description.
 */
public void refreshData() throws cbit.vcell.server.DataAccessException {
	if (isODEData) {
		updateScanParamChoices(); // this takes care of all logic to get the fresh data
	} else {
		pdeDataViewer.getPdeDataContext().refreshTimes();
	}
}

/**
 * Insert the method's description here.
 * Creation date: (10/17/2005 11:36:17 PM)
 * @param newMainViewer cbit.vcell.client.data.DataViewer
 */
private void setMainViewer(DataViewer newMainViewer) {
	mainViewer = newMainViewer;
}


/**
 * Insert the method's description here.
 * Creation date: (10/17/2005 11:36:17 PM)
 * @param newParamChoicesPanel javax.swing.JPanel
 */
private void setParamChoicesPanel(javax.swing.JPanel newParamChoicesPanel) {
	paramChoicesPanel = newParamChoicesPanel;
}


/**
 * Insert the method's description here.
 * Creation date: (10/17/2005 11:36:17 PM)
 * @param newSimulation cbit.vcell.solver.Simulation
 */
private void setSimulation(cbit.vcell.solver.Simulation newSimulation) {
	simulation = newSimulation;
}


/**
 * Insert the method's description here.
 * Creation date: (10/17/2005 11:36:17 PM)
 * @param newVcDataManager cbit.vcell.client.server.VCDataManager
 */
private void setVcDataManager(cbit.vcell.client.server.VCDataManager newVcDataManager) {
	vcDataManager = newVcDataManager;
}


/**
 * Insert the method's description here.
 * Creation date: (10/18/2005 12:44:06 AM)
 */
private void updateScanParamChoices(){
	
	// figure out what job data we are looking for
	String[] scanConstantNames = getSimulation().getMathOverrides().getScannedConstantNames();
	java.util.Arrays.sort(scanConstantNames);
	int[] indices = new int[scanConstantNames.length];
	int[] bounds = new int[scanConstantNames.length];
	for (int i = 0; i < indices.length; i++){
		indices[i] = ((JTable)choicesHash.get(scanConstantNames[i])).getSelectedRow();
		bounds[i] = getSimulation().getMathOverrides().getConstantArraySpec(scanConstantNames[i]).getNumValues() - 1;
	}
	int jobIndex = -1;
	try {
		jobIndex = cbit.util.BeanUtils.coordinateToIndex(indices, bounds);
	} catch (RuntimeException exc) {}
	
	// update viewer
	if (jobIndex == -1) {
		if (isODEData) {
			odeDataViewer.setOdeSolverResultSet(null);
		} else {
			pdeDataViewer.setPdeDataContext(null);
		}
		return;
	}
	VCDataIdentifier vcdid = new VCSimulationDataIdentifier(getSimulation().getSimulationInfo().getAuthoritativeVCSimulationIdentifier(), jobIndex);
	if (isODEData) {
		ODEDataManager odeDatamanager = new ODEDataManager(vcDataManager, vcdid);
		try {
			odeDataViewer.setOdeSolverResultSet(odeDatamanager.getODESolverResultSet());
			odeDataViewer.setVcDataIdentifier(vcdid);
		} catch (DataAccessException exc) {
			exc.printStackTrace();
			cbit.gui.DialogUtils.showErrorDialog(odeDataViewer, "Could not fetch data for requested parameter choices\nJob may have failed or not yet started\n" + exc.getMessage());
			odeDataViewer.setOdeSolverResultSet(null);
		}
	} else {
		PDEDataManager pdeDatamanager = new PDEDataManager(vcDataManager, vcdid);
		ClientPDEDataContext currentContext = (ClientPDEDataContext)pdeDataViewer.getPdeDataContext();
		if (currentContext == null || currentContext.getDataIdentifier() == null) {
			pdeDataViewer.setPdeDataContext(new NewClientPDEDataContext(pdeDatamanager));
		} else {
			try{
				currentContext.setDataManager(pdeDatamanager);
			}catch(Exception e){
				e.printStackTrace();
				cbit.gui.DialogUtils.showErrorDialog(pdeDataViewer, e.getMessage());
			}
		}
		if (pdeDataViewer.getPdeDataContext().getDataValues() == null) {
			JInternalFrame frame = (JInternalFrame)cbit.util.BeanUtils.findTypeParentOfComponent(this, JInternalFrame.class);
			frame.setSize(frame.getWidth(), frame.getHeight() - 1);
			frame.setSize(frame.getWidth(), frame.getHeight() + 1);
			cbit.gui.DialogUtils.showErrorDialog(pdeDataViewer, "Could not fetch data for requested parameter choices\nJob may have failed or not yet started\n");
		}
	}
}
}