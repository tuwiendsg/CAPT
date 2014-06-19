package org.cloudbus.cloudsim.soheilsim;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Log;

public class LogInFile {

	private static String filePref = "./output/";
	
	static void saveCloudlets(List<Cloudlet> list, String FileName)
	{ 
		int size = list.size();
		saveInfo("All received cloudlets: "+size, "Extra");
		try{
		File myFile = new File(filePref+FileName+"_"+Parameters.fileID +".txt");

		
		
		BufferedWriter out = new BufferedWriter(new FileWriter(myFile));
		

		Cloudlet cloudlet;

		String indent = "\t";
		
		out.write("Cloudlet ID" + indent + "STATUS" + indent +
				"Data center ID" + indent + "VM ID" + indent + "Time" + indent + "Start Time" + indent + "Finish Time"+ indent+"Job type"+indent + "Price");

		out.newLine();
		
		String status;
		
		//DecimalFormat dft = new DecimalFormat("###.##");
		for (int i = 0; i < size; i++) {
			cloudlet = list.get(i);
			//out.write( cloudlet.getCloudletId() + indent);

			switch(cloudlet.getCloudletStatus()){
			case Cloudlet.SUCCESS:
				status="SUCCESS";
				break;
			case Cloudlet.INEXEC:
				status="INEXEC";
				break;
				
			case Cloudlet.CANCELED:
				status="CANCELED";
				break;
				
			case Cloudlet.PAUSED:
				status="PAUSED";
				break;
				
			case Cloudlet.QUEUED:
				status="QUEUED";
				break;
				
				default:
					status="UNKNOWN";
					break;
					
				
			}

				out.write( cloudlet.getCloudletId() + indent+ status+ indent + cloudlet.getResourceId() + indent +  cloudlet.getVmId() +
						indent + cloudlet.getActualCPUTime() +
						indent + cloudlet.getExecStartTime() + indent + cloudlet.getFinishTime()+
						indent+ cloudlet.getJobType()+ indent +RequestGenBrokerExampleOpt.CalcPrice(cloudlet.getActualCPUTime(),cloudlet.getJobType()) );
				
				out.newLine();
			}
		
		
		out.close();
		
		//Log.printLine("File:\""+FileName+".log\" saved.");
		
	}
	catch(IOException e)
	{
		e.printStackTrace();
		System.out.println("Error: saving cloudlets into file failed!");
	}
	
	
	
	}




public static void saveInfo(String str,String fileName)
{ 
	
	
	try{
		String fullName= filePref+fileName+"_"+Parameters.fileID + ".txt";

		File file =new File(fullName);

		//if file doesnt exists, then create it
		if(!file.exists()){
			file.createNewFile();
		}

		//true = append file
		FileWriter fileWritter = new FileWriter(fullName,true);
	        BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
	        bufferWritter.write(str);
	        bufferWritter.newLine();
	        bufferWritter.close();

	       
	        
	}catch(IOException e){
		e.printStackTrace();
	}


}

}
