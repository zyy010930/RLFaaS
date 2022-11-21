package scs.util.loadGen.driver.serverless;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import scs.util.loadGen.driver.AbstractJobDriver;
import scs.util.loadGen.threads.FunctionExec;
import scs.util.loadGen.threads.LoadExecThread;
import scs.util.loadGen.threads.LoadExecThreadRandom;
import scs.util.repository.Repository;
import scs.util.tools.HttpClientPool;
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
        System.out.println("hash");
        if(true){
            System.out.println("hash-req");
            if(true){
                System.out.println("hash-req");
                if(false){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    int sleepUnit=1000;
                    for (int i=0;i<3;i++){
                        try {
                            Thread.sleep(500);
                            System.out.println("hash-req");
                            FunctionExec functionExec = new FunctionExec(httpClient, queryItemsStr, serviceId, jsonParmStr, sleepUnit * i, "POST");
                            functionExec.exec();
                        }catch (InterruptedException e) {
                            e.printStackTrace();
                        }
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
