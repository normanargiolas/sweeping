package it.namron.sweeping.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import it.namron.sweeping.model.DrawerItemModel;

/**
 * Created by norman on 22/05/17.
 */


public class DrawerItemAdapter extends RecyclerView.Adapter<DrawerItemAdapter.DrawerItemAdapterViewHolder> {

    public DrawerItemAdapter(@NonNull Context context, List<DrawerItemModel> drawerItemModel) {

    }


        @Override
    public DrawerItemAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(DrawerItemAdapterViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class DrawerItemAdapterViewHolder extends RecyclerView.ViewHolder {

        public DrawerItemAdapterViewHolder(View itemView) {
            super(itemView);
        }
    }
}
