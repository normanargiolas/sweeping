package it.namron.sweeping.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import it.namron.sweeping.data.dao.ErrorLogDAO;
import it.namron.sweeping.data.dao.HistoryDAO;
import it.namron.sweeping.utils.LogUtils;

/**
 * Created by norman on 08/06/17.
 */

public class DbHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = DbHelper.class.getSimpleName();


    /**
     * This is the name of our database. Database names should be descriptive and end with the
     * .db extension.
     **/
    public static final String DATABASE_NAME = "sweep.db";

    /**
     * DB version
     **/
    private static final int DATABASE_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time. This is where the creation of
     * tables and the initial population of the tables should happen.
     *
     * @param sqLiteDatabase The database.
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        /**
         * After we've spelled out our SQLite table creation statement above, we actually execute
         * that SQL with the execSQL method of our SQLite database object.
         **/
        sqLiteDatabase.execSQL(ErrorLogDAO.createTableSQL());
        sqLiteDatabase.execSQL(HistoryDAO.createTableSQL());
    }


    /**
     * This database is only a cache for online data, so its upgrade policy is simply to discard
     * the data and call through to onCreate to recreate the table. Note that this only fires if
     * you change the version number for your database (in our case, DATABASE_VERSION). It does NOT
     * depend on the version number for your application found in your app/build.gradle file. If
     * you want to update the schema without wiping data, commenting out the current body of this
     * method should be your top priority before modifying this method.
     *
     * @param sqLiteDatabase Database that is being upgraded
     * @param oldVersion     The old database version
     * @param newVersion     The new database version
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        LogUtils.LOGD_N(LOG_TAG, String.format("SQLiteDatabase.onUpgrade(%d -> %d)", oldVersion, newVersion));
        switch (oldVersion) {
            case 1:
                //upgrade logic from version 1 to 2
                // Drop table if existed, all data will be gone!!!
                sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ErrorLogDAO.TABLE_NAME);
                sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + HistoryDAO.TABLE_NAME);
                onCreate(sqLiteDatabase);
            case 2:
                //upgrade logic from version 2 to 3
            case 3:
                //upgrade logic from version 3 to 4
                break;
            default:
                throw new IllegalStateException(
                        "onUpgrade() with unknown oldVersion " + oldVersion);
        }
    }
}
