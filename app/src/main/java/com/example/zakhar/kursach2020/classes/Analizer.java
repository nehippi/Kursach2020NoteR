package com.example.zakhar.kursach2020.classes;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Analizer implements Runnable {
    public ConcurrentLinkedQueue<double[]> fftValues;

    private ArrayList<Double> freqs=new ArrayList<>();
    private Thread thread;
    public Analizer(ConcurrentLinkedQueue<double[]> fftValues) {
        this.fftValues = fftValues;
    }

    public void start() {
        this.thread = new Thread(this);
        thread.setName("analizer");
        this.thread.start();

        System.out.println("analizer started");
    }

    public void stop() {
        this.thread = null;
    }

    @Override

    public void run() {
        try {
            while (thread!=null) {
                while (!fftValues.isEmpty()) {
                    if(fftValues.peek().length==666){
                        thread=null;
                        fftValues.poll();
                        continue;
                    }
                    //analize start
                    double max = 0;
                    double[] fftValue = fftValues.poll();
                    for (int i = 0; i < fftValue.length / 2; i++) {
                        if (fftValue[i] > max) max = fftValue[i];
                    }
                    /**
                     * ищем максимальное значение массива после ффт
                     * и определяем в какой частоте оно расположено
                     */
                    double freq = 0;
                    for (int i = 0; i < fftValue.length / 2; i++) {
                        if (max == fftValue[i]) {
                            freq = i * 44100 / 1024;//возможно надо i+1 но это не точно
                            if (freq > 20 && freq < 2000) {
                                freqs.add(new Double(freq));
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
        }
    }

    public Thread getThread() {
        return thread;
    }

    int[] getRangeOfBigestColumn(double[] arr, int window) {
        int[] range = {0, 0};
        double max = 0;

        for (int i = 0; i < arr.length; i++) {
            double[] subArr = Arrays.copyOfRange(arr, i, i + window);
            double mean = getMeanFromArray(subArr);
            if (mean > max) {
                max = mean;
                range = new int[]{i, i + window};
            }
        }
        return range;
    }

    public static double getMeanFromArray(double[] array) {
        double sum = 0;
        for (int i = 0; i < array.length; i++) {
            sum += Math.abs(array[i]);
        }
        return sum / array.length;
    }

    public ArrayList<Double> getFreqs() {

        return freqs;
    }
}
