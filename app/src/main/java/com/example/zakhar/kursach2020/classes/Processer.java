package com.example.zakhar.kursach2020.classes;

import android.media.AudioFormat;

import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.ConcurrentLinkedQueue;

import android.media.AudioFormat;

public class Processer {
    public ConcurrentLinkedQueue<double[]> queueOfGraphs = new ConcurrentLinkedQueue<>();


    //AudioFormat.Builder formatBuilder; //= new AudioFormat.Builder();
    //AudioFormat format;
    //(44100, 32, 1, true, false);
    //AudioFormat.Encoding.PCM_SIGNED,44100,32,1,2,8000,false);
    ConcurrentLinkedQueue<byte[]> outFromMicro = new ConcurrentLinkedQueue<>();//выходной сигнал с микрофона
    ConcurrentLinkedQueue<double[]> fftValues = new ConcurrentLinkedQueue<>();//значения после преобразования фурье
 //   ConcurrentLinkedQueue<AudioInputStream> ais = new ConcurrentLinkedQueue<>();//аудиопотоки полученые из сигнала микрофона
    ConcurrentLinkedQueue<short[]> sampleInt = new ConcurrentLinkedQueue<>();//значения семплов
    ConcurrentLinkedQueue<Double> freqs = new ConcurrentLinkedQueue<>();//частоты
    ConcurrentLinkedQueue<Double> equalizedFreqs = new ConcurrentLinkedQueue<>();//сглаженые частоты

    private Analizer analizer;
    private FFTProcesser fftProcesser;
    private ConcurrentLinkedQueue<Double> koords;
    private SampleMaker sampleMaker;
    public Processer(ConcurrentLinkedQueue<Double> koords, File fos) {

        this.koords=koords;
        freqs=koords;
        int sampleRate = 44100;//format.getSampleRate();
        int sampleSize =32/8;
        sampleMaker=new SampleMaker(sampleInt,fos);
        analizer = new Analizer(fftValues, freqs);
        fftProcesser = new FFTProcesser(sampleInt, fftValues);
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
            fftProcesser.stop();
            analizer.stop();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
