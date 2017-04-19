package personal_data_interaction.group9.final_project;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import group9.assignment2.R;


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
    BarChart bc7Days;

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
        bc7Days = (BarChart) view.findViewById(R.id.bc_7_Days);

        Context context = view.getContext();
        List<UsageStatsItem> data = DataManager.getDayUsageStatsAsItems(context);
        Collections.sort(data, new Comparator<UsageStatsItem>() {
            @Override
            public int compare(UsageStatsItem o1, UsageStatsItem o2) {
                return Long.compare(o2.getTotalTimeInForeground(),o1.getTotalTimeInForeground());
            }
        });

        bc7Days.setData(data);

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
