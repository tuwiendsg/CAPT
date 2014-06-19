package org.cloudbus.cloudsim.soheilsim;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.SimEntity;
import org.cloudbus.cloudsim.core.SimEvent;


public  class RequestGenBroker extends SimEntity {

	private static final int CREATE_BROKER = 0;
	//public static final int PERIODIC_EVENT = 67567; //choose any unused value you want to represent the tag.
	
	private List<SoheilSimVm> vmList;
	private List<Cloudlet> cloudletList;
	private   List<SoheilSimBroker> brokerList;
	private int idShift=0;
	private int sim_count=0;
	private java.util.Random random ; //v2
	public RequestGenBroker(String name) {
		super(name);
		idShift=0;
		sim_count=0;
		vmList=new  LinkedList<SoheilSimVm> ();
		cloudletList=new LinkedList<Cloudlet> ();
		brokerList=new  LinkedList<SoheilSimBroker> ();
		
		random = new java.util.Random(Parameters.SEED2);//v2
		//setBroker(createBroker(super.getName()));
		//send(getId(),20,PERIODIC_EVENT,null);
		//if(broker==null)
		//	setBroker(createBroker(super.getName()));
		
	}

	@Override
	public void processEvent(SimEvent ev) {
		
		switch (ev.getTag()) {
		case CREATE_BROKER:
			//if(broker==null)
				//setBroker(createBroker(super.getName()));
			/*
			for(int i=0; i<Parameters.SIMULATION_LENGTH;i++){
				createVMandJob(2);
				
				
			}
			*/


			break;
	//	case PERIODIC_EVENT:
			//setBroker(createBroker(super.getName()+"_"));
		//	processPeriodicEvent(ev); 
		//	break;

		default:
			//processOtherEvent(ev);
			 Log.printLine("Warning: "+CloudSim.clock()+": "+this.getName()+": default case, ignored");
			break;
		}
		
		
	}

	@Override
	public void startEntity() {
		Log.printLine(super.getName()+" is starting...");
		schedule(getId(), 0, CREATE_BROKER);
		//schedule(getId(),20,PERIODIC_EVENT);
	}
	
	
	

	/* protected void processOtherEvent(SimEvent ev) {
	   if (ev == null){
	     Log.printLine("Warning: "+CloudSim.clock()+": "+this.getName()+": Null event ignored.");
	   } else {
	     int tag = ev.getTag();
	     switch(tag){
	       case PERIODIC_EVENT: processPeriodicEvent(ev); break;
	       default: Log.printLine("Warning: "+CloudSim.clock()+":"+this.getName()+": Unknown event ignored. Tag:" +tag);
	     }
	   }
	 }*/

	

	/* private void processPeriodicEvent(SimEvent ev) {
		   float delay=2; //contains the delay to the next periodic event
		   boolean generatePeriodicEvent; //true if new internal events have to be generated
		   
		 if(sim_count<Parameters.SIMULATION_LENGTH){
			 createVMandJob(5);
			generatePeriodicEvent=true;
		 }
		 else {
			 generatePeriodicEvent=false;
		 }
		
	   
	   if (generatePeriodicEvent) send(getId(),delay,CREATE_BROKER,ev);
	 }
	
	*/

	@Override
	public void shutdownEntity() {
	}

	
	private  SoheilSimBroker createBroker(String name){

		SoheilSimBroker broker = null;
		try {
			broker = new SoheilSimBroker(name);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	
		return broker;
	}
	
	
	//--------------------------------
	public void createVMandJob(int delay,JobType jtype) {
		
		SoheilSimBroker broker= createBroker(super.getName()+idShift);
		
		sim_count++;
		//Creates a container to store VMs. This list is passed to the broker later
		LinkedList<SoheilSimVm> list = new LinkedList<SoheilSimVm>();

		//VM Parameters
		long size = Parameters.STORAGE; //image size (MB)
		int ram = Parameters.RAM; //vm memory (MB)
		int mips = Parameters.MIPS;
		long bw = Parameters.BW;
		int pesNumber = Parameters.PES_NUM; //number of cpus
		String vmm = "Xen"; //VMM name

		//create VMs



		SoheilSimVm vm = new SoheilSimVm(idShift, broker.getId(), mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
		vm.setSubmissionTime(delay); /**************/
		list.add(vm);
		setVmList(list);
		broker.submitVmList(list);
		
		
		
		// Creates a container to store Cloudlets
				LinkedList<Cloudlet> clist = new LinkedList<Cloudlet>();

				//cloudlet parameters
				//long length = 400000;
				//Min + (int)(Math.random() * ((Max - Min) + 1))
				//long length = Parameters.MIN_EXEC_LENGTH + (long)(Math.random() * ((Parameters.MAX_EXEC_LENGTH - Parameters.MIN_EXEC_LENGTH) + 1)); old v1

				int diff=(int) (Parameters.MAX_EXEC_LENGTH - Parameters.MIN_EXEC_LENGTH);
				long length = Parameters.MIN_EXEC_LENGTH + random.nextInt(diff);
				//LogInFile.saveInfo(""+length, "length");
				
				long fileSize = 300;
				long outputSize = 300;
				//int pesNumber = 1;
				UtilizationModel utilizationModel = new UtilizationModelFull();

				

				if(jtype == JobType.ONDEMAND)
					LogInFile.saveInfo(jtype+"\t"+delay+"\t"+ length, "wload_od_soheil");
				else
					LogInFile.saveInfo(jtype+"\t"+delay+"\t"+ length, "wload_res_soheil");
					
				Cloudlet cloudlet = new Cloudlet(idShift, length, pesNumber, fileSize, outputSize, utilizationModel, utilizationModel, utilizationModel);
					
				cloudlet.setUserId(broker.getId());
				cloudlet.setJobType(jtype);
				clist.add(cloudlet);
					
				
				
				setCloudletList(clist);
				broker.submitCloudletList(clist);

				

					broker.bindCloudletToVm(idShift,idShift);
				
					brokerList.add(broker);
				
				idShift++;
				
				CloudSim.resumeSimulation();
				
		
	}



	//--------------------------------
	
	
	
	
	public List<SoheilSimVm> getVmList() {
		return vmList;
	}

	protected void setVmList(List<SoheilSimVm> vmList) {
		this.vmList.addAll(vmList);
	}

	public List<Cloudlet> getCloudletList() {
		return cloudletList;
	}

	protected void setCloudletList(List<Cloudlet> cloudletList) {
		this.cloudletList.addAll(cloudletList);
	}

	/*public SoheilSimBroker getBroker() {
		return broker;
	}

	protected void setBroker(SoheilSimBroker broker) {
		this.broker = broker;
	}*/

	
	public List<Cloudlet> getAllCloudletReceivedList()
	{
		LinkedList<Cloudlet> clist = new LinkedList<Cloudlet>();
		
	for	(SoheilSimBroker broker : brokerList) {
		clist.addAll(broker.getCloudletReceivedList());//getCloudletReceivedList());
	}
		return clist;
		
	}
}

