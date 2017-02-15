package group9.assignment2;

import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;


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
        return inflater.inflate(R.layout.fragment_test, container, false);

        //if (mListener != null) mListener.onResult(123);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed() {

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
}
