package com.example.zakhar.kursach2020.classes;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.provider.MediaStore;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;

import static android.content.ContentValues.TAG;

public class MicrophoneRecoder implements Runnable {
    private ConcurrentLinkedQueue<byte[]> outFromMicro;//на выход
    private Thread thread;
    private static final int SAMPPERSEC = 48000;
    private AudioFormat af;
    private int bufferSize ;//= 8192;//взято на форуме ,нужно тестить разные размеры
    AudioRecord audioRecord;



    public MicrophoneRecoder(ConcurrentLinkedQueue<byte[]> outFromMicro) {
        this.outFromMicro = outFromMicro;
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
        int channelConfiguration = AudioFormat.CHANNEL_IN_MONO;
        int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
        bufferSize = AudioRecord.getMinBufferSize(SAMPPERSEC, channelConfiguration, audioEncoding);
        audioRecord = new AudioRecord(android.media.MediaRecorder.AudioSource.MIC, SAMPPERSEC, channelConfiguration, audioEncoding, bufferSize);

        af = audioRecord.getFormat();


    }

    public void start() {
        try {
            this.thread = new Thread(this);
            this.thread.start();
            this.thread.setPriority(1);
            System.out.println("MicrophoneReader started");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        this.thread = null;
    }

    @Override
    public void run() {
        while (thread != null) {
            byte[] audioData = new byte[bufferSize];
            audioRecord.read(audioData, 0, bufferSize);
            outFromMicro.add(audioData);


        }
    }

    private AudioRecord createAudioRecorder() {
        AudioRecord aR;
        int sampleRate = 16000;
        int channelConfig = AudioFormat.CHANNEL_IN_MONO;
        int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
        int minInternalBufferSize = AudioRecord.getMinBufferSize(sampleRate,
                channelConfig, audioFormat);
        int internalBufferSize = minInternalBufferSize * 4;
        aR = new AudioRecord(MediaRecorder.AudioSource.MIC,
                sampleRate, channelConfig, audioFormat, internalBufferSize);
        return aR;
    }

    public AudioFormat getAf() {
        return af;
    }
}
