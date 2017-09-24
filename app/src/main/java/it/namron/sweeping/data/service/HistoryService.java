package it.namron.sweeping.data.service;

import android.database.Cursor;

import java.sql.Timestamp;
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

    /**
     * Restituisce l'id del record {@link History} se inserito correttamente.
     * Inizialmente start_time viene settato con l'orario corrente
     * mentre end_time è nullo.
     *
     * @param  folder  cartella
     * @param  files numero di file
     * @param  size dimensione dei file

     * @return      ID del record History appena inserito
     * @see         HistoryService
     * @see         History
     */
    public int insertHistory(String folder, int files, long size) {
        int historyId;

        History history = new History();
        history.setFolder(folder);
        history.setFile_number(files);
        history.setSize(size);
        historyId = mHistoryDAO.insert(history);

        return historyId;
    }

    public Cursor getAllHistoryCursor() {
        return mHistoryDAO.getAllHistoryCursor();
    }

    public List<History> getAllHistory() {
        return mHistoryDAO.getAllHistory();
    }
}
