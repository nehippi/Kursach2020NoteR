package com.example.zakhar.kursach2020.classes;

import com.example.zakhar.kursach2020.classes.SoundFile.SoundFile;

import java.io.File;
import java.nio.ShortBuffer;
import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SampleMaker implements Runnable {
    ConcurrentLinkedQueue out;
    Thread thread;
    File file;

    public void setFile(File file) {
        this.file = file;
    }

    public SampleMaker(ConcurrentLinkedQueue out, File file) {
        this.out = out;
        this.file = file;
    }

    private short[] returnSamples() {
        try {
            SoundFile soundFile = SoundFile.create(file.getAbsolutePath());
            int sizeBytes= soundFile.getFileSizeBytes();

            ShortBuffer samples = soundFile.getSamples();
            int size=samples.capacity();
            short[] out=new short[size];
            int i=0;
            while (samples.hasRemaining()){
                out[i]=samples.get();
                i++;
        }


            return  out;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return  null;
    }

    public void start(){
        this.thread=new Thread(this);
        thread.start();
    }



    @Override
    public void run() {
        short[] samples=returnSamples();

        int n = 1024;
        int countOfPortions=samples.length/n;

        for (int i = 0; i < countOfPortions-1; i++) {
            out.add(Arrays.copyOfRange(samples,i*n,(i+1)*n));

        }
        out.add(new short[666]);

    }
}
