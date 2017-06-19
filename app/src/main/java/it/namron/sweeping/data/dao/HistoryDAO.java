package it.namron.sweeping.data.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import it.namron.sweeping.data.DatabaseManager;
import it.namron.sweeping.data.entity.History;
import it.namron.sweeping.utils.LogUtils;

/**
 * Created by norman on 09/06/17.
 */

public class HistoryDAO {
    private static final String LOG_TAG = HistoryDAO.class.getSimpleName();

    public static final String TABLE_NAME = "history";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_FOLDER = "folder";
    public static final String COLUMN_FILE_NUMBER = "file_number";
    public static final String COLUMN_SIZE = "size";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    private History history;

    public HistoryDAO() {
        history = new History();
    }

    /**
     * CREATE TABLE SQL
     * This String will contain a simple SQL statement that will create a table that will
     * cache our weather data.
     **/
    private static final String SQL_CREATE_HISTORY_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_FOLDER + " TEXT NOT NULL, " +
                    COLUMN_FILE_NUMBER + " INTEGER NOT NULL, " +
                    COLUMN_SIZE + " BIGINT NOT NULL, " +
                    COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "); ";

    /**
     * RETURN ALL HISTORY TABLE ELEMENT SQL
     * This String will contain a simple SQL statement that will return all history table element
     **/
    private static final String SQL_GET_ALL_HISTORY_ELEMENTS = //todo da implementare
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_FOLDER + " TEXT NOT NULL, " +
                    COLUMN_FILE_NUMBER + " INTEGER NOT NULL, " +
                    COLUMN_SIZE + " BIGINT NOT NULL, " +
                    COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "); ";

    public static String createTableSQL() {
        return SQL_CREATE_HISTORY_TABLE;
    }

    public int insert(History history) {
        int historyId;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();

//        values.put(COLUMN_ID, errorLog.getId());
        values.put(COLUMN_FOLDER, history.getFolder());
        values.put(COLUMN_FILE_NUMBER, history.getFile_number());
        values.put(COLUMN_SIZE, history.getSize());
//        values.put(COLUMN_TIMESTAMP, history.getMsg());

        // Inserting Row
        historyId = (int) db.insert(TABLE_NAME, null, values);
        DatabaseManager.getInstance().closeDatabase();

        return historyId;
    }

    /**
     * Delete History table
     **/
    public void delete() {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.delete(TABLE_NAME, null, null);
        DatabaseManager.getInstance().closeDatabase();
    }

    /**
     * Delete element by History
     **/
    public void delete(History history) {
        //todo implement
    }

    /**
     * Delete element by id
     **/
    public void delete(int id) {
        //todo implement
    }

    /**
     * Get all History elements
     *
     * @return List<History> containing the list of History
     **/
    public List<History> getAllHistory() {
        LogUtils.LOGD_N(LOG_TAG, SQL_GET_ALL_HISTORY_ELEMENTS);

        List<History> historys = new ArrayList<History>();
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        Cursor cursor = db.rawQuery(SQL_GET_ALL_HISTORY_ELEMENTS, null);
        // looping through all rows and adding to list

        if (cursor.moveToFirst()) {
            do {
                History h = new History();
                h.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                h.setFolder(cursor.getString(cursor.getColumnIndex(COLUMN_FOLDER)));
                h.setFile_number(cursor.getInt(cursor.getColumnIndex(COLUMN_FILE_NUMBER)));
                h.setSize(cursor.getLong(cursor.getColumnIndex(COLUMN_SIZE)));
                try {
                    h.setTimestamp(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_TIMESTAMP))));
                } catch (Exception e) {
                    h.setTimestamp(null);
                }
                historys.add(h);
            } while (cursor.moveToNext());
        }
        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return historys;
    }

    /**
     * Get all History elements
     *
     * @return Cursor containing the list of History
     **/
    public Cursor getAllHistoryCursor() {
        LogUtils.LOGD_N(LOG_TAG, SQL_GET_ALL_HISTORY_ELEMENTS);

        List<History> historys = new ArrayList<History>();
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        Cursor cursor = db.rawQuery(SQL_GET_ALL_HISTORY_ELEMENTS, null);
        return cursor;
    }
}
