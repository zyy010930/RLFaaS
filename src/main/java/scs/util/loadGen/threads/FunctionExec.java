package scs.util.loadGen.threads;

import org.apache.http.impl.client.CloseableHttpClient;
import scs.util.repository.Repository;
import scs.util.tools.HttpClientPool;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class FunctionExec {
    private CloseableHttpClient httpclient;//httpclient对象
    private String url;//请求的url
    private CountDownLatch begin;
    private int serviceId;
    private String jsonObjectStr;
    private int sendDelay;
    private String requestType;

    public FunctionExec(CloseableHttpClient httpclient, String url, int serviceId, String jsonObjectStr, int sendDelay, String requestType){
        this.httpclient=httpclient;
        this.url=url;
        this.serviceId=serviceId;
        this.jsonObjectStr=jsonObjectStr;
        this.sendDelay=sendDelay;
        this.requestType=requestType;
    }

    public void exec() {
        try{
            if(requestType!=null && requestType.startsWith("G")){
                //int time=HttpClientPool.getResponseTime(httpclient, url);
                int time=new Random().nextInt(100);
                synchronized (Repository.onlineDataList.get(serviceId)) {
                    Repository.onlineDataList.get(serviceId).add(time);
                }
            } else {
                //int time=new Random().nextInt(100);
                System.out.println("exec now");
                int time= HttpClientPool.postResponseTime(httpclient, url, jsonObjectStr);
                synchronized (Repository.onlineDataList.get(serviceId)) {
                    Repository.onlineDataList.get(serviceId).add(time);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
