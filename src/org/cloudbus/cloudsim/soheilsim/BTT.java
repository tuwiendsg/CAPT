package org.cloudbus.cloudsim.soheilsim;
import java.lang.*;
import java.util.Calendar;

import org.cloudbus.cloudsim.Log;

public class BTT {





static final int MAX_PERIOD = 100000;
static final int SugPeriod =60;

static double r,T,n,v,S,X,H,L,q,x,val,h,l,p,u,d,t;
/*
double pow(double a,double b);
double ceil(double);
double floor(double);
double Math.log(double);
double exp(double);
double sqrt(double);
double C(double,double,double);
double N(double,double,double,double);
*/

static double[] CArray = new double[MAX_PERIOD+1]; //double CArray[MAX_PERIOD+1];

static void CArrayInit() //////Compute Log(Combination)
{
 double mul,test;
 int k;
 CArray[0]=0;
 mul=0;
 for(k=1;k<=n;k++)
 {
  mul+=Math.log(n+1-k);
  mul-=Math.log(k);
  CArray[k]=mul;
  test=Math.exp(mul);
 }
}

double C(double a1,double a2,double j)
{
  double mul,k;
  if(a1 < a2)
    mul=0; 
  else
    {
      mul=Math.log(1);
      for(k=a2+1;k<=a1;k++)
         mul+=Math.log(k);
      for(k=1;k<=a1-a2;k++)
         mul-=Math.log(k);
      mul+=j*Math.log(p)+(n-j)*Math.log(1-p);
      mul=Math.exp(mul);
    }
  return mul;
}

static int NearInt(double a)
{
  int b;
  b=(int)a;
  if((a-b)>0.5)
	  b++;
  return b;
}

static double N(double a,double b,double s,double j)
{
  int i,k;
  double nn,path,value;
  path=0;
  nn=n/(2*(h-l))+1;
  for(i=1;i<=nn;i++)
     {
       if(i%2==1)
         //path+=C(n,(n+a+b+(i-1)*s)/2,j)+C(n,(n-a-b+(i+1)*s)/2,j);
	   {
	    k=NearInt((n+a+b+(i-1)*s)/2);
		if(k<=n)
		{           ///////Compute Ai i=>odd
		 value=CArray[k];
		 value=value+j*Math.log(p)+(n-j)*Math.log(1-p);
         value=Math.exp(value);
		 //test=C(n,(n+a+b+(i-1)*s)/2,j);
		 path+=value;
        }  
		k=NearInt((n-a-b+(i+1)*s)/2);
		if(k<=n)
        {     ///Compute Bi i=>odd
         value=CArray[k];
		 value=value+j*Math.log(p)+(n-j)*Math.log(1-p);
         value=Math.exp(value);
		 //test=C(n,(n-a-b+(i+1)*s)/2,j);
	     path+=value;
		}
	   }
       else
        // path-=C(n,(n+a-b+i*s)/2,j)+C(n,(n-a+b+i*s)/2,j);
	   {
	    k=NearInt((n+a-b+i*s)/2);
		if(k<=n)
        {  //////Compute Ai i=>even
		 value=CArray[k];
		 value=value+j*Math.log(p)+(n-j)*Math.log(1-p);
         value=Math.exp(value);
		 //test=C(n,(n+a-b+i*s)/2,j);
		 path-=value;
		}
		k=NearInt((n-a+b+i*s)/2);
		if(k<=n)
		{  /////Compute Bi i=>even
         value=CArray[k];
		 value=value+j*Math.log(p)+(n-j)*Math.log(1-p);
         value=Math.exp(value);
		 //test=C(n,(n-a+b+i*s)/2,j);
	     path-=value;
		}
	   }
     }
  return path;
}

static double CombinatorialVanillaCall()
{   //Time to maturity= T-TriDeltaT
   int Period;
   double OptionValue=0;
   double temp,Stock;
   for(Period=(int)n;Period>=0;Period--)
   {
	    Stock=S*Math.pow(u,Period)*Math.pow(d,n-Period);
        temp=CArray[Period];
		temp=temp+Period*Math.log(p)+(n-Period)*Math.log(1-p);
		if(Stock>=X)
		OptionValue+=Math.exp(temp)*(Stock-X);
   }
   return OptionValue/Math.exp(r*t*n);
}




public static void main(String[] args) {
	
	  r=0.100000;
	  v=0.250000;
	  T=1.000000;
	  S=95.000000;
	  X=100.000000;
	  H=140.000000;
	  L=90.000000;
	  q=0.000000;
	Log.printLine("res: "+CalculateBTT(r,v,T,S,X,H,L,q));

}

public static double CalculateBTT(double r_, double v_, double T_, double S_, double X_, double H_, double L_,double q_)
{
	 // FILE *fp;
	  double j,times,temp,Stock,Up,LogH,LogL;
	  double[] OptionValue = new double[3];   //memorize the three option values of the first period
	  double[] OutOptionValue= new double[3]; //Compute knock out option
	  int xx;
	  
	  double finalResult=0;
	 
	 // struct timeb start,end;
	  Calendar startTime,endTime;
	 // fp=fopen("Result.txt","w");
	 /*
	 // printf("\nPlease input the annual interest rate in decimal = r.\nr==>");
	 // scanf("%lf",&r);
	  r=0;
	  
	  //fprintf(fp,"r=%f\n",r);
	  //printf("Please input the volatility = v.\nv==>");
	 // scanf("%lf",&v);
	  v=0;
	  
	  
	 // fprintf(fp,"v=%f\n",v);
	 // printf("Please input the maturity in years = T.\nT==>");
	 // scanf("%lf",&T);
	  T=0;
	  
	//  fprintf(fp,"T=%f\n",T);
	 // printf("Please input the current stock price = S.\nS==>");
	 // scanf("%lf",&S);
	  S=0;
	  
	 // fprintf(fp,"S=%f\n",S);
	 // printf("Please input the strike price = X.\nX==>");
	 // scanf("%lf",&X);
	  X=0;
	  
	 // fprintf(fp,"X=%f\n",X);
	 // printf("Please input the high barrier = H.\nH==>");
	 // scanf("%lf",&H);
	  H=0;
	  
	   LogH=Math.log(H);
	//  fprintf(fp,"H=%f\n",H);
	 // printf("Please input the low barrier = L.\nL==>");
	 // scanf("%lf",&L);
	   L=0;
	   
	   LogL=Math.log(L);
	 // fprintf(fp,"L=%f\n",L);
	// printf("Please input the continuous dividend yield in decimal = q.\nq==>");
	 // scanf("%lf",&q);
	   q=0;
	   */
	  
	  
	 // fprintf(fp,"q=%f\n",q);
	  
	  
	  /*r=0.100000;
	  v=0.250000;
	  T=1.000000;
	  S=95.000000;
	  X=100.000000;
	  H=140.000000;
	  L=90.000000;
	  q=0.000000;*/
	  
	  r=r_;
	  v=v_;
	  T=T_;
	  S=S_;
	  X=X_;
	  H=H_;
	  L=L_;
	  q=q_;
	  
	  LogL=Math.log(L);
	  LogH=Math.log(H);
	  
	  double OriginalS=S;  //S  put to OriginalS
	  ////   For Loop for number of time steps goes here
	 for(int NN=25;NN<=1000;NN+=25) //was 10000
	 {
	  n=NN;
	  S=OriginalS;
	  t=T/n;
	  double C=Math.ceil((LogH-LogL)/(2*v*Math.sqrt(t)));
	  t=Math.pow((LogH-LogL)/(2*v*C),2);
	  n=Math.floor(T/t)-1;
	  double TriDeltaT=T-t*n;
	  double Mean=Math.log(S)+(r-0.5*v*v)*TriDeltaT;
	  double LogDistance=(LogH-Mean)/(v*Math.sqrt(t));
	  int NStep;  // represent the number of steps in integers.
	  if(n-(int)(n)>0.5)  
		  NStep=(int)(n)+1;
	  else NStep=(int)(n);
	  double Shift;
	  double LogSPrice; // Compute the relative Math.log stock price for the following binomial tree
	  
	  if(NStep%2==0)
	  {  //even step
	    if((LogDistance % 2.0)>1) //fmod
			Shift=1;
		 else Shift=0;
	    LogSPrice=(int)(LogDistance)+Shift-2;
	  }
	  else
	  {   // odd steps
	     if(((LogDistance-1) % 2.0)>1)
			Shift=1;
		 else Shift=0;
	    LogSPrice=(int)(LogDistance)+Shift-2;
	  }
	  
	  S=H/Math.exp(v*Math.sqrt(t)*LogSPrice);
	  //ftime(&start);
	  startTime=Calendar.getInstance();
	  
	  for(int m=0;m<=2;m++)
	  {
	   u=Math.exp(v*Math.sqrt(t));
	   d=Math.exp(-v*Math.sqrt(t));
	   xx=(int)Math.ceil(Math.log(X/S)/(2*v*Math.sqrt(t))+n/2);
	   x=xx;
	   h=Math.ceil(Math.log(H/S)/(2*v*Math.sqrt(t))+n/2);
	   l=Math.floor(Math.log(L/S)/(2*v*Math.sqrt(t))+n/2);
	   p=(Math.exp((r-q)*t)-d)/(u-d);
	   val=0;
	   CArrayInit();
	   Stock=S*Math.pow(u,x)*Math.pow(d,n-x);
	   Up=Math.pow(u,2.0);
	   
	   for(j=x;j<=n;j++)
	   {
	      if((j<=l)||(j>=h))
		  {  
	        temp=CArray[NearInt(j)];
			temp=temp+j*Math.log(p)+(n-j)*Math.log(1-p);
			val+=Math.exp(temp)*(Stock-X);
		  } 
	      else  
	        val+=N(2*h-n,2*(h-j),2*(h-l),j)*(Stock-X);
	    Stock*=Up;   
	   } //end of for j
	   val=val/Math.exp(r*t*n);
	 
	  //fprintf(fp,"%4.0lf     %lf  %lf \n",n,val,                            ());
	  //fprintf(fp,"%4.0lf     %lf \n",n,val);
	  OptionValue[m]=val;   
	  OutOptionValue[m]=CombinatorialVanillaCall()-val;
	  S=S*Math.pow(d,2.0);
	 } //end of for m
	  
	  // Compute the discounted expected option value for the first step
	  Mean=(r-0.5*v*v)*TriDeltaT;
	  double Var=v*v*TriDeltaT;
	  //Compute Alpha, beta, gamma  Use the same definition as in the stair tree paper
	  S=H/Math.exp(v*Math.sqrt(t)*LogSPrice);
	  double Alpha=Math.log(S/OriginalS)-Mean;
	  S=S*Math.pow(d,2.0);
	  double Beta=Math.log(S/OriginalS)-Mean;
	  S=S*Math.pow(d,2.0);
	  double Gamma=Math.log(S/OriginalS)-Mean;
	  //Compute branching probabilities
	  double Det=(Beta-Alpha)*(Gamma-Alpha)*(Gamma-Beta);
	  double Pu=(Beta*Gamma+Var)*(Gamma-Beta)/Det;
	  double Pm=(Alpha*Gamma+Var)*(Alpha-Gamma)/Det;
	  double Pd=(Alpha*Beta+Var)*(Beta-Alpha)/Det;
	  double DBOptionValue=Math.exp(-r*TriDeltaT)*(Pu*OptionValue[0]+Pm*OptionValue[1]+Pd*OptionValue[2]);
	  double DBOutOptionValue=Math.exp(-r*TriDeltaT)*(Pu*OutOptionValue[0]+Pm*OutOptionValue[1]+Pd*OutOptionValue[2]);
	 // ftime(&end);
	  endTime=Calendar.getInstance();
	 // if(start.millitm>end.millitm)
	 //   times=(end.time-start.time-1)+(1000.0+end.millitm-start.millitm)/1000;
	//  else
	 //   times=(end.time-start.time)+(double)(end.millitm-start.millitm)/1000; 
	  
	  long ttimes= endTime.getTimeInMillis()-startTime.getTimeInMillis();
	  
	  finalResult=DBOutOptionValue;
	  
	  
	 
	  
	  //LogInFile.saveInfo("times: "+ttimes+"\t"+ "NN: "+NN+ "\t" + "n: "+"\t"+"OptionValue[0]: "+OptionValue[0]+"\t"+ "OptionValue[1]: "+OptionValue[1]+"\t"+ "OptionValue[2]: "+OptionValue[2]+"\t"+ "DBOptionValue: "+DBOptionValue +"\t"+ "DBOutOptionValue: "+DBOutOptionValue, "BTT");
	  
	  
	  
	 // fprintf(fp,"%lf\t %d\t %lf\t %lf\t %lf\t %lf\t %lf\t %3.9lf\n",times,NN,n,OptionValue[0],OptionValue[1],OptionValue[2],DBOptionValue,DBOutOptionValue);
	  //printf("%lf\t %d\t %lf\t %lf\t %lf\t %lf\t %lf\t\n",times,NN,n,OptionValue[0],OptionValue[1],OptionValue[2],DBOptionValue);
	 }
	  //fclose(fp);
	  //return 0;
	 
	 return finalResult;
}


}
