package scs.controller;

import java.sql.Time;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import net.sf.json.JSONArray;
import scs.pojo.PageQueryData;
import scs.pojo.QueryData;
import scs.util.format.DataFormats;
import scs.util.loadGen.recordDriver.RecordDriver;
import scs.util.repository.Repository;
import scs.util.tools.CSVReader;
import scs.util.tools.FunctionRequest;

/**
 * Load generator controller class, it includes interfaces as follows:
 * 1.Control the open/close of load generator
 * 2.Support the dynamic QPS setting
 * 3.support GPI for user to view the realtime latency and QPS
 * @author YananYang 
 * @date 2019-11-12
 * @email ynyang@tju.edu.cn
 */
@Controller
public class LoadGenController {
	private DataFormats dataFormat=DataFormats.getInstance();
	private Repository instance=Repository.getInstance();
	private static Map<Integer,Integer> mp;
	{
		mp = new HashMap<Integer, Integer>();
		mp.put(1,10);
		mp.put(2,16);
		mp.put(3,28);
		mp.put(4,29);
		mp.put(5,30);
		mp.put(6,31);
		mp.put(7,32);
	}
	private static Map<Integer,Double> mp2;
	{
		mp2 = new HashMap<Integer, Double>();
		mp2.put(1,0.6);
		mp2.put(2,0.6);
		mp2.put(3,0.6);
		mp2.put(4,0.6);
		mp2.put(5,0.6);
		mp2.put(6,0.6);
		mp2.put(7,0.6);
	}
	private ArrayList<Map<Integer,Integer>> functionList = new ArrayList<Map<Integer, Integer>>();
	/**
	 * Start the load generator for latency-critical services
	 * @param intensity The concurrent request number per second (RPS)
	 * @param serviceId The index id of web inference service, started from 0 by default
	 */
	@RequestMapping("/startOnlineQuery.do")
	public void startOnlineQuery(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="intensity",required=true) int intensity,
			@RequestParam(value="serviceId",required=true) int serviceId,
			@RequestParam(value="concurrency",required=true) int concurrency){
		try{
			if (serviceId < 0 || serviceId >= Repository.NUMBER_LC){
				response.getWriter().write("serviceId="+serviceId+" does not exist with service number="+Repository.NUMBER_LC);
			} else {
				if (concurrency > 0) {
					Repository.concurrency[serviceId]=1;
				} else {
					Repository.concurrency[serviceId]=0;
				}
				intensity=intensity<=0?1:intensity;//validation
				Repository.realRequestIntensity[serviceId]=intensity;
				
				if(Repository.onlineQueryThreadRunning[serviceId]==true){
					response.getWriter().write("online query threads"+serviceId+" are already running");
				}else{
					Repository.onlineDataFlag[serviceId]=true; 
					Repository.statisticsCount[serviceId]=0;//init statisticsCount
					Repository.totalQueryCount[serviceId]=0;//init totalQueryCount
					Repository.totalRequestCount[serviceId]=0;//init totalRequestCount
					Repository.onlineDataList.get(serviceId).clear();//clear onlineDataList
					Repository.windowOnlineDataList.get(serviceId).clear();//clear windowOnlineDataList
					if(true) {
						//RecordDriver.getInstance().execute(serviceId);
						CSVReader reader = new CSVReader();
						List<Map.Entry<String, ArrayList<Integer>>> list = reader.getAzure();
						FunctionRequest functionRequest = new FunctionRequest();
						System.out.println("build request!!!!!!!");
						Map<Integer,ArrayList<Integer>> InvokeMap = functionRequest.getMap(0,list); //一次取出7组调用记录
						System.out.println("Invoke Map Build------");
						Map<Integer,ArrayList<Integer>> funcMap = new TreeMap<>();
						for(int i = 1;i <= 7;i++)
						{
							ArrayList<Integer> timeList = new ArrayList<>();
							int lastIndex = 0;
							for(int j = 0;j < InvokeMap.get(i).size();j++)
							{
								if(InvokeMap.get(i).get(j) != 0)
								{
									Integer functionTime = j - lastIndex;
									timeList.add(functionTime);
									lastIndex = j;
								}
							}
							funcMap.put(mp.get(i),timeList);
						}
						/*
						for(int i=1;i<=10;i++)
						{
							Map<Integer,Integer> funcMap = new TreeMap<Integer, Integer>();
							for(int j=1;j<=7;j++)
							{
								if(Math.random()>=mp2.get(j)) {
									int functionTime = (int) (Math.random() * 60) + time;
									funcMap.put(functionTime, mp.get(j));
									//functionList.add(new HashMap<Integer, Integer>(j, functionTime));
								}
							}
							functionList.add(funcMap);
							time+=60;
						}*/
						System.out.println("start thread");
//						for(int i=0;i<functionList.size();i++)
//						{
//							int t = 0;
//							for(Map.Entry<Integer, Integer> entry : functionList.get(i).entrySet())
//							{
//								int start = entry.getKey() - t;
//								t = entry.getKey();
//								System.out.println("function:" + entry.getValue() + "sleep:" + start);
//								Thread.sleep(start*1000);
//								Repository.loaderMap.get(entry.getValue()).getAbstractJobDriver().executeJob(entry.getValue());
//							}
//						}
						ExecutorService executor = Executors.newFixedThreadPool(7);
						FunctionThread thread = new FunctionThread(16,funcMap.get(16));
						FunctionThread thread2 = new FunctionThread(10,funcMap.get(10));
						FunctionThread thread3 = new FunctionThread(29,funcMap.get(29));
						FunctionThread thread4 = new FunctionThread(30,funcMap.get(30));
						FunctionThread thread5 = new FunctionThread(31,funcMap.get(31));
						FunctionThread thread6 = new FunctionThread(32,funcMap.get(32));
						FunctionThread thread7 = new FunctionThread(28,funcMap.get(28));
						executor.execute(thread);
						executor.execute(thread2);
						executor.execute(thread3);
						executor.execute(thread4);
						executor.execute(thread5);
						executor.execute(thread6);
						executor.execute(thread7);
						executor.shutdown();
						//Repository.loaderMap.get(serviceId).getAbstractJobDriver().executeJob(serviceId);
					} else {
						response.getWriter().write("serviceId="+serviceId+"doesnot has loaderDriver instance with LC number="+Repository.NUMBER_LC);
					}
				}
			}


		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * dynamically set the RPS of web-inference service
	 * @param request
	 * @param response
	 * @param intensity The concurrent request number per second (RPS)
	 */
	@RequestMapping("/setIntensity.do")
	public void setIntensity(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="intensity",required=true) int intensity,
			@RequestParam(value="serviceId",required=true) int serviceId){
		try{ 
			intensity=intensity<0?0:intensity;//合法性校验
			Repository.realRequestIntensity[serviceId]=intensity;
			response.getWriter().write("serviceId="+serviceId+" realRequestIntensity is set to "+Repository.realRequestIntensity[serviceId]);
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	/**
	 * Stop the load generator for latency-critical services
	 * @param request
	 * @param response
	 */
	@RequestMapping("/stopOnlineQuery.do")
	public void stopOnlineQuery(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value="serviceId",required=true) int serviceId){
		try{
			
			Repository.realRequestIntensity[serviceId]=0;
			Repository.onlineDataFlag[serviceId]=false; 
			if(serviceId<Repository.NUMBER_LC && serviceId>=0) { 
				if(Repository.loaderMap.get(serviceId).getLoaderName().toLowerCase().contains("redis")){
					Repository.loaderMap.get(serviceId).getAbstractJobDriver().executeJob(serviceId);
				}
			}
			response.getWriter().write("serviceId="+serviceId+" stopped loader");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * Turn into the GPI page to see the real-time request latency line
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/goOnlineQuery.do")
	public String goOnlineQuery(HttpServletRequest request,HttpServletResponse response,Model model,
			@RequestParam(value="serviceId",required=true) int serviceId){
		StringBuffer strName0=new StringBuffer();
		StringBuffer strData0=new StringBuffer();
		StringBuffer strName1=new StringBuffer();
		StringBuffer strData1=new StringBuffer();
		StringBuffer HSeries=new StringBuffer();

		strName0.append("{name:'queryTime99th',");
		strData0.append("data:[");

		strName1.append("{name:'queryTimeAvg',");
		strData1.append("data:[");

		List<QueryData> list=new ArrayList<QueryData>();
		list.addAll(Repository.windowOnlineDataList.get(serviceId));
		while(list.size()==0){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			list.clear();
			list.addAll(Repository.windowOnlineDataList.get(serviceId));
		}
		int curSize=list.size();
		if(curSize<Repository.windowSize){
			int differ=Repository.windowSize-curSize;
			for(int i=0;i<differ;i++){
				list.add(list.get(curSize-1));
			}
		} 
		int size=list.size();
		for(int i=0;i<size-1;i++){
			strData0.append("[").append(list.get(i).getGenerateTime()).append(",").append(list.get(i).getQueryTime99th()).append("],");
			strData1.append("[").append(list.get(i).getGenerateTime()).append(",").append(list.get(i).getQueryTimeAvg()).append("],");

		}
		strData0.append("[").append(list.get(size-1).getGenerateTime()).append(",").append(list.get(size-1).getQueryTime99th()).append("]]}");
		strData1.append("[").append(list.get(size-1).getGenerateTime()).append(",").append(list.get(size-1).getQueryTimeAvg()).append("]]}");

		HSeries.append(strName0).append(strData0).append(",").append(strName1).append(strData1);

		model.addAttribute("seriesStr",HSeries.toString());  
		model.addAttribute("serviceId",serviceId);

		return "onlineData";
	}

	/**
	 * obtain the latest 99th latency of last second
	 * this is done by Ajax, no pages switch
	 * @param request
	 * @param response
	 */
	@RequestMapping("/getOnlineWindowAvgQueryTime.do")
	public void getOnlineQueryTime(HttpServletRequest request,HttpServletResponse response,
			@RequestParam(value="serviceId",required=true) int serviceId){
		try{
			PageQueryData pqd=new PageQueryData(Repository.latestOnlineData[serviceId]);
			float[] res=instance.getOnlineWindowAvgQueryTime(serviceId);
			pqd.setRealRps(Repository.realRequestIntensity[serviceId]);
			pqd.setWindowAvg99thQueryTime(dataFormat.subFloat(res[0],2));
			pqd.setWindowAvgAvgQueryTime(dataFormat.subFloat(res[1],2));
		
			response.getWriter().write(JSONArray.fromObject(pqd).toString());
			//response.getWriter().write(JSONArray.fromObject(Repository.latestOnlineData[serviceId]).toString().replace("}",",\"OnlineAvgQueryTime\":"+dataFormat.subFloat(instance.getOnlineAvgQueryTime(serviceId),2)+"}"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * obtain the latest 99th latency of last second
	 * this is done by Ajax, no pages switch
	 * @param request
	 * @param response
	 */
	@RequestMapping("/getLoaderGenQuery.do")
	public void getOnlineQueryTime(HttpServletRequest request,HttpServletResponse response) {
		try{
			List<PageQueryData> list=new ArrayList<PageQueryData>();
			for(int i=0; i<Repository.NUMBER_LC; i++){
				PageQueryData pqd=null;
				if(Repository.latestOnlineData[i]==null){
					pqd=new PageQueryData();
					pqd.setRealRps(Repository.realRequestIntensity[i]);
					pqd.setRealQps(Repository.realQueryIntensity[i]);
				} else {
					pqd=new PageQueryData(Repository.latestOnlineData[i]);
					float[] res=instance.getOnlineWindowAvgQueryTime(i);
					pqd.setRealRps(Repository.realRequestIntensity[i]);
					pqd.setWindowAvg99thQueryTime(dataFormat.subFloat(res[0],2));
					pqd.setWindowAvgAvgQueryTime(dataFormat.subFloat(res[1],2));
				}
				pqd.setLoaderName(Repository.loaderMap.get(i).getLoaderName());
				list.add(pqd);
			}
			response.getWriter().write(JSONArray.fromObject(list).toString());
			//response.getWriter().write(JSONArray.fromObject(Repository.latestOnlineData[serviceId]).toString().replace("}",",\"OnlineAvgQueryTime\":"+dataFormat.subFloat(instance.getOnlineAvgQueryTime(serviceId),2)+"}"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	static class FunctionThread extends Thread{
		private int serviceId;
		private ArrayList<Integer> list;

		public FunctionThread(){}

		public FunctionThread(int id, ArrayList<Integer> list)
		{
			this.serviceId = id;
			this.list = list;
		}

		public void run(){
			//Repository.loaderMap.get(serviceId).getAbstractJobDriver().executeJob(serviceId);
			for(int i=0;i<list.size();i++)
			{
				int t = 0;
				for(Integer time : list)
				{
					int start = time;
					System.out.println("function:" + serviceId + "sleep:" + start);
					try {
						Thread.sleep(start*60000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Repository.loaderMap.get(serviceId).getAbstractJobDriver().executeJob(serviceId);
				}
			}
		}
	}

}
