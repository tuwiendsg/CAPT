package org.cloudbus.cloudsim.soheilsim;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.core.SimEntity;
import org.cloudbus.cloudsim.core.SimEvent;

public class OptionHandler extends SimEntity {

	static double totalOptionBought=0.0;

	static int totalOptionBought_count=0;
	static int totalOptionExercised_count=0;
	static int totalOptionExpired_count=0;
	static int totalQOSViolation_count=0;

	static int totalODRejection=0;
	
	static int totalRequestBuyCount =0;
	static int totalRequestExerCount =0;
	
	//static int OD_QOSViolation_count =0; //in v2 added
	//static int R_QOSViolation_count =0; //in v2 added

	static int spotPriceNumber;//=Parameters.SPOT_PRICE.length;
	
	static int [] OptionBoughtCount; 
	/**   counts the spot price written in option **/
	static int [] OptionExerCount_op;

	//v3!
	static int [] onDemandOutCount; 
	/**   counts the spot price when the option should be exercised **/
	static int [] OptionExerCount_sp; //counts the price
	
	static List<Cloudlet> clCausingBuy=new ArrayList<Cloudlet>();
	static Map<Cloudlet, Option> clExercised = new HashMap<Cloudlet, Option>();

	static List <Integer> clRejected = new ArrayList<Integer>();

	public static List<Option> options_list= new ArrayList<Option>();

	static SpotPriceManager spotPriceMng; //v2

	/*****************************  deprecated!  ************************************/
	static LinkedList <Double> spotPriceTracker_d = new LinkedList<Double>();
	static LinkedList <Double> spotPriceTracker_s = new LinkedList<Double>();
	static LinkedList <Integer> demandTracker = new LinkedList<Integer>();
	static LinkedList <Integer> supplyTracker = new LinkedList<Integer>();
	/*******************************************************************************/
	


	
	
	public OptionHandler(String name,SpotPriceManager mng) {
		super(name);

		spotPriceMng=mng;
		spotPriceNumber=mng.spotPrice_count;


		OptionBoughtCount= new int[spotPriceNumber];
		OptionExerCount_op= new int[spotPriceNumber];
		onDemandOutCount = new int[spotPriceNumber];
		OptionExerCount_sp = new int[spotPriceNumber];
		
		for (int i=0; i<spotPriceNumber; i++)
		{
			OptionBoughtCount[i]=0;
			OptionExerCount_op[i]=0;
			onDemandOutCount[i]=0;
			OptionExerCount_sp[i] =0;
					
		}
	}


	@Override
	public void startEntity() {
		Log.printLine(getName() + " is starting...");

	}

	@Override
	public void processEvent(SimEvent ev) {

		switch (ev.getTag()) {
		case CloudSimTags.BUY_OPTION:
			Log.printLine("** OptionHandler: buy option");
			totalRequestBuyCount++;
			processBuyingOption(ev);
			break;

		case CloudSimTags.EXECUTE_OPTION:
			Log.printLine("** OptionHandler: exercise option");
			totalRequestExerCount++;
			processExercisingOption(ev);
			break;
		default:
			Log.printLine("** OptionHandlr: should not come here!");
			break;
		}

	}

	private void processBuyingOption(SimEvent ev){
		double st= calculateStrikePrice();
		
		addToDemandTrackerList(st);
		
		Cloudlet cloudlet = (Cloudlet) ev.getData();
		if (st<= Parameters.RESERVED_PRICE){
			double prem= calculatePremiumPrice(st);
			double ext= CloudSim.clock() + (Parameters.OPTION_EXPIRATION_TIME*24*60);
			Option newOption=new Option(prem, st, ext);
			totalOptionBought+=prem;
			totalOptionBought_count++;
			options_list.add(newOption);

			clCausingBuy.add(cloudlet);

			OptionBoughtCount[findSpotIndexByValue(st)]++;


			
			LogInFile.saveInfo(CloudSim.clock()+"\t" + cloudlet.getCloudletId()+"\t"+ newOption.getExpirationTime() + "\t" + newOption.getPremium() + "\t"+ newOption.getStrikePrice(), "CauseOfOptionBuy");

			LogInFile.saveInfo("BOUGHT"+"\t"+CloudSim.clock()+"\t" + newOption.getExpirationTime() + "\t" + newOption.getPremium() + "\t"+ newOption.getStrikePrice(), "Options");
		}
		else
		{
			//clRejected.add(cloudlet.getCloudletId()); //XXX ino ke comment mikonam total profit bishtar mishe az halate noOpt
			
			LogInFile.saveInfo("BUYING CANCELD"+"\t"+ CloudSim.clock()+"\t" +"Because of high strike price of "+st, "Options");

		}

	}


	private void processExercisingOption(SimEvent ev){

		Cloudlet cloudlet = (Cloudlet) ev.getData();
		double st= calculateStrikePrice();
		if (cloudlet.getJobType() == JobType.RESERVED)
		{
			if (haveOptions()){
				Option ex_option= options_list.get(0);

				//Cloudlet ID    STATUS    Data center ID    VM ID        Time    Start Time    Finish Time    Job type    Price
				LogInFile.saveInfo(CloudSim.clock()+ "\t"+cloudlet.getCloudletId() +  "\t" +"OUTSOURCED"+ "\t"+cloudlet.getVmId()+ "\t" +

				"\t"+ cloudlet.getJobType()+ "\t" +ex_option.getStrikePrice()+ "\t" + ex_option.getExpirationTime()+"\t"+ ex_option.getPremium(),"Outsourced");


				clExercised.put(cloudlet, ex_option);
				OptionExerCount_op[findSpotIndexByValue(ex_option.getStrikePrice())]++;
				OptionExerCount_sp[findSpotIndexByValue(st)]++;

				LogInFile.saveInfo("EXERCISED"+"\t"+CloudSim.clock()+"\t" + ex_option.getExpirationTime() + "\t" + ex_option.getPremium() + "\t"+ ex_option.getStrikePrice(), "Options");
				totalOptionExercised_count++;
				options_list.remove(0);
				
				addToSupplyTrackerList(st);

			}
			else
			{
				clRejected.add(cloudlet.getCloudletId());
				
				Log.printLine(CloudSim.clock()+"OptionHandlr:ERROR- no option to be executed");
				LogInFile.saveInfo("ERROR"+"\t"+ CloudSim.clock()+"\t "+ "QOS violation! no option to be executed", "Options");
				totalQOSViolation_count++;

				//if (cloudlet.getJobType() == JobType.ONDEMAND)
				//	OD_QOSViolation_count++;
			//	else if (cloudlet.getJobType() == JobType.RESERVED)
				//	R_QOSViolation_count++;



			}
		}
		else if (cloudlet.getJobType() == JobType.ONDEMAND){

			if (st<= Parameters.ONDEMAND_PRICE){
				Option temp_option = new Option(0, st, 0); // just to keep old data structure
				clExercised.put(cloudlet, temp_option);
				
				addToSupplyTrackerList(st);
				
				onDemandOutCount[findSpotIndexByValue(st)]++;

			}
			else
			{
				clRejected.add(cloudlet.getCloudletId());
				
				Log.printLine(CloudSim.clock()+"OptionHandlr:Warning- OnDemand request rejected because of high spot price");
				LogInFile.saveInfo("Warning"+"\t"+ CloudSim.clock()+"\t "+ "OnDemand request rejected because of high spot price of "+st, "OD_Rejection");
				totalODRejection++;
				
			}

		}

	}



	private double calculateStrikePrice() {
		//return limited_rand(Parameters.MAX_SPOT_PRICE,Parameters.MIN_SPOT_PRICE);
		// return rand_inRange(); old implementation
		return spotPriceMng.getSpotPriceForTime(Math.round(CloudSim.clock()));
	}


	static double limited_rand(double limitMax,double limitMin)
	{
		double rnd=-1;//= Math.random();
		while (true){
			rnd=Math.random();
			if( rnd>=limitMin && rnd<=limitMax)
				return rnd;
		}
	}


	/*	
	static double rand_inRange()
	{
		 old implementation
		//Min + (int)(Math.random() * ((Max - Min) + 1))
		int Max=Parameters.SPOT_PRICE.length;
		int randomIndex= (int)(Math.random() * (Max));
		if (randomIndex > (Parameters.SPOT_PRICE.length-1) )
			LogInFile.saveInfo("ERROR"+"\t"+ randomIndex, "ERROR");
		return Parameters.SPOT_PRICE[randomIndex];



	}
	 */

	static int findSpotIndexByValue(double val)
	{ 
		for (int i=0; i<spotPriceNumber;i++)
		{
			if (spotPriceMng.spotPrice[i]==val)
				return i;
		}
		return -1;
	}


	private double calculatePremiumPrice(double curStockPrice) {
		//curStockPrice=30;
		double r=0.1956;	//annual interest rate (risk free rate) in decimal (e.g. 0.1)
		double v=0.314000;	//volatility in decimal
		double T=0.082;//(Parameters.OPTION_EXPIRATION_TIME/365.0);	//Maturity in years //XXX dasti dadam
		double S=curStockPrice;	//current stock price
		double X=curStockPrice;//strike price
		double H=curStockPrice + 0.005;//high barrier
		double L=curStockPrice -0.005;//low barrier
		double q=0.000000; //Continuous dividend yield in decimal
		double optionValue= BTT.CalculateBTT(r, v, T, S, X, H, L, q);
		return optionValue;
	}


	@Override
	public void shutdownEntity() {
		// TODO Auto-generated method stub
		
		
		Log.printLine("totalOptionBought: "+ totalOptionBought);
		
		LogInFile.saveInfo("total Option Buy-Request count: "+ totalRequestBuyCount, "Extra");
		LogInFile.saveInfo("total Option Exer-Request count: "+ totalRequestExerCount, "Extra");
		
		LogInFile.saveInfo("total Option Bought price: "+ totalOptionBought, "Extra");
		LogInFile.saveInfo("total Option Bought Count: "+ totalOptionBought_count, "Extra");
		LogInFile.saveInfo("total Option Exercised Count: "+ totalOptionExercised_count, "Extra");
		LogInFile.saveInfo("total Option Expired Count: "+ totalOptionExpired_count, "Extra");
		LogInFile.saveInfo("total Option QOS Violation Count: "+ totalQOSViolation_count, "Extra");

		//LogInFile.saveInfo("OnDemand QOS Violation Count: "+ OD_QOSViolation_count, "Extra");
		//LogInFile.saveInfo("Reserved QOS Violation Count: "+ R_QOSViolation_count, "Extra");
		
		LogInFile.saveInfo("OnDemand Request rejection Count: "+ totalODRejection, "Extra");
		
		
		/* deprecated
		LogInFile.saveInfo("spotPriceTracker of demand Count: "+ spotPriceTracker_d.size(), "Extra");
		LogInFile.saveInfo("DemandTracker  Count: "+ demandTracker.size(), "Extra");
		
	
		LogInFile.saveInfo("spotPriceTracker of supply  Count: "+ spotPriceTracker_s.size(), "Extra");
		LogInFile.saveInfo("supplyTracker  Count: "+ supplyTracker.size(), "Extra");
		
		
		for (int i= 0 ; i < spotPriceTracker_d.size(); i++)
			LogInFile.saveInfo(spotPriceTracker_d.get(i) +"\t"+demandTracker.get(i), "demandTracker");
		
		for (int i= 0 ; i < spotPriceTracker_s.size(); i++)
			LogInFile.saveInfo(spotPriceTracker_s.get(i) +"\t"+supplyTracker.get(i), "supplyTracker");
*/
		
	}


	public static List<? extends Option> getOptions() {
		return options_list;
	}


	public static void setOptions(List<? extends Option> options) {
		options = options_list;
	}

	public static int numberOfAvailableOptions()
	{
		removeExpiredOptions();		
		return options_list.size();
	}

	public static void removeExpiredOptions()
	{


		Iterator<Option> itr = options_list.iterator();
		while(itr.hasNext())
		{
			Option ep = itr.next();
			if (ep.isExpired(CloudSim.clock()))
			{
				totalOptionExpired_count++;
				LogInFile.saveInfo("EXPIRED"+"\t"+CloudSim.clock()+"\t" + ep.getExpirationTime() + "\t" + ep.getPremium() + "\t"+ ep.getStrikePrice(), "Options");
				itr.remove();
			}

		}
		/*
		for (Option ep : options_list)
		{ 
			if (ep.isExpired(CloudSim.clock()))
			{
				totalOptionExpired_count++;
				LogInFile.saveInfo("EXPIRED"+"\t"+CloudSim.clock()+"\t" + ep.getExpirationTime() + "\t" + ep.getPremium() + "\t"+ ep.getStrikePrice(), "Options");
				options_list.remove(ep);
			}

		}
		 */


	}


	public static List<Cloudlet> getCloudletsCausingOptionBuy() {
		return clCausingBuy;
	}


	public static Map<Cloudlet, Option> getCloudletsAndExercisedFunctions() {
		return clExercised;
	}


	public static int[] getOptionBoughtCount() {
		return OptionBoughtCount;
	}


	public static int[] getOptionExerCount_sp() {
		return OptionExerCount_sp;
	}
	
	public static int[] getOptionExerCount_op() {
		return OptionExerCount_op;
	}
	
	public static int[] getOnDemandOutCount() {
		return onDemandOutCount;
	}
	
	
	public static double getTotalOptionBoughtCost()
	{
		return totalOptionBought;
	}
	
	public static List<Integer> getRejectedCloudlets() {
		return clRejected;
	}
	
	
	public void addToDemandTrackerList(double sprice)
	{
		if ( spotPriceTracker_d.size() == 0)
		{
			spotPriceTracker_d.addLast(sprice);
			demandTracker.addLast(1);
		}
		else
		{
			double lastItem = spotPriceTracker_d.getLast();
			
			if ( lastItem == sprice)
			{
				Integer last= demandTracker.pollLast();
				last++;
				demandTracker.addLast(last);
			}
			else
			{
				spotPriceTracker_d.addLast(sprice);
				demandTracker.addLast(1);
			}
		}
	}
	
	
	
	public void addToSupplyTrackerList (double sprice)
	{
		if ( spotPriceTracker_s.size() == 0)
		{
			spotPriceTracker_s.addLast(sprice);
			supplyTracker.addLast(1);
		}
		else
		{
			double lastItem = spotPriceTracker_s.getLast();
			
			if ( lastItem == sprice)
			{
				Integer last= supplyTracker.pollLast();
				last++;
				supplyTracker.addLast(last);
			}
			else
			{
				spotPriceTracker_s.addLast(sprice);
				supplyTracker.addLast(1);
			}
		}
		
		
	}

	public static LinkedList<Integer> getDemandTracker()
	{
		return demandTracker;
	}
	
	
	public static LinkedList<Double> getSpotPrice_d_Tracker()
	{
		return spotPriceTracker_d;
	}
	
	
	public static LinkedList<Integer> getSupplyTracker()
	{
		return supplyTracker;
	}
	
	
	public static LinkedList<Double> getSpotPrice_s_Tracker()
	{
		return spotPriceTracker_s;
	}
	
	
	public static boolean haveOptions()
	{
		removeExpiredOptions();
		if (options_list.size()>0)
			return true;
		else
			return false;
	}


}
