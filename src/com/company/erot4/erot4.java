package com.company.erot4;

import java.util.*;
import java.util.concurrent.Semaphore;

public class erot4 {
    static Random generator = new Random(1); // random generator
    static long SCALE = 1; // use this variable to scale up to real time by setting SCALE = 1000
    static long TIME = 4000  * SCALE; // total time in ms scaled to run the program
    static int GasStationPumpRemainingLitres = 200; // gas station starting and max Gas storage limit
    static int carsRefueledCount = 0; // total cars refueled
    static int gasStationRefuelCount = 0; // total Gas Station refuels
    static long carRefuelTimeTotal = 0; // total time Cars spent refueling
    static boolean RESUPPLY = false; // flag used to indicate if Gas Station needs to call for resupply

    // Car Class
    static class Car {
        // every car comes with a randomly filled fuel tank
        private final int currentTank;

        public Car(){
            this.currentTank = generator.nextInt(21) + 5; // random range in [5,25]
        }

        // calculating refueling time based on current tank
        public long getRefuelTime(){
            return (long) (SCALE * ((50 - currentTank) / 2.0));
        }

        public int getRefuelSize(){
            return 50 - currentTank;
        }
    }

    // GasStationRefuel begins when the GasStation calls for Resupply
    // class is sleeping for 300*SCALE ms then gas station Tank is filled MAX
    static class GasStationRefuel extends Thread {
        @Override
        public void run() {
            try {
                Thread.sleep(300 * SCALE);
                GasStationPumpRemainingLitres = 200;
                RESUPPLY = false;
                System.out.println("Gas Station refueled");
                gasStationRefuelCount++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Thread that simulates cars arriving at the Gas station
    // even when the Gas Station is refueling we still accept cars that arrive
    // and we insert them into a Deque<Car> queue
    static class InsertCarAtQueue extends Thread{
        private Deque<Car> deque;
        private boolean flag = true;
        public InsertCarAtQueue(Deque<Car> deque) {
            this.deque = deque;
        }

        public void stopRunning(){
            this.flag = false;
        }

        @Override
        public void run() {
            while (flag) {
                try {
                    // simulates the time a car arrives at the Gas station
                    Thread.sleep((generator.nextInt(271) + 30) * SCALE); // sleep for [30, 300] seconds
                    System.out.println("Car arrived for fuels.");
                    deque.add(new Car()); // adds car to the queue
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void main(String[] args) throws InterruptedException {
        Car car; // car instance
        long timeToRefuelCar; // time for each car meant to refuel
        int fuelNeeded; // how much fuel each car needs
        Deque<Car> deque = new LinkedList<>(); //  deque used for InsertCarAtQueue
        InsertCarAtQueue insertCarAtQueue = new InsertCarAtQueue(deque); // InsertCarAtQueue instance


        // Start time of cars arriving to the Gast Station
        insertCarAtQueue.start();

        // calculate total time to run the program
        long timeToEnd = System.currentTimeMillis() + TIME;

        // main loop
        while (System.currentTimeMillis() < timeToEnd) {
            // if gas station remaining litres less or equal to 10% of FULL size call for resupply
            if (GasStationPumpRemainingLitres <= 20 && !RESUPPLY){
                RESUPPLY = true;
                System.out.println("Gas station requested resupply.");
                new GasStationRefuel().start();
            }
            // if no car in line continue (and let some time pass in between so the simulation works as expected);
            if (deque.isEmpty()) {
                System.out.println("No car in line. Sleeping!");
                Thread.sleep(SCALE);
                continue;
            }

            car = deque.pop(); // next car to refuel
            fuelNeeded = car.getRefuelSize(); // fuel needed

            // if gas station remaining litres are less than next car in line needs
            // wait for the refuel Cargo to arrive than continue
            while (GasStationPumpRemainingLitres < fuelNeeded && RESUPPLY) {
                System.out.println("waiting to resupply gas station");
                Thread.sleep(SCALE);
            }

            GasStationPumpRemainingLitres -= car.getRefuelSize(); // reduce gas station Tank fuel
            timeToRefuelCar = car.getRefuelTime(); // calculate time needed for car to refuel
            Thread.sleep(timeToRefuelCar);  // wait for car to refuel
            carRefuelTimeTotal += timeToRefuelCar; // add to total time
            carsRefueledCount++; // increase count
        }

        // end thread inserting car to queue
        insertCarAtQueue.stopRunning();

        // print totals to the console
        System.out.println("Total cars fueled = " + carsRefueledCount);
        System.out.println("Average time of car refueling = " + carRefuelTimeTotal * 1. / carsRefueledCount);
        System.out.println("Times Gas Station got resupplied with fuel = " + gasStationRefuelCount);

    }

}
