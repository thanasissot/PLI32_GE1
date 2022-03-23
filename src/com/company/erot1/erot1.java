package com.company.erot1;

import java.text.DecimalFormat;
import java.util.HashMap;

public class erot1 {
    private static final DecimalFormat df = new DecimalFormat("0.000");

    public static void main(String[] args) {
        int simulationDays = 10_000_000;

        if (args.length > 0) {
            try {
                simulationDays = Integer.parseInt(args[0]);
            }
            catch (Exception e){
                System.out.println(e);
            }
            finally {
                simulationDays = 1_000_000;
            }
        }

        // number of customers
        int[] customers = {8, 10, 12, 14};
        // probability per customer number
        int[] customerFrequency = {35, 30, 25, 10};
        int n = customers.length;

        // koulouria sold per each customer
        int[] koulouria = {1, 2, 3, 4};
        // probability per koulouri number
        int[] koulouriaFrequency = {40, 30, 20, 10};
        int m = koulouria.length;

        // create and fill customers per day array, so for each number of KOulouria made per day
        // we will compare result to actual same numbers of customers
        int[] customersPerDay = new int[simulationDays];
        for (int i = 0; i < simulationDays; i++){
            customersPerDay[i] = myRand(customers, customerFrequency, n);
        }

        int totalBought;
        // same as customsersPerDay, we create a total of Koulouria requested per day
        // that is the number of customers and the number of koulouria each customer wants
        int[] koulouriaRequestedPerDay = new int[simulationDays];
        for (int i = 0; i < simulationDays; i++){
            totalBought = 0;
            for (int j = 0; j < customersPerDay[i]; j++){
                totalBought += myRand(koulouria, koulouriaFrequency, n);
            }
            koulouriaRequestedPerDay[i] = totalBought;
        }

        double koulouriStartingPrice = 1.2;
        double koulouriCost = 0.5;
        double koulouriLeftOverPrice = 0.4;
        int koulouriaMadeToday;
        int koulouriaSold;
        int koulouriaLeft;

        HashMap<Integer, Double> map = new HashMap<>();
        // minimum koulouria sold is = 8, maximum koulouria sold = 56
        for (int i = 8; i <= 56; i++) {
            koulouriaMadeToday = i;
            for (int j = 0; j < koulouriaRequestedPerDay.length; j++){
                koulouriaSold = koulouriaRequestedPerDay[j];
                // if koulouria sold is bigger than koulouria made today then all koulouria were sold
                // you cannot sell more koulouria than made, so create
                if (koulouriaSold > koulouriaMadeToday){
                    koulouriaSold = koulouriaMadeToday;
                    koulouriaLeft = 0;
                }
                else {
                    koulouriaLeft = koulouriaMadeToday - koulouriaSold;
                }
                // keep track of results in map, to calculate averages after
                map.merge(koulouriaMadeToday, (koulouriaSold * koulouriStartingPrice) + (koulouriaLeft * koulouriLeftOverPrice) - (koulouriaMadeToday * koulouriCost) , Double::sum);
            }
        }

        for (int i : map.keySet()){
            // updating the map, putting in average of profit and then print it to the console
            map.put(i, map.get(i) / simulationDays);
            System.out.println("Koulouria made: " + i + " | Average Profit: " + df.format(map.get(i)));
        }

    }

    // SOURCE: https://www.geeksforgeeks.org/random-number-generator-in-arbitrary-probability-distribution-fashion/

    // Utility function to find ceiling of r in arr[l..h]
    static int findCeil(int arr[], int r, int l, int h)
    {
        int mid;
        while (l < h)
        {
            mid = l + ((h - l) >> 1); // Same as mid = (l+h)/2
            if(r > arr[mid])
                l = mid + 1;
            else
                h = mid;
        }
        return (arr[l] >= r) ? l : -1;
    }

    // The main function that returns a random number
    // from arr[] according to distribution array
    // defined by freq[]. n is size of arrays.
    static int myRand(int arr[], int freq[], int n)
    {
        // Create and fill prefix array
        int prefix[] = new int[n], i;
        prefix[0] = freq[0];
        for (i = 1; i < n; ++i)
            prefix[i] = prefix[i - 1] + freq[i];

        // prefix[n-1] is sum of all frequencies.
        // Generate a random number with
        // value from 1 to this sum
        // 323567 random integer used as "seed" number
        int r = ((int)(Math.random()*(9438575)) % prefix[n - 1]) + 1;

        // Find index of ceiling of r in prefix array
        int indexc = findCeil(prefix, r, 0, n - 1);
        return arr[indexc];
    }

    // print percentage of appearance of customers in total SimulationDays to test convergence of probability distribution algorithm
    public static void probabilityDistributionPrintToConsole () {
        int[] customers = {8, 10, 12, 14};
        // probability per customer number
        int[] customerFrequency = {35, 30, 25, 10};
        int n = customers.length;

        int simulationDays = 100_0000;
        int[] customersPerDay = new int[simulationDays];
        for (int i = 0; i < simulationDays; i++){
            customersPerDay[i] = myRand(customers, customerFrequency, n);
        }

        HashMap<Integer, Integer> map = new HashMap<>();
        for (int a : customersPerDay) {
            map.merge(a, 1, Integer::sum);
        }

        for (int a : map.keySet()) System.out.println(a + " total is " + 100 * (double)map.get(a) / simulationDays);
    }

}
