package it.namron.sweeping.concurrency;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

import it.namron.sweeping.dto.FromPerformCopyDTO;
import it.namron.sweeping.listener.PerformeCopyAsyncTaskListener;
import it.namron.sweeping.utils.ResourceHashCode;

/**
 * Created by norman on 13/06/17.
 */

public class PerformeCopyAsyncTask extends AsyncTask<ArrayList<String>, Integer, Boolean> {
    private final String CLASS_NAME_HASH_CODE = getClass().getSimpleName() + super.hashCode();

    private final String LOG_TAG = getClass().getSimpleName();

    private Context mActivity;
    private String mDestination;
    private FromPerformCopyDTO mInfo;

    private PerformeCopyAsyncTaskListener mCallback;

    public PerformeCopyAsyncTask(Activity activity, PerformeCopyAsyncTaskListener callback, String destination, FromPerformCopyDTO info) {
        try {
            ResourceHashCode.setPerformeCopyAsyncTaskCode(CLASS_NAME_HASH_CODE);
        } catch (NullPointerException e) {
            Log.e(LOG_TAG, "PerformeCopyAsyncTask: " + "ResourceHashCode not initialized");
            e.printStackTrace();
        }
        mActivity = activity;
        mDestination = destination;
        mInfo = info;
        mCallback = callback;
    }

    @Override
    protected Boolean doInBackground(ArrayList<String>... sources) {

        //todo remove this only for test
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        Log.d(LOG_TAG, "onProgressUpdate: " + "values[0] = " + values[0]);
        if (values.length > 0) {
            mCallback.notifyOnPerformeCopyProgress(values[0], ResourceHashCode.getPerformeCopyAsyncTaskCode());
        }
    }

    @Override
    protected void onPreExecute() {
        mCallback.notifyOnPerformeCopyPreExecute(ResourceHashCode.getPerformeCopyAsyncTaskCode());
    }

    @Override
    protected void onPostExecute(Boolean result) {
        mCallback.notifyOnPerformeCopyResoult(result, ResourceHashCode.getPerformeCopyAsyncTaskCode());
    }
}
