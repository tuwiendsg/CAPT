package org.cloudbus.cloudsim.soheilsim;

import org.cloudbus.cloudsim.core.CloudSim;

public class Option {
	
	private double premium;
	private double strikePrice;
	private double expirationTime;
	
	public Option(double p, double st, double et)
	{
		premium=p;
		strikePrice=st;
		expirationTime= et;
	
	}

	
	public  boolean isExpired(double currTime){
		if (expirationTime >= currTime)
			return false;
		
		return true;
	}
	
	public double getPremium() {
		return premium;
	}

	public void setPremium(double premium) {
		this.premium = premium;
	}

	public double getStrikePrice() {
		return strikePrice;
	}

	public void setStrikePrice(double strikePrice) {
		this.strikePrice = strikePrice;
	}

	public double getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(double expirationTime) {
		this.expirationTime = expirationTime;
	}
}
