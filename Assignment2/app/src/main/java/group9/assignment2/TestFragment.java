package group9.assignment2;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Random;


public class TestFragment extends Fragment {

    private TestFragmentInteractionListener mListener;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_test, container, false);

        // Bind buttons
        btStart = (Button) view.findViewById(R.id.bt_start);
        btTest = (Button) view.findViewById(R.id.bt_test);
        txBig = (TextView) view.findViewById(R.id.tx_big);
        txEarly = (TextView) view.findViewById(R.id.tx_early);
        bgColor = (RelativeLayout) view.findViewById(R.id.activity_start);
        //final RelativeLayout bgColor = (RelativeLayout) view.findViewById(R.id.activity_start);

        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txEarly.setVisibility(View.GONE);
                btStart.setVisibility(View.GONE);
                runCounter = 0;
                testRun();
            }
        });


        btTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                long time2 = System.currentTimeMillis();
                colorChanger.interrupt();
                long timeDelta = time2-time1;

                if(early){
                    btTest.setVisibility(View.GONE);
                    txEarly.setVisibility(View.VISIBLE);
                    txEarly.setText("A bit too early..\nStart over");
                    runCounter = 0;
                    bgColor.setBackgroundColor(0xFFBBDEFB);
                    btStart.setVisibility(View.VISIBLE);
                    txBig.setText("Press button below to try again ;)");
                    txBig.setTextColor(0xFF2196F3);
                    txEarly.setTextColor(0xFF2196F3);
                    backgroundColor = true;

                }else{
                    reactTime = timeDelta;
                    reactionTimes[runCounter] = reactTime;
                    runCounter++;
                    if(runCounter<10){
                        testRun();
                    }else{
                        btTest.setVisibility(View.GONE);
                        btStart.setVisibility(View.VISIBLE);
                        bgColor.setBackgroundColor(0xFFBBDEFB);
                        txEarly.setTextColor(0xFF2196F3);
                        txBig.setTextColor(0xFF2196F3);
                        txBig.setText("Press button below to try again :)");

                        long mean = 0;
                        for(int i=0;i<10;i++){
                            mean += reactionTimes[i];
                        }
                        mean /= 10;

                        String meanText = "Your avg. response time was: "+mean+" ms";
                        txEarly.setText(meanText);
                        txEarly.setVisibility(View.VISIBLE);
                        if (mListener != null) mListener.onResult(mean);
                    }
                }
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TestFragmentInteractionListener) {
            mListener = (TestFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement TestFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface TestFragmentInteractionListener {
        void onResult(long result);
    }


    // Variables for UI
    private Button btStart;
    private Button btTest;
    private TextView txBig;
    private TextView txEarly;
    private RelativeLayout bgColor;
    private boolean backgroundColor = true;

    private boolean early; // To register "too fast" reactions
    private long time1;    // To store time at color change
    private long reactTime;
    private long[] reactionTimes = new long[10];
    private int runCounter;
    private int countdown;

    private Thread colorChanger;


    private void startNewColorChangerThread(){
        colorChanger = new Thread(new Runnable() {
            @Override
            public void run() {

                try{
                    Thread.sleep(countdown);
                }catch (InterruptedException e){
                    //e.printStackTrace();
                    return;
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //RelativeLayout bgColor = (RelativeLayout) findViewById(R.id.activity_start);
                        try {
                            if(backgroundColor){
                                bgColor.setBackgroundColor(0xdd99cc00);
                                backgroundColor = !backgroundColor;
                                txBig.setTextColor(Color.WHITE);
                                txEarly.setTextColor(Color.WHITE);
                                txEarly.setVisibility(View.GONE);
                            }else{
                                bgColor.setBackgroundColor(0xffff4444);
                                backgroundColor = !backgroundColor;
                                txEarly.setVisibility(View.GONE);
                            }
                            time1 = System.currentTimeMillis();
                            early = false;
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        colorChanger.start();
    }

    private void testRun(){
        if(backgroundColor){
            txBig.setText("Click when green..");
        }else{
            txBig.setText("Click when red..");
        }

        early = true;
        Random r = new Random();
        countdown = r.nextInt(3000 - 1000) + 1000;
        btTest.setVisibility(View.VISIBLE);

        startNewColorChangerThread();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(colorChanger != null)
            colorChanger.interrupt();
    }
}
