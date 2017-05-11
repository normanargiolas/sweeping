package it.namron.sweeping.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.namron.sweeping.activity.MainActivity;
import it.namron.sweeping.adapter.DirectoryAdapter;
import it.namron.sweeping.sweeping.R;

/**
 * Created by norman on 09/05/17.
 */

public class WhatsAppFragment extends Fragment {

    private static final String TAG = WhatsAppFragment.class.getSimpleName();

    private static final int NUM_LIST_ITEMS = 100;

    //References to RecyclerView and Adapter to reset the list to its
    //"pretty" state when the reset menu item is clicked.
    private DirectoryAdapter mDirectoryAdapter;
    private RecyclerView mDirectoryList;

    public WhatsAppFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_whatsapp, container, false);

        mDirectoryList = (RecyclerView) rootView.findViewById(R.id.rv_numbers);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mDirectoryList.setLayoutManager(layoutManager);
        mDirectoryList.setHasFixedSize(true);

        //The GreenAdapter is responsible for displaying each item in the list.
        mDirectoryAdapter = new DirectoryAdapter(NUM_LIST_ITEMS);
        mDirectoryList.setAdapter(mDirectoryAdapter);

        return rootView;
    }
}
