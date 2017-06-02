package it.namron.sweeping.exception;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;


/**
 * Created by norman on 02/06/17.
 */

public class GlobalExceptionHandler implements Thread.UncaughtExceptionHandler {
    private final static String TAG = GlobalExceptionHandler.class.getSimpleName();

    private final Context mContext;
    private final Thread.UncaughtExceptionHandler mRootHandler;


    public GlobalExceptionHandler(Context context) {
        this.mContext = context;
        // we should store the current exception handler -- to invoke it for all not handled exceptions ...
        mRootHandler = Thread.getDefaultUncaughtExceptionHandler();
        // we replace the exception handler now with us -- we will properly dispatch the exceptions ...
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(final Thread thread, final Throwable ex) {
        Log.d(TAG, "called for " + ex.getClass());

        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                // we cant start a dialog here, as the context is maybe just a background activity ...
                Toast.makeText(mContext, ex.getMessage() + " Application will close!", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();
    }
}
