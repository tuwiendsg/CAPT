package org.cloudbus.cloudsim.soheilsim;

import java.util.Calendar;

public class Parameters {
	
	public static final int MAX_CAPACITY=400; // max num. of cuncurrent vms on DS
	public static final int MAX_RESERVED_CAPACITY=(int)(MAX_CAPACITY/2);
	public static final int MAX_ONDEMAND_CAPACITY=(int)(MAX_CAPACITY/2);
	
	public static final long STORAGE = 16380; //image size (MB)
	public static final int RAM = 1740; //vm memory (MB)
	public static final int MIPS = 1000;//100000; //1000; // 1000->m1small 100000->others
	public static final long BW = 1000;
	public static final int PES_NUM = 1; //number of cpu
	
	public static long MIN_EXEC_LENGTH=12000000;//10000; //XXX
	public static long MAX_EXEC_LENGTH=864000000;
	
	
	//here you can get the prices:  http://aws.amazon.com/ec2/previous-generation/
	
	
	//public static final int SIMULATION_LENGTH=24; //in hours
	
	
	/* price per hour for amazon m1.small instance in US-East region */
	public static double ONDEMAND_PRICE=0.044;
	public static double RESERVED_PRICE=0.027;
	
	/* price per hour for amazon m1.large instance in US-East region */
	//public static double ONDEMAND_PRICE=0.175;
	//public static double RESERVED_PRICE=0.061;
	
	/* price per hour for amazon m2.2xlarge instance in US-East region */
	//public static double ONDEMAND_PRICE=0.490;
	//public static double RESERVED_PRICE=0.124;
	
	
	/* price per hour for amazon c1.medium instance in US-East region */
	//public static double ONDEMAND_PRICE=0.130;
	//public static double RESERVED_PRICE=0.049;
	
	
	//public static double MIN_SPOT_PRICE=0.005;
	//public static double MAX_SPOT_PRICE=0.040;
	
	//public static double [] SPOT_PRICE={0.007,0.01,0.015,0.02,0.025,0.03,0.034,0.04,0.045};//,0.05,0.055};
	
	//from here you can take the archive of spot instance price: http://spot.scem.uws.edu.au/ec2si/Home.jsp
	//public static String SPOT_PRICE_SOURCE_FILE ="us-east-1a.Linux-m1.large.csv"; 
	public static String SPOT_PRICE_SOURCE_FILE ="us-east-1a.Linux-m1.small.csv"; 
	//public static String SPOT_PRICE_SOURCE_FILE ="us-east-1a.Linux-m2.2xlarge.csv"; 
	//public static String SPOT_PRICE_SOURCE_FILE ="us-east-1a.Linux-c1.medium.csv"; 
	
	
	public static long fileID=Calendar.getInstance().getTimeInMillis();
	
	//public static final int [] weekDayLoad={13, 14 , 16, 20, 27, 35, 43, 54, 66, 77, 85, 89, 90, 89, 85, 78, 66, 54, 43, 35, 27, 21, 16, 14};
	
	public static final int [] weekDayLoad={11, 12, 14, 18, 25, 33, 41, 52, 64, 75, 83, 87, 88, 87, 83, 76, 64, 52, 41, 33, 25, 19, 14, 12};
	public static final int [] weekEndLoad={4, 5, 6, 8, 11, 15, 19, 22, 30, 34, 39, 42, 43, 42, 39, 34, 30, 22, 19, 15, 11, 8, 6, 5 };
	
	//public static final int [] weekDayLoad={20, 22, 24, 34, 38, 64, 80, 100, 124, 148, 162, 170, 172, 170, 162, 148, 124, 100, 80, 62, 46, 32, 22, 20};
	//public static final int [] weekEndLoad={5, 9, 10, 12, 20, 28,34, 40, 56, 64, 74, 80, 82, 80, 74, 63, 56, 42, 33, 26, 20, 14, 8, 7 };
	
	
	//public static final int [] weekDayLoad={150, 150, 150, 150, 150, 150, 150, 150, 150, 150, 150, 150, 150, 150, 150, 150, 150, 150, 150, 150, 150, 150, 150, 150};
	//public static final int [] weekEndLoad={150, 150, 150, 150, 150, 150, 150, 150, 150, 150, 150, 150, 150, 150, 150, 150, 150, 150, 150, 150, 150, 150, 150, 150};
	
	
	//public static final int [] weekDayLoad={15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90, 95, 100, 105, 110, 115, 120, 125, 130};
	
	//public static final int [] weekDayLoad={6, 7, 8, 12, 20, 28, 36,47, 57, 70, 78, 82, 83, 82, 78, 71, 59, 47, 36, 28, 20, 14, 9, 7};
	//public static final int [] weekEndLoad={1, 2, 3, 5, 8, 12, 16, 19, 27, 31, 36, 39, 40, 39, 39, 31, 27, 19, 16, 12, 8, 5, 3, 2 };
	
	public static int OPTION_EXPIRATION_TIME= 30; // IN DAYS
	

	////////////////////////////////////////
	public static long SEED1 = 1995;
	public static long SEED2 = 9519;
	
	public static double RESERVED_SERIAL_PROB = 0.244;
	public static double RESERVED_POW2_PROB =0.576 ;
	public static double RESERVED_ULOW = 0.8f; //0.8f
	public static double RESERVED_UMED =4.5f;//4.2f ; //orig: 4.5f
	public static double RESERVED_UHI = 7f;// 5f; //7f;
	public static double RESERVED_UPROB = 0.86;

	public static double RESERVED_A1 =4.2 ;
	public static double RESERVED_B1 =0.94;//0.9 ; //0.94;
	public static double RESERVED_A2 =312 ;
	public static double RESERVED_B2 = 0.03;
	public static double RESERVED_PA = -0.0054;
	public static double RESERVED_PB =  0.78;

	public static double RESERVED_AARR =10.2302;// 5.2303 ; //10.2302;
	public static double RESERVED_BARR = 0.4871;//0.5071; //0.4871;
	public static double RESERVED_ANUM = 8.1737;//6.1773; //8.1737;
	public static double RESERVED_BNUM = 3.9631;//2.9631 ; //3.9631;
	public static double RESERVED_ARAR = 1.0225;
	
	
	public static double ONDEMAND_SERIAL_PROB = 0.244;
	public static double ONDEMAND_POW2_PROB =0.576 ;
	public static double ONDEMAND_ULOW = 0.8f;
	public static double ONDEMAND_UMED =4.2f ; //orig: 4.5f
	public static double ONDEMAND_UHI =  5f; //7f
	public static double ONDEMAND_UPROB = 0.86;

	public static double ONDEMAND_A1 =4.2 ;
	public static double ONDEMAND_B1 =0.9 ; //0.94
	public static double ONDEMAND_A2 =312 ;
	public static double ONDEMAND_B2 = 0.03;
	public static double ONDEMAND_PA = -0.0054;
	public static double ONDEMAND_PB =  0.78;

	public static double ONDEMAND_AARR = 5.2303 ; //10.2302
	public static double ONDEMAND_BARR = 0.5071; //0.4871
	public static double ONDEMAND_ANUM = 6.1773; //8.1737
	public static double ONDEMAND_BNUM = 2.9631 ; //3.9631
	public static double ONDEMAND_ARAR = 1.0225;
	
	
	

	
}
