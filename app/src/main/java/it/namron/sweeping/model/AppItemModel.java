package it.namron.sweeping.model;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import java.io.Serializable;

/**
 * Created by norman on 17/05/17.
 */

public class AppItemModel implements Parcelable{
    private String appName;
    private String txtPrimary;
    private String txtSecondary;
    private String infoInstallation;
//    private ImageView appIcon;
    private Drawable appIcon;

    public AppItemModel(){

    }

    protected AppItemModel(Parcel in) {
        appName = in.readString();
        txtPrimary = in.readString();
        txtSecondary = in.readString();
        infoInstallation = in.readString();
    }

    public static final Creator<AppItemModel> CREATOR = new Creator<AppItemModel>() {
        @Override
        public AppItemModel createFromParcel(Parcel in) {
            return new AppItemModel(in);
        }

        @Override
        public AppItemModel[] newArray(int size) {
            return new AppItemModel[size];
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

        if ( appIcon != null ) {
            Bitmap bitmap = (Bitmap) ((BitmapDrawable) appIcon).getBitmap();
            dest.writeParcelable(bitmap, flags);
        }
        else {
            dest.writeParcelable(null, flags);
        }
    }
}
