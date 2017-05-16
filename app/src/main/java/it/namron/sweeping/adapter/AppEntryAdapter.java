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

//import it.namron.core.utility.AppEntry;
import it.namron.sweeping.model.AppItemModel;
import it.namron.sweeping.sweeping.R;

/**
 * Created by norman on 16/05/17.
 */

public class AppEntryAdapter extends RecyclerView.Adapter<AppEntryAdapter.AppEntryAdapterViewHolder> {
    private static final String TAG = AppEntryAdapter.class.getSimpleName();

    private final Context mContext;
    List<AppItemModel> appEntry;

    public AppEntryAdapter(@NonNull Context context, List<AppItemModel> appEntry) {
        this.appEntry = appEntry;
        mContext = context;
    }

    @Override
    public AppEntryAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutIdForListItem = R.layout.app_list_row;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        AppEntryAdapterViewHolder viewHolder = new AppEntryAdapterViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AppEntryAdapterViewHolder appEntryAdapterViewHolder, int position) {
        Log.d(TAG, "#" + position);
        AppItemModel appModel = appEntry.get(position);

        appEntryAdapterViewHolder.appName.setText(appModel.getAppName());
        appEntryAdapterViewHolder.txtPrimary.setText(appModel.getTxtPrimary());
        appEntryAdapterViewHolder.txtSecondary.setText(appModel.getTxtSecondary());

//        appListAdapterViewHolder.infoInstallation.setText(appModel.getInfoInstallation);

    }

//    @Override
//    public long getItemId(int position) {
//        return appEntry.get(position).getId();
//    }

    @Override
    public int getItemCount() {
        if (null == appEntry) return 0;
        return appEntry.size();
    }

    public void swapFolder(List<AppItemModel> appEntry) {
        this.appEntry = appEntry;
        notifyDataSetChanged();
    }

    public class AppEntryAdapterViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout appContainer;
        public TextView appName;
        public TextView txtPrimary;
        public TextView txtSecondary;
        public RelativeLayout iconContainer;
        public RelativeLayout appIconFront;
        public ImageView appIcon;
        public TextView infoInstallation;

        public AppEntryAdapterViewHolder(View itemView) {
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
