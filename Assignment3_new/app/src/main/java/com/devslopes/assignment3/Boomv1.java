package com.devslopes.assignment3;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class Boomv1 extends AppCompatActivity {

    Button bt_left;
    Button bt_right;
    Button bt_purrfect;
    SeekBar seek_bar;
    ImageView nyan_cat;
    TextView tx_val;

    int move = 20;
    int maxTime = 500;
    long waitTime = 0;


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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boom);
        bt_left =  (Button) findViewById(R.id.btLeft);
        bt_right =  (Button) findViewById(R.id.btRight);
        bt_purrfect =  (Button) findViewById(R.id.btInstant);
        seek_bar = (SeekBar) findViewById(R.id.seekBar);
        seek_bar.setMax(maxTime);
        nyan_cat = (ImageView) findViewById(R.id.nyanCat);
        tx_val = (TextView) findViewById(R.id.txVal);


        bt_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leftTime();
            }
        });

        bt_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rightTime();

            }
        });

        seek_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                waitTime = i;
                tx_val.setText(""+i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });




    }
}
