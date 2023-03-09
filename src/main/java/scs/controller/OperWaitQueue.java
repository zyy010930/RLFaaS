package scs.controller;

import scs.util.loadGen.driver.AbstractJobDriver;
import scs.util.repository.Repository;
import scs.util.tools.SSHTool;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @ClassName OperWaitQueue
 * @Description ...
 * @Author @WZhang
 * @Date 2023/3/7 10:22
 * @Version 1.0
 */
public class OperWaitQueue {
    private static SSHTool tool1 = new SSHTool("192.168.3.154", "root", "wnlof309b507", StandardCharsets.UTF_8);
    public static void execQueueFunc() {
        if(ConfigPara.waitQueue.size() != 0) {
            for(int i = 0; i < ConfigPara.funcFlagArray.length; i++) {
                if(ConfigPara.funcFlagArray[i] == 1) {
                    releaseFunc(i+1);
                }
            }
            for(int j = 0; j < ConfigPara.waitQueue.size(); j++) {
                if(ConfigPara.funcCapacity[ConfigPara.waitQueue.peek()-1] <= ConfigPara.getRemainMemCapacity()) {
                    Integer sid = ConfigPara.waitQueue.poll();
                    ConfigPara.setMemoryCapacity(ConfigPara.getRemainMemCapacity() - ConfigPara.funcCapacity[sid-1]);
                    ConfigPara.funcFlagArray[sid-1] = 2;
                    Repository.loaderMap.get(sid).getAbstractJobDriver().executeJob(sid);
                } else {
                    break;
                }
            }
        }
    }

    public static void execRequests(Integer serviceId) {
        if(ConfigPara.waitQueue.size() != 0) {
            ConfigPara.waitQueue.add(serviceId);
        }else {
            Repository.loaderMap.get(serviceId).getAbstractJobDriver().executeJob(serviceId);
        }
    }

    public static void releaseFunc(Integer sid) {
        switch (sid){
            case 1:
                try {
                    tool1.exec("bash /home/zyy/INFless/developer/servingFunctions/resnet-50.sh");
                    ConfigPara.setMemoryCapacity(ConfigPara.getRemainMemCapacity() + ConfigPara.funcCapacity[sid-1]);
                    ConfigPara.funcFlagArray[sid-1] = 0;
                }catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    tool1.exec("bash /home/zyy/INFless/developer/servingFunctions/mobilenet.sh");
                    ConfigPara.setMemoryCapacity(ConfigPara.getRemainMemCapacity() + ConfigPara.funcCapacity[sid-1]);
                    ConfigPara.funcFlagArray[sid-1] = 0;
                }catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                try {
                    tool1.exec("bash /home/zyy/BBServerless/BurstyServerlessBenchmark/DIC/WebServices/openfaas/python-code/hash.sh");
                    ConfigPara.setMemoryCapacity(ConfigPara.getRemainMemCapacity() + ConfigPara.funcCapacity[sid-1]);
                    ConfigPara.funcFlagArray[sid-1] = 0;
                }catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                try {
                    tool1.exec("bash /home/zyy/BBServerless/BurstyServerlessBenchmark/DIC/WebServices/openfaas/python-code/Md5.sh");
                    ConfigPara.setMemoryCapacity(ConfigPara.getRemainMemCapacity() + ConfigPara.funcCapacity[sid-1]);
                    ConfigPara.funcFlagArray[sid-1] = 0;
                }catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 5:
                try {
                    tool1.exec("bash /home/zyy/BBServerless/BurstyServerlessBenchmark/DIC/WebServices/openfaas/python-code/hello.sh");
                    ConfigPara.setMemoryCapacity(ConfigPara.getRemainMemCapacity() + ConfigPara.funcCapacity[sid-1]);
                    ConfigPara.funcFlagArray[sid-1] = 0;
                }catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 6:
                try {
                    tool1.exec("bash /home/zyy/BBServerless/BurstyServerlessBenchmark/DIC/WebServices/openfaas/python-code/sort.sh");
                    ConfigPara.setMemoryCapacity(ConfigPara.getRemainMemCapacity() + ConfigPara.funcCapacity[sid-1]);
                    ConfigPara.funcFlagArray[sid-1] = 0;
                }catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 7:
                try {
                    tool1.exec("bash /home/zyy/BBServerless/BurstyServerlessBenchmark/DIC/WebServices/openfaas/python-code/cryptography.sh");
                    ConfigPara.setMemoryCapacity(ConfigPara.getRemainMemCapacity() + ConfigPara.funcCapacity[sid-1]);
                    ConfigPara.funcFlagArray[sid-1] = 0;
                }catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                System.out.println("Function Release Error!");
        }
    }
}