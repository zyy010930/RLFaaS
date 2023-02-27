package scs.util.tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CSVReader {
 
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
 
}