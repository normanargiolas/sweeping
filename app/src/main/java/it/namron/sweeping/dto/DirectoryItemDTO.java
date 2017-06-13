package it.namron.sweeping.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by norman on 15/05/17.
 */

public class DirectoryItemDTO implements Parcelable {
    private int id;

    private boolean isSelected;
    private String name;
    private String path;
    private String sizeString;
    private long sizeByte;

    public DirectoryItemDTO() {
    }

    protected DirectoryItemDTO(Parcel in) {
        id = in.readInt();
        isSelected = in.readByte() != 0;
        name = in.readString();
        path = in.readString();
        sizeString = in.readString();
        sizeByte = in.readLong();
    }

    public static final Creator<DirectoryItemDTO> CREATOR = new Creator<DirectoryItemDTO>() {
        @Override
        public DirectoryItemDTO createFromParcel(Parcel in) {
            return new DirectoryItemDTO(in);
        }

        @Override
        public DirectoryItemDTO[] newArray(int size) {
            return new DirectoryItemDTO[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getSizeByte() {
        return sizeByte;
    }

    public void setSizeByte(long sizeByte) {
        this.sizeByte = sizeByte;
    }

    public String getSizeString() {
        return sizeString;
    }

    public void setSizeString(String sizeString) {
        this.sizeString = sizeString;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeByte((byte) (isSelected ? 1 : 0));
        dest.writeString(name);
        dest.writeString(path);
        dest.writeString(sizeString);
        dest.writeLong(sizeByte);
    }
}
