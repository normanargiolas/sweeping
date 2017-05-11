package it.namron.sweeping.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import it.namron.sweeping.sweeping.R;

/**
 * Created by norman on 11/05/17.
 */

public class DirectoryAdapter extends RecyclerView.Adapter<DirectoryAdapter.NumberViewHolder> {

    private static final String TAG = DirectoryAdapter.class.getSimpleName();

    private int mNumberItems;

    public DirectoryAdapter(int numberOfItems) {
        mNumberItems = numberOfItems;
    }

    @Override
    public NumberViewHolder onCreateViewHolder(ViewGroup parentViewGroup, int viewType) {
        Context context = parentViewGroup.getContext();
        int layoutIdForListItem = R.layout.directory_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parentViewGroup, shouldAttachToParentImmediately);
        NumberViewHolder viewHolder = new NumberViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NumberViewHolder holder, int position) {
        Log.d(TAG, "#" + position);
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mNumberItems;
    }

    public class NumberViewHolder extends RecyclerView.ViewHolder {

        // Will display the position in the list, ie 0 through getItemCount() - 1
        TextView listItemDirectoryView;

        public NumberViewHolder(View itemView) {
            super(itemView);
            listItemDirectoryView = (TextView) itemView.findViewById(R.id.tv_item_number);
        }

        void bind(int listIndex) {
            listItemDirectoryView.setText(String.valueOf(listIndex));
        }
    }
}
