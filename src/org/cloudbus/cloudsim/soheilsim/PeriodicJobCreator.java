package org.cloudbus.cloudsim.soheilsim;

import org.cloudbus.cloudsim.Log;

public class PeriodicJobCreator {

	
	RequestGenBroker gBroker;
	private static final String WEEKDAY="weekday";
	private static final String WEEKEND="weekend";
	
	private static int total_ondemand_count;
	private static int total_reserved_count;
	
	private java.util.Random random ; //v2

	static int totalHourCount;
	
	public PeriodicJobCreator() {
		gBroker= new RequestGenBroker("GlobalBroker");
		totalHourCount=0;
		total_ondemand_count=0;
		total_reserved_count=0;
		WriteToFileExpectedTotalLoad();
		
		random = new java.util.Random(Parameters.SEED1);//v2
	}
	public void createWeekdayJobs(String endOrDay){
		int[] load;
		if(endOrDay==WEEKEND)
			load=Parameters.weekEndLoad;
		else 
			load=Parameters.weekDayLoad;
		
		int reservedload=0;
		int ondemandload=0;
		for(int thisHourLoad : load ){
			reservedload= (int)Math.floor(thisHourLoad/3.0);
			ondemandload= thisHourLoad-reservedload;
			
			for ( int i=0 ; i<ondemandload;i++){
							//Min + (int)(Math.random() * ((Max - Min) + 1))
				//int delay= (totalHourCount*60) + (int)(Math.random() * (((totalHourCount+1)*60) - (totalHourCount*60) + 1));//((int)(Math.random()*100 % 60))+(totalHourCount*60); old:v1
				int delay= (totalHourCount*60) +random.nextInt(((totalHourCount+1)*60) - (totalHourCount*60) );
				//LogInFile.saveInfo(""+delay, "delay_od");
				gBroker.createVMandJob(delay,JobType.ONDEMAND);
				total_ondemand_count++;
				
			}
			
			for ( int i=0 ; i<reservedload;i++){
				
				//int delay= (totalHourCount*60) + (int)(Math.random() * (((totalHourCount+1)*60) - (totalHourCount*60) + 1));//((int)(Math.random()*100 % 60))+(totalHourCount*60); old v1
				int delay= (totalHourCount*60) +random.nextInt(((totalHourCount+1)*60) - (totalHourCount*60) );
				//LogInFile.saveInfo(""+delay, "delay_r");
				gBroker.createVMandJob(delay,JobType.RESERVED);
				total_reserved_count++;
				
			}
			
			totalHourCount++;
		}
		
	}
	
	public void createWeeklyJobs()
	{
		for (int i=0;i<5;i++)
			createWeekdayJobs(WEEKDAY);
		
		createWeekdayJobs(WEEKEND);
		createWeekdayJobs(WEEKEND);
		

		 
	}
	
	public void createXWeeksJobs(int numberOfWeeks)
	{
		for (int i=0;i<numberOfWeeks;i++)
			createWeeklyJobs();
		
		
		LogInFile.saveInfo("Total HourCount: "+totalHourCount , "Extra");
		 LogInFile.saveInfo("Total onDemand jobs: "+total_ondemand_count , "Extra"); 	 
		 LogInFile.saveInfo("Total reserved jobs: "+total_reserved_count , "Extra"); 
	}
	
	public void createMonthlyJobs()
	{
		for (int i=0;i<4;i++)
			createWeeklyJobs();
		
		/*LogInFile.saveInfo("Total HourCount: "+totalHourCount , "Extra");
		 LogInFile.saveInfo("Total onDemand jobs: "+total_ondemand_count , "Extra"); 
		 LogInFile.saveInfo("Total reserved jobs: "+total_reserved_count , "Extra"); */
	}
	
	public void createHalfYearJobs()
	{
		for (int i=0;i<6;i++)
			createMonthlyJobs();
		
		LogInFile.saveInfo("Total HourCount: "+totalHourCount , "Extra");
		 LogInFile.saveInfo("Total onDemand jobs: "+total_ondemand_count , "Extra"); 
		 LogInFile.saveInfo("Total reserved jobs: "+total_reserved_count , "Extra"); 
	}
	
	public void createSeasonJobs()
	{
		for (int i=0;i<3;i++)
			createMonthlyJobs();
		
		LogInFile.saveInfo("Total HourCount: "+totalHourCount , "Extra");
		 LogInFile.saveInfo("Total onDemand jobs: "+total_ondemand_count , "Extra"); 
		 LogInFile.saveInfo("Total reserved jobs: "+total_reserved_count , "Extra"); 
	}
	
	
	public RequestGenBroker getBroker() {
		return gBroker;
	}
	public void setBroker(RequestGenBroker gBroker) {
		this.gBroker = gBroker;
	}
	
	
	private void WriteToFileExpectedTotalLoad()
	{
		int[] eload=Parameters.weekEndLoad;
		int[] dload=Parameters.weekDayLoad;
		int dsum=0;
		int esum=0;
		 for (int i=0; i<dload.length; i++)
		 {
			 dsum+=dload[i];
			 
		 }
		 LogInFile.saveInfo("Total number of load in 1 weekday: "+dsum , "Extra");
		 
		 
		 for (int i=0; i<eload.length; i++)
		 {
			 esum+=eload[i];
			 
		 }
		 LogInFile.saveInfo("Total number of load in 1 weekend: "+esum , "Extra");
		 
		 int res=(5*dsum)+(2*esum);
		 LogInFile.saveInfo("Total number of  load in whole week: "+res , "Extra"); 
		 
		 

		 
		 
	}
	
}
