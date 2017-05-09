package it.namron.sweeping.sweeping;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by norman on 09/05/17.
 */

public class ManageFragment extends Fragment {

    public ManageFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_manage, container, false);
        return rootView;
    }
}
