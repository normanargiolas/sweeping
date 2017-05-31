package it.namron.sweeping.concurrency;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.util.List;

import it.namron.sweeping.listener.FolderSizeAsyncTaskListener;
import it.namron.sweeping.model.DirectoryItemModel;

/**
 * Created by norman on 31/05/17.
 */
//AsyncTask<Params, Progress, Result>
public class FolderSizeAsyncTask extends AsyncTask<Uri, Integer, Integer> {

    private final String TAG = getClass().getSimpleName();


    private final Activity mActivityContext;
    private FolderSizeAsyncTaskListener mCallback;

    public FolderSizeAsyncTask(Activity activityContext, FolderSizeAsyncTaskListener mCallback) {
        this.mActivityContext = activityContext;
        this.mCallback = mCallback;
    }

    @Override
    protected Integer doInBackground(Uri... params) {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return 500;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        Log.d(TAG, "onProgressUpdate: " + "values[0] = " + values[0]);

    }

    @Override
    protected void onPostExecute(Integer result) {
        mCallback.notifyOnFolderSizeResoult(result, "result_size");
    }
}
