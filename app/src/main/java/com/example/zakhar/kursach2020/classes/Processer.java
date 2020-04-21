package com.example.zakhar.kursach2020.classes;

import android.media.AudioFormat;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import android.media.AudioFormat;

public class Processer {
    public ConcurrentLinkedQueue<double[]> queueOfGraphs = new ConcurrentLinkedQueue<>();


    ConcurrentLinkedQueue<byte[]> outFromMicro = new ConcurrentLinkedQueue<>();//выходной сигнал с микрофона
    ConcurrentLinkedQueue<double[]> fftValues = new ConcurrentLinkedQueue<>();//значения после преобразования фурье
    ConcurrentLinkedQueue<short[]> sampleInt = new ConcurrentLinkedQueue<>();//значения семплов
    ConcurrentLinkedQueue<Double> equalizedFreqs = new ConcurrentLinkedQueue<>();//сглаженые частоты

    private Analizer analizer;
    private FFTProcesser fftProcesser;
//    private ArrayList<Double> koords;
    private SampleMaker sampleMaker;
    private File file;
    public Processer(File fos) {

        int sampleRate = 44100;//format.getSampleRate();
        int sampleSize =32/8;
        this.file=fos;
        sampleMaker=new SampleMaker(sampleInt,file);
        analizer = new Analizer(fftValues);
        fftProcesser = new FFTProcesser(sampleInt, fftValues);
    }

    public Thread getAnalizerThread(){

        return analizer.getThread();
    }
    public ArrayList<Double> getFreqs(){
        return analizer.getFreqs();

    }

    public void setFile(File file) {
        this.file = file;
        sampleMaker.setFile(file);
    }

    public void start() {
        try {
            sampleMaker.start();
           // audioGetter.start();
            analizer.start();
            fftProcesser.start();
          //  waveFileBuilder.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop(){
        try {
            //fftProcesser.stop();
            analizer.stop();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
