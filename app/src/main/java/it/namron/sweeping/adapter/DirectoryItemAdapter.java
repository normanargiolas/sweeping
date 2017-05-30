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

import it.namron.sweeping.model.DirectoryItemModel;
import it.namron.sweeping.sweeping.R;

/**
 * Created by norman on 11/05/17.
 */

public class DirectoryItemAdapter extends RecyclerView.Adapter<DirectoryItemAdapter.DirectoryItemAdapterViewHolder> {

    private static final String TAG = DirectoryItemAdapter.class.getSimpleName();

    private final Context mContext;

    private List<DirectoryItemModel> messages;
    private DirectoryAdapterListener listener;

    public interface DirectoryAdapterListener {
        void onIconDirectoryClicked(int position);
    }

    public DirectoryItemAdapter(@NonNull Context context, DirectoryAdapterListener listener, List<DirectoryItemModel> messages) {
        this.listener = listener;
        this.messages = messages;
        mContext = context;
    }

    @Override
    public DirectoryItemAdapterViewHolder onCreateViewHolder(ViewGroup parentViewGroup, int viewType) {
        int layoutIdForListItem = R.layout.directory_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parentViewGroup, shouldAttachToParentImmediately);
        DirectoryItemAdapterViewHolder viewHolder = new DirectoryItemAdapterViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DirectoryItemAdapterViewHolder directoryItemAdapterViewHolder, int position) {
        Log.d(TAG, "#" + position);
        DirectoryItemModel message = messages.get(position);

        directoryItemAdapterViewHolder.listItemDirectoryView.setText(message.getFolderName());

//        directoryAdapterViewHolder.bind(position);
        applyDirectory(directoryItemAdapterViewHolder, message);

        applyClickEvents(directoryItemAdapterViewHolder, position);
    }

    private void applyDirectory(DirectoryItemAdapterViewHolder holder, DirectoryItemModel message) {
        if (message.isSelected()) {
            holder.directoryIcon.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.folder_selected));
//            holder.directoryIcon.setColorFilter(ContextCompat.getColor(mContext, R.color.icon_tint_selected));
        } else {
            holder.directoryIcon.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.folder));
//            holder.directoryIcon.setColorFilter(ContextCompat.getColor(mContext, R.color.icon_tint_normal));
        }
    }

    private void applyClickEvents(DirectoryItemAdapterViewHolder holder, final int position) {
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

    public void swapFolder(List<DirectoryItemModel> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }


    public class DirectoryItemAdapterViewHolder extends RecyclerView.ViewHolder {

        // Will display the position in the list, ie 0 through getItemCount() - 1
        final TextView listItemDirectoryView;
        final ImageView directoryIcon;


        public DirectoryItemAdapterViewHolder(View itemView) {
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
