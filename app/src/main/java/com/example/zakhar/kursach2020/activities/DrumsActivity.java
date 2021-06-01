package com.example.zakhar.kursach2020.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.zakhar.kursach2020.R;

public class DrumsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        setContentView(R.layout.activity_drums);
    }
}