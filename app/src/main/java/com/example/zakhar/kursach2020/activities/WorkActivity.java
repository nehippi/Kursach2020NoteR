package com.example.zakhar.kursach2020.activities;

import com.example.zakhar.kursach2020.*;
import com.example.zakhar.kursach2020.classes.Processer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;


import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WorkActivity extends AppCompatActivity {
    ConcurrentLinkedQueue<Double> koords = new ConcurrentLinkedQueue<>();
    Processer processer;
    ConstraintLayout llMain;
    ImageView imageView;
    int high;
    Bird bird;
    int bottom;
    private MediaRecorder mediaRecorder;
    private File audioFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);
        audioFile = new File(Environment.getExternalStorageDirectory(),
                "audio_test4.3gp");
        try {
            if (audioFile.exists()) {
                audioFile.delete();
            }
            audioFile.createNewFile();

        } catch (Exception e) {
            e.printStackTrace();
        }
        llMain = (ConstraintLayout) findViewById(R.id.LLMain);
        imageView = (ImageView) findViewById(R.id.imageView);
        high = llMain.getMaxHeight();
        bottom = 0;
        bird = new Bird(koords, imageView);
        int permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        int permissionStatus2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        } else {
            if (permissionStatus2 != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
            if (permissionStatus == PackageManager.PERMISSION_GRANTED && permissionStatus2 == PackageManager.PERMISSION_GRANTED) {

                processer = new Processer(koords, audioFile);
            }

        }

    }

    public void onStartClick(View view) {
    if(mediaRecorder==null) {
       // microphoneRecoderV4.start();
        mediaRecorder = new MediaRecorder();
        resetRecorder();
        mediaRecorder.start();
    }

    }

    public void onSopClick(View view) {
        if(mediaRecorder!=null) {
            mediaRecorder.stop();

//        try {
//            Thread.sleep(1000);
//        } catch (Exception e){
//            e.printStackTrace();
//        }
            if (audioFile.canRead()) {
                processer.start();
            } else {
                System.err.println("problem with file " + audioFile.getAbsolutePath());
            }
            //processer.stop();
            bird.start();
        }
        //    bird.stop();
    }

    public class Bird implements Runnable {
        ConcurrentLinkedQueue<Double> koords;
        ImageView bird;
        Thread thread;

        public Bird(ConcurrentLinkedQueue<Double> koords, ImageView bird) {
            this.koords = koords;
            this.bird = bird;
            this.thread = new Thread(this);
        }

        public void start() {
            try {
                this.thread.start();
                System.out.println("main started");

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
                if (!koords.isEmpty()) {
                    System.err.println("current  is:" + koords.peek());
                    try {
                        bird.setY(koords.poll().intValue());
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

    private void resetRecorder() {
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.UNPROCESSED);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setAudioEncodingBitRate(16);
        mediaRecorder.setAudioSamplingRate(44100);
        mediaRecorder.setOutputFile(audioFile.getAbsolutePath());

        try {
            mediaRecorder.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
