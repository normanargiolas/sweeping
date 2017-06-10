package it.namron.sweeping.data.dao;

import it.namron.sweeping.data.entity.History;

/**
 * Created by norman on 09/06/17.
 */

public class HistoryDAO {
    public static final String TABLE_NAME = "history";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_FOLDER = "folder";
    public static final String COLUMN_FILE_NUMBER = "file_number";
    public static final String COLUMN_SIZE = "size";
    public static final String COLUMN_HISTORY_ERROR_LOG_ID = "error_log_id";
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
                    COLUMN_HISTORY_ERROR_LOG_ID + " INT, " +
                    "FOREIGN KEY(" + COLUMN_HISTORY_ERROR_LOG_ID + ") REFERENCES " +
                    ErrorLogDAO.TABLE_NAME + "(id)" +
                    "); ";

    public static String createTableSQL() {
        return SQL_CREATE_HISTORY_TABLE;
    }
}
