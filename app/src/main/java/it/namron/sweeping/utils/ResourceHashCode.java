package it.namron.sweeping.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import it.namron.sweeping.concurrency.PerformeCopyAsyncTask;

/**
 * Created by norman on 01/06/17.
 */

public class ResourceHashCode {

    //add here our classes instances
    //--------------------
    private static String mFolderSizeAsyncTaskCode;
    private static String mPerformeCopyAsyncTaskCode;
    private static SQLiteDatabase mDb;
    private static Context mContext;

    //    private static ArrayList<PerformeCopyAsyncTask> mPerformeCopyTasks = new ArrayList<>();
    private static Map<String, PerformeCopyAsyncTask> mPerformeCopyTasks = new HashMap<>();

    public static void addPerformeCopyTask(@NonNull String app, @NonNull PerformeCopyAsyncTask task) {
        if (null == ResourceHashCode.mContext) {
            throw new NullPointerException();
        }
        ResourceHashCode.mPerformeCopyTasks.put(app, task);
    }

    public static PerformeCopyAsyncTask getPerformeCopyTask(@NonNull String key) {
        if (null == ResourceHashCode.mContext) {
            throw new NullPointerException();
        }
        return ResourceHashCode.mPerformeCopyTasks.get(key);
    }

    public static void removePerformeCopyTask(@NonNull PerformeCopyAsyncTask task) {
        if (null == ResourceHashCode.mContext) {
            throw new NullPointerException();
        }
        ResourceHashCode.mPerformeCopyTasks.values().remove(task);
    }


    private static class SingletonHelper {

        private static final ResourceHashCode INSTANCE = new ResourceHashCode();
    }

    public static ResourceHashCode getInstance(@NonNull Context c) {
        ResourceHashCode.mContext = c;
        return SingletonHelper.INSTANCE;
    }

    public static Context getContext() {
        if (null == ResourceHashCode.mContext) {
            throw new NullPointerException();
        }

        return mContext;
    }

    public static String getPerformeCopyAsyncTaskCode() {
        return mPerformeCopyAsyncTaskCode;
    }

    public static void setPerformeCopyAsyncTaskCode(@NonNull String performeCopyAsyncTaskCode) {
        if (null == ResourceHashCode.mContext) {
            throw new NullPointerException();
        }
        ResourceHashCode.mPerformeCopyAsyncTaskCode = performeCopyAsyncTaskCode;
    }

    public static String getFolderSizeAsyncTaskCode() {
        return mFolderSizeAsyncTaskCode;
    }

    public static void setFolderSizeAsyncTaskCode(@NonNull String folderSizeAsyncTaskCode) {
        if (null == ResourceHashCode.mContext) {
            throw new NullPointerException();
        }
        ResourceHashCode.mFolderSizeAsyncTaskCode = folderSizeAsyncTaskCode;
    }

//    /**
//     * Keep a reference to the mDb until paused or killed. Get a writable database
//     * because you will be adding restaurant customers
//     **/
//    public static void setWritableDatabase(@NonNull SQLiteDatabase database) {
//        if (null == ResourceHashCode.mContext) {
//            throw new NullPointerException();
//        }
//        ResourceHashCode.mDb = database;
//    }
//
//    public static SQLiteDatabase getWritableDatabase() {
//        return mDb;
//    }

}
