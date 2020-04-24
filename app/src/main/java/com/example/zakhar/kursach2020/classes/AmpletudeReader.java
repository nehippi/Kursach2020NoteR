package com.example.zakhar.kursach2020.classes;

import android.media.AudioFormat;
import android.media.AudioRecord;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;


public class AmpletudeReader {
        public ArrayList<byte[]> AudioData;
        public static AudioRecord audioRecord;
        public boolean recordFlag=false;

        private int bufflen;
        private static final int SAMPPERSEC = 48000;
        int info;
        public AmpletudeReader() {
            android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
            int channelConfiguration = AudioFormat.CHANNEL_CONFIGURATION_DEFAULT;
            int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
            bufflen = AudioRecord.getMinBufferSize(SAMPPERSEC, channelConfiguration, audioEncoding);
            audioRecord = new AudioRecord(android.media.MediaRecorder.AudioSource.MIC, SAMPPERSEC, channelConfiguration, audioEncoding, bufflen);
            audioRecord.startRecording();
            AudioData=new ArrayList<>();
        }

        private byte getMax(byte[] arr, int count) {
            byte m = 0;
            for (int i = 0; i < count; i++) {
                byte c = (byte) Math.abs(arr[i]);
                if (m < c) {
                    m = c;
                }
            }
            return m;
        }


        public void start() {


            while (!recordFlag) {
                info=audioRecord.getState();
                byte[] curr = new byte[bufflen];
                int currread = audioRecord.read(curr, 0, bufflen);

                System.err.println("current amplitude is:" + getMax(curr, currread));
              //  AudioData.add(curr);
              //  if (audioRecord.read(curr, 0, bufflen) == 0) break;
              //  if( getMax(curr, currread)>1000){
               //     recordFlag=true;
                //}
            }

        }

        public void stop(){

            audioRecord.stop();
            //audioRecord.release();

            info=audioRecord.getState();

        }
    }

