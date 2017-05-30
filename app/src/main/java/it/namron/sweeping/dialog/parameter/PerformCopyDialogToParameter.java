package it.namron.sweeping.dialog.parameter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;

import it.namron.sweeping.sweeping.R;

/**
 * Created by norman on 27/05/17.
 */

public class PerformCopyDialogToParameter implements Parcelable {
    private String title;
    private String folder;
    private Drawable icon;

    protected PerformCopyDialogToParameter(Parcel in) {
        title = in.readString();
        folder = in.readString();

        Bitmap bitmap = (Bitmap) in.readParcelable(getClass().getClassLoader());
        if (bitmap != null) {
            icon = new BitmapDrawable(Resources.getSystem(), bitmap);
        } else {
            icon = null;
        }
    }

    public static final Creator<PerformCopyDialogToParameter> CREATOR = new Creator<PerformCopyDialogToParameter>() {
        @Override
        public PerformCopyDialogToParameter createFromParcel(Parcel in) {
            return new PerformCopyDialogToParameter(in);
        }

        @Override
        public PerformCopyDialogToParameter[] newArray(int size) {
            return new PerformCopyDialogToParameter[size];
        }
    };

    public PerformCopyDialogToParameter() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(folder);
        if (icon != null) {
            Bitmap bitmap = (Bitmap) ((BitmapDrawable) icon).getBitmap();
            dest.writeParcelable(bitmap, flags);
        } else {
            dest.writeParcelable(null, flags);
        }
    }
}
