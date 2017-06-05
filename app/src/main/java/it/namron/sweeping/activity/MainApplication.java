package it.namron.sweeping.activity;

import android.app.Application;

import it.namron.sweeping.exception.UncaughtHandler;
import it.namron.sweeping.utils.ResourceHashCode;

/**
 * Created by norman on 02/06/17.
 */

/**
 * We register our Global Exception handler for the Android Application
 * or do other staff
 */
public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //Set singleton resource hash code
        ResourceHashCode.getInstance(getApplicationContext());
        //decommentare in futuro
        //new UncaughtHandler(this);
    }
}
