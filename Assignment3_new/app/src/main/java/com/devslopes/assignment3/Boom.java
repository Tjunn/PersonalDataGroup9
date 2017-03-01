package com.devslopes.assignment3;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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


    int move = 25;
    int maxTime = 150;
    int minTime = 0;
    long waitTime = 0;

    void disableButtons(){
        bt_instant.setVisibility(View.GONE);
        bt_not_instant.setVisibility(View.GONE);
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
                ((ViewGroup.MarginLayoutParams)nyan_cat.getLayoutParams()).leftMargin += move;
                nyan_cat.requestLayout();
            }
        }.start();
    }

    void setRandomTime(){
        Random r = new Random();
        waitTime = r.nextInt(maxTime - minTime) + minTime;
    }

    public void addResult(boolean is_instant){
        boolean is_female = rb_female.isChecked();
        String age = ed_age.getText().toString();
        String gender;
        String instanty;

        if(is_female){
            gender = "female";
        }else{
            gender = "male";
        }
        if(is_instant){
            instanty = "instant";
        }else{
            instanty = "not instant";
        }

        Calendar c = Calendar.getInstance();
        String actual_time = c.getTime().toString().substring(0, 19);

        String dataToBeWriten = actual_time+";"+gender+";"+age+";"+instanty+";"+waitTime+"\n";
        checkPermission();

        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(dir, "instantData.csv");

        if(!dir.exists())
            if(!dir.mkdirs())
                    Log.e("e","Can't create dirs");

        if(!file.exists())
            try {
                if(file.createNewFile())
                    Log.e("e","Can't create file");
            } catch (IOException e) {
                e.printStackTrace();
            }

        //Write to file
        try (FileWriter fileWrite = new FileWriter(file, true)) {
            fileWrite.append(dataToBeWriten);
            Toast.makeText(Boom.this,
                    "Result stored", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("e","oh noo, exception :'(");
            Toast.makeText(Boom.this,
                    "Something went wrong.. Contact your local government", Toast.LENGTH_SHORT).show();
            //Handle exception
        }


    }

    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    void checkPermission(){
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(Boom.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.

        }
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

        checkPermission();

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
