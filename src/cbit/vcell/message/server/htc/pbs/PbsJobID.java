package cbit.vcell.message.server.htc.pbs;

import cbit.vcell.message.server.htc.HtcJobID;

public class PbsJobID extends HtcJobID {
	
	public PbsJobID(String jobID){
		super(jobID,BatchSystemType.PBS);
	}
	
	public long getPbsJobNumber(){
		return getJobNumber();
	}
		
}