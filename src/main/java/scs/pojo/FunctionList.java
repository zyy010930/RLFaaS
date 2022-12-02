package scs.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FunctionList {
    private ArrayList<Function> functionArrayList;
    public static Map<Integer,Boolean> funcMap = new HashMap<Integer,Boolean>();
    static {
        funcMap.put(10,true);
        funcMap.put(16,true);
        funcMap.put(28,true);
        funcMap.put(29,true);
        funcMap.put(30,true);
        funcMap.put(31,true);
        funcMap.put(32,true);
    };

    public static Map<Integer, Date> timeMap = new HashMap<Integer,Date>();

    public FunctionList(){
        this.functionArrayList = new ArrayList<Function>();
    }

    public ArrayList<Function> getFunctionArrayList() {
        return functionArrayList;
    }

    public void setFunctionArrayList(ArrayList<Function> functionArrayList) {
        this.functionArrayList = functionArrayList;
    }

    public int getFunctionNum() {
        return this.functionArrayList.size();
    }
}
