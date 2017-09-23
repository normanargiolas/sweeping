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

import it.namron.sweeping.adapter.ErrorLogItemAdapter;
import it.namron.sweeping.data.service.ErrorLogService;
import it.namron.sweeping.sweeping.R;
import it.namron.sweeping.utils.LogUtils;

/**
 * Created by norman on 21/09/17.
 */

public class ErrorLogFragment extends Fragment implements
        ErrorLogItemAdapter.ErrorLogAdapterListener {
    private static final String LOG_TAG = ErrorLogFragment.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private ErrorLogItemAdapter mErrorLogItemAdapter;
    private ErrorLogService errorLogService = new ErrorLogService();


    /**
     * This method is used to notify from ErrorLogItemAdapterViewHolder that implement
     * ErrorLogAdapterListener has clicked.
     */
    @Override
    public void onErrorLogClicked(int position) {

    }

    public ErrorLogFragment() {

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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_error_log, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.list_recycler);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        mRecyclerView.setHasFixedSize(true);

        // Get all history info from the database and save in a cursor
        Cursor cursor = errorLogService.getAllErrorLogCursor();
        //The DirectoryItemAdapter is responsible for displaying each item in the list.
        mErrorLogItemAdapter = new ErrorLogItemAdapter(getContext(), this, cursor);
        mRecyclerView.setAdapter(mErrorLogItemAdapter);


        return rootView;
    }


}
