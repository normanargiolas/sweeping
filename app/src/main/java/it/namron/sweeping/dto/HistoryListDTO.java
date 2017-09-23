package it.namron.sweeping.dto;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Timestamp;

/**
 * Created by norman on 19/06/17.
 */

public class HistoryListDTO implements Parcelable {
    private String folder;
    private int file_number;
    private long size;
    private int errorlogId;
    private Drawable appIcon;
    private Timestamp startTime;
    private Timestamp endTime;

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public int getFile_number() {
        return file_number;
    }

    public void setFile_number(int file_number) {
        this.file_number = file_number;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTIme) {
        this.endTime = endTIme;
    }

    public int getErrorlogId() {
        return errorlogId;
    }

    public void setErrorlogId(int errorlogId) {
        this.errorlogId = errorlogId;
    }

    public HistoryListDTO() {
    }

    protected HistoryListDTO(Parcel in) {
        super();
        folder = in.readString();
        file_number = in.readInt();
        size = in.readLong();
        endTime = (Timestamp) in.readSerializable();
        startTime = (Timestamp) in.readSerializable();
        errorlogId = in.readInt();
        Bitmap bitmap = (Bitmap) in.readParcelable(getClass().getClassLoader());
        if (bitmap != null) {
            appIcon = new BitmapDrawable(Resources.getSystem(), bitmap);
        } else {
            appIcon = null;
        }
    }

    public static final Creator<HistoryListDTO> CREATOR = new Creator<HistoryListDTO>() {
        @Override
        public HistoryListDTO createFromParcel(Parcel in) {
            return new HistoryListDTO(in);
        }

        @Override
        public HistoryListDTO[] newArray(int size) {
            return new HistoryListDTO[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(folder);
        dest.writeInt(file_number);
        dest.writeLong(size);
        dest.writeSerializable(startTime);
        dest.writeSerializable(endTime);

        dest.writeInt(errorlogId);
        if (appIcon != null) {
            Bitmap bitmap = (Bitmap) ((BitmapDrawable) appIcon).getBitmap();
            dest.writeParcelable(bitmap, flags);
        } else {
            dest.writeParcelable(null, flags);
        }
    }

}
