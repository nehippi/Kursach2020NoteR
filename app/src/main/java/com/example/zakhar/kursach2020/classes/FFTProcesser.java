//import java.util.ConcurrentLinkedQueue;
package com.example.zakhar.kursach2020.classes;

import java.util.concurrent.ConcurrentLinkedQueue;

public class FFTProcesser implements Runnable {
    ConcurrentLinkedQueue<short[]> samples;
    Thread thread;
    ConcurrentLinkedQueue<double[]> fftvalues;

    public FFTProcesser(ConcurrentLinkedQueue<short[]> samples, ConcurrentLinkedQueue<double[]> fftvalues) {
        this.samples = samples;
        this.fftvalues = fftvalues;
    }

    public void start() {
        this.thread = new Thread(this);
        thread.setName("fftProcesser");
        thread.start();
        System.out.println("fftProcesser started");
    }

    public void stop() {
        thread = null;
    }

    @Override
    public void run() {
        while (thread != null) {
            /**
             * из значений семплов херню от фурье
             */


            while (!samples.isEmpty()) {

                try {
                    short[] samplesArr = samples.poll();
                    Complex[] samplesComplex = new Complex[1024];
                    for (int i = 0; i < 1024; i++) {
                        samplesComplex[i] = new Complex((double) samplesArr[i], 0.0);
                    }
                    // System.out.println("fft");
                    Complex[] out = FFT.fft(samplesComplex);
                    double[] realPart = new double[out.length];
                    double[] imaginaryPart = new double[out.length];
                    for (int i = 0; i < out.length; i++) {
                        realPart[i] = out[i].re();
                        imaginaryPart[i] = out[i].im();
                    }
                   // System.err.println("current  is:" + realPart.peek());
                    fftvalues.add(realPart);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }
    }
}
