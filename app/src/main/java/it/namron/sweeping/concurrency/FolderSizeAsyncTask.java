package it.namron.sweeping.concurrency;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import org.apache.commons.io.FileUtils;

import java.io.File;

import it.namron.sweeping.listener.FolderSizeAsyncTaskListener;
import it.namron.sweeping.utils.ResourceHashCode;

import static it.namron.sweeping.constant.Constant.ILLEGAL_ARGUMENT_FOLDER_SIZE;

/**
 * Created by norman on 31/05/17.
 */

//AsyncTask<Params, Progress, Result>
public class FolderSizeAsyncTask extends AsyncTask<Uri, Integer, Long> {
    private final String CLASS_NAME_HASH_CODE = getClass().getSimpleName() + super.hashCode();

    private final String LOG_TAG = getClass().getSimpleName();


    private final Activity mActivityContext;
    private FolderSizeAsyncTaskListener mCallback;
    private final int mIndex;

    public FolderSizeAsyncTask(Activity activityContext, FolderSizeAsyncTaskListener mCallback, int index) {
        try{
            ResourceHashCode.setFolderSizeAsyncTaskCode(CLASS_NAME_HASH_CODE);
        } catch (NullPointerException e) {
            Log.e(LOG_TAG, "FolderSizeAsyncTask: " + "ResourceHashCode not initialized");
            e.printStackTrace();
        }
        this.mActivityContext = activityContext;
        this.mCallback = mCallback;
        this.mIndex = index;
    }

    @Override
    protected Long doInBackground(@NonNull Uri... params) {
//        try {
//            Thread.sleep(4000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


        return getFolderSize(params[0]);
    }

    /**
     * size of directory in bytes,
     * 0 if directory is security restricted,
     * a negative number when the real total is greater than Long.MAX_VALUE.
     **/
    private Long getFolderSize(Uri path) {
        Log.d(LOG_TAG, "getFolderSize");
        try {
            long bytes = FileUtils.sizeOfDirectory(new File(path.getPath()));
//            long kilobyte = folder_size_bytes / (2 ^ 10);
            return bytes;

        } catch (IllegalArgumentException e) {
            Log.e(LOG_TAG, "Exception: ", e);
            return (long) ILLEGAL_ARGUMENT_FOLDER_SIZE;
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        Log.d(LOG_TAG, "onProgressUpdate: " + "values[0] = " + values[0]);

    }

    @Override
    protected void onPostExecute(Long result) {
        mCallback.notifyOnFolderSizeResoult(result, mIndex, ResourceHashCode.getFolderSizeAsyncTaskCode());
    }

}
