package it.namron.sweeping.listener;

/**
 * Created by norman on 31/05/17.
 */

public interface FolderSizeAsyncTaskListener {
    void notifyOnFolderSizeResoult(Long resoult, int index, String senderCode);

    void notifyOnFolderSizeProgress(Integer progress, String senderCode);
}
