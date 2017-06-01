package it.namron.sweeping.utils;

import android.content.Context;

/**
 * Created by norman on 01/06/17.
 */

public class ResourceHashCode {

    //add here our classes instances
    //--------------------
    private static String mFolderSizeAsyncTaskCode;
    private static Context mContext;

    private static class SingletonHelper {

        private static final ResourceHashCode INSTANCE = new ResourceHashCode();
    }

    public static ResourceHashCode getInstance(Context c) {
        ResourceHashCode.mContext = c;
        return SingletonHelper.INSTANCE;
    }

    public static Context getContext() {
        if (null == ResourceHashCode.mContext) {
            throw new NullPointerException();
        }

        return mContext;
    }

    public static String getFolderSizeAsyncTaskCode() {
        return mFolderSizeAsyncTaskCode;
    }

    public static void setFolderSizeAsyncTaskCode(String folderSizeAsyncTaskCode) {
        if (null == ResourceHashCode.mContext) {
            throw new NullPointerException();
        }
        ResourceHashCode.mFolderSizeAsyncTaskCode = folderSizeAsyncTaskCode;
    }


}
