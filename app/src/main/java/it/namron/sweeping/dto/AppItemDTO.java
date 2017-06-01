package it.namron.sweeping.dto;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by norman on 17/05/17.
 */

public class AppItemDTO implements Parcelable {
    private String appName;
    private String txtPrimary;
    private String txtSecondary;
    private String infoInstallation;
    //    private ImageView appIcon;
    private Drawable appIcon;
    private int id;

    public AppItemDTO() {

    }

    protected AppItemDTO(Parcel in) {
        appName = in.readString();
        txtPrimary = in.readString();
        txtSecondary = in.readString();
        infoInstallation = in.readString();


        Bitmap bitmap = (Bitmap) in.readParcelable(getClass().getClassLoader());
        if (bitmap != null) {
            appIcon = new BitmapDrawable(Resources.getSystem(), bitmap);
        } else {
            appIcon = null;
        }
    }

    public static final Creator<AppItemDTO> CREATOR = new Creator<AppItemDTO>() {
        @Override
        public AppItemDTO createFromParcel(Parcel in) {
            return new AppItemDTO(in);
        }

        @Override
        public AppItemDTO[] newArray(int size) {
            return new AppItemDTO[size];
        }
    };

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getTxtPrimary() {
        return txtPrimary;
    }

    public void setTxtPrimary(String txtPrimary) {
        this.txtPrimary = txtPrimary;
    }

    public String getTxtSecondary() {
        return txtSecondary;
    }

    public void setTxtSecondary(String txtSecondary) {
        this.txtSecondary = txtSecondary;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public String getInfoInstallation() {
        return infoInstallation;
    }

    public void setInfoInstallation(String infoInstallation) {
        this.infoInstallation = infoInstallation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(appName);
        dest.writeString(txtPrimary);
        dest.writeString(txtSecondary);
        dest.writeString(infoInstallation);

        if (appIcon != null) {
            Bitmap bitmap = (Bitmap) ((BitmapDrawable) appIcon).getBitmap();
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
