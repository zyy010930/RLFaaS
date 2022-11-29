package scs.util.tools;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FunctionRequest {
    public Map<Integer,Integer> getMap()
    {
        CSVReader reader = new CSVReader();
        List<Map.Entry<String, Integer>> list = reader.getAzure();
        Map<Integer,Integer> mp = new HashMap<>();
        int num = 0;
        int t = 1;
        for(int i=0;i<list.size();i++)
        {
            if(i<(list.size()/7)*t) {
                num += list.get(i).getValue();
            }
            else{
                mp.put(t,num);
                t++;
                num = list.get(i).getValue();
            }
        }
        return mp;
    }
}
