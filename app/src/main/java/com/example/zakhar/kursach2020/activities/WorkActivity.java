package com.example.zakhar.kursach2020.activities;

import com.example.zakhar.kursach2020.*;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class WorkActivity extends AppCompatActivity {
    ConstraintLayout llMain;
    ImageView imageView;
    ProgressBar progressBar2;
    TextView textView;
    TextView timer;
    String etalonName = "etalon.3gp";
    String audioName = "Audio.3gp";
    InputStream is;
    OutputStream os;
    Socket socket;
    String ip = "37.212.16.31";
    String port = "8080";
    ImageButton imageButton;
    EditText editIp;
    int high;
    Sender sender;
    int bottom;
    private static MediaRecorder mediaRecorder;
    boolean isRecording = false;
    private File audioFile;
    private Button stop;
    private Button start;
    private ProgressBar progressBar;
    private boolean isFirstTime = true;
    MyTimer myTimer;
    String ipString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);
        editIp = findViewById(R.id.ipEdit);
        stop = findViewById(R.id.stop);
        start = findViewById(R.id.start);
        progressBar = findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.INVISIBLE);
        progressBar2 = findViewById(R.id.progressBar3);
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);
        imageButton = findViewById(R.id.imageButton);
        imageButton.setVisibility(View.INVISIBLE);
        timer = findViewById(R.id.timer);
        myTimer = new MyTimer(timer, false);
        // timer.setVisibility(View.INVISIBLE);
        hideRecordong();

        audioFile = new File(Environment.getExternalStorageDirectory(),
                "audio_test4.3gp");
        llMain = (ConstraintLayout) findViewById(R.id.LLMain);
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

            }

        }

    }

    public void onSendClick(View view) {
        ipString = editIp.getText().toString();

        if (ipString != "") {
            sender=new Sender();
            System.out.println("on send click");
            sender.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    public void onEtalonClick(View view) {
        imageButton.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        myTimer.setMainRecord(false);
        Thread thread = new Thread(myTimer);
        EtalonRecoder etalonRecoder = new EtalonRecoder(thread);
        timer.setVisibility(View.VISIBLE);
        thread.start();

        etalonRecoder.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        isFirstTime = false;
    }


    public void onStartClick(View view) {
        isRecording = true;
        imageButton.setVisibility(View.INVISIBLE);
        if (!isFirstTime) {
            Recoder recoder = new Recoder();
            showRecording();
            recoder.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            Toast.makeText(this, "Firstly record an Standart", Toast.LENGTH_LONG)
                    .show();
        }
    }

    public void onStopClick(View view) {
        hideRecordong();
       mediaRecorder.stop();
        isRecording = false;
        imageButton.setVisibility(View.VISIBLE);

    }


    private void resetRecorder(File file) {
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.UNPROCESSED);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        //mediaRecorder.setAudioEncodingBitRate(16);
        //mediaRecorder.setAudioSamplingRate(44100);
        mediaRecorder.setOutputFile(file.getAbsolutePath());

        try {
            mediaRecorder.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class EtalonRecoder extends AsyncTask<Void, Void, Void> {
        Thread thread;

        public EtalonRecoder(Thread thread) {
            this.thread = thread;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            try {
                File audioFileEt = new File(Environment.getExternalStorageDirectory(),
                        "etalon.3gp");
                if (audioFileEt.exists()) {
                    audioFileEt.delete();
                }
                audioFileEt.createNewFile();


                if (mediaRecorder == null) {
                    mediaRecorder = new MediaRecorder();
                }
                mediaRecorder.setMaxDuration(2400);
                resetRecorder(audioFileEt);

                stop.setClickable(false);
                start.setClickable(false);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                thread.join();
                mediaRecorder.start();
                int counter = 0;
                progressBar.setMax(2400);
                while (counter < 2400) {
                    counter++;
                    progressBar.setProgress(counter);
                    TimeUnit.MILLISECONDS.sleep(1);

                }
                progressBar.setProgress(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            super.onPostExecute(aVoid);
            try {
                mediaRecorder.stop();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
            try {
                mediaRecorder.reset();
//                mediaRecorder.release();
                mediaRecorder = null;
            } catch (Exception e) {
                e.printStackTrace();
            }

            stop.setClickable(true);
            start.setClickable(true);
            progressBar.setVisibility(View.INVISIBLE);
        }


    }

    class Recoder extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {

            super.onPreExecute();
            try {
                File audioFileEt = new File(Environment.getExternalStorageDirectory(),
                        "Audio.3gp");
                if (audioFileEt.exists()) {
                    audioFileEt.delete();
                }
                audioFileEt.createNewFile();


                if (mediaRecorder == null) {
                    mediaRecorder = new MediaRecorder();
                }
                resetRecorder(audioFileEt);
                stop.setClickable(true);
                start.setClickable(false);


            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;

            start.setClickable(true);
            stop.setClickable(false);

        }

        @Override
        protected Void doInBackground(Void... voids) {

            mediaRecorder.start();
            while (isRecording) {

            }

            try {
                mediaRecorder.stop();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public void showRecording() {

        progressBar2.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.VISIBLE);
    }

    public void hideRecordong() {

        progressBar2.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.INVISIBLE);
        imageView.setVisibility(View.INVISIBLE);

    }


    public class MyTimer implements Runnable {
        TextView timer;
        boolean isMainRecord;

        public MyTimer(TextView timer, boolean isMainRecord) {
            this.timer = timer;
            this.isMainRecord = isMainRecord;
        }

        public void setMainRecord(boolean mainRecord) {
            isMainRecord = mainRecord;
        }

        @Override
        public void run() {
            for (int i = 3; i >= 0; i--) {
                timer.setText("" + i);
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
            timer.setText("");
        }
    }

    private class Sender extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            System.out.println("on pe sender");
            super.onPreExecute();
            imageButton.setVisibility(View.INVISIBLE);
            progressBar2.setVisibility(View.VISIBLE);


        }


        @RequiresApi(api = Build.VERSION_CODES.O)
        private void writeAudio(String audioName, String etalonName) {
            try {
                byte[] toSendAudio = getAudioBytes(audioName);
                byte[] toSendEtalon=getAudioBytes(etalonName);
                DataOutputStream dos = new DataOutputStream(os);

                System.out.println(toSendAudio.length);
                System.out.println(toSendEtalon.length);

                dos.writeInt(toSendAudio.length);
                dos.write(toSendAudio);

                dos.writeInt(toSendEtalon.length);
                dos.write(toSendEtalon);

                dos.flush();
                dos.close();

                System.out.println("writing audio is complited");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        private void reciveFromSocket(InputStream is) {
            try {
                int answ;
                while ((answ = is.read()) != 666) {
                    answ = 0;
                }

                System.out.println("writing audio is complited");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        private byte[] getAudioBytes(String fileName) {
            File audioFileEt = new File(Environment.getExternalStorageDirectory(),
                    fileName);
            try {
                byte[] out = Files.readAllBytes(Paths.get(audioFileEt.getAbsolutePath()));
                return out;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        }


        @Override
        protected void onPostExecute(Void aVoid) {
            System.out.println("on post sender");
            super.onPostExecute(aVoid);
            progressBar2.setVisibility(View.INVISIBLE);

        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                System.out.println("do in back sender");
                ipString = "192.168.1.240";
                socket = new Socket(ipString, Integer.parseInt(port));
                socket.setKeepAlive(true);
                is = socket.getInputStream();
                os = socket.getOutputStream();
                System.out.println("Connected!");
                writeAudio(audioName,etalonName);
                //writeAudio(etalonName);
              //  while (!socket.isInputShutdown()) {

              //  }
                System.out.println("bytes writed");
            } catch (Exception x) {
                x.printStackTrace();
            }

            return null;
        }
    }


}

