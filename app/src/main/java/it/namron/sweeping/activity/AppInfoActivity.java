package it.namron.sweeping.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by norman on 19/05/17.
 */

public class AppInfoActivity extends Activity {

    private static final String LOG_TAG = AppInfoActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Always call super class for necessary
        // initialization/implementation.
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null) {
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
