package scs.util.tools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ARIMAReader {
    public static Map<Integer,ArrayList<Double>> arimaList = new HashMap<>();

    public ARIMAReader(){}

    public void getARIMA() throws FileNotFoundException {
        String csvFile = "/home/dmy/person.csv";
        String line = "";
        String cvsSplitBy = ",";
        int num = 1;
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))){
            while ((line = br.readLine()) != null) {
                String[] country = line.split(cvsSplitBy);
                if(country.length <= 0)
                {
                    continue;
                }
                else
                {
                    if(country.length == 1 && country[0] != "0")
                        continue;
                    ArrayList<Double> list = new ArrayList<>();
                    for (int i = 0; i < country.length; i++) {
                        list.add(Double.parseDouble(country[i]));
                    }
                    arimaList.put(num,list);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
