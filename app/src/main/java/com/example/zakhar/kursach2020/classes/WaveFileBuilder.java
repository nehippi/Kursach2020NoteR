package com.example.zakhar.kursach2020.classes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
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
                byte[] dataBytes = data.poll();
                int[] dataInt=new int[dataBytes.length/4];
                int j=0;
                for(int i=0;i<dataBytes.length-4;i=i+4){
                    int sample = ByteBuffer.wrap(Arrays.copyOf(dataBytes,4))
                            .order(ByteOrder.LITTLE_ENDIAN).getInt();
                    dataInt[j]=sample;
                    j++;
                }
                try {

                    WaveFile waveFile = new WaveFile(sampleSize, sampleRate,1, dataInt);
                    long[] samples=new long[waveFile.getData().length/sampleSize];
                    for (int i = 0; i < waveFile.getData().length/sampleSize; i++) {
                        samples[i] = waveFile.getSampleInt(i);
                    }
                    int n = 1024;
                    int countOfPortions=(waveFile.getData().length/sampleSize)/n;

                   // long[] samplesPortion = new long[n];
                    for (int i = 0; i < countOfPortions; i++) {
                        sampleInt.add(Arrays.copyOf(samples,n));
                        //samplesPortion[i] = waveFile.getSampleInt(i);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }



        }
    }
}
