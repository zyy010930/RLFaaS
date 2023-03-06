package scs.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FunctionList {
    private ArrayList<Function> functionArrayList;
    public static Map<Integer,Boolean> funcMap = new HashMap<Integer,Boolean>();
    static {
        funcMap.put(1,true);
        funcMap.put(2,true);
        funcMap.put(3,true);
        funcMap.put(4,true);
        funcMap.put(5,true);
        funcMap.put(6,true);
        funcMap.put(7,true);
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
