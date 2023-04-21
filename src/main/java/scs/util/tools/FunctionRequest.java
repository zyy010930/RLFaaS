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
//                "a57bac412d613fe8bf26af57e2e4fe2054274e6fe66d120b1ef3a9315c38a5cc",
//                "c108b4864b866b38b80d0e4594cc6d038f39668b804a1ba88d2b95d682a8ab20",
//                "e4750c990ae62c562798f2556ffb69dc24f7a7e4e685fcba05824f8885bdd604",
//                "b84c86b95ba7f7e1c0ea58ab7f3e9f685c138d1009789ef20ba93f7f5342149e",
//                "7054706e8b0cbf30c40e65a8eefb438bd11ea21593d95d49e1d3f44a02d037a7",
//                "8b492f4d307e34921e662e08b071ed15bee2ad67bcd4d302e70a425edcf767ac",
//                "6ddf9d84df9ed32bb7ab4c51b5cd849dbaf46eaf63601aaea42adeafbe51f5db",
                "11bf9d9b72b9feee4b81819af67540f853b254694f4077a9068d74f58694c62f",
                "c4a854b9db299f7ca4bbe29795abc95fa9397a7e1f5b448f89fe851f09793d09",
                "8351e94654b8a08207ad81ed5e1e6523b66485594a41093c354892df16472c73",
                "543b395fb9e1d53e11fe3295debe2bb40d87d22ff011400399313ffaaed1096a",
                "2e2e491f56ab2fd57a1c09f258631db573cf99d75b56ce1e06e98b570881b6bd",
                "85d593148f30200455e483fc5f4957e2b778bb5227a9daba3b7a44e591051967",
                "8930fabb127ef8b4ccf11b1a4c507c0d59e1b3bd8e957857c7acfa17ef0ae29a",
                "b79d28bb98abf02b909e1839bd71ef37017e1cdf0e09d3c1e1e9d01590e6e48b",
                "e63c24bdc1242476e26b99e33a3ba6d0f6d421ad41c6f71fb95799b33309b45c",
                "6b9baaebfd7aaefe799277b0c86fa6a2a33281b088532f100c88780daaa4dbaf",
                "f0b5d0b24c2f9b8de14f971e1b7b1d7bfebe96c0e2baff387fe7041eedb9bb8e",
                "b48afdeb42162509d6483a6494b6d166c2e19bc15016f2598a2989ae3ae6a697",
                "fc9e90183695d085b89a3ba9afa728fc711dfadb82c722ea40d2983e48614b6d",
                "982239d51348c08c813302cdaff1a7c0a040f17f11e2040e257bb91fb4dc7c0d",
                "403cf7f908a59a6bf9d94bd4897e74b4057e95fb7dc5461c08b7612563cc2658",
                "dca12906e58414388141d3ca4060aef692906f66e95805aa9b30ec1adc759043"
        };
        for(int i = 1; i <= 16; i++)
        {
            System.out.println(str[i-1]);
            mp1.put(i,mp.get(str[i-1]));
        }
        return mp1;
    }
}
