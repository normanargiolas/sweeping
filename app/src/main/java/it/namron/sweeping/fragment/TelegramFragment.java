package it.namron.sweeping.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.namron.sweeping.sweeping.R;

/**
 * Created by norman on 09/05/17.
 */

public class TelegramFragment extends Fragment {

    public TelegramFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_telegram, container, false);
        return rootView;
    }
}
