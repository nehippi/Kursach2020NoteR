package com.example.zakhar.kursach2020.activities;

import com.example.zakhar.kursach2020.*;
import com.example.zakhar.kursach2020.classes.NoteRecognizer;
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
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class WorkActivity extends AppCompatActivity {
    ArrayList<Double> freqs;
    Processer processer;
    ConstraintLayout llMain;
    NoteRecognizer noteRecognizer;
    ImageView imageView;
    int high;
    int bottom;
    private MediaRecorder mediaRecorder;
    private File audioFile;
    private Double etalon = null;
    private Button stop;
    private Button start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);
        stop = findViewById(R.id.stop);
        start = findViewById(R.id.start);
        audioFile = new File(Environment.getExternalStorageDirectory(),
                "audio_test4.3gp");
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

                processer = new Processer(audioFile);
            }

        }

    }

    public void onStartClick(View view) {

        try {
            if (audioFile.exists()) {
                audioFile.delete();
            }
            audioFile.createNewFile();
            processer.setFile(audioFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //start.setBackgroundColor(Color.YELLOW);
        // start.setClickable(false);
        //stop.setClickable(true);


        if (etalon == null) {
            etalon = getEtalon();

        } else {

                mediaRecorder = new MediaRecorder();
                resetRecorder();
                mediaRecorder.start();
        }

    }

    public void onSopClick(View view) {
        // start.setClickable(true);
        //stop.setClickable(false);

        if (mediaRecorder != null) {
            try {
                mediaRecorder.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }

                processer.setFile(audioFile);
                processer.start();

            try {
                processer.getAnalizerThread().join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            freqs = processer.getFreqs();


            if (etalon != null) {
                noteRecognizer = new NoteRecognizer(freqs, etalon);
                System.err.println(noteRecognizer.recognizeNotes());
            }

        }

    }

    private double getEtalon() {
        //audioFile = new File(Environment.getExternalStorageDirectory(),
          //      "audio_test4.3gp");
//        try {
//            if (audioFile.exists()) {
//                audioFile.delete();
//            }
//            audioFile.createNewFile();
//            processer.setFile(audioFile);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }//создали файл и привязали к немо обработчик

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setMaxDuration(2500);
        resetRecorder();//создали рекодер

        mediaRecorder.start();
        try {
            Thread.sleep(2600);
        } catch (Exception e) {
            e.printStackTrace(); //запустили и ждем
        }

        try {
            mediaRecorder.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (audioFile.canRead()) {//запскаем обработку
            processer.start();
        } else {
            System.err.println("problem with file " + audioFile.getAbsolutePath());
        }

        try {
            processer.getAnalizerThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ArrayList<Double> freqs = processer.getFreqs();//получили лист с частотами


        double sum = 0;
        for (int i = 0; i < freqs.size(); i++) {
            sum += freqs.get(i);
        }
        double etalon = sum / freqs.size();//получили эталон
        System.err.println("*************************etalon is " + etalon);

        return etalon;

    }

    private void resetRecorder() {
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.UNPROCESSED);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
     //   mediaRecorder.setAudioEncodingBitRate(16);
        //mediaRecorder.setAudioSamplingRate(44100);
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

