package it.namron.sweeping.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by norman on 27/05/17.
 */

public class FromPerformCopyDTO implements Parcelable {
    private String folder;
    private Boolean original;

    protected FromPerformCopyDTO(Parcel in) {
        folder = in.readString();
        original = in.readByte() != 0;     //original == true if byte != 0
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
