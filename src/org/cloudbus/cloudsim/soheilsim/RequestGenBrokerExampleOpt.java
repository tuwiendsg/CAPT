package org.cloudbus.cloudsim.soheilsim;




/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation
 *               of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009, The University of Melbourne, Australia
 */




import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.SimEntity;
import org.cloudbus.cloudsim.core.SimEvent;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;


/**
 * An example showing how to create simulation entities
 * (a DatacenterBroker in this example) in run-time using
 * a globar manager entity (GlobalBroker).
 */
public class RequestGenBrokerExampleOpt {

	/** The cloudlet list. */
	public static OptionHandler optionHandler;


	////////////////////////// STATIC METHODS ///////////////////////

	/**
	 * Creates main() to run this example
	 */
	public static void main(String[] args) {
		
		LogInFile.saveInfo("SPOT_PRICE_SOURCE_FILE: "+ Parameters.SPOT_PRICE_SOURCE_FILE,"parameters");
		LogInFile.saveInfo("ONDEMAND_PRICE: "+ Parameters.ONDEMAND_PRICE,"parameters");
		LogInFile.saveInfo("RESERVED_PRICE: "+ Parameters.RESERVED_PRICE,"parameters");
		String wDay="";
		String wEnd="";
		for (int i =0; i< Parameters.weekDayLoad.length ;i++)
		{
			wDay += Parameters.weekDayLoad[i] +" ";
			wEnd += Parameters.weekEndLoad[i] + " ";
			
		}
		LogInFile.saveInfo("Weekday load: "+ wDay,"parameters");
		
		LogInFile.saveInfo("Weekend load: "+ wEnd,"parameters");
		
		LogInFile.saveInfo("MIPS : "+ Parameters.MIPS,"parameters");
		
		Log.printLine("Starting SoheilSim...");

		try {
			// First step: Initialize the CloudSim package. It should be called
			// before creating any entities.
			int num_user = 2;   // number of grid users
			Calendar calendar = Calendar.getInstance();
			boolean trace_flag = false;  // mean trace events

			// Initialize the CloudSim library
			CloudSim.init(num_user, calendar, trace_flag);

			

			// Second step: Create Datacenters
			//Datacenters are the resource providers in CloudSim. We need at list one of them to run a CloudSim simulation
			@SuppressWarnings("unused")
			Datacenter datacenter0 = createDatacenter("Datacenter_0");
			@SuppressWarnings("unused")
			Datacenter datacenter1 = createSpotDatacenter("Datacenter_1");
			
		/*	for ( int i=0 ; i<13;i++){
				int delay=(int)(Math.random()*100 % 60)+1;
				Log.printLine(delay);
			} */
			
			LogInFile.saveInfo("CloudletId" + "\t" + "DatacenterId"+ "\t" +"vmId"+ "\t" +"Time"+ "\t" +"JobType" , "Failed");
			
			SpotPriceManager mng= new SpotPriceManager();
			optionHandler=new OptionHandler("OptionHanlder_",mng);
			
			/*
			RequestGenBroker rg_broker = new RequestGenBroker("GlobalBroker");
			 rg_broker.createVMandJob(0,JobType.ONDEMAND);
			 rg_broker.createVMandJob(0,JobType.ONDEMAND);
			 rg_broker.createVMandJob(0,JobType.ONDEMAND);
			 rg_broker.createVMandJob(0,JobType.RESERVED);
			 rg_broker.createVMandJob(0,JobType.ONDEMAND);
			 rg_broker.createVMandJob(0,JobType.RESERVED);
*/
		

			
		
			
			PeriodicJobCreator pj=new PeriodicJobCreator();
			//pj.createHalfYearJobs();
			pj.createXWeeksJobs(2);
			//pj.createMonthlyJobs();
			// pj.createWeekdayJobs("weekday");
			//pj.createSeasonJobs();
/*
			
			try{
			// A thread that will create a new broker at 200 clock time
						Runnable monitor = new Runnable() {
							@Override
							public void run() {
								CloudSim.pauseSimulation(400);
								while (true) {
									if (CloudSim.isPaused()) {
										break;
									}
									try {
										Thread.sleep(100);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								}

								
								Log.printLine("\n\n\n" + CloudSim.clock() + ": The simulation is paused for 2 sec \n\n");

								try {
									Thread.sleep(2000);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								
								
								//VmRequestGenBroker globalBroker = new VmRequestGenBroker("GlobalBroker");
								globalBroker.createVMandJob(1);

								CloudSim.resumeSimulation();
							}
						};

						new Thread(monitor).start();
						Thread.sleep(1000);
		}
		catch (Exception e)
						{
							e.printStackTrace();
							Log.printLine("The simulation has been terminated due to an unexpected error");
						}
			
			*/
			// Fifth step: Starts the simulation
			CloudSim.startSimulation();

			// Final step: Print results when simulation is over
			
			//List<Cloudlet> newList = rg_broker.getAllCloudletReceivedList();			
			List<Cloudlet> newList = pj.getBroker().getAllCloudletReceivedList();
			
			

			CloudSim.stopSimulation();
			


			printCloudletList(newList);
			
			LogInFile.saveCloudlets(newList, "Result" );
			
			
			
			String indent="\t";
			List<Cloudlet> cl_list=OptionHandler.getCloudletsCausingOptionBuy();
			double totalPrice=0;
			double price=0;
					
			for(Cloudlet cl: cl_list)
			{
				for (Cloudlet res: newList)
				{
					if ( cl.getCloudletId()==res.getCloudletId())
					{
						price=RequestGenBrokerExampleOpt.CalcPrice(res.getActualCPUTime(),res.getJobType());
						totalPrice+=price;
						LogInFile.saveInfo( res.getCloudletId() + indent + res.getResourceId() + indent +  res.getVmId() +
								indent + res.getActualCPUTime() +
								indent + res.getExecStartTime() + indent + res.getFinishTime()+
								indent+ res.getJobType()+ indent +price,"CauseOfOptionBuy_full" );
						break;
					}
				}
			}
			
			LogInFile.saveInfo("Total Price of OnDemand jobs running in reserved pool: "+totalPrice , "Extra");
			
			
			
			Map<Cloudlet, Option> clOp_map=OptionHandler.getCloudletsAndExercisedFunctions();
			
			double totaloutCost=0;
			double price_o,price_i;
			double totalBen=0;
			int odCount=0;
			int rCount=0;
			for (Map.Entry<Cloudlet, Option> entry : clOp_map.entrySet()) {

				Cloudlet cl=entry.getKey();
				Option op=entry.getValue();
				for (Cloudlet res: newList)
				{
					
					if ( cl.getCloudletId()==res.getCloudletId())		
					{
						if(res.getJobType()== JobType.ONDEMAND)
							odCount++;
						else if (res.getJobType()== JobType.RESERVED)
							rCount++;
							
						
						price_o=RequestGenBrokerExampleOpt.CalclOutsourcedPrice(res.getActualCPUTime(),op);
						price_i=RequestGenBrokerExampleOpt.CalcPrice(res.getActualCPUTime(),res.getJobType());
						totaloutCost+=price_o;
						totalBen+=(price_i-price_o);
						LogInFile.saveInfo( res.getCloudletId() + indent + res.getResourceId() + indent +  res.getVmId() +
								indent + res.getActualCPUTime() +
								indent + res.getExecStartTime() + indent + res.getFinishTime()+
								indent+ res.getJobType()+ indent +price_i +indent+ price_o,"Outsourced_full" );
						break;
					}
				}
				
			}
			
			
			totalBen = totalBen- OptionHandler.getTotalOptionBoughtCost(); //v2

			LogInFile.saveInfo("Total Cost of  jobs running in spot market (outsourced) : "+totaloutCost , "Extra");
			LogInFile.saveInfo("Total Benefit (inCost-outCost) of  jobs running in spot market (outsourced) : "+totalBen , "Extra");
			
			LogInFile.saveInfo("Total number of OnDemand jobs running in spot market (outsourced) : "+odCount , "Extra");
			LogInFile.saveInfo("Total number of Reserved jobs running in spot market (outsourced) : "+rCount , "Extra");
			
			
			
			
			double inhouseRevenue=0.0;
			List <Integer> rejectedcls = OptionHandler.getRejectedCloudlets();
			
			for (Integer nn : rejectedcls)
				LogInFile.saveInfo(""+nn , "rejectedCloudlets");
			
			for (Cloudlet cl : newList)
			{
				if (!rejectedcls.contains((Integer) cl.getCloudletId()))
				{
					inhouseRevenue += RequestGenBrokerExampleOpt.CalcPrice(cl.getActualCPUTime(),cl.getJobType());
									
				}
				
			}
			
			double overallProfit = inhouseRevenue - totaloutCost - OptionHandler.getTotalOptionBoughtCost();
			LogInFile.saveInfo("Total Benefit of  Provider : "+overallProfit , "Extra");
			
			
			/*
			String tempStr="";
			for (int i=0; i<mng.spotPrice_count;i++)
				tempStr+=mng.spotPrice[i]+ "\t";
			
			LogInFile.saveInfo("Spot Price :\t"+tempStr , "Elasticity");
			
			 tempStr="";
			 int [] temBoughtCnt=OptionHandler.getOptionBoughtCount();
			for (int i=0; i<temBoughtCnt.length;i++)
				tempStr+=(temBoughtCnt[i]+ "\t");
			
			LogInFile.saveInfo("Bought count :\t"+tempStr , "Elasticity");
			
			
			tempStr="";
			 int [] temExerCnt=OptionHandler.getOptionExerCount();
			for (int i=0; i<temExerCnt.length;i++)
				tempStr+=(temExerCnt[i]+ "\t");
			
			LogInFile.saveInfo("Exer Count :\t"+tempStr , "Elasticity");
			
			
			
			double PEoS=0;
			double PEoD=0;
			for (int i=1;i< mng.spotPrice_count; i++)
			{
				PEoS= calcElasticity(temExerCnt[i], temExerCnt[i-1], mng.spotPrice[i], mng.spotPrice[i-1]);
				PEoD= calcElasticity(temBoughtCnt[i], temBoughtCnt[i-1], mng.spotPrice[i], mng.spotPrice[i-1]);
				LogInFile.saveInfo("PEoS for change in price [" +mng.spotPrice[i-1]+" , "+  mng.spotPrice[i]+ "] : "+ PEoS,"Elasticity");
				LogInFile.saveInfo("PEoD for change in price [" +mng.spotPrice[i-1]+" , "+  mng.spotPrice[i]+ "] : "+ PEoD,"Elasticity");
			}
			*/
			
			//Elasticity file v2
			LogInFile.saveInfo("Spot Price\tBaughtCount\tExerciseCount\tODcount\tQuantityDemanded\tQuantitySupplied" , "Elasticity2");
			 String tempStr="";
			 int [] temBoughtCnt = OptionHandler.getOptionBoughtCount();
			 int [] temExerCnt = OptionHandler.getOptionExerCount_sp();
			 int [] temODCnt = OptionHandler.getOnDemandOutCount();
			for (int i=0; i<mng.spotPrice_count;i++){
				LogInFile.saveInfo(mng.spotPrice[i]+ "\t" + temBoughtCnt[i] +"\t" + temExerCnt[i] +"\t" + temODCnt[i] + "\t" + (temBoughtCnt[i]+temODCnt[i]) +"\t"+( 5000-(temBoughtCnt[i]+temODCnt[i])) , "Elasticity2");
					
			}
			
			
			LogInFile.saveInfo("From price\tTo price\tPEoD\tPEoS", "Elasticity");
					
			double PEoS=0;
			double PEoD=0;
			for (int i=1;i< mng.spotPrice_count; i++)
			{
				PEoS= calcElasticity((temExerCnt[i]+temODCnt[i]), (temExerCnt[i-1]+temODCnt[i-1]), mng.spotPrice[i], mng.spotPrice[i-1]);
				PEoD= calcElasticity((temBoughtCnt[i]+temODCnt[i]), (temBoughtCnt[i-1]+temODCnt[i-1]), mng.spotPrice[i], mng.spotPrice[i-1]);
				LogInFile.saveInfo(mng.spotPrice[i-1]+"\t"+  mng.spotPrice[i]+ "\t"+ PEoD + "\t"+ PEoS,"Elasticity");
				//LogInFile.saveInfo("PEoD for change in price [" +mng.spotPrice[i-1]+" , "+  mng.spotPrice[i]+ "] : "+ PEoD,"Elasticity");
			}
			
			
			
			/* deprecated
			LinkedList<Double> spotPrice_d =OptionHandler.getSpotPrice_d_Tracker();
			LinkedList<Double> spotPrice_s =OptionHandler.getSpotPrice_s_Tracker();
			LinkedList<Integer> demandCount =OptionHandler.getDemandTracker();
			LinkedList<Integer> supplyCount =OptionHandler.getSupplyTracker();
			
			double PEoS=0;
			 double PEoD=0;
			for (int i=1;i< spotPrice_s.size(); i++)
			{
				PEoS = calcElasticity(supplyCount.get(i), supplyCount.get(i-1), spotPrice_s.get(i),spotPrice_s.get(i-1));
				LogInFile.saveInfo(spotPrice_s.get(i-1)+"\t"+  spotPrice_s.get(i)+ "\t"+ PEoS,"PEoS");
			}
			
			for (int i=1;i< spotPrice_d.size(); i++)
			{

				PEoD= calcElasticity(demandCount.get(i), demandCount.get(i-1),spotPrice_d.get(i), spotPrice_d.get(i-1));

				LogInFile.saveInfo(spotPrice_d.get(i-1) +"\t"+  spotPrice_d.get(i)+ "\t"+ PEoD,"PEoD");
			}
			 */
			
			
			
			Log.printLine("SoheilSim finished!");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Log.printLine("The simulation has been terminated due to an unexpected error");
		}
	}

	private static Datacenter createDatacenter(String name){

		// Here are the steps needed to create a PowerDatacenter:
		// 1. We need to create a list to store one or more
		//    Machines
		List<Host> hostList = new ArrayList<Host>();

		// 2. A Machine contains one or more PEs or CPUs/Cores. Therefore, should
		//    create a list to store these PEs before creating
		//    a Machine.
		List<Pe> peList1 = new ArrayList<Pe>();

		int mips = Parameters.MIPS *Parameters.MAX_CAPACITY;

		// 3. Create PEs and add these into the list.

		peList1.add(new Pe(0, new PeProvisionerSimple(mips))); // need to store Pe id and MIPS Rating
		

		

		//4. Create Hosts with its id and list of PEs and add them to the list of machines
		int hostId=0;
		int ram = Parameters.RAM *Parameters.MAX_CAPACITY;; //host memory (MB)
		long storage = Parameters.STORAGE *Parameters.MAX_CAPACITY;; //host storage
		int bw = (int)Parameters.BW *Parameters.MAX_CAPACITY;

		hostList.add(
    			new Host(
    				hostId,
    				new RamProvisionerSimple(ram),
    				new BwProvisionerSimple(bw),
    				storage,
    				peList1,
    				new VmSchedulerTimeShared(peList1)
    			)
    		); // This is our first machine



		// 5. Create a DatacenterCharacteristics object that stores the
		//    properties of a data center: architecture, OS, list of
		//    Machines, allocation policy: time- or space-shared, time zone
		//    and its price (G$/Pe time unit).
		String arch = "x86";      // system architecture
		String os = "Linux";          // operating system
		String vmm = "Xen";
		double time_zone = 10.0;         // time zone this resource located
		double cost = 3.0;              // the cost of using processing in this resource
		double costPerMem = 0.05;		// the cost of using memory in this resource
		double costPerStorage = 0.1;	// the cost of using storage in this resource
		double costPerBw = 0.1;			// the cost of using bw in this resource
		LinkedList<Storage> storageList = new LinkedList<Storage>();	//we are not adding SAN devices by now

		DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
                arch, os, vmm, hostList, time_zone, cost, costPerMem, costPerStorage, costPerBw);


		// 6. Finally, we need to create a PowerDatacenter object.
		Datacenter datacenter = null;
		try {
			datacenter = new Datacenter(name, characteristics, new VmAllocationPolicySimple(hostList), storageList, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return datacenter;
	}
	
	
	private static Datacenter createSpotDatacenter(String name){

		// Here are the steps needed to create a PowerDatacenter:
		// 1. We need to create a list to store one or more
		//    Machines
		List<Host> hostList = new ArrayList<Host>();

		// 2. A Machine contains one or more PEs or CPUs/Cores. Therefore, should
		//    create a list to store these PEs before creating
		//    a Machine.
		List<Pe> peList1 = new ArrayList<Pe>();

		int mips = Parameters.MIPS *Parameters.MAX_CAPACITY*1000;

		// 3. Create PEs and add these into the list.

		peList1.add(new Pe(0, new PeProvisionerSimple(mips))); // need to store Pe id and MIPS Rating
		

		

		//4. Create Hosts with its id and list of PEs and add them to the list of machines
		int hostId=0;
		int ram = Parameters.RAM *Parameters.MAX_CAPACITY * 1000; //host memory (MB)
		long storage = Parameters.STORAGE *Parameters.MAX_CAPACITY * 1000; //host storage
		int bw = (int)Parameters.BW *Parameters.MAX_CAPACITY * 1000;

		hostList.add(
    			new Host(
    				hostId,
    				new RamProvisionerSimple(ram),
    				new BwProvisionerSimple(bw),
    				storage,
    				peList1,
    				new VmSchedulerTimeShared(peList1)
    			)
    		); // This is our first machine



		// 5. Create a DatacenterCharacteristics object that stores the
		//    properties of a data center: architecture, OS, list of
		//    Machines, allocation policy: time- or space-shared, time zone
		//    and its price (G$/Pe time unit).
		String arch = "x86";      // system architecture
		String os = "Linux";          // operating system
		String vmm = "Xen";
		double time_zone = 10.0;         // time zone this resource located
		double cost = 3.0;              // the cost of using processing in this resource
		double costPerMem = 0.05;		// the cost of using memory in this resource
		double costPerStorage = 0.1;	// the cost of using storage in this resource
		double costPerBw = 0.1;			// the cost of using bw in this resource
		LinkedList<Storage> storageList = new LinkedList<Storage>();	//we are not adding SAN devices by now

		DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
                arch, os, vmm, hostList, time_zone, cost, costPerMem, costPerStorage, costPerBw);


		// 6. Finally, we need to create a PowerDatacenter object.
		Datacenter datacenter = null;
		try {
			datacenter = new Datacenter(name, characteristics, new VmAllocationPolicySimple(hostList), storageList, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return datacenter;
	}
	
	

	//We strongly encourage users to develop their own broker policies, to submit vms and cloudlets according
	//to the specific rules of the simulated scenario
	private static DatacenterBroker createBroker(String name){

		DatacenterBroker broker = null;
		try {
			broker = new DatacenterBroker(name);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return broker;
	}

	/**
	 * Prints the Cloudlet objects
	 * @param list  list of Cloudlets
	 */
	private static void printCloudletList(List<Cloudlet> list) {
		int size = list.size();
		
		Log.printLine("----> "+ list.size());
		Cloudlet cloudlet;

		String indent = "    ";
		Log.printLine();
		Log.printLine("========== OUTPUT ==========");
		Log.printLine("Cloudlet ID" + indent + "STATUS" + indent +
				"Data center ID" + indent + "VM ID" + indent + indent + "Time" + indent + "Start Time" + indent + "Finish Time"+ indent+"Job type"+indent + "Price");

		DecimalFormat dft = new DecimalFormat("###.##");
		for (int i = 0; i < size; i++) {
			cloudlet = list.get(i);
			Log.print(indent + cloudlet.getCloudletId() + indent + indent);

			//if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS){
				//Log.print("SUCCESS");
			
			switch(cloudlet.getCloudletStatus()){
			case Cloudlet.SUCCESS:
				Log.print("SUCCESS");
				break;
			case Cloudlet.INEXEC:
				Log.print("INEXEC");
				break;
				
			case Cloudlet.CANCELED:
				Log.print("CANCELED");
				break;
				
			case Cloudlet.PAUSED:
				Log.print("PAUSED");
				break;
				
			case Cloudlet.QUEUED:
				Log.print("QUEUED");
				break;
				
				default:
					Log.print("UNKNOWN");
					break;
					
				
			}

				Log.printLine( indent + indent + cloudlet.getResourceId() + indent + indent + indent + cloudlet.getVmId() +
						indent + indent + indent + dft.format(cloudlet.getActualCPUTime()) +
						indent + indent + dft.format(cloudlet.getExecStartTime())+ indent + indent + indent + dft.format(cloudlet.getFinishTime())+
						indent+ cloudlet.getJobType()+ indent +CalcPrice(cloudlet.getActualCPUTime(),cloudlet.getJobType()) );
				
				
			//}
		}
		


	}
	
	public static double CalcPrice(double execTime, JobType jtype){
		
		int time= (int)Math.ceil(execTime/60);
		double result = -1.0;
		//Log.printLine("num-->"+ time);
		switch (jtype){
		case ONDEMAND:
			result=time*Parameters.ONDEMAND_PRICE;
			break;
		case RESERVED:
			result= time * Parameters.RESERVED_PRICE;
			break;
			
		}
		
		return result;
			
	}
	
private static double CalclOutsourcedPrice(double execTime, Option op){
		
		int time= (int)Math.ceil(execTime/60);		
		return  time* op.getStrikePrice();
			
			
	}

static double calcElasticity(double ds_new, double ds_old, double price_new, double price_old)
{
	  /*with this formula the order of old and new parameters does not matter
	   * PEoD = (% Change in Quantity Demanded)/(% Change in Price)

		(% Change in Quantity Demanded) = [[QDemand(NEW) - QDemand(OLD)] / [QDemand(OLD) + QDemand(NEW)]] *2]

		(% Change in Price) = [[Price(NEW) - Price(OLD)] / [Price(OLD) + Price(NEW)]] *2]
	   */
	  
	  
	//  double topPart=(ds_new - ds_old) / ((ds_old + ds_new)/2);
	 // double downPart= (price_new - price_old) / ((price_old + price_new)/2);
	  
	  /*
	   * [QDemand(NEW) - QDemand(OLD)] / QDemand(OLD)
	   * [Price(NEW) - Price(OLD)] / Price(OLD)
	   */
	  double topPart= (ds_new - ds_old) / (ds_old);
	  double downPart=(price_new - price_old) / (price_old);

	  double result =topPart/downPart;
	  
	  return Math.abs(result);
}
	
	
	
	
}

