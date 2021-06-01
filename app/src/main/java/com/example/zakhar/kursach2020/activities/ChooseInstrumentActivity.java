package com.example.zakhar.kursach2020.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.zakhar.kursach2020.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChooseInstrumentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_instrument);
        Bundle args = getIntent().getExtras();
    }

    public void onGuitarClick(View view){
        Intent intent = new Intent(this, GuitarActivity.class);
        intent.putExtra("notes",getIntent().getExtras().getIntArray("notes"));
        startActivity(intent);
    }
}