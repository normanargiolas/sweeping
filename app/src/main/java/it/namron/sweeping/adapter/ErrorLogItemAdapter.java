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

import static it.namron.sweeping.data.dao.ErrorLogDAO.COLUMN_FILE;
import static it.namron.sweeping.data.dao.ErrorLogDAO.COLUMN_MSG;
import static it.namron.sweeping.data.dao.ErrorLogDAO.COLUMN_TIMESTAMP;

/**
 * Created by norman on 23/09/17.
 */

public class ErrorLogItemAdapter extends RecyclerView.Adapter<ErrorLogItemAdapter.ErrorLogItemAdapterViewHolder> {
    private static final String LOG_TAG = ErrorLogItemAdapter.class.getSimpleName();

    private final Context mContext;
    private Cursor mCursor;
    private ErrorLogAdapterListener listener;

    //todo da finire in seguito
    public interface ErrorLogAdapterListener {
        void onErrorLogClicked(int position);
    }

    public ErrorLogItemAdapter(@NonNull Context context,
                               @NonNull ErrorLogAdapterListener listener,
                               Cursor cursor) {
        this.mContext = context;
        this.mCursor = cursor;
        this.listener = listener;
    }

    @Override
    public ErrorLogItemAdapterViewHolder onCreateViewHolder(ViewGroup parentViewGroup, int viewType) {
        int layoutIdForListItem = R.layout.error_log_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parentViewGroup, shouldAttachToParentImmediately);
        ErrorLogItemAdapter.ErrorLogItemAdapterViewHolder viewHolder = new ErrorLogItemAdapter.ErrorLogItemAdapterViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ErrorLogItemAdapterViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position))
            return; // fails if returned null
        String file = mCursor.getString(mCursor.getColumnIndex(COLUMN_FILE));
        String msg = mCursor.getString(mCursor.getColumnIndex(COLUMN_MSG));
        String timestamp = mCursor.getString(mCursor.getColumnIndex(COLUMN_TIMESTAMP));

        holder.file.setText(file);
        holder.msg.setText(msg);
        holder.timestamp.setText(timestamp);

        if(position % 2 == 1){
            holder.descriptionRow.setBackgroundColor(ContextCompat.getColor(mContext, R.color.row_odd));
        }
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public class ErrorLogItemAdapterViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout descriptionRow;
        public TextView file;
        public TextView msg;
        public TextView timestamp;

        public ErrorLogItemAdapterViewHolder(View itemView) {
            super(itemView);
            descriptionRow = (LinearLayout) itemView.findViewById(R.id.description_row);
            file = (TextView) itemView.findViewById(R.id.col_file);
            msg = (TextView) itemView.findViewById(R.id.col_msg);
            timestamp = (TextView) itemView.findViewById(R.id.col_time);
        }
    }
}
