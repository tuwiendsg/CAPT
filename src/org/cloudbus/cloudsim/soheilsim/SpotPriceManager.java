package org.cloudbus.cloudsim.soheilsim;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;

import org.cloudbus.cloudsim.Log;

public class SpotPriceManager {
	TreeMap <Long,Double> spotPricesInTime;
	int spotPrice_count;
	double spotPrice[];
	
	public SpotPriceManager()
	{
		
		spotPricesInTime= new TreeMap<Long,Double>();
		
		TreeSet <Double> spotPrices = new TreeSet <Double>();
		
		
		String filePath = "./resource/" + Parameters.SPOT_PRICE_SOURCE_FILE;
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
	 
		try {
	 	 
			br = new BufferedReader(new FileReader(filePath));
			while ((line = br.readLine()) != null) {
	 
				String[] entry = line.split(cvsSplitBy);	 
				spotPricesInTime.put(Long.parseLong(entry[0]), Double.parseDouble(entry[1])); 
				spotPrices.add( Double.parseDouble(entry[1]));
			}
			spotPrice_count=spotPrices.size();
			spotPrice=new double[spotPrice_count];
			int count=0;
			for (Double price : spotPrices) {
				spotPrice[count]=price.doubleValue();
				count++;
			}
	
	 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	 
		System.out.println("Done");
		
		
		
	}
	
	
	double getSpotPriceForTime(long currentTime)
	{
		long lastRecordedTime= spotPricesInTime.lastKey().longValue() ;
		
		long indexKey = currentTime % (lastRecordedTime +1);
		Log.printLine("indexKey: "+ indexKey);
		
		Entry<Long,Double> resultEntry= spotPricesInTime.floorEntry(indexKey);
		double result= resultEntry.getValue().doubleValue();
		
		return result;
	}
	
	public void printSpotPrices()
	{
		for (Entry<Long,Double> entry : spotPricesInTime.entrySet()) {
			 
			System.out.println("Entry [time in min= " + entry.getKey() + " , spot price="
				+ entry.getValue() + "]");
 
		}
		
		for (int i = 0 ; i<spotPrice.length ; i++) {
			 
			System.out.println("SpotProce: " + spotPrice[i]);
 
		}
	}
	

}
