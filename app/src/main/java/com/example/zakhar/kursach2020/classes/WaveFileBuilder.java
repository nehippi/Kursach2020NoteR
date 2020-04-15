package com.example.zakhar.kursach2020.classes;

import java.util.concurrent.ConcurrentLinkedQueue;
//import java.util.ConcurrentLinkedQueue;

public class WaveFileBuilder implements Runnable {
    ConcurrentLinkedQueue<byte[]> data;
    ConcurrentLinkedQueue<long[]> sampleInt;
    Thread thread;
    int sampleSize;
    int sampleRate;

    public void start() {
        this.thread = new Thread(this);
        thread.setName("waveBuilder");
        thread.start();
        thread.setPriority(10);
        System.out.println("WFBuilder started");
    }

    public void stop() {
        thread = null;
    }

    public WaveFileBuilder(ConcurrentLinkedQueue<byte[]> data, int sampleSize, int sampleRate, ConcurrentLinkedQueue<long[]> sampleInt) {
        this.data = data    ;
        this.sampleInt = sampleInt;
        this.sampleRate = sampleRate;
        this.sampleSize = sampleSize;
    }

    @Override
    public void run() {
        while (thread != null) {
            /**
             * из потока аудио в вэйв файл
             */
            while (!data.isEmpty()) {
                byte[] info = data.poll();

                try {

                    WaveFile waveFile = new WaveFile(sampleSize, sampleRate, info);
                    int n = 1024;
                    long[] samples = new long[(int) n];
                    for (int i = 0; i < n; i++) {
                        samples[i] = waveFile.getSampleInt(i);
                    }
                    sampleInt.add(samples);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }
    }
}
