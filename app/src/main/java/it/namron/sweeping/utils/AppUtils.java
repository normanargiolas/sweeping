package it.namron.sweeping.utils;

import android.net.Uri;
import android.os.Environment;

import org.apache.commons.io.filefilter.DirectoryFileFilter;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import it.namron.sweeping.concurrency.AppEntry;
import it.namron.sweeping.constant.Constant;
import it.namron.sweeping.dto.AppItemDTO;

import static it.namron.sweeping.constant.Constant.APP_TARGET_LIST;

/**
 * Created by norman on 08/05/17.
 */

public class AppUtils {
    private static final String LOG_TAG = AppUtils.class.getSimpleName();

    public static Boolean isExternalStorageCompatible() {
        if (Environment.getExternalStorageState() != null) {
            LogUtils.LOGD_N(LOG_TAG, Environment.getExternalStorageDirectory().getPath(), true);
            return true;
        }
        LogUtils.LOGD_N(LOG_TAG, Constant.EXTERNAL_STORAGE_STATE, false);
        return false;
    }


    public static enum folder {
        WHATSAPP_PACKAGE("com.whatsapp"),
        WHATSAPP_DIRECTORY("WhatsApp"),
        WHATSAPP_MEDIA("Media"),
        TELEGRAM_PACKAGE("com.telegram"),
        TELEGRAM_DIRECTORY("Telegram"),
        TELEGRAM_MEDIA("Telegram");

        private final String text;

        private folder(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }
//public static enum folder {
//    TELEGRAM_PACKAGE("com.telegram"),
//    TELEGRAM_DIRECTORY("Telegram"),
//    TELEGRAM_MEDIA("Telegram");
//
//    private final String text;
//
//    private folder(String text) {
//        this.text = text;
//    }
//
//    public String getText() {
//        return text;
//    }
//}

    public static List<String> listOfWhatsAppDirectory() {
        List<String> listDirectory = new ArrayList<>();

        File whatsAppDirectory;
        File mediaDirectory;

        if (Environment.getExternalStorageState() == null || Environment.isExternalStorageRemovable()) {
            LogUtils.LOGD_N(LOG_TAG, Constant.EXTERNAL_STORAGE_STATE);
            return null;
        } else if (Environment.getExternalStorageState() != null) {
            // search for directory on SD card
            File baseDirectory = new File(Environment.getExternalStorageDirectory().getPath());

            LogUtils.LOGD_N(LOG_TAG, "baseDirectory", baseDirectory);

            whatsAppDirectory = new File(baseDirectory, AppUtils.folder.WHATSAPP_DIRECTORY.getText());
            if (whatsAppDirectory.exists()) {
                mediaDirectory = new File(whatsAppDirectory, AppUtils.folder.WHATSAPP_MEDIA.getText());
                if (mediaDirectory.exists()) {
                    File[] subdirs = mediaDirectory.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY);

                    if (subdirs != null) {  //todo controllare i permessi dell'app
                        for (int i = 0; i < subdirs.length; i++) {
                            String dir = subdirs[i].getPath();

                            Uri dirUri = Uri.parse(dir);
                            String folder = dirUri.getLastPathSegment();
                            if (!folder.startsWith(".")) {
                                listDirectory.add(dir);
                            }
                        }
                    }
                }
            }
        }
        return listDirectory;
    }


    public static List<String> listOfTelegramAppDirectory() {
        List<String> listDirectory = new ArrayList<>();
        File telegramAppDirectory;

        if (Environment.getExternalStorageState() == null) {
//            todo da pensare come procedere
            //cercare nella memoria interna del dispositivo
        } else if (Environment.getExternalStorageState() != null) {
            // search for directory on SD card
            File baseDirectory = new File(Environment.getExternalStorageDirectory().getPath());
            telegramAppDirectory = new File(baseDirectory, AppUtils.folder.TELEGRAM_DIRECTORY.getText());
            if (telegramAppDirectory.exists()) {
                File[] subdirs = telegramAppDirectory.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY);
                for (int i = 0; i < subdirs.length; i++) {
                    String dir = subdirs[i].getPath();

                    Uri dirUri = Uri.parse(dir);
                    String folder = dirUri.getLastPathSegment();
                    if (!folder.startsWith(".")) {
                        listDirectory.add(folder);
                    }
                }
                return listDirectory;
            }

        }

        return null;
    }

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
