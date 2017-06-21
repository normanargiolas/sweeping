package it.namron.sweeping.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import it.namron.sweeping.sweeping.R;
import it.namron.sweeping.wrapper.WrappedFormatter;

import static it.namron.sweeping.data.dao.HistoryDAO.COLUMN_FILE_NUMBER;
import static it.namron.sweeping.data.dao.HistoryDAO.COLUMN_FOLDER;
import static it.namron.sweeping.data.dao.HistoryDAO.COLUMN_SIZE;
import static it.namron.sweeping.data.dao.HistoryDAO.COLUMN_TIMESTAMP;

/**
 * Created by norman on 19/06/17.
 */

public class HistoryItemAdapter extends RecyclerView.Adapter<HistoryItemAdapter.HistoryItemAdapterViewHolder> {

    private final Context mContext;
    private Cursor mCursor;
    private HistoryAdapterListener listener;

    //todo da finire in seguito
    public interface HistoryAdapterListener {
        void onHistoryClicked(int position);
    }


    public HistoryItemAdapter(@NonNull Context context,
                              @NonNull HistoryAdapterListener listener,
                              Cursor cursor) {

        this.mContext = context;
        this.mCursor = cursor;
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
        if (!mCursor.moveToPosition(position))
            return; // bail if returned null
        String folder = mCursor.getString(mCursor.getColumnIndex(COLUMN_FOLDER));
        int numberOfFiles = mCursor.getInt(mCursor.getColumnIndex(COLUMN_FILE_NUMBER));
        long size = mCursor.getLong(mCursor.getColumnIndex(COLUMN_SIZE));
        String timestamp = mCursor.getString(mCursor.getColumnIndex(COLUMN_TIMESTAMP));

        holder.folder.setText(folder);
        holder.numberOfFiles.setText(WrappedFormatter.filesNumber(numberOfFiles));
        holder.sizeOfFiles.setText(WrappedFormatter.byteSize(size));
        holder.timestamp.setText(timestamp);

        if(position % 2 == 1){
            holder.descriptionRow.setBackgroundColor(ContextCompat.getColor(mContext, R.color.row_odd));
        }
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public class HistoryItemAdapterViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout descriptionRow;
        public TextView folder;
        public TextView numberOfFiles;
        public TextView sizeOfFiles;
        public TextView timestamp;

        public HistoryItemAdapterViewHolder(View itemView) {
            super(itemView);
            descriptionRow = (LinearLayout) itemView.findViewById(R.id.description_row);
            folder = (TextView) itemView.findViewById(R.id.col_folder);
            numberOfFiles = (TextView) itemView.findViewById(R.id.col_files);
            sizeOfFiles = (TextView) itemView.findViewById(R.id.col_size);
            timestamp = (TextView) itemView.findViewById(R.id.col_time);
        }

    }
}
