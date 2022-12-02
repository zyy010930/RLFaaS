package scs.util.loadGen.driver.serverless;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import scs.pojo.FunctionList;
import scs.util.loadGen.driver.AbstractJobDriver;
import scs.util.loadGen.threads.FunctionExec;
import scs.util.loadGen.threads.LoadExecThread;
import scs.util.loadGen.threads.LoadExecThreadRandom;
import scs.util.repository.Repository;
import scs.util.tools.HttpClientPool;
import scs.util.tools.SSHTool;

/**
 * Image recognition service request class
 * GPU inference
 * @author Yanan Yang
 *
 */
public class ResNet50FaasTFServingDriver extends AbstractJobDriver{
	/**
	 * Singleton code block
	 */
	private static ResNet50FaasTFServingDriver driver=null;
	private SSHTool tool = new SSHTool("192.168.3.154", "root", "wnlof309b507", StandardCharsets.UTF_8);


	public ResNet50FaasTFServingDriver(){initVariables();}
	public synchronized static ResNet50FaasTFServingDriver getInstance() {
		if (driver == null) {
			driver = new ResNet50FaasTFServingDriver();
		}
		return driver;
	}

	@Override
	protected void initVariables() {
		httpClient=HttpClientPool.getInstance().getConnection();
		queryItemsStr=Repository.resNet50FaasBaseURL;
		jsonParmStr=Repository.resNet50ParmStr; 
		queryItemsStr=queryItemsStr.replace("Ip","192.168.3.154");
		queryItemsStr=queryItemsStr.replace("Port","31112");
	}

	/**
	 * using countDown to send requests in open-loop
	 */
	public void executeJob(int serviceId) {
		//ExecutorService executor = Executors.newCachedThreadPool();
		System.out.println("resnet-50");
		//Repository.onlineQueryThreadRunning[serviceId]=true;
		//Repository.sendFlag[serviceId]=true;
		//if(Repository.onlineDataFlag[serviceId]==true){
		if(true){
			System.out.println("res-req");
			//if(Repository.sendFlag[serviceId]==true){
			if(true){
				System.out.println("res-req");
				//CountDownLatch begin=new CountDownLatch(1);
				//if (Repository.realRequestIntensity[serviceId]==0){
				if(false){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					int sleepUnit=1000;
					try {
						Thread.sleep(500);
						System.out.println("res-req");
						FunctionExec functionExec = new FunctionExec(httpClient, queryItemsStr, serviceId, jsonParmStr, sleepUnit, "POST");

						if(!FunctionList.funcMap.get(10)) {
							System.out.println(tool.exec("bash /home/zyy/INFless/developer/servingFunctions/resnet-50-create.sh"));
						}
						functionExec.exec();

						Date now = new Date();
						Date deleteTime = new Date(now.getTime() + 60000);
						FunctionList.timeMap.put(10,deleteTime);
						// 创建定时器
						Timer timer = new Timer();
						// 创建定时器任务
						TimerTask timerTask = new TimerTask() {
							@Override
							public void run() {
								Date now = new Date();
								if(FunctionList.timeMap.get(10).compareTo(now) < 0)
								{
									try {
										FunctionList.funcMap.put(10,false);
										System.out.println(tool.exec("bash /home/zyy/INFless/developer/servingFunctions/resnet-50.sh"));
									} catch (IOException e) {
										e.printStackTrace();
									}
								}
							}
						};
						timer.schedule(timerTask, 60000); //2分钟后判断函数是否删除

//						System.out.println(tool.exec("bash /home/zyy/INFless/developer/servingFunctions/resnet-50.sh"));
					}catch (InterruptedException | IOException e) {
						e.printStackTrace();
					}
				}
				//Repository.sendFlag[serviceId]=false;
				//Repository.totalRequestCount[serviceId]+=Repository.realRequestIntensity[serviceId];
				//begin.countDown();
			}else{
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			/*	System.out.println("loader watting "+TestRepository.list.size());*/
			}
		}
		/*
		executor.shutdown();
		while(!executor.isTerminated()){
			try {
				Thread.sleep(2000);
			} catch(InterruptedException e){
				e.printStackTrace();
			}
		}  */
		//Repository.onlineQueryThreadRunning[serviceId]=false;
	}

}
