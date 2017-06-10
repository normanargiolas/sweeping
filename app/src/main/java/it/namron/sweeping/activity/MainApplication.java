package it.namron.sweeping.activity;

import android.app.Application;

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
        //todo decommentare in futuro per la gestione delle eccezioni e relativo invio feedback
        //new UncaughtHandler(this);

        //Set singleton resource hash code
        ResourceHashCode.getInstance(getApplicationContext());
    }
}
