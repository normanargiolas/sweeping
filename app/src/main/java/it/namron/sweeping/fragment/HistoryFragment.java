package it.namron.sweeping.fragment;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import it.namron.sweeping.adapter.HistoryItemAdapter;
import it.namron.sweeping.data.service.HistoryService;
import it.namron.sweeping.dto.HistoryListDTO;
import it.namron.sweeping.sweeping.R;
import it.namron.sweeping.utils.LogUtils;

/**
 * Created by norman on 17/06/17.
 */

public class HistoryFragment extends Fragment implements
        HistoryItemAdapter.HistoryAdapterListener {
    private static final String LOG_TAG = HistoryFragment.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private HistoryItemAdapter mHistoryAdapter;
    private ArrayList<HistoryListDTO> mHistoryListDTO = new ArrayList<>();

    private HistoryService historyService = new HistoryService();


    /**
     * This method is used to notify from HistoryItemAdapterViewHolder that implement
     * HistoryAdapterListener has clicked.
     */
    @Override
    public void onHistoryClicked(int position) {

    }

    public HistoryFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LogUtils.LOGD_N(LOG_TAG, "onAttach");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Preserve across reconfigurations
        setRetainInstance(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.list_recycler);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        mRecyclerView.setHasFixedSize(true);

        // Get all history info from the database and save in a cursor
        Cursor cursor = historyService.getAllHistoryCursor();
        //The DirectoryItemAdapter is responsible for displaying each item in the list.
        mHistoryAdapter = new HistoryItemAdapter(getContext(), this, cursor);
        mRecyclerView.setAdapter(mHistoryAdapter);


        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
