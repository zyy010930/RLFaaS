package scs.methods.LRFU;

import scs.controller.ConfigPara;
import scs.controller.OperWaitQueue;

/**
 * @ClassName LRU
 * @Description ...
 * @Author @WZhang
 * @Date 2023/3/8 12:52
 * @Version 1.0
 */
public class LRU {
    private static long[] funcStartExecTime;
    private static long startTime;

    public LRU() {
        funcStartExecTime = new long[]{0, 0, 0, 0, 0, 0, 0};
        startTime = System.currentTimeMillis();
    }

    public static void run(Integer sid) {
        if(ConfigPara.waitQueue.size() != 0) {
            if (ConfigPara.funcFlagArray[sid - 1] == 0) {
                long tempTimeInterval = 0;
                Integer tempSid = 0;
                ConfigPara.waitQueue.add(sid);

                for(int i = 0; i < ConfigPara.funcFlagArray.length; i++) {
                    if(ConfigPara.funcFlagArray[i] == 1 && funcStartExecTime[i] > tempTimeInterval) {
                        tempTimeInterval = funcStartExecTime[i];
                        tempSid = i + 1;
                    }
                }
                if(tempSid != 0) {
                    OperWaitQueue.releaseFunc(tempSid);

                    for(int j = 0; j < ConfigPara.waitQueue.size(); j++) {
                        if(ConfigPara.funcCapacity[ConfigPara.waitQueue.peek()-1] <= ConfigPara.getRemainMemCapacity()) {
                            Integer tempSid1 = ConfigPara.waitQueue.poll();
                            OperWaitQueue.execFunc(tempSid1);
                        } else {
                            break;
                        }
                    }
                }
            }
            else {
                long t1 = System.currentTimeMillis();
                funcStartExecTime[sid-1] = t1 - startTime;
                OperWaitQueue.execFunc(sid);
            }
        }
        else {
            if(ConfigPara.funcCapacity[sid-1] <= ConfigPara.getRemainMemCapacity()) {
                long t1 = System.currentTimeMillis();
                funcStartExecTime[sid-1] = t1 - startTime;
                OperWaitQueue.execFunc(sid);
            }
            else {
                ConfigPara.waitQueue.add(sid);
            }
        }
    }
}
