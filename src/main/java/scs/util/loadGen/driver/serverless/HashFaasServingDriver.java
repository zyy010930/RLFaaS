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
public class HashFaasServingDriver extends AbstractJobDriver{
    /**
     * Singleton code block
     */
    private static HashFaasServingDriver driver=null;
    private SSHTool tool = new SSHTool("192.168.3.154", "root", "wnlof309b507", StandardCharsets.UTF_8);
    public HashFaasServingDriver(){initVariables();}
    public synchronized static HashFaasServingDriver getInstance() {
        if (driver == null) {
            driver = new HashFaasServingDriver();
        }
        return driver;
    }

    @Override
    protected void initVariables() {
        httpClient=HttpClientPool.getInstance().getConnection();
        queryItemsStr=Repository.HashFaasBaseURL;
        jsonParmStr=Repository.resNet50ParmStr;
        queryItemsStr=queryItemsStr.replace("Ip","192.168.3.154");
        queryItemsStr=queryItemsStr.replace("Port","31112");
    }

    /**
     * using countDown to send requests in open-loop
     */
    public void executeJob(int serviceId) {
        int sleepUnit=1000;
        try {
            System.out.println("hash-req");
            FunctionExec functionExec = new FunctionExec(httpClient, queryItemsStr, serviceId, jsonParmStr, sleepUnit, "POST");

            if(!FunctionList.funcMap.get(28)) {
                System.out.println(tool.exec("bash /home/zyy/BBServerless/BurstyServerlessBenchmark/DIC/WebServices/openfaas/python-code/hash-create.sh"));
                FunctionList.funcMap.put(28,true);
            }
            functionExec.exec();
/*
                        Date now = new Date();
                        Date deleteTime = new Date(now.getTime() + 60000);
                        FunctionList.timeMap.put(28,deleteTime);
                        // 创建定时器
                        Timer timer = new Timer();
                        // 创建定时器任务
                        TimerTask timerTask = new TimerTask() {
                            @Override
                            public void run() {
                                Date now = new Date();
                                if(FunctionList.timeMap.get(28).compareTo(now) < 0)
                                {
                                    try {
                                        FunctionList.funcMap.put(28,false);
                                        System.out.println(tool.exec("bash /home/zyy/BBServerless/BurstyServerlessBenchmark/DIC/WebServices/openfaas/python-code/hash.sh"));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        };
                        timer.schedule(timerTask, 60000); //2分钟后判断函数是否删除*/
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void coldStartManage(int serviceId) {

    }

}
