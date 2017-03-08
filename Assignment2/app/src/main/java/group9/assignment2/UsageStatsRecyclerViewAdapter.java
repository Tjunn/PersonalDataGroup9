package group9.assignment2;

import android.app.usage.UsageStats;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    private final PackageManager packageManager;

    public UsageStatsRecyclerViewAdapter(List<UsageStats> items, OnUsageListInteractionListener listener, PackageManager packageManager) {
        mValues = items;
        mListener = listener;
        this.packageManager = packageManager;
    }

    public interface OnUsageListInteractionListener{

        void onListFragmentInteraction(UsageStats item);
    }

    @Override
    public UsageStatsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_usage, parent, false);
        return new UsageStatsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final UsageStatsViewHolder holder, int position) {
        holder.item = mValues.get(position);

        double time = (holder.item.getTotalTimeInForeground()/1000D)/60D;
        holder.time.setText(String.format(Locale.getDefault(),"%,.1f",time)+" min");

        String packageName = holder.item.getPackageName();

        //Get Icon
        Drawable icon = null;
        try {
            icon = packageManager.getApplicationIcon(packageName);
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        if(icon == null)
            icon = packageManager.getDefaultActivityIcon();
        holder.avatar.setImageDrawable(icon);

        //Get Application Label
        CharSequence trueName = null;
        try {
            ApplicationInfo info = packageManager.getApplicationInfo(packageName,PackageManager.GET_META_DATA);
            trueName = packageManager.getApplicationLabel(info);
        } catch (PackageManager.NameNotFoundException e) {
        }
        if(trueName == null)
            trueName = packageName;
        holder.name.setText(trueName);

        Calendar start = Calendar.getInstance();
        start.setTimeInMillis(holder.item.getFirstTimeStamp());
        holder.start.setText(String.format(Locale.getDefault(),"%tF",start));

        Calendar end = Calendar.getInstance();
        end.setTimeInMillis(holder.item.getLastTimeStamp());
        holder.end.setText(String.format(Locale.getDefault(),"%tF",end));

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
        public ImageView avatar;

        public UsageStatsViewHolder(View view) {
            super(view);

            this.view = view;
            start = (TextView) view.findViewById(R.id.item_usage_start);
            end = (TextView) view.findViewById(R.id.item_usage_end);
            name = (TextView) view.findViewById(R.id.item_usage_package);
            time = (TextView) view.findViewById(R.id.item_usage_time);
            avatar = (ImageView) view.findViewById(R.id.item_usage_avatar);

        }

    }
}
