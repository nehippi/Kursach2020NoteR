package com.example.zakhar.kursach2020.classes;

import android.media.MediaRecorder;
import android.os.ParcelFileDescriptor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MicrophoneRecoderV3 implements Runnable {
    MediaRecorder mediaRecorder;
    Thread thread;
    ConcurrentLinkedQueue<byte[]> dataFromMicro;
    ByteArrayOutputStream byteArrayOutputStream;
    ParcelFileDescriptor[] descriptors;
    ParcelFileDescriptor parcelRead;
    ParcelFileDescriptor parcelWrite;
    int counter = 0;
    boolean isRecording;
    InputStream inputStream;


    public MicrophoneRecoderV3(ConcurrentLinkedQueue<byte[]> dataFromMicro) {
        try {
            descriptors = ParcelFileDescriptor.createPipe();
            parcelRead = new ParcelFileDescriptor(descriptors[0]);
            parcelWrite = new ParcelFileDescriptor(descriptors[1]);


        } catch (Exception e) {
            e.printStackTrace();
        }


        this.dataFromMicro = dataFromMicro;
        mediaRecorder = new MediaRecorder();


    }

    public void start(boolean isRecording) {
        this.isRecording = isRecording;
        if (byteArrayOutputStream == null) {
            byteArrayOutputStream = new ByteArrayOutputStream();
        }
        inputStream = new ParcelFileDescriptor.AutoCloseInputStream(parcelRead);
        this.thread = new Thread(this);
        this.thread.setName("Microphone");

        thread.start();
        System.err.println("micro started");
    }

    public void stop() {
       try {
           mediaRecorder.stop();
       }catch (Exception e){

       }
        mediaRecorder.reset();
       //mediaRecorder.release();
        this.thread = null;
        System.err.println("micro stopped");

    }

    //два режима один для записи другой для загрузки данных дальше
    @Override
    public void run() {
        if (isRecording) {//если режим записи то в цикле сичываем звук в массив и пихаем его в стрим
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.UNPROCESSED);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.WEBM);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            //mediaRecorder.setAudioEncodingBitRate(32);
            //mediaRecorder.setAudioSamplingRate(44100);

            mediaRecorder.setOutputFile(parcelWrite.getFileDescriptor());
            try {
                mediaRecorder.prepare();
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("prepare failed");
            }//init
            mediaRecorder.start();
            byte[] data = new byte[44100];
            int read;//too init


            try {
                while (thread != null && (read = inputStream.read(data, 0, data.length)) != -1) {
                    byteArrayOutputStream.write(data, 0, read);
                    byteArrayOutputStream.flush();
                    counter++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }//record
        } else {//если режим загрузки то режем массив на порции и если очередь свобдна отправляем дальше
            try {
                byteArrayOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            byte[] arr = byteArrayOutputStream.toByteArray();

            dataFromMicro.add(arr);
        }
    }

    public void resetData() {//для повторного использования класса вызвать этот метод
        inputStream = null;
        byteArrayOutputStream = null;
    }

}