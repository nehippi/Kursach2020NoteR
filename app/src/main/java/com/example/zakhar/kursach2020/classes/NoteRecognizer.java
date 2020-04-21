package com.example.zakhar.kursach2020.classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;

public class NoteRecognizer {
    ArrayList<Double> freqs;
    Thread thread;
    int l=20; //size of "window"
    int err=30; //error from noise
    double[] arr;// array with freqs

    double etalon;

    public NoteRecognizer(ArrayList<Double> freqs,double etalon) {
        this.freqs = freqs;
        this.etalon=etalon;
        arr=new double[freqs.size()];
       for(int i=0;i<freqs.size();i++){
           arr[i]=freqs.get(i);
       }

    }

    public ArrayList<Integer> recognizeNotes(){
        ArrayList<Integer> notes=new ArrayList<>();
     for(int i=0;i<arr.length-l;i++){
         if(getAverage(i+1)>getAverage(i+1)+err||getAverage(i+1)<getAverage(i)-err){
             notes.add(recognizeNote(getAverage(i)));
         }
     }
     return notes;
    }

    private double getAverage(int n){
        int a=n;
        int b=n+l;
        double[] subArr=Arrays.copyOfRange(this.arr,a,b);
        double sum=0;
        for(int i=0;i<subArr.length;i++){
            sum+=subArr[i];
        }
        return sum/subArr.length;
    }

    private int recognizeNote(double freq){

        double numberOfTone= (Math.log(freq/etalon) / Math.log(2))*12;
        return (int)numberOfTone;
    }
}
