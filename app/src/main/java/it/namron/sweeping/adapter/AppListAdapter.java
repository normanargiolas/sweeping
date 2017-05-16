package it.namron.sweeping.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import it.namron.sweeping.model.AppListModel;
import it.namron.sweeping.model.Message;
import it.namron.sweeping.sweeping.R;

/**
 * Created by norman on 16/05/17.
 */

public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.AppListAdapterViewHolder> {
    private static final String TAG = AppListAdapter.class.getSimpleName();

    private final Context mContext;
    List<AppListModel> appListModel;

    public AppListAdapter(@NonNull Context context, List<AppListModel> appListModel) {
        this.appListModel = appListModel;
        mContext = context;
    }

    @Override
    public AppListAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutIdForListItem = R.layout.app_list_row;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        AppListAdapterViewHolder viewHolder = new AppListAdapterViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AppListAdapterViewHolder appListAdapterViewHolder, int position) {
        Log.d(TAG, "#" + position);
        AppListModel appModel = appListModel.get(position);

        appListAdapterViewHolder.appName.setText(appModel.getAppName());
        appListAdapterViewHolder.txtPrimary.setText(appModel.getTxtPrimary());
        appListAdapterViewHolder.txtSecondary.setText(appModel.getTxtSecondary());

//        appListAdapterViewHolder.infoInstallation.setText(appModel.getInfoInstallation);

    }

//    @Override
//    public long getItemId(int position) {
//        return appListModel.get(position).getId();
//    }

    @Override
    public int getItemCount() {
        if (null == appListModel) return 0;
        return appListModel.size();
    }

    public class AppListAdapterViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout appContainer;
        public TextView appName;
        public TextView txtPrimary;
        public TextView txtSecondary;
        public RelativeLayout iconContainer;
        public RelativeLayout appIconFront;
        public ImageView appIcon;
        public TextView infoInstallation;

        public AppListAdapterViewHolder(View itemView) {
            super(itemView);
            appContainer = (LinearLayout) itemView.findViewById(R.id.app_container);
            appName = (TextView) itemView.findViewById(R.id.app_name);
            txtPrimary = (TextView) itemView.findViewById(R.id.txt_primary);
            txtSecondary = (TextView) itemView.findViewById(R.id.txt_secondary);
            appIconFront = (RelativeLayout) itemView.findViewById(R.id.app_icon_front);
            appIcon = (ImageView) itemView.findViewById(R.id.app_icon);
            infoInstallation = (TextView) itemView.findViewById(R.id.info_installation);
        }
    }
}
