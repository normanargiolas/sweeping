package it.namron.sweeping.data.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Timestamp;

/**
 * Created by norman on 09/06/17.
 */

public class History implements Parcelable {

    private int id;

    private String folder;

    private int file_number;

    private long size;

    public ErrorLog errorlog;

    private Timestamp timestamp;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public ErrorLog getErrorlog() {
        return errorlog;
    }

    public void setErrorlog(ErrorLog errorlog) {
        this.errorlog = errorlog;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public History() {
        super();
    }

    public History(int id, String folder, int file_number, long size, ErrorLog errorLog, Timestamp timestamp) {
        super();
        this.id = id;
        this.folder = folder;
        this.file_number = file_number;
        this.size = size;
        this.errorlog = errorLog;
        this.timestamp = timestamp;
    }


    protected History(Parcel in) {
        super();
        id = in.readInt();
        folder = in.readString();
        file_number = in.readInt();
        size = in.readLong();
        errorlog = in.readParcelable(ErrorLog.class.getClassLoader());
        timestamp = (Timestamp) in.readSerializable();
        this.errorlog = in.readParcelable(ErrorLog.class.getClassLoader());
    }

    public static final Creator<History> CREATOR = new Creator<History>() {
        @Override
        public History createFromParcel(Parcel in) {
            return new History(in);
        }

        @Override
        public History[] newArray(int size) {
            return new History[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(folder);
        dest.writeInt(file_number);
        dest.writeLong(size);
        dest.writeParcelable(errorlog, flags);
        dest.writeSerializable(timestamp);
        dest.writeParcelable(getErrorlog(), flags);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        History other = (History) obj;
        if (id != other.id)
            return false;
        return true;
    }
}
