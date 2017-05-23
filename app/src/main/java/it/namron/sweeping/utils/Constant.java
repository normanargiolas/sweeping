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
