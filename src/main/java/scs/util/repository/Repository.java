package scs.util.repository;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import scs.pojo.LoaderDriver;
import scs.pojo.QueryData;
import scs.pojo.ThreeTuple;
import scs.util.loadGen.driver.example.ExampleDriver;
import scs.util.loadGen.driver.serverless.*;
import scs.util.rmi.RmiService;

/**
 * System static repository class
 * Provide memory storage in the form of static variables for data needed in system operation
 * Including some system parameters, application run data, control signs and so on
 * @author Yanan Yang
 *
 */
public class Repository{ 
	private static Repository repository=null;
	private Repository(){}
	public synchronized static Repository getInstance() {
		if (repository == null) {
			repository = new Repository();
		}
		return repository;
	}  

	public final static int NUMBER_LC=8; //number of LC services
	
	public static int windowSize=60; //window size of latency recorder
	public static int recordInterval=1000; //record interval of latency recorder
	private static boolean rmiServiceEnable=false;
	/**
	 * System variables of online load generator module 
	 */
	
	public static boolean[] onlineQueryThreadRunning=new boolean[NUMBER_LC]; 
	public static boolean[] onlineDataFlag=new boolean[NUMBER_LC]; 
	public static boolean[] sendFlag=new boolean[NUMBER_LC]; 
	
	public static int[] realRequestIntensity=new int[NUMBER_LC]; 
	public static int[] realQueryIntensity=new int[NUMBER_LC];  
	private static int[] windowOnLineDataListCount=new int[NUMBER_LC];	
	public static int[] statisticsCount=new int[NUMBER_LC];	
	public static int[] totalRequestCount=new int[NUMBER_LC];
	public static int[] totalQueryCount=new int[NUMBER_LC];
	
	public static int[] concurrency=new int[NUMBER_LC];
	
	public static List<ArrayList<Integer>> onlineDataList=new ArrayList<ArrayList<Integer>>();
	public static List<ArrayList<ThreeTuple<Integer,String,Timestamp>>> onlineDataListSpec=new ArrayList<ArrayList<ThreeTuple<Integer,String,Timestamp>>>();// <latency,html,collectTime>
	public static List<ArrayList<Integer>> tempOnlineDataList=new ArrayList<ArrayList<Integer>>();
	public static List<ArrayList<QueryData>> windowOnlineDataList=new ArrayList<ArrayList<QueryData>>();
	private static List<ArrayList<QueryData>> tempWindowOnlineDataList=new ArrayList<ArrayList<QueryData>>();
	
	public static QueryData[] latestOnlineData=new QueryData[NUMBER_LC];
	public static float[] windowAvgPerSec99thQueryTime=new float[NUMBER_LC];
	public static float[] windowAvgPerSecAvgQueryTime=new float[NUMBER_LC];
 
	public static Map<Integer,LoaderDriver> loaderMap=new HashMap<Integer,LoaderDriver>(); 
	
	public static String serverIp="";
	public static int rmiPort;
	public static String system_sdcbench_script="/home/tank/sdcloud2.0/";
	//public static String system_sdcbench_script="/home/sdc05/sdcloud/";
	// inference
	public static String mobileNetBaseURL="";
	public static String mobileNetParmStr="";
	public static String resNet50BaseURL="";
	public static String resNet50ParmStr="";
	public static String resNetBaseURL="";
	public static String resNetParmStr="";
	public static String mnistBaseURL="";
	public static String mnistParmStr="";
	public static String halfBaseURL="";
	public static String halfParmStr="";
	public static String catdogBaseURL="";
	public static String catdogParmStr="";
	public static String lstm2365BaseURL="";
	public static String lstm2365ParmStr="";
	public static String textcnn69BaseURL="";
	public static String textcnn69ParmStr="";  
	public static String textcnn20BaseURL="";
	public static String textcnn20ParmStr=""; 
	public static String ssdBaseURL="";
	public static String ssdParmStr=""; 
	public static String yamNetBaseURL="";
	public static String yamNetParmStr=""; 
	
	
	// Openfaas-inference
	
	public static String socialNetworkBaseURL="";
	public static String socialNetworkParmStr="";
	
	public static String resNet50FaasBaseURL="";
	public static String lstm2365FaasBaseURL="";
	public static String textcnn69FaasBaseURL="";
	public static String textcnn20FaasBaseURL="";
	public static String catdogFaasBaseURL="";
	public static String ssdFaasBaseURL="";
	public static String mobileNetFaasBaseURL="";
	public static String yamNetFaasBaseURL="";
	
	//aliyun
	public static String catdogAliyunFaasBaseURL="";
	public static String catdogAliyunParmStr="";
	
	//webservice sdc
	public static String solrSearchBaseURL="";
	public static String solrSearchParmStr="";
 	
	public static String exampleURL="";

	public static String HelloFaasBaseURL="";
	public static String Md5FaasBaseURL="";
	public static String CryptographyFaasBaseURL="";
	public static String SortFaasBaseURL="";
	public static String HashFaasBaseURL="";
	/**
	 * static code
	 */
	static {
		initList();
		readProperties();
		initLoaderMap();
		System.out.println("init之后，if之前");
		if(Repository.rmiServiceEnable==true){
                         System.out.println("if中");
			RmiService.getInstance().service(Repository.serverIp, Repository.rmiPort);//start the RMI service
	}
	}
	/**
	 * read properties 
	 */
	private static void readProperties(){
		Properties prop = new Properties();
		InputStream is = Repository.class.getResourceAsStream("/conf/sys.properties");
		try {
			prop.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Repository.windowSize=Integer.parseInt(prop.getProperty("windowSize").trim());
		Repository.serverIp=prop.getProperty("serverIp").trim();
		//Repository.serverIp="192.168.1.129";
		Repository.rmiPort=Integer.parseInt(prop.getProperty("rmiPort").trim()); //22222 default
		Repository.recordInterval=Integer.parseInt(prop.getProperty("recordInterval").trim()); 
		
		if(prop.getProperty("rmiServiceEnable")==null||prop.getProperty("rmiServiceEnable").equals("false")){
			rmiServiceEnable=false;
		}else{
			rmiServiceEnable=true;
		}
		Repository.exampleURL=prop.getProperty("exampleURL").trim();
		
		// dnn inference
		Repository.mobileNetBaseURL=prop.getProperty("mobileNetBaseURL").trim();
		Repository.mobileNetParmStr=prop.getProperty("mobileNetParmStr").trim();
		Repository.resNet50BaseURL=prop.getProperty("resNet50BaseURL").trim();
		Repository.resNet50ParmStr=prop.getProperty("resNet50ParmStr").trim(); 
		Repository.resNetBaseURL=prop.getProperty("resNetBaseURL").trim();
		Repository.resNetParmStr=prop.getProperty("resNetParmStr").trim(); 
		Repository.mnistBaseURL=prop.getProperty("mnistBaseURL").trim();
		Repository.mnistParmStr=prop.getProperty("mnistParmStr").trim();
		Repository.halfBaseURL=prop.getProperty("halfBaseURL").trim();
		Repository.halfParmStr=prop.getProperty("halfParmStr").trim();
		Repository.catdogBaseURL=prop.getProperty("catdogBaseURL").trim();
		Repository.catdogParmStr=prop.getProperty("catdogParmStr").trim();
		Repository.lstm2365BaseURL=prop.getProperty("lstm2365BaseURL").trim();
		Repository.lstm2365ParmStr=prop.getProperty("lstm2365ParmStr").trim();
		Repository.textcnn69BaseURL=prop.getProperty("textcnn69BaseURL").trim();
		Repository.textcnn69ParmStr=prop.getProperty("textcnn69ParmStr").trim();
		Repository.textcnn20BaseURL=prop.getProperty("textcnn20BaseURL").trim();
		Repository.textcnn20ParmStr=prop.getProperty("textcnn20ParmStr").trim();
		Repository.ssdBaseURL=prop.getProperty("ssdBaseURL").trim();
		Repository.ssdParmStr=prop.getProperty("ssdParmStr").trim();
		Repository.yamNetBaseURL=prop.getProperty("yamNetBaseURL").trim();
		Repository.yamNetParmStr=prop.getProperty("yamNetParmStr").trim();
		
		// openfaas
		Repository.socialNetworkBaseURL=prop.getProperty("socialNetworkBaseURL").trim();
		Repository.socialNetworkParmStr=prop.getProperty("socialNetworkParmStr").trim();
		
		Repository.resNet50FaasBaseURL=prop.getProperty("resNet50FaasBaseURL").trim();
		Repository.lstm2365FaasBaseURL=prop.getProperty("lstm2365FaasBaseURL").trim();
		Repository.textcnn69FaasBaseURL=prop.getProperty("textcnn69FaasBaseURL").trim();
		Repository.textcnn20FaasBaseURL=prop.getProperty("textcnn20FaasBaseURL").trim();
		Repository.catdogFaasBaseURL=prop.getProperty("catdogFaasBaseURL").trim();
		Repository.mobileNetFaasBaseURL=prop.getProperty("mobileNetFaasBaseURL").trim();
		Repository.yamNetFaasBaseURL=prop.getProperty("yamNetFaasBaseURL").trim();
		Repository.ssdFaasBaseURL=prop.getProperty("ssdFaasBaseURL").trim();
		// aliyun
		Repository.catdogAliyunFaasBaseURL=prop.getProperty("catdogAliyunFaaSBaseURL").trim();
		Repository.catdogAliyunParmStr=prop.getProperty("catdogAliyunParmStr").trim();
		
		// sdc web service
		Repository.solrSearchBaseURL=prop.getProperty("solrSearchBaseURL").trim();
		Repository.solrSearchParmStr=prop.getProperty("solrSearchParmStr").trim();

		Repository.HelloFaasBaseURL=prop.getProperty("HelloFaasBaseURL").trim();
		Repository.Md5FaasBaseURL=prop.getProperty("Md5FaasBaseURL").trim();
		Repository.HashFaasBaseURL=prop.getProperty("HashFaasBaseURL").trim();
		Repository.CryptographyFaasBaseURL=prop.getProperty("CryptographyFaasBaseURL").trim();
		Repository.SortFaasBaseURL=prop.getProperty("SortFaasBaseURL").trim();
		
	}	
	/**
	 * init 
	 */
	private static void initList(){
		 for(int i=0;i<NUMBER_LC;i++){
			 onlineDataList.add(new ArrayList<Integer>());
			 onlineDataListSpec.add(new ArrayList<ThreeTuple<Integer, String, Timestamp>>());
			 tempOnlineDataList.add(new ArrayList<Integer>());
			 windowOnlineDataList.add(new ArrayList<QueryData>());
			 tempWindowOnlineDataList.add(new ArrayList<QueryData>());
		 }
		 
	}
	private static void initLoaderMap(){
		 for(int i=0;i<NUMBER_LC;i++){
			 loaderMap.put(i,loaderMapping(i)); 
			 System.out.println("init loaderMapping: loaderIndex="+i+",loaderDriver="+loaderMap.get(i).getLoaderName()+" url="+loaderMap.get(i).getAbstractJobDriver().queryItemsStr);
		 }
		 
	}
	/**
	 * Adds a new data to the window array
	 * Loop assignment in Repository.windowSize
	 * @param data
	 */
	public void addWindowOnlineDataList(QueryData data, int serviceId){
		latestOnlineData[serviceId]=data;
		realQueryIntensity[serviceId]=data.getRealQps();
		synchronized (windowOnlineDataList.get(serviceId)) {
			if(windowOnlineDataList.get(serviceId).size()<windowSize){
				windowOnlineDataList.get(serviceId).add(data);
			}else{
				windowOnlineDataList.get(serviceId).set(windowOnLineDataListCount[serviceId]%windowSize,data);
				windowOnLineDataListCount[serviceId]++;
			}
		}
	}

	/**
	 * Calculate the variance of query time
	 * @return 
	 */
	/*public float getOnlineVarQueryTime(int serviceId){
		tempWindowOnlineDataList.get(serviceId).clear();
		tempWindowOnlineDataList.get(serviceId).addAll(Repository.windowOnlineDataList.get(serviceId));
		int size=tempWindowOnlineDataList.get(serviceId).size();
		float avgQueryTime=0;

		for(QueryData item:tempWindowOnlineDataList.get(serviceId)){
			avgQueryTime+=item.getQueryTime99th();
		}
		avgQueryTime=avgQueryTime/size;

		float var=0;
		for(QueryData item:tempWindowOnlineDataList.get(serviceId)){
			var+=Math.pow((item.getQueryTime99th()-avgQueryTime),2); 
		}
		var=var/size;
		return var;
	}*/
	/**
	 * Calculate the mean of query time
	 * @return 
	 */
	public float[] getOnlineWindowAvgQueryTime(int serviceId){
		while (windowOnlineDataList.get(serviceId).isEmpty()) {
			 try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		tempWindowOnlineDataList.get(serviceId).clear();
		synchronized (windowOnlineDataList.get(serviceId)) {
			tempWindowOnlineDataList.get(serviceId).addAll(windowOnlineDataList.get(serviceId));
		}
		//tempWindowOnlineDataList.get(serviceId).addAll(Repository.windowOnlineDataList.get(serviceId));
		int size=tempWindowOnlineDataList.get(serviceId).size();
		float avg99thQueryTime=0;
		float avgAvgQueryTime=0;
		for(QueryData item:tempWindowOnlineDataList.get(serviceId)){
			avg99thQueryTime+=item.getQueryTime99th();
			avgAvgQueryTime+=item.getQueryTimeAvg();
		} 
		avg99thQueryTime=avg99thQueryTime/size; 
		avgAvgQueryTime=avgAvgQueryTime/size;
		windowAvgPerSec99thQueryTime[serviceId]=avg99thQueryTime;
		windowAvgPerSecAvgQueryTime[serviceId]=avgAvgQueryTime;
		
		return new float[]{avg99thQueryTime,avgAvgQueryTime};
	}
	/**
	 * maps the loaderIndex with the loaderDriver instance
	 * @param loaderIndex
	 * @return
	 */
	private static LoaderDriver loaderMapping(int loaderIndex){
		if(loaderIndex==0){
			return new LoaderDriver("example", ExampleDriver.getInstance());
		}
		if(loaderIndex==1){
			return new LoaderDriver("resnet-50", ResNet50FaasTFServingDriver.getInstance());
		}
		if(loaderIndex==2){
			return new LoaderDriver("mobilenet", MobileNetFaasTFServingDriver.getInstance());
		}
		if(loaderIndex==3){
			return new LoaderDriver("hash", HashFaasServingDriver.getInstance());
		}
		if(loaderIndex==4){
			return new LoaderDriver("md5", Md5FaasServingDriver.getInstance());
		}
		if(loaderIndex==5){
			return new LoaderDriver("hello", HelloFaasServingDriver.getInstance());
		}
		if(loaderIndex==6){
			return new LoaderDriver("sort", SortFaasServingDriver.getInstance());
		}
		if(loaderIndex==7){
			return new LoaderDriver("cryptography", CryptographyFaasServingDriver.getInstance());
		}
		if(loaderIndex==8){
			return new LoaderDriver("nodeinfo", NodeInfoFaasServingDriver.getInstance());
		}
		if(loaderIndex==9){
			return new LoaderDriver("alpine", AlpineFaasServingDriver.getInstance());
		}
		if(loaderIndex==10){
			return new LoaderDriver("curl", CurlFaasServingDriver.getInstance());
		}
		if(loaderIndex==11){
			return new LoaderDriver("cows", CowsFaasServingDriver.getInstance());
		}
		if(loaderIndex==12){
			return new LoaderDriver("sleep", SleepFaasServingDriver.getInstance());
		}
		if(loaderIndex==13){
			return new LoaderDriver("printer", PrinterFaasServingDriver.getInstance());
		}
		return null;
	} 


}
