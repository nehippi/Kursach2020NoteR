package com.example.zakhar.kursach2020.classes;

import android.media.AudioFormat;
import android.media.AudioRecord;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MicrophoneRecoderV2 extends Thread {
    public ConcurrentLinkedQueue<byte[]> AudioData;
    public static AudioRecord audioRecord;
    public boolean recordFlag = false;
    private int bufflen;
    private static final int SAMPPERSEC = 44100;
    int info;

    public MicrophoneRecoderV2(ConcurrentLinkedQueue<byte[]> outFromMicro) {
        {
            this.setName("Micro");
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
            int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_DEFAULT;
            int audioEncoding = AudioFormat.ENCODING_PCM_FLOAT;
            bufflen = AudioRecord.getMinBufferSize(SAMPPERSEC, channelConfiguration, audioEncoding);
            audioRecord = new AudioRecord(android.media.MediaRecorder.AudioSource.MIC, SAMPPERSEC, channelConfiguration, audioEncoding, bufflen);
            audioRecord.startRecording();
            AudioData = outFromMicro;
        }

    }

    @Override
    public void run() {
        recordFlag = true;

        while (recordFlag) {
            info = audioRecord.getState();
            byte[] curr = new byte[bufflen];
            // int currread = audioRecord.read(curr, 0, bufflen);

            //System.err.println("current amplitude is:" + getMax(curr, currread));
          //  if (true||AudioData.isEmpty()) {
                System.err.println( getMax(curr));
                AudioData.add(curr);
           // }
            if (audioRecord.read(curr, 0, bufflen) == 0) break;

        }

    }

    @Override
    public void interrupt() {
        recordFlag = false;
        audioRecord.stop();
        // audioRecord.release();

        info = audioRecord.getState();

    }
    private byte getMax(byte[] mas){
        byte max=Byte.MIN_VALUE;
        for(int i=0;i<mas.length;i++){
            if(mas[i]>max)max=mas[i];

        }
        return max;
    }
}