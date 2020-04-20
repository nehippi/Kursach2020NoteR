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
import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WorkActivity extends AppCompatActivity {
    ConcurrentLinkedQueue<Double> koords = new ConcurrentLinkedQueue<>();
    Processer processer;
    ConstraintLayout llMain;
    ImageView imageView;
    int high;
    int bottom;
    private MediaRecorder mediaRecorder;
    private File audioFile;
    private Double etalon = null;

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
        if (mediaRecorder == null) {
            if (etalon == null) {
                mediaRecorder.setMaxDuration(2500);
                onSopClick(view);
                getEtalon(koords);
            }
            mediaRecorder = new MediaRecorder();
            resetRecorder();
            mediaRecorder.start();
        }

    }

    public void onSopClick(View view) {
        if (mediaRecorder != null) {
            mediaRecorder.stop();

            if (audioFile.canRead()) {
                processer.start();
            } else {
                System.err.println("problem with file " + audioFile.getAbsolutePath());
            }
        }
    }

    private double getEtalon(ConcurrentLinkedQueue<Double> queue) {
        int j = 0;
        double[] arr = new double[queue.size()];
        while (!queue.isEmpty()) {
            arr[j] = queue.poll();
            j++;
        }

        double sum = 0;
        for (int i = 0; i < arr.length; i++) {
            sum += arr[i];
        }
        return sum / arr.length;
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
