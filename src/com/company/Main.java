package com.company;

import com.company.erot1.erot1;

import java.text.DecimalFormat;
import java.util.*;

public class Main {
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public static void main(String[] args) {
        int N = 1000;
        HashMap<Integer, List<Integer>> map = new HashMap<>();
        double c3, d3;
        int result;

        for (int c = 1; c <= N; c++){
            c3 = Math.pow(c, 3);

            for (int d = 1; d <= N; d++){
                d3 = Math.pow(d, 3);

                result = (int) (c3 + d3);
                if (!map.containsKey(result)){
                    map.put(result, new ArrayList<>());
                }
                map.get(result).add(c);
                map.get(result).add(d);

            }
        }

        for (int list : map.keySet()){
            for (int a : map.get(result)){
                for (int)
            }
        }
    }
}
