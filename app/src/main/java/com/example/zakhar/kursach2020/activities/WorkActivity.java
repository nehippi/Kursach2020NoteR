package com.example.zakhar.kursach2020.activities;

import com.example.zakhar.kursach2020.*;
import com.example.zakhar.kursach2020.classes.Processer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;


import java.io.Console;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WorkActivity extends AppCompatActivity {
    ConcurrentLinkedQueue<Double> koords = new ConcurrentLinkedQueue<>();
    Processer processer;
    ConstraintLayout llMain;
    ImageView imageView;
    int high;
    Bird bird;
    int bottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);

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

                processer = new Processer(koords);
            }

        }
    }

    public void onStartClick(View view) {


        processer.start();
    }

    public void onSopClick(View view) {
        processer.stop();
        bird.start();
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
}
