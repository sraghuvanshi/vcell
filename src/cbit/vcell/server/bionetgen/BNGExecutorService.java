/*
 * Copyright (C) 1999-2011 University of Connecticut Health Center
 *
 * Licensed under the MIT License (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *  http://www.opensource.org/licenses/mit-license.php
 */

package cbit.vcell.server.bionetgen;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.vcell.util.ExecutableException;
import org.vcell.util.FileUtils;

import cbit.vcell.mapping.BioNetGenUpdaterCallback;
import cbit.vcell.resource.OperatingSystemInfo;
import cbit.vcell.resource.ResourceUtil;
import cbit.vcell.resource.VersionedLibrary;
import cbit.vcell.solvers.BioNetGenExecutable;

/**
 * Insert the type's description here.
 * Creation date: (9/13/2006 9:26:26 AM)
 * @author: Fei Gao
 */
public class BNGExecutorService {
	private final static String EXE_SUFFIX; 
	private final static String EXE_BNG; 
	private final static String RES_EXE_BNG; 
	// run_network is called from within BNG2.exe, so the extension is not controlled from here
	private final static String EXE_RUN_NETWORK;
	private final static String RES_EXE_RUN_NETWORK;

	private final static String suffix_input = ".bngl";

	private static File workingDir = null;
	private BioNetGenExecutable executable = null;
	private final BNGInput bngInput;
	private final Long timeoutDurationMS; // ignore if null, else gives time limit for process
	
	private transient List<BioNetGenUpdaterCallback> callbacks = null;
	private boolean stopped = false;
	private long startTime = System.currentTimeMillis();

	private static File file_exe_bng = null;
	private static File file_exe_run_network = null;
	private static Logger lg = Logger.getLogger(BNGExecutorService.class);
	private static String sharedLibrarySuffix = null; 
	static {
		OperatingSystemInfo osi = OperatingSystemInfo.getInstance();
		EXE_SUFFIX = osi.isWindows() ? ".exe" : "";
		EXE_BNG = "BNG2" + EXE_SUFFIX;
		//RES_EXE_BNG = ResourceUtil.RES_PACKAGE + "/" + EXE_BNG;
		RES_EXE_BNG = osi.getResourcePackage() + EXE_BNG;
		// run_network is called from within BNG2.exe, so the extension is not controlled from here
		EXE_RUN_NETWORK = "run_network" + (osi.isWindows() ? ".exe" : "");
		RES_EXE_RUN_NETWORK = osi.getResourcePackage() + EXE_RUN_NETWORK;
		sharedLibrarySuffix = osi.getNativelibSuffix();
	}

/**
 * BNGUtils constructor comment.
 */
public BNGExecutorService(BNGInput bngInput, Long timeoutDurationMS) {
	super();
	this.bngInput = bngInput;
	this.timeoutDurationMS = timeoutDurationMS;
}
public void registerBngUpdaterCallback(BioNetGenUpdaterCallback callbackOwner) {
	getCallbacks().add(callbackOwner);
}
public List<BioNetGenUpdaterCallback> getCallbacks() {
	if(callbacks == null) {
		callbacks = new ArrayList<BioNetGenUpdaterCallback>();
	}
	return callbacks;
}

///**
// * Insert the method's description here.
// * Creation date: (7/11/2006 3:46:33 PM)
// * @return java.io.File
// * @param parentDir java.io.File
// */
//private static File createTempDirectory(String prefix, File parentDir) {
//	while (true) {
//		int  counter = new java.util.Random().nextInt() & 0xffff;
//		
//		File tempDir = new File(parentDir, prefix + Integer.toString(counter));
//		if (!tempDir.exists()) {
//			tempDir.mkdir();
//			return tempDir;
//		}
//	}
//}

/**
 * Insert the method's description here.
 * Creation date: (6/23/2005 3:57:30 PM)
 */
public BNGOutput executeBNG() throws BNGException {
	if (executable != null) {
		throw new BNGException("You can only run BNG one at a time!");
	}

	BNGOutput bngOutput = null;
	startTime = System.currentTimeMillis();
	
	try {
		// prepare executables and working directory;
		
		initialize();
		
		File bngInputFile = null;
		FileOutputStream fos = null;


		String tempFilePrefix = workingDir.getName();
		try {
			bngInputFile = new File(workingDir, tempFilePrefix + suffix_input);
			fos = new java.io.FileOutputStream(bngInputFile);
		}catch (java.io.IOException e){
			if (lg.isEnabledFor(Level.WARN) ) {
				lg.warn("error opening input file '"+bngInputFile,e);
			}
			e.printStackTrace(System.out);
			throw new RuntimeException("error opening input file '"+bngInputFile.getName()+": "+e.getMessage());
		}	
			
		PrintWriter inputFile = new PrintWriter(fos);
		inputFile.print(bngInput.getInputString());
		inputFile.close();
		
		// run BNG
		String[] cmd = new String[] {file_exe_bng.getAbsolutePath(), bngInputFile.getAbsolutePath()};
//		executable = new org.vcell.util.Executable(cmd);
		long timeoutMS = 0;
		if (timeoutDurationMS != null){
			timeoutMS = timeoutDurationMS.longValue();
		}
		;executable = new BioNetGenExecutable(cmd,timeoutMS);
		executable.setWorkingDir(workingDir);
		executable.inheritCallbacks(getCallbacks());
		executable.start();
		
		String stdoutString = executable.getStdoutString();
		if (executable.getStatus() == org.vcell.util.ExecutableStatus.STOPPED && stdoutString.length() == 0) {
			// TODO: this never gets executed, there is some stdout string even though the executable was stopped by the user
			stdoutString = "Stopped by user. No output from BioNetGen.";	
		}
		
		File[] files = workingDir.listFiles();
		ArrayList<String> filenames = new ArrayList<String>();
		ArrayList<String> filecontents = new ArrayList<String>();
		Pattern sharedLibPattern = OperatingSystemInfo.getInstance().getSharedLibraryPattern();
	
		for (int i = 0; i < files.length; i ++) {
			String filename = files[i].getName();
			if (sharedLibPattern.matcher(filename).matches() 
					|| filename.equals(file_exe_bng.getName()) || filename.equals(file_exe_run_network.getName())){
				continue;
			}
			filenames.add(files[i].getName());
			filecontents.add(FileUtils.readFileToString(files[i]));
		}		
		
		bngOutput = new BNGOutput(stdoutString, filenames.toArray(new String[0]), filecontents.toArray(new String[0]));
		
	} catch(ExecutableException | IOException ex ){
		if (lg.isEnabledFor(Level.WARN) ) {
			lg.warn("error executable BNG", ex); 
		}
		if (executable.getStderrString().trim().length() == 0) {
			throw new BNGException("Error executing BNG", ex); 
		}
		throw new BNGException(executable.getStderrString(),ex);
	} finally {
		executable = null;		
		if (lg.getEffectiveLevel( ).isGreaterOrEqual(Level.INFO) ) {
			if (workingDir != null && workingDir.exists()) {
				File[] files = workingDir.listFiles();

				for (int i = 0; i < files.length; i ++) {
					files[i].delete();
				}		
			}
		}

		workingDir = null;
	}

	return bngOutput;
}

/**
 * Insert the method's description here.
 * Creation date: (9/19/2006 11:36:49 AM)
 * @throws IOException 
 */
private static synchronized void initialize() throws IOException {
	File bngHome = new File(ResourceUtil.getVcellHome(), "BioNetGen");
	if (!bngHome.exists()) {
		bngHome.mkdirs();
	}
	
	workingDir = bngHome;//createTempDirectory(prefix, bngHome);	
	System.out.println("BNG working directory is " + bngHome.getAbsolutePath());
	
	file_exe_bng = new java.io.File(bngHome, EXE_BNG);
	file_exe_run_network = new java.io.File(bngHome, EXE_RUN_NETWORK);
	ResourceUtil.loadExecutable(EXE_BNG, VersionedLibrary.CYGWIN_DLL_BIONETGEN,bngHome); 
		
	if (!file_exe_bng.exists()) {
		ResourceUtil.writeFileFromResource(RES_EXE_BNG, file_exe_bng);
	}
	if (!file_exe_run_network.exists()) {	
		ResourceUtil.writeFileFromResource(RES_EXE_RUN_NETWORK, file_exe_run_network);
	}
}

/**
 * Insert the method's description here.
 * Creation date: (6/23/2005 3:57:30 PM)
 */
public void stopBNG() throws Exception {
	setToStopped();
	if (executable != null) {
		executable.stop();
	}
}
private void setToStopped() {
	this.stopped = true;
}
public boolean isStopped() {
	return stopped;
}
public final long getStartTime() {
	return startTime;
}
}