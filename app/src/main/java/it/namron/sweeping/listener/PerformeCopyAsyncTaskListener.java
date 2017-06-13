package it.namron.sweeping.listener;

/**
 * Created by norman on 05/06/17.
 */

public interface PerformeCopyAsyncTaskListener {
    void notifyOnPerformeCopyResoult(Boolean resoult, String senderCode);

    void notifyOnPerformeCopyProgress(Integer progress, String senderCode);

    void notifyOnPerformeCopyPreExecute(String performeCopyAsyncTaskCode);
}
