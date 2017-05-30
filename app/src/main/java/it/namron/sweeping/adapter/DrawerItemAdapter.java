package it.namron.sweeping.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import it.namron.sweeping.model.DrawerItemModel;
import it.namron.sweeping.sweeping.R;

/**
 * Created by norman on 16/05/17.
 */

public class DrawerItemAdapter extends RecyclerView.Adapter<DrawerItemAdapter.DrawerItemAdapterViewHolder> {
    private static final String TAG = DrawerItemAdapter.class.getSimpleName();

    private final Context mContext;
    List<DrawerItemModel> drawerItemList;

    /*
       * An on-click listener that we've defined to make it easy for an Activity to interface with
       * our RecyclerView
       */
    private final DrawerItemAdapterOnClickListener mOnClickListener;

    /**
     * The interface that receives onClick messages.
     */
    public interface DrawerItemAdapterOnClickListener {
        boolean onNavigationItemSelected(DrawerItemModel clickedItem);
    }


    public DrawerItemAdapter(@NonNull Context context, List<DrawerItemModel> drawerItemList, DrawerItemAdapterOnClickListener clickListener) {
        this.drawerItemList = drawerItemList;
        mContext = context;
        mOnClickListener = clickListener;
    }

    @Override
    public DrawerItemAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.drawer_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        DrawerItemAdapterViewHolder viewHolder = new DrawerItemAdapterViewHolder(view);
        return viewHolder;

//        View itemView = LayoutInflater.from(viewGroup.getContext())
//                .inflate(R.layout.drawer_list_item, viewGroup, false);
//        return new DrawerItemAdapterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DrawerItemAdapterViewHolder drawerItemAdapterViewHolder, int position) {
        Log.d(TAG, "#" + position);
        DrawerItemModel drawerModel = drawerItemList.get(position);

        drawerItemAdapterViewHolder.title.setText(drawerModel.getDrawerName());
        ImageView drawerIcon = new ImageView(mContext);
        drawerIcon.setImageDrawable(drawerModel.getDrawerIcon());
        drawerItemAdapterViewHolder.icon.setImageDrawable(drawerIcon.getDrawable());
    }

//    @Override
//    public long getItemId(int position) {
//        return drawerItemList.get(position).getId();
//    }

    @Override
    public int getItemCount() {
        if (null == drawerItemList) return 0;
        return drawerItemList.size();
    }

    public void updateDrawer(List<DrawerItemModel> drawerItemList) {
        this.drawerItemList = drawerItemList;
        notifyDataSetChanged();
    }

    public class DrawerItemAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView icon;
        public TextView title;

        public DrawerItemAdapterViewHolder(View itemView) {
            super(itemView);

            icon = (ImageView) itemView.findViewById(R.id.icon);
            title = (TextView) itemView.findViewById(R.id.title);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mOnClickListener.onNavigationItemSelected(drawerItemList.get(adapterPosition));
//            v.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK);
        }
    }
}
