package group9.assignment2;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import group9.assignment2.ResultsFragment.OnListFragmentInteractionListener;
import group9.assignment2.ResultStorageManager.ResultItem;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link ResultItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ResultItemRecyclerViewAdapter extends RecyclerView.Adapter<ResultItemRecyclerViewAdapter.ViewHolder> {

    private final List<ResultItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public ResultItemRecyclerViewAdapter(List<ResultItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.item = mValues.get(position);

        Date d = new Date(holder.item.time);

        holder.textPrimary.setText(d.toString());
        holder.textSecondary.setText(""+holder.item.result);

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

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        public final TextView textPrimary;
        public final TextView textSecondary;
        public ResultItem item;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            textPrimary = (TextView) view.findViewById(R.id.item_result_primary);
            textSecondary = (TextView) view.findViewById(R.id.item_result_secondary);
        }

        @Override
        public String toString() {
            return "Test";//super.toString() + " '" + textSecondary.getText() + "'";
        }
    }
}
