package it.namron.sweeping.listener;

/**
 * Created by norman on 05/06/17.
 */

public interface PerformeCopyLoaderListener {

    void notifyOnPostBackground(String senderCode);
    void notifyOnPreBackground(String senderCode);
    void notifyOnFolderCopied(String folder, int index, String senderCode);
    void notifyOnErrorOccurred(String folder, int index, String senderCode);

}
