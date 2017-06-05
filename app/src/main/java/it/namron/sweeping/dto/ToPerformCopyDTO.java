package it.namron.sweeping.dto;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by norman on 27/05/17.
 */

public class ToPerformCopyDTO implements Parcelable {
    private String title;
    private String folder;
    private Drawable icon;

    protected ToPerformCopyDTO(Parcel in) {
        title = in.readString();
        folder = in.readString();

        Bitmap bitmap = (Bitmap) in.readParcelable(getClass().getClassLoader());
        if (bitmap != null) {
            icon = new BitmapDrawable(Resources.getSystem(), bitmap);
        } else {
            icon = null;
        }
    }

    public static final Creator<ToPerformCopyDTO> CREATOR = new Creator<ToPerformCopyDTO>() {
        @Override
        public ToPerformCopyDTO createFromParcel(Parcel in) {
            return new ToPerformCopyDTO(in);
        }

        @Override
        public ToPerformCopyDTO[] newArray(int size) {
            return new ToPerformCopyDTO[size];
        }
    };

    public ToPerformCopyDTO() {
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
