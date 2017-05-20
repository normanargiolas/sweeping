package it.namron.sweeping.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import it.namron.sweeping.sweeping.R;

/**
 * Created by norman on 19/05/17.
 */

public class AppInfoActivity extends BaseActivity {

    private static final String LOG_TAG = AppInfoActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Always call super class for necessary
        // initialization/implementation.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        set(null, null, this);


//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        if (savedInstanceState != null) {
            // The activity is being re-created. Use the
            // savedInstanceState bundle for initializations either
            // during onCreate or onRestoreInstanceState().
            Log.d(LOG_TAG,
                    "onCreate(): activity re-created from savedInstanceState");

        } else {
            // Activity is being created anew.  No prior saved
            // instance state information available in Bundle object.
            Log.d(LOG_TAG,
                    "onCreate(): activity created");
        }
    }
}
