package it.namron.sweeping.listener;

import android.app.Activity;

/**
 * Created by norman on 05/06/17.
 */

public interface PerformeCopyAsyncTaskListener {
    void notifyOnPerformeCopyResoult(Activity activity, Boolean resoult, String senderCode);

    void notifyOnPerformeCopyProgress(Integer progress, String senderCode);

    void notifyOnPerformeCopyPreExecute(String performeCopyAsyncTaskCode);
}
