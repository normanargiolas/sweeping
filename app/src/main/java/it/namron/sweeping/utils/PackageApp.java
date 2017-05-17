package it.namron.sweeping.utils;

import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.namron.core.utility.AppEntry;
import it.namron.sweeping.model.AppItemModel;

/**
 * Created by norman on 08/05/17.
 */

public class PackageApp {

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


    public static final String WHATSAPP = "com.whatsapp";
    public static final String WHATSAPP_DIRECTORY = "WhatsApp";
    public static final String WHATSAPP_MEDIA = "Media";

    public static final String TELEGRAM = "com.telegram";





    public static final List<String> APP_TARGET_LIST = Collections.unmodifiableList(
            new ArrayList<String>() {{
                add("whatsapp");
                add("telegram");
                add("facebook");
            }});


    public static List<AppItemModel> listOftargetApp(List<AppEntry> appList) {
        List<AppItemModel> appItemModelList = new ArrayList<>();
        AppItemModel appItemModel;
        for (AppEntry appEntry : appList) {
            String appPackage = appEntry.getApplicationInfo().packageName;

            for (String app : APP_TARGET_LIST) {
                if (appPackage.contains(app)) {
                    appItemModel = new AppItemModel();
                    appItemModel.setAppName(appEntry.getLabel());
                    appItemModel.setTxtPrimary(appEntry.getApplicationInfo().processName);
                    appItemModel.setTxtSecondary(appEntry.getApplicationInfo().dataDir);
                    appItemModel.setAppIcon(appEntry.getIcon());
                    appItemModelList.add(appItemModel);
                }
            }

        }

        return appItemModelList;\
    }


//Require API level 24
//    public static final Map<String, String> ListApp = Stream.of(
//            new SimpleEntry<>("WHATSAPP", "com.whatsapp"),
//            new SimpleEntry<>("tmp", "tmp"))
//            .collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue));

//    public static final Map<String, String> listApp;
//    static {
//        Map<String, String> aApp = new HashMap<String, String>();
//        aApp.put("WHATSAPP", "com.whatsapp");
//        aApp.put("tmp", "tmp");
//        listApp = Collections.unmodifiableMap(aApp);
//    }
}
