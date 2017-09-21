package it.namron.sweeping.data.service;

import android.database.Cursor;

import java.util.List;

import it.namron.sweeping.data.dao.ErrorLogDAO;
import it.namron.sweeping.data.entity.ErrorLog;
import it.namron.sweeping.data.entity.History;

/**
 * Created by norman on 22/06/17.
 */


public class ErrorLogService {

    private ErrorLogDAO mErrorLogDAO;

    public ErrorLogService() {
        mErrorLogDAO = new ErrorLogDAO();
    }

    public void insertErrorLog(String file, String method, int line,
                               String msg, String stackTrace,
                               History history) {
        ErrorLog error = new ErrorLog();
        error.setFile(file);
        error.setMethod(method);
        error.setLine(line);
        error.setMsg(msg);
        error.setStackTrace(stackTrace);
        error.setHistory(history);
        mErrorLogDAO.insert(error);
    }

    public Cursor getAllErrorLogCursor() {
        return mErrorLogDAO.getAllErrorLogCursor();
    }

    public List<ErrorLog> getAllErrorLog() {
        return mErrorLogDAO.getAllErrorLog();
    }
}
