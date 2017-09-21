package it.namron.sweeping.data.service;

import android.database.Cursor;

import java.util.List;

import it.namron.sweeping.data.dao.HistoryDAO;
import it.namron.sweeping.data.entity.History;

/**
 * Created by norman on 19/06/17.
 */

public class HistoryService {

    private HistoryDAO mHistoryDAO;

    public HistoryService() {
        mHistoryDAO = new HistoryDAO();
    }

    public void insertHistory(String folder, int files, long size) {
//        HistoryDAO historyDAO = new HistoryDAO();
        History history = new History();
        history.setFolder(folder);
        history.setFile_number(files);
        history.setSize(size);
        mHistoryDAO.insert(history);
    }

    public Cursor getAllHistoryCursor() {
        return mHistoryDAO.getAllHistoryCursor();
    }

    public List<History> getAllHistory() {
        return mHistoryDAO.getAllHistory();
    }
}
