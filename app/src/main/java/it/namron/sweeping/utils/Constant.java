package it.namron.sweeping.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by norman on 21/05/17.
 */

public class Constant {

    public static final String APP_WHATSAPP = "whatsapp";
    public static final String APP_TELEGRAM = "telegram";
    public static final String APP_FACEBOOK = "facebook";
    public static final String APP_MESSENGER = "messenger";


//    public static final String DIALOG_ICON_APP_ICON = "dialog_icon_app_icon";
//    public static final String DIALOG_TITLE_APP_NAME = "dialog_title_app_name";
//    public static final String DIALOG_FOLDER_OUT = "dialog_folder_out";
    public static final String PERFORM_COPY_DIALOG_PARAMETER_BUNDLE = "perform_copy_dialog_parameter_bundle";
    public static final String PERFORM_COPY_DIALOG_PARAMETER_TAG = "perform_copy_dialog_parameter_TAG";
    public static final String ALERT_FOLDER_DIALOG_TAG = "alertFolderDialog_TAG";





    public static final int DIALOG_FRAGMENT = 1;


    /**
     * This string will uniquely identify our bundle
     */
    public static final String APP_SELECTED_BUNDLE = "APP_SELECTED_BUNDLE";
    public static final String APP_NAME_BUNDLE = "APP_NAME_BUNDLE";

    public static enum target {
        WHATSAPP("whatsapp"),
        TELEGRAM("telegram"),
        FACEBOOK("facebook");

        private final String text;

        private target(String text) {
            this.text = text;
        }

        public String getName() {
            return text;
        }
    }

    public static final List<String> APP_TARGET_LIST = Collections.unmodifiableList(
            new ArrayList<String>() {{
                add("whatsapp");
                add("telegram");
                add("facebook");
            }});

}
