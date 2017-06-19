package it.namron.sweeping.data.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import it.namron.sweeping.data.DatabaseManager;
import it.namron.sweeping.data.entity.ErrorLog;
import it.namron.sweeping.data.entity.History;
import it.namron.sweeping.utils.LogUtils;

import static it.namron.sweeping.constant.Constant.ID_ITEM_NOT_PRESENT;

/**
 * Created by norman on 09/06/17.
 */

public class ErrorLogDAO {
    private static final String LOG_TAG = ErrorLogDAO.class.getSimpleName();

    public static final String TABLE_NAME = "error_log";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_FILE = "file";
    public static final String COLUMN_METHOD = "method";
    public static final String COLUMN_LINE = "line";
    public static final String COLUMN_MSG = "msg";
    public static final String COLUMN_LOG = "log";
    public static final String COLUMN_STACK_TRACE = "stack_trace";
    public static final String COLUMN_ERROR_LOG_HISTORY_ID = "history_id";


    private ErrorLog errorLog;

    public ErrorLogDAO() {
        errorLog = new ErrorLog();
    }

    /**
     * CREATE TABLE SQL
     * This String will contain a simple SQL statement that will create a table that will
     * cache our weather data.
     **/
    private static final String SQL_CREATE_ERROR_LOG_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_FILE + " TEXT NOT NULL, " +
                    COLUMN_METHOD + " TEXT NOT NULL, " +
                    COLUMN_LINE + " TEXT NOT NULL, " +
                    COLUMN_MSG + " TEXT NOT NULL, " +
                    COLUMN_LOG + " TEXT NOT NULL, " +
                    COLUMN_STACK_TRACE + " TEXT NOT NULL, " +
                    COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    COLUMN_ERROR_LOG_HISTORY_ID + " INT, " +
                    "FOREIGN KEY(" + COLUMN_ERROR_LOG_HISTORY_ID + ") REFERENCES " +
                    HistoryDAO.TABLE_NAME + "(id)" +
                    "); ";

    public static String createTableSQL() {
        return SQL_CREATE_ERROR_LOG_TABLE;
    }

    public int insert(ErrorLog errorLog) {
        int errorLogId;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();

//        values.put(COLUMN_ID, errorLog.getId());
        values.put(COLUMN_FILE, errorLog.getFile());
        values.put(COLUMN_METHOD, errorLog.getMethod());
        values.put(COLUMN_LINE, errorLog.getLine());
        values.put(COLUMN_MSG, errorLog.getMsg());
        values.put(COLUMN_LOG, errorLog.getLog());
        values.put(COLUMN_STACK_TRACE, errorLog.getStackTrace());
//        values.put(COLUMN_TIMESTAMP, errorLog.getTimestamp());
        try {
            values.put(COLUMN_ERROR_LOG_HISTORY_ID, errorLog.getHistory().getId());
        } catch (NullPointerException e) {
            values.put(COLUMN_ERROR_LOG_HISTORY_ID, ID_ITEM_NOT_PRESENT);
        }
        // Inserting Row
        errorLogId = (int) db.insert(TABLE_NAME, null, values);
        DatabaseManager.getInstance().closeDatabase();

        return errorLogId;
    }

    /**
     * delete ErrorLog table
     **/
    public void delete() {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.delete(TABLE_NAME, null, null);
        DatabaseManager.getInstance().closeDatabase();
    }

    public void delete(ErrorLog errorLog) {
        /**
         * delete element by ErrorLog
         **/
    }

    public void delete(int id) {
        /**
         * delete element by id
         **/
    }

    /**
     * get all ErrorLog elements
     **/
    public List<ErrorLog> getAllErrorLog() {
        List<ErrorLog> errors = new ArrayList<ErrorLog>();
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();

        String selectQuery = " ";//todo set right query
        LogUtils.LOGD_N(LOG_TAG, selectQuery);
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ErrorLog error = new ErrorLog();
                error.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                error.setFile(cursor.getString(cursor.getColumnIndex(COLUMN_FILE)));
                error.setMethod(cursor.getString(cursor.getColumnIndex(COLUMN_METHOD)));
                error.setLine(cursor.getString(cursor.getColumnIndex(COLUMN_LINE)));
                error.setMsg(cursor.getString(cursor.getColumnIndex(COLUMN_MSG)));
                error.setLog(cursor.getString(cursor.getColumnIndex(COLUMN_LOG)));
                error.setStackTrace(cursor.getString(cursor.getColumnIndex(COLUMN_STACK_TRACE)));
                try {
                    error.setTimestamp(Timestamp.valueOf(cursor.getString(cursor.getColumnIndex(COLUMN_TIMESTAMP))));
                } catch (Exception e) {
                    error.setTimestamp(null);
                }
                History history = new History();
                history.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ERROR_LOG_HISTORY_ID)));
                //todo vedere come si comporta con gli altri campi di ErrorLog
                error.setHistory(history);

                errors.add(error);
            } while (cursor.moveToNext());
        }
        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return errors;
    }

}
