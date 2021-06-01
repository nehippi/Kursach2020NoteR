package com.example.zakhar.kursach2020.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;

import com.example.zakhar.kursach2020.R;
import com.example.zakhar.kursach2020.classes.FingerBoard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GuitarActivity extends AppCompatActivity {

    private List<Integer> recognizedNotes;
    private ImageView[][] fingerboard = new ImageView[13][6];
    private TableLayout table;
    private int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }
        setContentView(R.layout.activity_guitar);
        Bundle arguments = getIntent().getExtras();
        int[] arr = arguments.getIntArray("notes");
        recognizedNotes = Arrays.stream(arr).boxed().collect(Collectors.toList());
        table = findViewById(R.id.fingerboard);
        for (int i = 0; i < table.getChildCount(); i++) {
            ViewGroup row = (ViewGroup) table.getChildAt(i);
            for (int j = 0; j < row.getChildCount(); j++) {
                ImageView view = (ImageView) row.getChildAt(j);
                fingerboard[i][j] = view;
            }
        }
        setAllInvisible();
        onNextClick(null);
    }

    public void onNextClick(View view) {
        setAllInvisible();

        Map<Integer, Integer> map = FingerBoard.getNoteOnFingerBoard(recognizedNotes.get(currentIndex));
        for (int i = 0; i < 6; i++) {
            if (map.get(i) != null) {
                fingerboard[map.get(i)][i].setVisibility(View.VISIBLE);
            }
        }
        currentIndex++;
        if (currentIndex == recognizedNotes.size()) {
            currentIndex = 0;
        }
    }

    public void onPreviousClick(View view) {
        setAllInvisible();

        Map<Integer, Integer> map = FingerBoard.getNoteOnFingerBoard(recognizedNotes.get(currentIndex));
        for (int i = 0; i < 6; i++) {
            if (map.get(i) != null) {
                fingerboard[map.get(i)][i].setVisibility(View.VISIBLE);
            }
        }
        currentIndex--;
        if (currentIndex < 0) {
            currentIndex = recognizedNotes.size() - 1;
        }
    }

    private void setAllInvisible() {
        for (int i = 0; i < 13; i++) {
            for (int j = 0; j < 6; j++) {
                fingerboard[i][j].setVisibility(View.INVISIBLE);
            }
        }
    }


}