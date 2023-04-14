package scs.util.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FunctionRequest {
    public Map<Integer,ArrayList<Integer>> getMap(int p,List<Map.Entry<String, ArrayList<Integer>>> list)
    {
        Map<Integer,ArrayList<Integer>> mp = new HashMap<>();
        for(int i = p * 7,j = 1;i<(((p+1)*7)%list.size());i++,j++)
        {
            System.out.println(list.get(i).getKey());
            mp.put(j,list.get(i).getValue());
        }
        return mp;
    }

    public Map<Integer,ArrayList<Integer>> getMapTest(Map<String,ArrayList<Integer>> mp)
    {
        Map<Integer,ArrayList<Integer>> mp1 = new HashMap<>();
        String str[] = new String[]{
                "a57bac412d613fe8bf26af57e2e4fe2054274e6fe66d120b1ef3a9315c38a5cc",
                "c108b4864b866b38b80d0e4594cc6d038f39668b804a1ba88d2b95d682a8ab20",
                "e4750c990ae62c562798f2556ffb69dc24f7a7e4e685fcba05824f8885bdd604",
                "b84c86b95ba7f7e1c0ea58ab7f3e9f685c138d1009789ef20ba93f7f5342149e",
                "7054706e8b0cbf30c40e65a8eefb438bd11ea21593d95d49e1d3f44a02d037a7",
                "8b492f4d307e34921e662e08b071ed15bee2ad67bcd4d302e70a425edcf767ac",
                "6ddf9d84df9ed32bb7ab4c51b5cd849dbaf46eaf63601aaea42adeafbe51f5db",
        };
        for(int i = 1; i <= 7; i++)
        {
            System.out.println(str[i-1]);
            mp1.put(i,mp.get(str[i-1]));
        }
        return mp1;
    }
}
