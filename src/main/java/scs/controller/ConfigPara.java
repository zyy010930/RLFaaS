package scs.controller;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * @ClassName ConfigPara
 * @Description ...
 * @Author @WZhang
 * @Date 2023/3/6 15:47
 * @Version 1.0
 */
public class ConfigPara {
    private static Double maxFuncCapacity;      //The maximum memory capacity storing functions
    private static Double minFuncCapacity;      //The minimum memory capacity storing functions
    private static Double currFuncCapacity;     //The current remaining memory capacity storing functions
    public static Double[] funcCapacity;        //The sizes of functions
    public static Integer[] kpArray;            //This array records the keep-alive time for functions

    /*********
        When functions are running at memory and the remaining memory capacity cannot support to response other functions,
        the functions are added into the waitQueue. Therefore, the waitQueue stores ids of waiting functions.
    ********/
    public static Queue<Integer> waitQueue;

    /********
        This array records the state of each function.
        0: not initial;
        1: keep-alive;
        2: running.
    ********/
    public static Integer[] funcFlagArray;

    public ConfigPara() {
        maxFuncCapacity = 47.0;
        minFuncCapacity = 9.0;
        currFuncCapacity = 23.5;
        funcCapacity = new Double[]{7.0, 6.0, 6.0, 6.0, 7.0, 9.0, 6.0};

        kpArray = new Integer[7];
        funcFlagArray = new Integer[7];
        waitQueue = new LinkedList<>();
    }

    public static void setMemoryCapacity(Double currFuncCapacity1) {
        if(currFuncCapacity1 < maxFuncCapacity && currFuncCapacity1 >= minFuncCapacity) {
            currFuncCapacity = currFuncCapacity1;
        }
        else{
            System.out.println("Capacity Setting Error!");
        }
    }

    public static Double getRemainMemCapacity(){
        return currFuncCapacity;
    }
}
