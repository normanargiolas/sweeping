package it.namron.sweeping.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import it.namron.sweeping.model.AppItemModel;
import it.namron.sweeping.sweeping.R;

/**
 * Created by norman on 16/05/17.
 */

public class AppItemAdapter extends RecyclerView.Adapter<AppItemAdapter.AppItemAdapterViewHolder> {
    private static final String TAG = AppItemAdapter.class.getSimpleName();

    private final Context mContext;
    List<AppItemModel> appItemList;

    /*
       * An on-click listener that we've defined to make it easy for an Activity to interface with
       * our RecyclerView
       */
    private final AppItemAdapterOnClickListener mOnClickListener;

    /**
     * The interface that receives onClick messages.
     */
    public interface AppItemAdapterOnClickListener {
        void onListItemClick(AppItemModel clickedItem);
    }


    public AppItemAdapter(@NonNull Context context, List<AppItemModel> appItemList, AppItemAdapterOnClickListener clickListener) {
        this.appItemList = appItemList;
        mContext = context;
        mOnClickListener = clickListener;
    }

    @Override
    public AppItemAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
//        Context context = viewGroup.getContext();
//        int layoutIdForListItem = R.layout.app_list_row;
//        LayoutInflater inflater = LayoutInflater.from(context);
//        boolean shouldAttachToParentImmediately = false;
//
//        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
//        AppItemAdapterViewHolder viewHolder = new AppItemAdapterViewHolder(view);
//        return viewHolder;

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.app_list_row, viewGroup, false);

        return new AppItemAdapterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AppItemAdapterViewHolder appItemAdapterViewHolder, int position) {
        Log.d(TAG, "#" + position);
        AppItemModel appModel = appItemList.get(position);

        appItemAdapterViewHolder.appName.setText(appModel.getAppName());
        appItemAdapterViewHolder.txtPrimary.setText(appModel.getTxtPrimary());
        appItemAdapterViewHolder.txtSecondary.setText(appModel.getTxtSecondary());

        ImageView appIcon = new ImageView(mContext);
        appIcon.setImageDrawable(appModel.getAppIcon());
        appItemAdapterViewHolder.appIcon.setImageDrawable(appIcon.getDrawable());

        appItemAdapterViewHolder.infoInstallation.setText(appModel.getInfoInstallation());

    }

//    @Override
//    public long getItemId(int position) {
//        return appItemList.get(position).getId();
//    }

    @Override
    public int getItemCount() {
        if (null == appItemList) return 0;
        return appItemList.size();
    }

    public void swapFolder(List<AppItemModel> appItemList) {
        this.appItemList = appItemList;
        notifyDataSetChanged();
    }

    public class AppItemAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public LinearLayout appContainer;
        public TextView appName;
        public TextView txtPrimary;
        public TextView txtSecondary;
        public RelativeLayout iconContainer;
        public RelativeLayout appIconFront;
        public ImageView appIcon;
        public TextView infoInstallation;

        public AppItemAdapterViewHolder(View itemView) {
            super(itemView);
            appContainer = (LinearLayout) itemView.findViewById(R.id.app_container);
            appName = (TextView) itemView.findViewById(R.id.app_name);
            txtPrimary = (TextView) itemView.findViewById(R.id.txt_primary);
            txtSecondary = (TextView) itemView.findViewById(R.id.txt_secondary);
            appIconFront = (RelativeLayout) itemView.findViewById(R.id.app_icon_front);
            appIcon = (ImageView) itemView.findViewById(R.id.app_icon);
            infoInstallation = (TextView) itemView.findViewById(R.id.info_installation);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(appItemList.get(adapterPosition));
//            v.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK);
        }
    }
}
