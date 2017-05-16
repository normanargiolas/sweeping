package it.namron.sweeping.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import it.namron.sweeping.model.Message;
import it.namron.sweeping.sweeping.R;

/**
 * Created by norman on 11/05/17.
 */

public class DirectoryAdapter extends RecyclerView.Adapter<DirectoryAdapter.DirectoryAdapterViewHolder> {

    private static final String TAG = DirectoryAdapter.class.getSimpleName();

    private final Context mContext;

    private List<Message> messages;
    private MessageAdapterListener listener;

    public interface MessageAdapterListener {
        void onIconDirectoryClicked(int position);
    }

    public DirectoryAdapter(@NonNull Context context, MessageAdapterListener listener, List<Message> messages) {
        this.listener = listener;
        this.messages = messages;
        mContext = context;
    }

    @Override
    public DirectoryAdapterViewHolder onCreateViewHolder(ViewGroup parentViewGroup, int viewType) {
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
        Message message = messages.get(position);

        directoryAdapterViewHolder.listItemDirectoryView.setText(message.getFolderName());

//        directoryAdapterViewHolder.bind(position);
        applyDirectory(directoryAdapterViewHolder, message);

        applyClickEvents(directoryAdapterViewHolder, position);
    }

    private void applyDirectory(DirectoryAdapterViewHolder holder, Message message) {
        if (message.isSelected()) {
            holder.directoryIcon.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.folder_selected));
//            holder.directoryIcon.setColorFilter(ContextCompat.getColor(mContext, R.color.icon_tint_selected));
        } else {
            holder.directoryIcon.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.folder));
//            holder.directoryIcon.setColorFilter(ContextCompat.getColor(mContext, R.color.icon_tint_normal));
        }
    }

    private void applyClickEvents(DirectoryAdapterViewHolder holder, final int position) {
        holder.directoryIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onIconDirectoryClicked(position);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return messages.get(position).getId();
    }

    @Override
    public int getItemCount() {
        if (null == messages) return 0;
        return messages.size();

//        if (null == mCursor) return 0;
//        return mCursor.getColumnCount();
    }

//    public void swapCursor(Cursor newCursor) {
//        mCursor = newCursor;
//        notifyDataSetChanged();
//    }

    public void swapFolder(List<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }


    public class DirectoryAdapterViewHolder extends RecyclerView.ViewHolder {

        // Will display the position in the list, ie 0 through getItemCount() - 1
        final TextView listItemDirectoryView;
        final ImageView directoryIcon;


        public DirectoryAdapterViewHolder(View itemView) {
            super(itemView);
            directoryIcon = (ImageView) itemView.findViewById(R.id.directory_icon);

            listItemDirectoryView = (TextView) itemView.findViewById(R.id.directory_folder);
//            itemView.setOnClickListener(this);
        }

        void bind(int listIndex) {
            listItemDirectoryView.setText(String.valueOf(listIndex));
        }

    }
}
