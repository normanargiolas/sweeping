package it.namron.sweeping.exception;

import android.app.ApplicationErrorReport;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.PrintWriter;
import java.io.StringWriter;


/**
 * Created by norman on 02/06/17.
 */

public class UncaughtHandler implements Thread.UncaughtExceptionHandler {
    private final static String TAG = UncaughtHandler.class.getSimpleName();

    private final Context mContext;
    private final Thread.UncaughtExceptionHandler mRootHandler;

    private final String LINE_SEPARATOR = "\n";


    private static volatile boolean mCrashing = false;
    private static IBinder mApplicationObject;


    public UncaughtHandler(Context context) {
        this.mContext = context;
        // we should store the current exception handler -- to invoke it for all not handled exceptions ...
        mRootHandler = Thread.getDefaultUncaughtExceptionHandler();
        // we replace the exception handler now with us -- we will properly dispatch the exceptions ...
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

//    /**
//     * Set the object identifying this application/process, for reporting VM
//     * errors.
//     */
//    public static final void setApplicationObject(IBinder app) {
//        mApplicationObject = app;
//    }
//
//    public static final IBinder getApplicationObject() {
//        return mApplicationObject;
//    }

    public static Intent createFeedbackIntent(Context context, Throwable error) {
        ApplicationErrorReport report = new ApplicationErrorReport();
        report.packageName = report.processName = context.getPackageName();
        report.time = System.currentTimeMillis();
        report.type = ApplicationErrorReport.TYPE_CRASH;
        report.systemApp = false;

        ApplicationErrorReport.CrashInfo crash = new ApplicationErrorReport.CrashInfo();
        crash.exceptionClassName = error.getClass().getSimpleName();
        crash.exceptionMessage = error.getMessage();

        StringWriter writer = new StringWriter();
        PrintWriter printer = new PrintWriter(writer);
        error.printStackTrace(printer);

        crash.stackTrace = writer.toString();

        StackTraceElement stack = error.getStackTrace()[0];
        crash.throwClassName = stack.getClassName();
        crash.throwFileName = stack.getFileName();
        crash.throwLineNumber = stack.getLineNumber();
        crash.throwMethodName = stack.getMethodName();

        report.crashInfo = crash;

        Intent intent = new Intent(Intent.ACTION_APP_ERROR);
        intent.putExtra(Intent.EXTRA_BUG_REPORT, report);

        return intent;
    }

    @Override
    public void uncaughtException(final Thread thread, final Throwable ex) {
        try {
            Log.d(TAG, "called for " + ex.getClass());

            // Don't re-enter -- avoid infinite loops if crash-reporting crashes.
            if (mCrashing) return;
            mCrashing = true;

//    todo da finire


//            ApplicationErrorReport report = new ApplicationErrorReport();
//            report.packageName = report.processName = mContext.getPackageName();
//            report.time = System.currentTimeMillis();
//            report.type = ApplicationErrorReport.TYPE_CRASH;
//            report.systemApp = false;
//
//            ApplicationErrorReport.CrashInfo crash = new ApplicationErrorReport.CrashInfo();
//            crash.exceptionClassName = ex.getClass().getSimpleName();
//            crash.exceptionMessage = ex.getMessage();
//
//            StringWriter writer = new StringWriter();
//            PrintWriter printer = new PrintWriter(writer);
//            ex.printStackTrace(printer);
//
//            crash.stackTrace = writer.toString();
//
//            StackTraceElement stack = ex.getStackTrace()[0];
//            crash.throwClassName = stack.getClassName();
//            crash.throwFileName = stack.getFileName();
//            crash.throwLineNumber = stack.getLineNumber();
//            crash.throwMethodName = stack.getMethodName();
//
//            report.crashInfo = crash;
//
//            Intent intent = new Intent(Intent.ACTION_APP_ERROR);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.putExtra(Intent.EXTRA_BUG_REPORT, report);
//            mContext.startActivity(intent);














//            Intent intent = createFeedbackIntent(mContext, ex);
//
//            mContext.startActivity(intent);
//
            new Thread() {
                @Override
                public void run() {
                    Looper.prepare();
                    // we cant start a dialog here, as the context is maybe just a background activity ...
                    Toast.makeText(mContext, ex.getMessage() + " Application will close!", Toast.LENGTH_LONG).show();
                    Looper.loop();
                }
            }.start();


//            mContext.startActivity(intent);


        } catch (Throwable t2) {
            try {
                Log.e(TAG, "Error reporting crash", t2);
            } catch (Throwable t3) {
                // Even Log.e() fails!  Oh cacca!
            }
        } finally {
            // Try everything to make sure this process goes away.
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(10);
        }







//        Log.d(TAG, "called for " + ex.getClass());
//
//        StringWriter stackTrace = new StringWriter();
//        ex.printStackTrace(new PrintWriter(stackTrace));
//        StringBuilder errorReport = new StringBuilder();
//        errorReport.append("************ CAUSE OF ERROR ************\n\n");
//        errorReport.append(stackTrace.toString());
//
//        errorReport.append("\n************ DEVICE INFORMATION ***********\n");
//        errorReport.append("Brand: ");
//        errorReport.append(Build.BRAND);
//        errorReport.append(LINE_SEPARATOR);
//        errorReport.append("Device: ");
//        errorReport.append(Build.DEVICE);
//        errorReport.append(LINE_SEPARATOR);
//        errorReport.append("Model: ");
//        errorReport.append(Build.MODEL);
//        errorReport.append(LINE_SEPARATOR);
//        errorReport.append("Id: ");
//        errorReport.append(Build.ID);
//        errorReport.append(LINE_SEPARATOR);
//        errorReport.append("Product: ");
//        errorReport.append(Build.PRODUCT);
//        errorReport.append(LINE_SEPARATOR);
//        errorReport.append("\n************ FIRMWARE ************\n");
//        errorReport.append("SDK: ");
//        errorReport.append(Build.VERSION.SDK);
//        errorReport.append(LINE_SEPARATOR);
//        errorReport.append("Release: ");
//        errorReport.append(Build.VERSION.RELEASE);
//        errorReport.append(LINE_SEPARATOR);
//        errorReport.append("Incremental: ");
//        errorReport.append(Build.VERSION.INCREMENTAL);
//        errorReport.append(LINE_SEPARATOR);
//
//
//
//        new Thread() {
//            @Override
//            public void run() {
//                Looper.prepare();
//                // we cant start a dialog here, as the context is maybe just a background activity ...
//                Toast.makeText(mContext, ex.getMessage() + " Application will close!", Toast.LENGTH_LONG).show();
//                Looper.loop();
//            }
//        }.start();
//
//        Intent intent = new Intent(mContext, ApplicationErrorReport Activity.class);
//        intent.putExtra("error", errorReport.toString());
//        mContext.startActivity(intent);
//
//
//        android.os.Process.killProcess(android.os.Process.myPid());
//        System.exit(0);
    }
}
