package it.namron.sweeping.data.dao;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import it.namron.sweeping.data.DatabaseManager;
import it.namron.sweeping.data.entity.ErrorLog;

/**
 * Created by norman on 09/06/17.
 */

public class ErrorLogDAO {
    public static final String TABLE_NAME = "error_log";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_FILE = "file";
    public static final String COLUMN_METHOD = "method";
    public static final String COLUMN_LINE = "line";
    public static final String COLUMN_MSG = "msg";
    public static final String COLUMN_LOG = "log";
    public static final String COLUMN_STACK_TRACE = "stack_trace";

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
                    "); ";

    public static String createTableSQL() {
        return SQL_CREATE_ERROR_LOG_TABLE;
    }

    public int insert(ErrorLog errorLog) {
        int errorLogId;
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_ID, errorLog.getId());
        values.put(COLUMN_FILE, errorLog.getFile());
        values.put(COLUMN_METHOD, errorLog.getMethod());
        values.put(COLUMN_LINE, errorLog.getLine());
        values.put(COLUMN_MSG, errorLog.getMsg());
        values.put(COLUMN_LOG, errorLog.getLog());
        values.put(COLUMN_STACK_TRACE, errorLog.getStackTrace());
//        values.put(COLUMN_TIMESTAMP, errorLog.getTimestamp());

        // Inserting Row
        errorLogId = (int) db.insert(TABLE_NAME, null, values);
        DatabaseManager.getInstance().closeDatabase();

        return errorLogId;
    }

    public void delete() {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.delete(TABLE_NAME, null, null);
        DatabaseManager.getInstance().closeDatabase();
    }

}
