package group9.assignment2;

import android.app.usage.UsageStats;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import group9.assignment2.ResultStorageManager.ResultItem;
import group9.assignment2.ResultsFragment.OnListFragmentInteractionListener;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * {@link RecyclerView.Adapter} that can display a {@link android.app.usage.UsageStats} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class UsageStatsRecyclerViewAdapter extends RecyclerView.Adapter<UsageStatsRecyclerViewAdapter.UsageStatsViewHolder> {

    private final List<UsageStats> mValues;
    private final OnUsageListInteractionListener mListener;

    public UsageStatsRecyclerViewAdapter(List<UsageStats> items, OnUsageListInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    public interface OnUsageListInteractionListener{

        void onListFragmentInteraction(UsageStats item);
    }

    @Override
    public UsageStatsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_result, parent, false);
        return new UsageStatsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final UsageStatsViewHolder holder, int position) {
        holder.item = mValues.get(position);

        long time = holder.item.getTotalTimeInForeground();
        holder.time.setText(String.format(Locale.getDefault(),"%,d",time));

        String packageName = holder.item.getPackageName();
        holder.time.setText(packageName);

        Calendar start = Calendar.getInstance();
        start.setTimeInMillis(holder.item.getFirstTimeStamp());
        holder.time.setText(String.format(Locale.getDefault(),"%tF",start));

        Calendar end = Calendar.getInstance();
        end.setTimeInMillis(holder.item.getLastTimeStamp());
        holder.time.setText(String.format(Locale.getDefault(),"%tF",end));

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.item);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class UsageStatsViewHolder extends RecyclerView.ViewHolder {
        public UsageStats item;

        public View view;
        public TextView start;
        public TextView end;
        public TextView name;
        public TextView time;

        public UsageStatsViewHolder(View view) {
            super(view);

            this.view = view;
            start = (TextView) view.findViewById(R.id.item_usage_start);
            end = (TextView) view.findViewById(R.id.item_usage_end);
            name = (TextView) view.findViewById(R.id.item_usage_package);
            time = (TextView) view.findViewById(R.id.item_usage_time);

        }

    }
}
