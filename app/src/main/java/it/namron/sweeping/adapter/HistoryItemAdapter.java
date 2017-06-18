package it.namron.sweeping.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.namron.sweeping.sweeping.R;

/**
 * Created by norman on 19/06/17.
 */

public class HistoryItemAdapter extends RecyclerView.Adapter<HistoryItemAdapter.HistoryItemAdapterViewHolder> {

    private final Context mContext;
    private Cursor mHistoryCursor;
    private HistoryAdapterListener listener;

    public interface HistoryAdapterListener {
        void onHistoryClicked(int position);
    }


    public HistoryItemAdapter(@NonNull Context context,
                              @NonNull HistoryAdapterListener listener,
                              Cursor cursor) {

        this.mContext = context;
        this.mHistoryCursor = cursor;
        this.listener = listener;
    }

    @Override
    public HistoryItemAdapterViewHolder onCreateViewHolder(ViewGroup parentViewGroup, int viewType) {
        int layoutIdForListItem = R.layout.history_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parentViewGroup, shouldAttachToParentImmediately);
        HistoryItemAdapter.HistoryItemAdapterViewHolder viewHolder = new HistoryItemAdapter.HistoryItemAdapterViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HistoryItemAdapterViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mHistoryCursor.getCount();
    }

    public class HistoryItemAdapterViewHolder extends RecyclerView.ViewHolder {
        public HistoryItemAdapterViewHolder(View itemView) {
            super(itemView);
        }

    }
}
