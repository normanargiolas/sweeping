

package it.namron.sweeping.utils;

import android.util.Log;

import it.namron.sweeping.sweeping.BuildConfig;

public class LogUtils {
    private static final String LOG_PREFIX = "sweepeng_";
    private static final int LOG_PREFIX_LENGTH = LOG_PREFIX.length();
    private static final int MAX_LOG_TAG_LENGTH = 23;

    public static boolean LOGGING_ENABLED = !BuildConfig.BUILD_TYPE.equalsIgnoreCase("release");

    public static String makeLogTag(String str) {
        if (str.length() > MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH) {
            return LOG_PREFIX + str.substring(0, MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH - 1);
        }

        return LOG_PREFIX + str;
    }

    /**
     * Don't use this when obfuscating class names!
     */
    public static String makeLogTag(Class cls) {
        return makeLogTag(cls.getSimpleName());
    }

    public static void LOGD(final String tag, String message) {
        if (LOGGING_ENABLED) {
            if (Log.isLoggable(tag, Log.DEBUG)) {
                Log.d(tag, message);
            }
        }
    }

    public static void LOGD(final String tag, String message, Throwable cause) {
        if (LOGGING_ENABLED) {
            if (Log.isLoggable(tag, Log.DEBUG)) {
                Log.d(tag, message, cause);
            }
        }
    }

    public static void LOGV(final String tag, String message) {
        if (LOGGING_ENABLED) {
            if (Log.isLoggable(tag, Log.VERBOSE)) {
                Log.v(tag, message);
            }
        }
    }

    public static void LOGV(final String tag, String message, Throwable cause) {
        if (LOGGING_ENABLED) {
            if (Log.isLoggable(tag, Log.VERBOSE)) {
                Log.v(tag, message, cause);
            }
        }
    }

    public static void LOGI(final String tag, String message) {
        if (LOGGING_ENABLED) {
            Log.i(tag, message);
        }
    }

    public static void LOGI(final String tag, String message, Throwable cause) {
        if (LOGGING_ENABLED) {
            Log.i(tag, message, cause);
        }
    }

    public static void LOGW(final String tag, String message) {
        Log.w(tag, message);
    }

    public static void LOGW(final String tag, String message, Throwable cause) {
        Log.w(tag, message, cause);
    }

    public static void LOGE(final String tag, String message) {
        Log.e(tag, message);
    }

    public static void LOGE(final String tag, String message, Throwable cause) {
        Log.e(tag, message, cause);
    }

    private LogUtils() {
    }

    /**
     * Log with info trace
     */
    public static void LOGD_N(final String tag, final String message, final Object... arguments) {
        if (LOGGING_ENABLED) {
            String msg = message;
            String m;
            Throwable cause = null;
            for (Object arg : arguments) {
                m = null;
                if (arg instanceof Throwable) {
                    cause = (Throwable) arg;
                } else {
                    m = arg.toString();
                    msg = msg + " " + m;
                }
            }
            String fullClassName = Thread.currentThread().getStackTrace()[3].getClassName();
            String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
            String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
            int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();
            String log = className + "." + methodName + "():" + lineNumber + " -->   " + msg;

            Log.d(tag, log, cause);
        }
    }


//    public static void LOGD_N(final String tag, String message, Throwable cause) {
//        if (LOGGING_ENABLED) {
////            if (Log.isLoggable(tag, Log.DEBUG)) {
//            String fullClassName = Thread.currentThread().getStackTrace()[3].getClassName();
//            String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
//            String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
//            int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();
//            String log = className + "." + methodName + "():" + lineNumber + " -->   " + message;
//            Log.d(tag, log, cause);
//
////            }
//        }
//    }
}
