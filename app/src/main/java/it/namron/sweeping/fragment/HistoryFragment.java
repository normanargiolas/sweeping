package it.namron.sweeping.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.namron.sweeping.sweeping.R;
import it.namron.sweeping.utils.LogUtils;

/**
 * Created by norman on 17/06/17.
 */

public class HistoryFragment extends Fragment {
    private static final String LOG_TAG = HistoryFragment.class.getSimpleName();

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
