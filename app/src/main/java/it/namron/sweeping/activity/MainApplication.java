package it.namron.sweeping.activity;

import android.app.Application;

import it.namron.sweeping.data.DatabaseManager;
import it.namron.sweeping.data.DbHelper;
import it.namron.sweeping.utils.ResourceHashCode;

/**
 * Created by norman on 02/06/17.
 */

/**
 * We register our Global Exception handler for the Android Application
 * or do other staff
 */
public class MainApplication extends Application {

    private static DbHelper dbHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        //todo decommentare in futuro per la gestione delle eccezioni e relativo invio feedback
        //new UncaughtHandler(this);

        //Set singleton resource hash code
        ResourceHashCode.getInstance(getApplicationContext());
        // Create a DB helper (this will create the DB if run for the first time)
        dbHelper = new DbHelper(getApplicationContext());
        DatabaseManager.initializeInstance(dbHelper);
    }
}
