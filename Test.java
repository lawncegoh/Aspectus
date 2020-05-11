import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.stream.Collectors;
import java.util.*;
import java.io.*;

public class Test {

    /**
     * Returns the same 3D array that is sorted based on descending number of sales
     * of Y
     */
    public static String[][][] firstSort(String[][][] productVar) {
        for (int i = 0; i < productVar.length; i++) {
            Arrays.sort(productVar[i], new Comparator<String[]>() {
                public int compare(String[] o1, String[] o2) {
                    if ((Integer.parseInt(o1[2]) > (Integer.parseInt(o2[2])))) {
                        return -1;
                    } else if ((Integer.parseInt(o1[2]) < (Integer.parseInt(o2[2])))) {
                        return 1;
                    } else {
                        return 0;
                    }
                }
            });
        }

        for (int a = 0; a < productVar.length; a++) {
            for (int b = 0; b < productVar[a].length; b++) {
                for (int j = 0; j < productVar[b].length; j++) {
                    System.out.println(productVar[a][b][j]);
                }
            }
        }
        return productVar;
    }

    /**
     * Returns a hashmap that gives index of X and the average number of Y sales for
     * each X
     */
    public static HashMap<Integer, Integer> secondSort(String[][][] productVar) {
        HashMap<Integer, Integer> avgHash = new HashMap<>();

        // calculate the totalSalesPerX through a for loop through middle loop
        // getting the average of this current X and storing into eachXavg
        // store this integer, integer pair into the HashMap
        for (int container = 0; container < productVar.length; container++) { // outer loop for X
            int totalSalesPerX = 0;
            int eachXavg = 0;
            int currlength = productVar[container].length;
            for (int variant = 0; variant < currlength; variant++) { // middle loop for y
                totalSalesPerX += Integer.parseInt(productVar[container][variant][2]);
            }
            eachXavg = totalSalesPerX / currlength;
            avgHash.put(container, eachXavg);
        }

        LinkedHashMap<Integer, Integer> sortedHash = new LinkedHashMap<>();

        // storing in descending order in a HashMap, the X string value + the average
        // number of Y sales for it
        sortedHash = avgHash.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

        return sortedHash;
    }

    public static void outputCSV(String[][][] sorted, String fileName) {
        ArrayList<Integer> product = new ArrayList<>();
        ArrayList<Integer> average = new ArrayList<>();
        secondSort(sorted).entrySet().forEach(entry -> {
            product.add(entry.getKey());
            average.add(entry.getValue());
        });

        try (PrintWriter writer = new PrintWriter(new File(fileName))) {

            StringBuilder sb = new StringBuilder();
            sb.append("product");
            sb.append(',');
            sb.append("variants");
            sb.append(',');
            sb.append("price");
            sb.append(',');
            sb.append("sales");
            sb.append(',');
            sb.append("avg_sales");
            sb.append('\n');

            for (int i = 0; i < product.size(); i++) {
                for (int j = 0; j < sorted[i].length; j++) {
                    sb.append(product.get(i));
                    sb.append(",");
                    sb.append(sorted[product.get(i)][j][0]);
                    sb.append(",");
                    sb.append(sorted[product.get(i)][j][1]);
                    sb.append(",");
                    sb.append(sorted[product.get(i)][j][2]);
                    sb.append(",");
                    sb.append(average.get(i));
                    sb.append('\n');
                }
            }
            writer.write(sb.toString());

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        String[][][] testArray = { { { "description11", "22", "111" }, { "description14", "44", "322" } }, // x1 variant
                                                                                                           // 1 and 2
                { { "description21", "55", "441" }, { "description24", "20", "555" } }, // x2 variant 1 and 2
                { { "description31", "12", "1131" }, { "description34", "88", "1000" } } // x3 variant 1 and 2
        };

        firstSort(testArray);
        System.out.println("---");
        secondSort(testArray).entrySet().forEach(entry -> {
            System.out.println(entry.getKey() + " " + entry.getValue());
        });
        outputCSV(firstSort(testArray), "test.csv");
    }
}