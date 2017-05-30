package it.namron.sweeping.model;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by norman on 17/05/17.
 */

public class DrawerItemModel implements Parcelable {
    private String drawerName;
    private Drawable drawerIcon;
    private int id;

    public DrawerItemModel() {

    }

    protected DrawerItemModel(Parcel in) {
        drawerName = in.readString();
        Bitmap bitmap = (Bitmap) in.readParcelable(getClass().getClassLoader());
        if (bitmap != null) {
            drawerIcon = new BitmapDrawable(Resources.getSystem(), bitmap);
        } else {
            drawerIcon = null;
        }
    }

    public static final Creator<DrawerItemModel> CREATOR = new Creator<DrawerItemModel>() {
        @Override
        public DrawerItemModel createFromParcel(Parcel in) {
            return new DrawerItemModel(in);
        }

        @Override
        public DrawerItemModel[] newArray(int size) {
            return new DrawerItemModel[size];
        }
    };

    public String getDrawerName() {
        return drawerName;
    }

    public void setDrawerName(String drawerName) {
        this.drawerName = drawerName;
    }


    public Drawable getDrawerIcon() {
        return drawerIcon;
    }

    public void setDrawerIcon(Drawable drawerIcon) {
        this.drawerIcon = drawerIcon;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(drawerName);
        if (drawerIcon != null) {
            Bitmap bitmap = (Bitmap) ((BitmapDrawable) drawerIcon).getBitmap();
            dest.writeParcelable(bitmap, flags);
        } else {
            dest.writeParcelable(null, flags);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
