package scs.util.tools;
 
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CSVReader {
 
    public List<Map.Entry<String, Integer>> getAzure() {

        String csvFile = "C:/Users/86180/Desktop/Azure/invocations_per_function_md.anon.d01.csv";
        String line = "";
        String cvsSplitBy = ",";
        Map<String,Integer> mp = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] country = line.split(cvsSplitBy);
                if(country[3].compareTo("http") == 0) {
                    int num = 0;
                    for (int i = 4; i <= 1443; i++) {
                        num += Integer.parseInt(country[i]);
                    }
                    if (mp.containsKey(country[2]) == false) {
                        mp.put(country[2], num);
                    } else {
                        mp.put(country[2], mp.get(country[2]) + num);
                    }
                    System.out.println("[function= " + country[2] + " , trigger=" + country[3] + "]" + " num:" + num);
                }

            }
            List<Map.Entry<String, Integer>> entryList2 = new ArrayList<Map.Entry<String, Integer>>(mp.entrySet());
            Collections.sort(entryList2, new Comparator<Map.Entry<String, Integer>>() {
                @Override
                public int compare(Map.Entry<String, Integer> me1, Map.Entry<String, Integer> me2) {
                    return me1.getValue().compareTo(me2.getValue()); // 升序排序
                    //return me2.getValue().compareTo(me1.getValue()); // 降序排序
                }
            });
            return entryList2;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
 
    }
 
}