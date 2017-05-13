package it.namron.sweeping.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import it.namron.sweeping.sweeping.R;

/**
 * Created by norman on 11/05/17.
 */

public class DirectoryAdapter extends RecyclerView.Adapter<DirectoryAdapter.DirectoryAdapterViewHolder> {

    private static final String TAG = DirectoryAdapter.class.getSimpleName();

//    private int mNumberItems;
    private Cursor mCursor;

    private final Context mContext;

    public DirectoryAdapter(@NonNull Context context) {
        mContext = context;
//        mNumberItems = numberOfItems;
    }

    @Override
    public DirectoryAdapterViewHolder onCreateViewHolder(ViewGroup parentViewGroup, int viewType) {

//        Context context = parentViewGroup.getContext();
        int layoutIdForListItem = R.layout.directory_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parentViewGroup, shouldAttachToParentImmediately);
        DirectoryAdapterViewHolder viewHolder = new DirectoryAdapterViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DirectoryAdapterViewHolder directoryAdapterViewHolder, int position) {
        Log.d(TAG, "#" + position);
        mCursor.moveToPosition(position);

        directoryAdapterViewHolder.iconView.setImageResource(R.drawable.folder);

        directoryAdapterViewHolder.listItemDirectoryView.setText(mCursor.getColumnName(position));

//        directoryAdapterViewHolder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getColumnCount();
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    public class DirectoryAdapterViewHolder extends RecyclerView.ViewHolder {

        // Will display the position in the list, ie 0 through getItemCount() - 1
        final TextView listItemDirectoryView;
//        final CheckBox checkBox;
        final ImageView iconView;


        public DirectoryAdapterViewHolder(View itemView) {
            super(itemView);
            iconView = (ImageView) itemView.findViewById(R.id.directory_icon);
//            checkBox = (CheckBox) itemView.findViewById(R.id.directory_check);
            listItemDirectoryView = (TextView) itemView.findViewById(R.id.directory_folder);
        }

        void bind(int listIndex) {
            listItemDirectoryView.setText(String.valueOf(listIndex));
        }
    }
}
