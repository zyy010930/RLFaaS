package scs.util.tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CSVReader {
    public static Map<String,Integer> FuncIdMp = new HashMap<>();

    public static Map<Integer,String> hashMap = new HashMap<>();
    static {
        hashMap.put(1,"740c5c767e4b9978ee59a97d1829cfbaf755a47806a3114f0d4c182bb5a7e253");
        hashMap.put(2,"c108b4864b866b38b80d0e4594cc6d038f39668b804a1ba88d2b95d682a8ab20");
        hashMap.put(3,"e4750c990ae62c562798f2556ffb69dc24f7a7e4e685fcba05824f8885bdd604");
        hashMap.put(4,"b84c86b95ba7f7e1c0ea58ab7f3e9f685c138d1009789ef20ba93f7f5342149e");
        hashMap.put(5,"7054706e8b0cbf30c40e65a8eefb438bd11ea21593d95d49e1d3f44a02d037a7");
        hashMap.put(6,"8b492f4d307e34921e662e08b071ed15bee2ad67bcd4d302e70a425edcf767ac");
        hashMap.put(7,"6ddf9d84df9ed32bb7ab4c51b5cd849dbaf46eaf63601aaea42adeafbe51f5db");
    }
 
    public List<Map.Entry<String, ArrayList<Integer>>> getAzure() {

        String csvFile = "/home/dmy/azureFunction/invocations_per_function_md.anon.d01.csv";
        String line = "";
        String cvsSplitBy = ",";
        Map<String,ArrayList<Integer>> InvokeMap = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] country = line.split(cvsSplitBy);
                if(country[3].compareTo("http") == 0) {
                    ArrayList<Integer> list = new ArrayList<>();
                    for (int i = 4; i <= 1443; i++) {
                        list.add(Integer.parseInt(country[i]));
                    }
                    if (InvokeMap.containsKey(country[2]) == false) {
                        InvokeMap.put(country[2], list);
                    }
                    System.out.println("[function= " + country[2] + " , trigger=" + country[3] + "]" + " num:" + list.size());
                }

            }
            List<Map.Entry<String, ArrayList<Integer>>> entryList2 = new ArrayList<>(InvokeMap.entrySet());
            /*
            Collections.sort(entryList2, new Comparator<Map.Entry<String, ArrayList<Integer>>>() {
                @Override
                public int compare(Map.Entry<String, ArrayList<Integer>> me1, Map.Entry<String, ArrayList<Integer>> me2) {
                    return Integer.valueOf(me1.getValue().size()).compareTo(me2.getValue().size()); // 升序排序
                    //return me2.getValue().compareTo(me1.getValue()); // 降序排序
                }
            });*/
            return entryList2;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<String,ArrayList<Integer>> getAzureTest() {
        String csvFile = "/home/dmy/azureFunction/invocations_per_function_md.anon.d01.csv";
        String line = "";
        String cvsSplitBy = ",";
        Map<String,ArrayList<Integer>> InvokeMap = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            int num = 1;
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] country = line.split(cvsSplitBy);
                if(country[3].compareTo("http") == 0) {
                    ArrayList<Integer> list = new ArrayList<>();
                    for (int i = 4; i <= 1443; i++) {
                        list.add(Integer.parseInt(country[i]));
                    }
                    if (InvokeMap.containsKey(country[2]) == false) {
                        InvokeMap.put(country[2], list);
                    }
                    FuncIdMp.put(country[2],num);
                    System.out.println("[function= " + country[2] + " , trigger=" + country[3] + "]" + " num:" + list.size());
                }
                num++;

            }
            return InvokeMap;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}