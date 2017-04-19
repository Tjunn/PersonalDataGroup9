package personal_data_interaction.group9.final_project;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import group9.assignment2.R;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class UsageStatsFragment extends Fragment {


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public UsageStatsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_usage_stats, container, false);

        Context context = view.getContext();

        List<UsageStatsItem> data = DataManager.getDayUsageStatsAsItems(context);
        Collections.sort(data, new Comparator<UsageStatsItem>() {
            @Override
            public int compare(UsageStatsItem o1, UsageStatsItem o2) {
                return Long.compare(o1.getTotalTimeInForeground(),o2.getTotalTimeInForeground());
            }
        });
        Collections.reverse(data);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.fragment_usage_stats_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new UsageStatsRecyclerViewAdapter(data, null));
        ViewCompat.setNestedScrollingEnabled(recyclerView, false);

        BarChart chart = (BarChart) view.findViewById(R.id.fragment_usage_stats_barchart);
        chart.setData(data);

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof UsageStatsRecyclerViewAdapter.OnUsageListInteractionListener) {
            mListener = (UsageStatsRecyclerViewAdapter.OnUsageListInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

}
