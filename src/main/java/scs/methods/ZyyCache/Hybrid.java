package scs.methods.ZyyCache;

import scs.controller.ConfigPara;
import scs.controller.OperWaitQueue;

import java.util.Queue;

public class Hybrid {
    public static Double[] priority = new Double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
    public Hybrid(){}

    public static void queuePrint(Queue<Integer> queue)
    {
        for(Integer i : queue)
        {
            System.out.println(i + " ");
        }
    }
    public static void run(Integer sid) {
        System.out.println("毕设");
        ConfigPara.invokeTime[sid - 1]++;
        priority[sid - 1] = ConfigPara.invokeTime[sid - 1] * ConfigPara.initTime[sid - 1] / ConfigPara.funcCapacity[sid - 1];
        if(ConfigPara.funcFlagArray[sid - 1] == 0) {
            Double pri = Double.MAX_VALUE;
            Integer tempSid = 0;
            while (ConfigPara.funcCapacity[sid - 1] > ConfigPara.getRemainMemCapacity()) {
                for (int i = 0; i < ConfigPara.funcFlagArray.length; i++) {
                    if (priority[i] < pri) {
                        pri = priority[i];
                        tempSid = i + 1;
                    }
                }
                if (tempSid != 0) {
                    System.out.println(tempSid + "-----------release-----------");
                    OperWaitQueue.releaseFunc(tempSid);
                }
            }
            System.out.println("释放内存");
        }
        OperWaitQueue.execFuncHybrid(sid);


    }
}
