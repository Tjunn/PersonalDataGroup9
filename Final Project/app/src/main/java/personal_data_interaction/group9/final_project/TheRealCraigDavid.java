package personal_data_interaction.group9.final_project;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import group9.assignment2.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TheRealCraigDavid.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TheRealCraigDavid#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TheRealCraigDavid extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public TheRealCraigDavid() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TheRealCraigDavid.
     */
    // TODO: Rename and change types and number of parameters
    public static TheRealCraigDavid newInstance(String param1, String param2) {
        TheRealCraigDavid fragment = new TheRealCraigDavid();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    TextView tvBigTime, tvBigTimeTitle, tvSmallTime, tvSmallTimeTitle, tvPersonalGoal;
    ImageView ivPersonalGoal;
    HistogramBarChart bc7Days;

    int personalGoal = 2;
    int tmpPersonalGoal;

    private void showRadioButtonDialog() {
        // custom dialog
        final Dialog dialog = new Dialog(this.getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //dialog.setTitle("Set Personal Goal");
        dialog.setContentView(R.layout.radiobutton_dialog);
        List<String> stringList=new ArrayList<>();  // here is list
        stringList.add(1+" hour");
        for(int i=1;i<10;i++) {
            stringList.add((i + 1)+" hours");
        }
        RadioGroup rg = (RadioGroup) dialog.findViewById(R.id.radio_group);

        for(int i=0;i<stringList.size();i++){
            RadioButton rb=new RadioButton(this.getActivity()); // dynamically creating RadioButton and adding to RadioGroup.
            rb.setText(stringList.get(i));
            rb.setPadding(50,0,0,0);
            rg.addView(rb);
            if(personalGoal == i+1){
                rb.setChecked(true);
            }
        }
        boolean checkRadioButton = true;
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int childCount = group.getChildCount();
                for (int x = 0; x < childCount; x++) {
                    RadioButton btn = (RadioButton) group.getChildAt(x);
                    if (btn.getId() == checkedId) {
                        Log.e("selected RadioButton->",btn.getText().toString());
                        tmpPersonalGoal = Integer.parseInt(btn.getText().toString().substring(0, 1));


                    }
                }
            }
        });

        dialog.show();

    }

    void setPersonalGoal(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_the_real_craig_david, container, false);

        tvBigTime = (TextView) view.findViewById(R.id.tv_Big_Time);
        tvBigTimeTitle = (TextView) view.findViewById(R.id.tv_Big_Time_Title);
        tvSmallTime = (TextView) view.findViewById(R.id.tv_Small_Time);
        tvSmallTimeTitle = (TextView) view.findViewById(R.id.tv_Small_Time_title);
        tvPersonalGoal = (TextView) view.findViewById(R.id.tv_Personal_Goal);
        ivPersonalGoal = (ImageView) view.findViewById(R.id.iv_Personal_Goal);
        bc7Days = (HistogramBarChart) view.findViewById(R.id.bc_7_Days);

        Context context = view.getContext();
        bc7Days.setData(DataManager.getLast7Days(context),2*60*60*1000);

        ivPersonalGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRadioButtonDialog();
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
