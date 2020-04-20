package com.example.zakhar.kursach2020.classes;

import android.media.MediaRecorder;
import android.os.ParcelFileDescriptor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ConcurrentLinkedQueue;

public class AudioGetter implements Runnable {
    private ConcurrentLinkedQueue<byte[]> dataFromMicro;
    private ByteArrayOutputStream byteArrayOutputStream;
    private Thread thread;
    private File audioFile;
    private InputStream inputStream;

    public AudioGetter(ConcurrentLinkedQueue<byte[]> dataFromMicro, File audioFile) {
        this.dataFromMicro = dataFromMicro;
        this.audioFile = audioFile;


    }

    public void start() {
        try {
            inputStream = new FileInputStream(audioFile);

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (byteArrayOutputStream == null) {
            byteArrayOutputStream = new ByteArrayOutputStream();
        }
        this.thread = new Thread(this);
        this.thread.setName("AudioGetter");

        thread.start();
        System.out.println("AudioGetter started");
    }

    public void stop() {
        try {
            inputStream.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        this.thread = null;
        System.err.println("AudioGetter stopped");

    }

    public void run() {

            byte[] data = new byte[44100];
            int read;//too init
            try {
                while (thread != null && (read = inputStream.read(data, 0, data.length)) != -1) {
                    byteArrayOutputStream.write(data, 0, read);
                    byteArrayOutputStream.flush();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }//read from file

            try {
                byteArrayOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            byte[] arr = byteArrayOutputStream.toByteArray();

            dataFromMicro.add(arr);
        }
    }


