/*
 * Copyright (C) 1999-2011 University of Connecticut Health Center
 *
 * Licensed under the MIT License (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *  http://www.opensource.org/licenses/mit-license.php
 */

package cbit.vcell.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

import org.vcell.util.DataAccessException;
import org.vcell.util.document.VCDataIdentifier;

import cbit.plot.PlotData;
import cbit.rmi.event.ExportEvent;
import cbit.vcell.export.server.ExportSpecs;
import cbit.vcell.field.FieldDataFileOperationResults;
import cbit.vcell.field.FieldDataFileOperationSpec;
import cbit.vcell.simdata.DataIdentifier;
import cbit.vcell.simdata.DataOperation;
import cbit.vcell.simdata.DataOperationResults;
import cbit.vcell.simdata.DataSetMetadata;
import cbit.vcell.simdata.DataSetTimeSeries;
import cbit.vcell.simdata.OutputContext;
import cbit.vcell.simdata.gui.SpatialSelection;
import cbit.vcell.solver.AnnotatedFunction;
import cbit.vcell.solvers.CartesianMesh;
/**
 * This interface was generated by a SmartGuide.
 * 
 */
public interface DataSetController extends Remote {
public FieldDataFileOperationResults fieldDataFileOperation(FieldDataFileOperationSpec fieldDataFileOperationSpec) throws RemoteException, DataAccessException;


/**
 * This method was created by a SmartGuide.
 * @exception java.rmi.RemoteException The exception description.
 */
public DataIdentifier[] getDataIdentifiers(OutputContext outputContext, VCDataIdentifier vcdataID) throws RemoteException, DataAccessException;
/**
 * This method was created by a SmartGuide.
 * @exception java.rmi.RemoteException The exception description.
 */
public double[] getDataSetTimes(VCDataIdentifier vcdataID) throws RemoteException, DataAccessException;
/**
 * Insert the method's description here.
 * Creation date: (10/11/00 6:21:10 PM)
 * @param function cbit.vcell.math.Function
 * @exception org.vcell.util.DataAccessException The exception description.
 * @exception java.rmi.RemoteException The exception description.
 */
 AnnotatedFunction[] getFunctions(OutputContext outputContext,VCDataIdentifier vcdataID) throws DataAccessException, RemoteException; 
/**
 * This method was created by a SmartGuide.
 * @return cbit.plot.PlotData
 * @param variable java.lang.String
 * @param time double
 * @param spatialSelection cbit.vcell.simdata.gui.SpatialSelection
 * @exception java.rmi.RemoteException The exception description.
 */
public PlotData getLineScan(OutputContext outputContext, VCDataIdentifier vcdataID, String variable, double time, SpatialSelection spatialSelection) throws RemoteException, DataAccessException;
/**
 * This method was created in VisualAge.
 * @return CartesianMesh
 */
CartesianMesh getMesh(VCDataIdentifier vcdataID) throws RemoteException, DataAccessException;
/**
 * Insert the method's description here.
 * Creation date: (1/13/00 6:21:10 PM)
 * @param odeSimData cbit.vcell.export.data.ODESimData
 * @exception org.vcell.util.DataAccessException The exception description.
 * @exception java.rmi.RemoteException The exception description.
 */
cbit.vcell.solver.ode.ODESimData getODEData(VCDataIdentifier vcdataID) throws DataAccessException, RemoteException;
/**
 * This method was created in VisualAge.
 * @return ParticleData
 * @param time double
 * @exception org.vcell.util.DataAccessException The exception description.
 * @exception java.rmi.RemoteException The exception description.
 */
cbit.vcell.simdata.ParticleDataBlock getParticleDataBlock(VCDataIdentifier vcdataID, double time) throws DataAccessException, RemoteException;
/**
 * This method was created in VisualAge.
 * @return boolean
 */

public DataSetMetadata getDataSetMetadata(VCDataIdentifier vcdataID) throws DataAccessException, RemoteException;

public DataSetTimeSeries getDataSetTimeSeries(VCDataIdentifier vcdataID, String[] variableNames) throws DataAccessException, RemoteException;

public DataOperationResults doDataOperation(DataOperation dataOperation) throws DataAccessException, RemoteException;


public boolean getParticleDataExists(VCDataIdentifier vcdataID) throws DataAccessException, RemoteException;
/**
 * This method was created by a SmartGuide.
 * @return java.lang.String
 * @exception java.rmi.RemoteException The exception description.
 */
public cbit.vcell.simdata.SimDataBlock getSimDataBlock(OutputContext outputContext, VCDataIdentifier vcdataID, String varName, double time) throws RemoteException, DataAccessException;
/**
 * This method was created by a SmartGuide.
 * @return double[]
 * @param varName java.lang.String
 * @param x int
 * @param y int
 * @param z int
 * @exception java.rmi.RemoteException The exception description.
 */
public org.vcell.util.document.TimeSeriesJobResults getTimeSeriesValues(OutputContext outputContext, VCDataIdentifier vcdataID, org.vcell.util.document.TimeSeriesJobSpec timeSeriesJobSpec) throws RemoteException, DataAccessException;
/**
 * Insert the method's description here.
 * Creation date: (10/22/2001 2:53:44 PM)
 * @return cbit.rmi.event.ExportEvent
 * @param exportSpecs cbit.vcell.export.server.ExportSpecs
 * @exception org.vcell.util.DataAccessException The exception description.
 * @exception java.rmi.RemoteException The exception description.
 */
ExportEvent makeRemoteFile(OutputContext outputContext,ExportSpecs exportSpecs) throws DataAccessException, java.rmi.RemoteException;
}
