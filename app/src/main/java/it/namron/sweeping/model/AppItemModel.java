package it.namron.sweeping.model;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * Created by norman on 17/05/17.
 */

public class AppItemModel {
    private String appName;
    private String txtPrimary;
    private String txtSecondary;
    private String infoInstallation;
//    private ImageView appIcon;
    private Drawable appIcon;


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


}
