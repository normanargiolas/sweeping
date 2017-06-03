package it.namron.sweeping.activity;

import android.app.Application;

import it.namron.sweeping.exception.UncaughtHandler;

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
        //decommentare in futuro
        //new UncaughtHandler(this);
    }
}
