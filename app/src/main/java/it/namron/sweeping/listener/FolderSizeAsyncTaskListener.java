package it.namron.sweeping.listener;

import android.net.Uri;

/**
 * Created by norman on 31/05/17.
 */

public interface FolderSizeAsyncTaskListener {
    void notifyOnFolderSizeResoult(Integer resoult, String senderCode);
    void notifyOnFolderSizeProgress(Integer progress, String senderCode);
}
