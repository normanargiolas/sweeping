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

import it.namron.sweeping.dto.DirectoryItemDTO;
import it.namron.sweeping.sweeping.R;
import it.namron.sweeping.wrapper.WrappedDirectorySize;

/**
 * Created by norman on 11/05/17.
 */

public class DirectoryItemAdapter extends RecyclerView.Adapter<DirectoryItemAdapter.DirectoryItemAdapterViewHolder> {

    private static final String TAG = DirectoryItemAdapter.class.getSimpleName();

    private final Context mContext;

    private List<DirectoryItemDTO> mDirectoryItemList;
    private DirectoryAdapterListener listener;

    public interface DirectoryAdapterListener {
        void onIconDirectoryClicked(int position);
    }

    public DirectoryItemAdapter(@NonNull Context context, DirectoryAdapterListener listener, List<DirectoryItemDTO> directoryItemList) {
        this.listener = listener;
        mDirectoryItemList = directoryItemList;
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
        DirectoryItemDTO directoryItem = mDirectoryItemList.get(position);

        directoryItemAdapterViewHolder.listItemDirectoryView.setText(directoryItem.getName());

        directoryItemAdapterViewHolder.directorySize.setText(WrappedDirectorySize.size(directoryItem.getSizeByte()));

        applyDirectory(directoryItemAdapterViewHolder, directoryItem);

        applyClickEvents(directoryItemAdapterViewHolder, position);
    }

    private void applyDirectory(DirectoryItemAdapterViewHolder holder, DirectoryItemDTO message) {
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

    public void updateSize(long size, int position) {
        mDirectoryItemList.get(position).setSizeByte(size);
        mDirectoryItemList.get(position).setSizeString(WrappedDirectorySize.size(size));
        notifyDataSetChanged();
    }

    public void populateDirectoryItem(List<DirectoryItemDTO> directoryItemList) {
        mDirectoryItemList = directoryItemList;
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return mDirectoryItemList.get(position).getId();
    }

    @Override
    public int getItemCount() {
        if (null == mDirectoryItemList) return 0;
        return mDirectoryItemList.size();
    }

    public class DirectoryItemAdapterViewHolder extends RecyclerView.ViewHolder {

        // Will display the position in the list, ie 0 through getItemCount() - 1
        final TextView listItemDirectoryView;
        final TextView directorySize;
        final ImageView directoryIcon;


        public DirectoryItemAdapterViewHolder(View itemView) {
            super(itemView);
            directoryIcon = (ImageView) itemView.findViewById(R.id.directory_icon);
            listItemDirectoryView = (TextView) itemView.findViewById(R.id.directory_folder);
            directorySize = (TextView) itemView.findViewById(R.id.directory_size);

//            itemView.setOnClickListener(this);
        }

        void bind(int listIndex) {
            listItemDirectoryView.setText(String.valueOf(listIndex));
        }

    }
}
