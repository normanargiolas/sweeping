package it.namron.sweeping.utils;

import java.util.ArrayList;
import java.util.List;

import it.namron.sweeping.dto.AppItemDTO;

import static it.namron.sweeping.utils.Constant.APP_TARGET_LIST;

/**
 * Created by norman on 08/05/17.
 */

public class PackageApp {



    public static List<AppItemDTO> listOfTargetDir(List<AppEntry> appList) {
        /**
         * todo inserire le cartelle
         * download
         * bluethoot
         **/

        return null;
    }

        public static List<AppItemDTO> listOfTargetApp(List<AppEntry> appList) {
        List<AppItemDTO> appItemModelList = new ArrayList<>();
        AppItemDTO appItemModel;
        int mId = 0;
        for (AppEntry appEntry : appList) {
            String appPackage = appEntry.getApplicationInfo().packageName;
            for (String app : APP_TARGET_LIST) {
                if (appPackage.contains(app)) {
                    appItemModel = new AppItemDTO();
                    appItemModel.setId(mId++);
                    appItemModel.setAppName(appEntry.getLabel());
                    appItemModel.setTxtPrimary(appEntry.getApplicationInfo().processName);
                    appItemModel.setTxtSecondary(appEntry.getApplicationInfo().dataDir);
                    appItemModel.setAppIcon(appEntry.getIcon());
                    appItemModelList.add(appItemModel);
                }
            }

        }

        return appItemModelList;
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
