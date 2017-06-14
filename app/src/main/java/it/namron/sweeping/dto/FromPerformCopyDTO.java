package it.namron.sweeping.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by norman on 27/05/17.
 */

public class FromPerformCopyDTO implements Parcelable {
    private String folder;
    private Boolean delete;

    protected FromPerformCopyDTO(Parcel in) {
        folder = in.readString();
        delete = in.readByte() != 0;     //delete == true if byte != 0
    }

    public static final Creator<FromPerformCopyDTO> CREATOR = new Creator<FromPerformCopyDTO>() {
        @Override
        public FromPerformCopyDTO createFromParcel(Parcel in) {
            return new FromPerformCopyDTO(in);
        }

        @Override
        public FromPerformCopyDTO[] newArray(int size) {
            return new FromPerformCopyDTO[size];
        }
    };

    public FromPerformCopyDTO() {
    }

    public Boolean isDelete() {
        return delete;
    }

    public void setDelete(Boolean isDelete) {
        this.delete = isDelete;
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
        dest.writeByte((byte) (delete ? 1 : 0));     //if delete == true, byte == 1
//        dest.writeBooleanArray(new boolean[] {delete});
        dest.writeString(folder);
    }
}
