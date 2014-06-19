package org.cloudbus.cloudsim.soheilsim;

public class Job {

	private JobType jobType;
	private long startTime;
	private long runTime;
	
	
public Job(JobType jtype, long startTime, long runTime)
{
	this.jobType=jtype;
	this.startTime=startTime;
	this.runTime=runTime;
}
	
	public JobType getJobType() {
		return jobType;
	}
	public void setJobType(JobType jobType) {
		this.jobType = jobType;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public long getRunTime() {
		return runTime;
	}
	public void setRunTime(long endTime) {
		this.runTime = endTime;
	}
	
	public void printJob()
	{
		System.out.println("JobType: "+ this.jobType +" startTime: " +this.startTime+" runTime: "+this.runTime);

		LogInFile.saveInfo(jobType+"\t"+startTime+"\t"+runTime, "Workload");
	}
}
