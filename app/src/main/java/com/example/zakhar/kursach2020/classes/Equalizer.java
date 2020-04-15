package com.example.zakhar.kursach2020.classes;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Equalizer implements Runnable {
    ConcurrentLinkedQueue<Double> freqs;
    ConcurrentLinkedQueue<Double> equlizedFreqs;
    Thread thread;

    public Equalizer(ConcurrentLinkedQueue<Double> freqs, ConcurrentLinkedQueue<Double> equlizedFreqs) {
        this.freqs = freqs;
        this.equlizedFreqs = equlizedFreqs;
    }

    public void start() {
        this.thread = new Thread(this);
        this.thread.start();

        System.out.println("Equalizer started");
    }

    public void stop() {
        this.thread = null;
    }

    @Override
    public void run() {
        double a;
        double b;
        while (thread != null) {
            while (freqs.size() > 2) {
                a = freqs.poll();
                b = freqs.peek();

                if (b > a) {
                    for (double i = a; i < b; i += 3) {
                        equlizedFreqs.add(i);
                    }
                } else {
                    for (double i = a; i > b; i -= 3) {
                        equlizedFreqs.add(i);
                    }
                }

            }
        }
    }
}