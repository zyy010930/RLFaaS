package scs.util.loadGen.driver.serverless;
  
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import scs.controller.ConfigPara;
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
public class MobileNetFaasTFServingDriver extends AbstractJobDriver{
	/**
	 * Singleton code block
	 */
	private static MobileNetFaasTFServingDriver driver=null;
	private SSHTool tool = new SSHTool("192.168.3.154", "root", "wnlof309b507", StandardCharsets.UTF_8);
	//private StringBuilder builder=new StringBuilder();
	
	public MobileNetFaasTFServingDriver(){initVariables();}
	public synchronized static MobileNetFaasTFServingDriver getInstance() {
		if (driver == null) {
			driver = new MobileNetFaasTFServingDriver();
		}
		return driver;
	}
 
	@Override
	protected void initVariables() {
		httpClient=HttpClientPool.getInstance().getConnection();
		queryItemsStr=Repository.mobileNetFaasBaseURL;
		jsonParmStr=Repository.mobileNetParmStr;
		queryItemsStr=queryItemsStr.replace("Ip","192.168.3.154");
		queryItemsStr=queryItemsStr.replace("Port","31112");
	}

	/**
	 * using countDown to send requests in open-loop
	 */
	public void executeJob(final int serviceId) {
		int sleepUnit=1000;
		try {
			System.out.println("mob-req");
			FunctionExec functionExec = new FunctionExec(httpClient, queryItemsStr, serviceId, jsonParmStr, sleepUnit, "POST");

			if(!FunctionList.funcMap.get(serviceId)) {
				System.out.println(tool.exec("bash /home/zyy/INFless/developer/servingFunctions/mobilenet-create.sh"));
				FunctionList.funcMap.put(serviceId, true);
			}

			ConfigPara.kpArray[serviceId-1] = 5*60000;        //Setting the keep-alive is 5 min
			ConfigPara.funcFlagArray[serviceId-1] = 2;
			functionExec.exec();
			ConfigPara.funcFlagArray[serviceId-1] = 1;

			Date now = new Date();
			Date deleteTime = new Date(now.getTime() + ConfigPara.kpArray[serviceId-1]);
			FunctionList.timeMap.put(serviceId, deleteTime);
			Timer timer = new Timer();
			TimerTask timerTask = new TimerTask() {
				@Override
				public void run() {
					Date now = new Date();
					if(FunctionList.timeMap.get(serviceId).compareTo(now) < 0)
					{
						try {
							FunctionList.funcMap.put(serviceId, false);
							System.out.println(tool.exec("bash /home/zyy/INFless/developer/servingFunctions/mobilenet.sh"));
							ConfigPara.funcFlagArray[serviceId-1] = 0;
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			};
			timer.schedule(timerTask, ConfigPara.kpArray[serviceId-1]);

		}catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void coldStartManage(int serviceId) {

	}

	/**
	 * generate random json parameter str for tf-serving
	 * @return str
	 */
	/*private String geneJsonParmStr(int length){
		builder.setLength(0);
		builder.append(jsonParmStr);
		for(int i=0;i<length;i++){
			builder.append(random.nextDouble()).append(",");
		}
		builder.append(random.nextDouble());
		builder.append("]}]}");
		return builder.toString();
	}*/
	


}
