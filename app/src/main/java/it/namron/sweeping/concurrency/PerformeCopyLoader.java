package it.namron.sweeping.concurrency;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.List;

import it.namron.sweeping.listener.FolderSizeAsyncTaskListener;

/**
 * Created by norman on 05/06/17.
 */

public class PerformeCopyLoader extends AsyncTaskLoader<Boolean> {

    private Context mContext;
    private ArrayList<String> mDirList;

    private FolderSizeAsyncTaskListener mCallback;



    public PerformeCopyLoader(Context context, ArrayList<String> directories) {
        super(context);
        mContext = context;
        mDirList = directories;
    }

    /**
     * Handles a request to start the Loader.
     */
    @Override
    protected void onStartLoading() {

    }

    @Override
    public Boolean loadInBackground() {
        return null;
    }

    /**
     * Called when there is new data to deliver to the client.  The
     * super class will take care of delivering it; the implementation
     * here just adds a little more logic.
     */
    @Override
    public void deliverResult(Boolean res) {

    }

    /**
     * Handles a request to stop the Loader.
     */
    @Override
    protected void onStopLoading() {
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }
}
