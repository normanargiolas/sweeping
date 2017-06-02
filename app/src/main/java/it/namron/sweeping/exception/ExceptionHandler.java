package it.namron.sweeping.exception;

import android.content.Context;

import it.namron.sweeping.activity.BaseActivity;

/**
 * Created by norman on 02/06/17.
 */

public class ExceptionHandler implements Thread.UncaughtExceptionHandler {
    private final static String TAG = ExceptionHandler.class.getSimpleName();

    private final Context mContext;
    private final Thread.UncaughtExceptionHandler mRootHandler;

    public ExceptionHandler(Context context) {
        this.mContext = context;
        // we should store the current exception handler -- to invoke it for all not handled exceptions ...
        mRootHandler = Thread.getDefaultUncaughtExceptionHandler();
        // we replace the exception handler now with us -- we will properly dispatch the exceptions ...
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(final Thread thread, final Throwable ex) {
        if (ex instanceof CustomException) {

            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(10);
        } else {
            mRootHandler.uncaughtException(thread, ex);
        }
    }
}
