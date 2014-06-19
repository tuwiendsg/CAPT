package org.cloudbus.cloudsim.soheilsim;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.cloudbus.cloudsim.Log;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//Min + (int)(Math.random() * ((Max - Min) + 1))
	/*	for (int i=0;i <20; i++ )
		{
			double rand=rand_inRange();
			//double rand=1/((1/Parameters.MAX_SPOT_PRICE) + (Math.random() * (((1/Parameters.MIN_SPOT_PRICE) - (1/Parameters.MAX_SPOT_PRICE)) + 1)));
			//double rand= limited_rand(Parameters.MAX_SPOT_PRICE,Parameters.MIN_SPOT_PRICE);
Log.printLine(rand);
		}
		*/
		Log.printLine("PEoD: "+ calcElasticity(150,110,9,10));
		
		/////////////
		
	/*	ArrayList <Job> jobs = new ArrayList<Job>();
		WorkloadLublin99 wl=new WorkloadLublin99(JobType.ONDEMAND, 0, 100000000);
		
		//WorkloadLublin99Orig wl=new WorkloadLublin99Orig(300,false,Parameters.SEED);
		wl.setStartHour(0);
		wl.setNumJobs(1000);
		wl.setMaximumWorkloadDuration(14*24);
		jobs = wl.generateWorkload();
		
		System.out.println("jobs size: "+ jobs.size());
		
		
		for (Job j : jobs){
		j.printJob();
		
		}
		*/
		
		SpotPriceManager mng= new SpotPriceManager();
		mng.printSpotPrices();
		System.out.println("spot price retrieved: "+mng.getSpotPriceForTime(10L));
		
		
		
	}
	
	static double limited_rand(double limitMax,double limitMin)
	{
		//DecimalFormat dft = new DecimalFormat("###.##");
		
	  double rnd=-1;//= Math.random();
	  while (true){
		  rnd=Math.random();
		  if( rnd>=limitMin && rnd<=limitMax)
			  return rnd;
	}
	}
	  
	/*static double rand_inRange()
	{
		//Min + (int)(Math.random() * ((Max - Min) + 1))
		int Max=Parameters.SPOT_PRICE.length;
		int randomIndex= (int)(Math.random() * (Max));
		return Parameters.SPOT_PRICE[randomIndex];
	}*/
	
	
	  static double calcElasticity(double ds_new, double ds_old, double price_new, double price_old)
	  {
		  /*with this formula the order of old and new parameters does not matter
		   * PEoD = (% Change in Quantity Demanded)/(% Change in Price)

			(% Change in Quantity Demanded) = [[QDemand(NEW) - QDemand(OLD)] / [QDemand(OLD) + QDemand(NEW)]] *2]

			(% Change in Price) = [[Price(NEW) - Price(OLD)] / [Price(OLD) + Price(NEW)]] *2]
		   */
		  
		  
		  double topPart=(ds_new - ds_old) / ((ds_old + ds_new)/2);
		  double downPart= (price_new - price_old) / ((price_old + price_new)/2);
		  
		  /*
		   * [QDemand(NEW) - QDemand(OLD)] / QDemand(OLD)
		   * [Price(NEW) - Price(OLD)] / Price(OLD)
		   */
		 // double topPart= (ds_new - ds_old) / (ds_old);
		 // double downPart=(price_new - price_old) / (price_old);

		  double result =topPart/downPart;
		  
		  return Math.abs(result);
	  }

}
