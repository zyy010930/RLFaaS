package scs.util.loadGen.driver.serverless;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
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
public class CryptographyFaasServingDriver extends AbstractJobDriver{
    /**a
     * Singleton code block
     */
    private static CryptographyFaasServingDriver driver=null;
    private SSHTool tool = new SSHTool("192.168.3.154", "root", "wnlof309b507", StandardCharsets.UTF_8);
    public CryptographyFaasServingDriver(){initVariables();}
    public synchronized static CryptographyFaasServingDriver getInstance() {
        if (driver == null) {
            driver = new CryptographyFaasServingDriver();
        }
        return driver;
    }

    @Override
    protected void initVariables() {
        httpClient=HttpClientPool.getInstance().getConnection();
        queryItemsStr=Repository.CryptographyFaasBaseURL;
        jsonParmStr=Repository.resNet50ParmStr;
        queryItemsStr=queryItemsStr.replace("Ip","192.168.3.154");
        queryItemsStr=queryItemsStr.replace("Port","31112");
    }

    /**
     * using countDown to send requests in open-loop
     */
    public void executeJob(final int serviceId,int type) {
        int sleepUnit=1000;
        try {
            System.out.println("Cryptography-req");
            FunctionExec functionExec = new FunctionExec(httpClient, queryItemsStr, serviceId, jsonParmStr, sleepUnit, "POST");

            if(ConfigPara.funcFlagArray[serviceId-1] == 0) {
                System.out.println(tool.exec("bash /home/zyy/BBServerless/BurstyServerlessBenchmark/DIC/WebServices/openfaas/python-code/cryptography-create.sh"));
                FunctionList.funcMap.put(serviceId, true);
                ConfigPara.funcFlagArray[serviceId-1] = 2;
            }

            if(type == 3)
            {
                //直方图初始化过程，前20次采用最小和最大调用间隔进行划分，达到20次则改用直方图策略5%和99%。
                if(start == false)
                {
                    oldTime = new Date().getTime();
                    start = true;
                }else {
                    long nowTime = new Date().getTime();
                    System.out.println("now:" + nowTime + " ,old:" + oldTime);
                    long t = nowTime - oldTime;
                    timeList.add(t);
                    oldTime = nowTime;
                    if(mean == 0)
                    {
                        mean = (double)t;
                    }else{
                        double oldMean = mean;
                        mean = oldMean + ((double)t - oldMean)/timeList.size();
                        standard = standard + (t - oldMean)*(t - mean);
                        cv = standard/mean;
                        System.out.println("mean:" + mean + ", " + "standard:" + standard + ", cv:" + cv);
                    }
                }
                timeList.sort(Comparator.naturalOrder());
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
                            System.out.println(tool.exec("bash /home/zyy/BBServerless/BurstyServerlessBenchmark/DIC/WebServices/openfaas/python-code/cryptography.sh"));
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

}
