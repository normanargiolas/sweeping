package it.namron.sweeping.data.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Timestamp;

/**
 * Created by norman on 09/06/17.
 */

public class ErrorLog implements Parcelable {

    private int id;

    private String file;

    private String method;

    private int line;

    private String msg;

    private String stackTrace;

    private Timestamp timestamp;

    private History history;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public ErrorLog() {
        super();
    }

    public ErrorLog(int id, String file, String method, int line, String msg, String log, String stackTrace, Timestamp timestamp, History history) {
        super();
        this.id = id;
        this.file = file;
        this.method = method;
        this.line = line;
        this.msg = msg;
        this.stackTrace = stackTrace;
        this.timestamp = timestamp;
        this.history = history;
    }

    protected ErrorLog(Parcel in) {
        super();
        id = in.readInt();
        file = in.readString();
        method = in.readString();
        line = in.readInt();
        msg = in.readString();
        stackTrace = in.readString();
        timestamp = (Timestamp) in.readSerializable();
        history = in.readParcelable(History.class.getClassLoader());
    }

    public static final Creator<ErrorLog> CREATOR = new Creator<ErrorLog>() {
        @Override
        public ErrorLog createFromParcel(Parcel in) {
            return new ErrorLog(in);
        }

        @Override
        public ErrorLog[] newArray(int size) {
            return new ErrorLog[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(file);
        dest.writeString(method);
        dest.writeInt(line);
        dest.writeString(msg);
        dest.writeString(stackTrace);
        dest.writeSerializable(timestamp);
        dest.writeParcelable(history, flags);
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
        ErrorLog other = (ErrorLog) obj;
        if (id != other.id)
            return false;
        return true;
    }

    public History getHistory() {
        return history;
    }

    public void setHistory(History history) {
        this.history = history;
    }
}
