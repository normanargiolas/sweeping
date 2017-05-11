package it.namron.sweeping.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.namron.sweeping.sweeping.R;

/**
 * Created by norman on 09/05/17.
 */

public class WhatsAppFragment extends Fragment {

    public WhatsAppFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_whatsapp, container, false);

        return rootView;
    }
}
