package code.challenge;

import java.util.*;

public class SorttByValue {


    public static Map<String, Double> sortByValue(Map<String, Double> unsortMap) {

        // 1. Convert Map to List of Map
        List<Map.Entry<String, Double>> list =
                new LinkedList<Map.Entry<String, Double>>(unsortMap.entrySet());

        // 2. Sort list with Collections.sort(), provide a custom Comparator
        Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
            public int compare(Map.Entry<String, Double> o2,
                               Map.Entry<String, Double> o1) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // 3. Loop the sorted list and put it into a new insertion order Map LinkedHashMap
        Map<String, Double> sortedMap = new LinkedHashMap<String, Double>();
        for (Map.Entry<String, Double> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    public static <K, V> void printMap(int c, Map<K, V> map) {
        System.out.println("Top "+c+" customers with the highest Simple Lifetime Value from data");
        int counter = 0;
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (counter == c)
                break;
            counter++;

            System.out.println(entry.getKey()
                    + " LifeTimeValue : " + entry.getValue());

        }
    }

}