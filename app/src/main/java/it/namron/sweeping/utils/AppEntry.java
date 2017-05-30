package it.namron.sweeping.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;

import java.io.File;

/**
 * Created by norman on 16/05/17.
 */

public class AppEntry {
    private final AppListLoader mLoader;
    private final ApplicationInfo mInfo;
    private final File mApkFile;
    private String mLabel;
    private Drawable mIcon;
    private boolean mMounted;

    public ApplicationInfo getApplicationInfo() {
        return mInfo;
    }

    public String getLabel() {
        return mLabel;
    }

    public Drawable getIcon() {
        if (mIcon == null) {
            if (mApkFile.exists()) {
                mIcon = mInfo.loadIcon(mLoader.mPm);
                return mIcon;
            } else {
                mMounted = false;
            }
        } else if (!mMounted) {
            // If the app wasn't mounted but is now mounted, reload
            // its icon.
            if (mApkFile.exists()) {
                mMounted = true;
                mIcon = mInfo.loadIcon(mLoader.mPm);
                return mIcon;
            }
        } else {
            return mIcon;
        }
//todo rimuovere deprecato
        return mLoader.getContext().getResources().getDrawable(
                android.R.drawable.sym_def_app_icon);
    }

    @Override public String toString() {
        return mLabel;
    }

    public AppEntry(AppListLoader loader, ApplicationInfo info) {
        mLoader = loader;
        mInfo = info;
        mApkFile = new File(info.sourceDir);
    }

    void loadLabel(Context context) {
        if (mLabel == null || !mMounted) {
            if (!mApkFile.exists()) {
                mMounted = false;
                mLabel = mInfo.packageName;
            } else {
                mMounted = true;
                CharSequence label = mInfo.loadLabel(context.getPackageManager());
                mLabel = label != null ? label.toString() : mInfo.packageName;
            }
        }
    }
}
