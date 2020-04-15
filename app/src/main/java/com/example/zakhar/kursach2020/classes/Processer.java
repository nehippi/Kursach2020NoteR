package com.example.zakhar.kursach2020.classes;

import android.media.AudioFormat;

import java.util.concurrent.ConcurrentLinkedQueue;

import android.media.AudioFormat;

public class Processer {
    public ConcurrentLinkedQueue<double[]> queueOfGraphs = new ConcurrentLinkedQueue<>();


    AudioFormat.Builder formatBuilder; //= new AudioFormat.Builder();
    AudioFormat format;
    //(44100, 32, 1, true, false);
    //AudioFormat.Encoding.PCM_SIGNED,44100,32,1,2,8000,false);
    ConcurrentLinkedQueue<byte[]> outFromMicro = new ConcurrentLinkedQueue<>();//выходной сигнал с микрофона
    ConcurrentLinkedQueue<double[]> fftValues = new ConcurrentLinkedQueue<>();//значения после преобразования фурье
 //   ConcurrentLinkedQueue<AudioInputStream> ais = new ConcurrentLinkedQueue<>();//аудиопотоки полученые из сигнала микрофона
    ConcurrentLinkedQueue<long[]> sampleInt = new ConcurrentLinkedQueue<>();//значения семплов
    ConcurrentLinkedQueue<Double> freqs = new ConcurrentLinkedQueue<>();//частоты
    ConcurrentLinkedQueue<Double> equalizedFreqs = new ConcurrentLinkedQueue<>();//сглаженые частоты

    private Analizer analizer;
    private FFTProcesser fftProcesser;
    private WaveFileBuilder waveFileBuilder;
    private MicrophoneRecoder microphoneRecoder;
    private ConcurrentLinkedQueue<Double> koords;
    private MicrophoneRecoderV2 microphoneRecoderV2;
    public Processer(ConcurrentLinkedQueue<Double> koords) {

        this.koords=koords;
        freqs=koords;
       // formatBuilder = new AudioFormat.Builder();
       // formatBuilder.setSampleRate(441000);
       // formatBuilder.setEncoding(AudioFormat.ENCODING_PCM_FLOAT);//32 bit

        microphoneRecoder = new MicrophoneRecoder(outFromMicro);
        format = microphoneRecoder.getAf();//formatBuilder.build();
        int sampleRate = 44100;//format.getSampleRate();
        int sampleSize =32 / 8;
        microphoneRecoderV2=new MicrophoneRecoderV2(outFromMicro);
        analizer = new Analizer(fftValues, freqs);
        waveFileBuilder = new WaveFileBuilder(outFromMicro, sampleSize, sampleRate, sampleInt);
        fftProcesser = new FFTProcesser(sampleInt, fftValues);
        Equalizer equalizer = new Equalizer(freqs, equalizedFreqs);
    }

    public void start() {
        try {
            analizer.start();
            fftProcesser.start();
            waveFileBuilder.start();
            microphoneRecoderV2.start();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop(){
        try {
            microphoneRecoderV2.interrupt();
            fftProcesser.stop();
            waveFileBuilder.stop();
            analizer.stop();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
