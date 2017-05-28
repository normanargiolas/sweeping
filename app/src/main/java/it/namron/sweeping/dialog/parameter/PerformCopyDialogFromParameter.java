package it.namron.sweeping.dialog.parameter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by norman on 27/05/17.
 */

public class PerformCopyDialogFromParameter implements Parcelable {
    private String folder;
    private Boolean original;

    protected PerformCopyDialogFromParameter(Parcel in) {
        folder = in.readString();
        original = in.readByte() != 0;     //original == true if byte != 0
    }

    public static final Creator<PerformCopyDialogFromParameter> CREATOR = new Creator<PerformCopyDialogFromParameter>() {
        @Override
        public PerformCopyDialogFromParameter createFromParcel(Parcel in) {
            return new PerformCopyDialogFromParameter(in);
        }

        @Override
        public PerformCopyDialogFromParameter[] newArray(int size) {
            return new PerformCopyDialogFromParameter[size];
        }
    };

    public PerformCopyDialogFromParameter() {
    }

    public Boolean getOriginal() {
        return original;
    }

    public void setOriginal(Boolean original) {
        this.original = original;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (original ? 1 : 0));     //if original == true, byte == 1
//        dest.writeBooleanArray(new boolean[] {original});
        dest.writeString(folder);

    }
}
