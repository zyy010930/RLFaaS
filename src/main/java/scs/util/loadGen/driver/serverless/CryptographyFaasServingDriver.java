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
    public void executeJob(int serviceId) {
        System.out.println("Cryptography");
        if(true){
            System.out.println("Cryptography-req");
            if(true){
                System.out.println("Cryptography-req");
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
                        System.out.println("Cryptography-req");
                        FunctionExec functionExec = new FunctionExec(httpClient, queryItemsStr, serviceId, jsonParmStr, sleepUnit, "POST");

                        if(!FunctionList.funcMap.get(32)) {
                            System.out.println(tool.exec("bash /home/zyy/BBServerless/BurstyServerlessBenchmark/DIC/WebServices/openfaas/python-code/cryptography-create.sh"));
                        }
                        functionExec.exec();

                        Date now = new Date();
                        Date deleteTime = new Date(now.getTime() + 60000);
                        FunctionList.timeMap.put(32,deleteTime);
                        // 创建定时器
                        Timer timer = new Timer();
                        // 创建定时器任务
                        TimerTask timerTask = new TimerTask() {
                            @Override
                            public void run() {
                                Date now = new Date();
                                if(FunctionList.timeMap.get(32).compareTo(now) < 0)
                                {
                                    try {
                                        FunctionList.funcMap.put(32,false);
                                        System.out.println(tool.exec("bash /home/zyy/BBServerless/BurstyServerlessBenchmark/DIC/WebServices/openfaas/python-code/cryptography.sh"));
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        };
                        timer.schedule(timerTask, 60000); //2分钟后判断函数是否删除
                    }catch (InterruptedException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }else{
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
