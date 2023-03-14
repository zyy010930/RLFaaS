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
            System.out.println(FuncName[serviceId-1] + " request");
            FunctionExec functionExec = new FunctionExec(httpClient, queryItemsStr, serviceId, jsonParmStr, sleepUnit, "POST");

            if(ConfigPara.funcFlagArray[serviceId-1] == 0) {
                coldStartTime++;
                System.out.println(tool.exec(createCmd[serviceId-1]));
                FunctionList.funcMap.put(serviceId, true);
                ConfigPara.funcFlagArray[serviceId-1] = 2;
                System.out.println(FuncName[serviceId-1] + " cold start time is" + coldStartTime);
            }

            if(type == 3)
            {
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
                        cv = standard/mean/(60000.0*60000.0);
                        System.out.println("mean:" + mean + ", " + "standard:" + standard + ", cv:" + cv);
                        if(timeList.size() >= 50 && cv <= 2.0) //样本数目足够且直方图具有代表性，采用5%和99%的样本点
                        {
                            preWarm = (double)timeList.get(Math.min(timeList.size() - 1,((int)(timeList.size()*0.05) - 1)));
                            keepAlive = (double)timeList.get(Math.max(0,((int)(timeList.size()*0.99) - 1)));
                        } else { //样本不足或者直方图不具有代表性，pre-warm设置为0，keep-alive设置一个较长时间
                            preWarm = 0.0;
                            keepAlive = 600000.0;
                        }
                    }
                }
                timeList.sort(Comparator.naturalOrder());
            }

            ConfigPara.kpArray[serviceId-1] = (int)keepAlive;        //Setting the keep-alive
            ConfigPara.funcFlagArray[serviceId-1] = 2;
            functionExec.exec();
            ConfigPara.funcFlagArray[serviceId-1] = 1;
            invokeTime++;
            System.out.println(FuncName[serviceId-1] + " Invoke time is " + invokeTime + ", cold start time is " + coldStartTime);

            if(preWarm != 0.0) {
                Date now1 = new Date();
                Date preWarmTime = new Date(now1.getTime() + (long) preWarm);
                FunctionList.preMap.put(serviceId, preWarmTime);
                Timer timer1 = new Timer();
                TimerTask timerTask1 = new TimerTask() {
                    @Override
                    public void run() {
                        Date now = new Date();
                        if (FunctionList.preMap.get(serviceId).compareTo(now) < 0) {
                            try {
                                FunctionList.funcMap.put(serviceId, true);
                                System.out.println(FuncName[serviceId-1] + " prewarm now. pre-warm is " + preWarm);
                                System.out.println(tool.exec(createCmd[serviceId-1]));
                                ConfigPara.funcFlagArray[serviceId - 1] = 1;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };
                timer1.schedule(timerTask1, (long) preWarm);
            }

            Date now = new Date();
            Date deleteTime = new Date(now.getTime() + ConfigPara.kpArray[serviceId-1] - (long) preWarm);
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
                            System.out.println(FuncName[serviceId-1] + " keepAlive over. keepalive is " + keepAlive);
                            System.out.println(tool.exec(deleteCmd[serviceId-1]));
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
