package personal_data_interaction.group9.final_project;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import group9.assignment2.R;

import java.util.List;
import java.util.Locale;

/**
 * {@link RecyclerView.Adapter} that can display a {@link UsageStatsItem} and makes a call to the
 * specified {@link OnUsageListInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class UsageStatsRecyclerViewAdapter extends RecyclerView.Adapter<UsageStatsRecyclerViewAdapter.UsageStatsViewHolder> {

    private final List<UsageStatsItem> mValues;
    private final OnUsageListInteractionListener mListener;

    UsageStatsRecyclerViewAdapter(List<UsageStatsItem> items, OnUsageListInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    public interface OnUsageListInteractionListener{

        void onListFragmentInteraction(UsageStatsItem item);
    }

    @Override
    public UsageStatsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_usage, parent, false);
        return new UsageStatsViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final UsageStatsViewHolder holder, int position) {
        UsageStatsItem item = mValues.get(position);
        holder.item = item;
        mValues.get(position);

        holder.time.setText(DataManager.toHumanShortString(item.getTotalTimeInForeground()));

        holder.avatar.setImageDrawable(item.getIcon());

        holder.name.setText(item.getLabel());

        //holder.start.setText(String.format(Locale.getDefault(),"%tF",item.getStart()));
        //holder.start.setText(String.format(Locale.ROOT,"%tc",item.getStart()));
        holder.start.setText(String.format(Locale.getDefault(),"%1$td %1$tb %1$tT",item.getStart()));
        //holder.start.setText(String.format(Locale.ROOT,"%1$td %1$tb %1$tT %1$tZ",item.getStart()));

        //holder.end.setText(String.format(Locale.getDefault(),"%tF",item.getEnd()));
        //holder.end.setText(String.format(Locale.ROOT,"%tc",item.getEnd()));
        holder.end.setText(String.format(Locale.getDefault(),"%1$td %1$tb %1$tT",item.getEnd()));
        //holder.end.setText(String.format(Locale.ROOT,"%1$td %1$tb %1$tT %1$tZ",item.getEnd()));

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

    class UsageStatsViewHolder extends RecyclerView.ViewHolder {
        UsageStatsItem item;

        View view;
        TextView start;
        TextView end;
        TextView name;
        TextView time;
        ImageView avatar;

        UsageStatsViewHolder(View view) {
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
