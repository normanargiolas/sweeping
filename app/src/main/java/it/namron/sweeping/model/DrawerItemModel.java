package it.namron.sweeping.model;

import android.graphics.drawable.Drawable;

/**
 * Created by norman on 22/05/17.
 */

public class DrawerItemModel {
    private String appName;
    private String infoInstallation;
    private Drawable appIcon;

    public DrawerItemModel() {
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getInfoInstallation() {
        return infoInstallation;
    }

    public void setInfoInstallation(String infoInstallation) {
        this.infoInstallation = infoInstallation;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }
}
