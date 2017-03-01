package com.devslopes.assignment3;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Random;

public class Boom extends AppCompatActivity {

    Button bt_left;
    Button bt_right;
    Button bt_instant;
    Button bt_not_instant;
    ImageView nyan_cat;

    RadioButton rb_female;
    EditText ed_age;


    int move = 20;
    int maxTime = 150;
    int minTime = 0;
    long waitTime = 0;

    void disableButtons(){
        bt_instant.setVisibility(View.GONE);
        bt_not_instant.setVisibility(View.GONE);
        Toast.makeText(Boom.this,
                "Result stored", Toast.LENGTH_LONG).show();
    }

    void enableButtons(){
        bt_instant.setVisibility(View.VISIBLE);
        bt_not_instant.setVisibility(View.VISIBLE);
    }

    void leftTime(){
        new CountDownTimer(waitTime,waitTime){

            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                ((ViewGroup.MarginLayoutParams)nyan_cat.getLayoutParams()).leftMargin -= move;
                nyan_cat.requestLayout();
            }
        }.start();
    }

    void rightTime(){
        new CountDownTimer(waitTime,waitTime){

            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                int mar = ((ViewGroup.MarginLayoutParams)nyan_cat.getLayoutParams()).leftMargin;

                ((ViewGroup.MarginLayoutParams)nyan_cat.getLayoutParams()).leftMargin += move;
                nyan_cat.requestLayout();
            }
        }.start();
    }



    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }



    void setRandomTime(){
        Random r = new Random();
        waitTime = r.nextInt(maxTime - minTime) + minTime;
    }

    public void addResult(boolean is_instant){
        boolean is_female = rb_female.isChecked();
        int age = Integer.parseInt(ed_age.getText().toString());



        FileOutputStream fos;
        try {
            fos = getApplicationContext().openFileOutput("IsItInstant_data.csv", Context.MODE_APPEND);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        Log.e(getApplicationContext()+"","what");

        OutputStreamWriter osw = new OutputStreamWriter(fos, Charset.forName("UTF-8"));
        BufferedWriter bw = new BufferedWriter(osw);

        String gender;
        if(is_female){
            gender = "female";
        }else{
            gender = "male";
        }
        String instanty;
        if(is_instant){
            instanty = "instant";
        }else{
            instanty = "not instant";
        }

        Calendar c = Calendar.getInstance();
        String actual_time = c.getTime().toString();

        try {
            bw.write(actual_time+";"+gender+";"+age+";"+instanty+";"+waitTime);
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

       // String dataToBeWriten = actual_time+";"+gender+";"+age+";"+instanty+";"+waitTime+"\n";




    }






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boom);
        bt_left =  (Button) findViewById(R.id.btLeft);
        bt_right =  (Button) findViewById(R.id.btRight);
        bt_instant =  (Button) findViewById(R.id.btInstant);
        bt_not_instant =  (Button) findViewById(R.id.btNotInstant);
        nyan_cat = (ImageView) findViewById(R.id.nyanCat);

        rb_female = (RadioButton) findViewById(R.id.rbFemale);
        ed_age = (EditText) findViewById(R.id.edAge);

        setRandomTime();


        bt_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                leftTime();
                enableButtons();

            }
        });

        bt_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rightTime();
                enableButtons();



            }
        });

        bt_instant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRandomTime();
                addResult(true);
                disableButtons();

            }
        });

        bt_not_instant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setRandomTime();
                addResult(false);
                disableButtons();

            }
        });






    }
}
