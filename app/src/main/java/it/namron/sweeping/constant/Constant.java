package it.namron.sweeping.constant;

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

    public static final String PERFORM_COPY_DIALOG_PARAMETER_BUNDLE = "perform_copy_dialog_parameter_bundle";
    public static final String PERFORM_COPY_DIALOG_PARAMETER_TAG = "perform_copy_dialog_parameter_TAG";

    public static final String ALERT_FOLDER_DIALOG_TAG = "alertFolderDialog_TAG";
    public static final String ALERT_MAIN_FOLDER_DIALOG_TAG = "AlertMainFolderDialog_TAG";
    public static final String ALERT_SELECTED_FOLDER_DIALOG_TAG = "AlertSelectedFolderDialog_TAG";
    public static final String EXTERNAL_STORAGE_COMPATIBILITY_DIALOG_TAG = "ExternalStorageCompatibilityDialog_TAG";
    public static final String ENOUGHT_FREE_MEMORY_DIALOG_TAG = "EnoughtFreeMemoryDialog_TAG";


    /**
     * This number will uniquely identify our Loader
     */
    public static final int ID_APP_LIST_LOADER = 20;
    public static final int ID_PREPARE_COPY_LOADER = 21;
    public static final int ID_APP_INFO_FOLDER_LOADER = 22;


    /**
     * Valori per gestire size_byte della classe DirectoryItemDTO
     *
     * */
    public static final int NOT_INITIALIZED_FOLDER_SIZE = -2;
    public static final int ILLEGAL_ARGUMENT_FOLDER_SIZE = -1;
    public static final int NULL_ARGUMENT_FOLDER_SIZE = -3;


    public static final int DIALOG_FRAGMENT = 1;


    public static final long MB_MARGIN = 1048576;


    /**
     * This string will uniquely identify our bundle
     */
    public static final String APP_SELECTED_BUNDLE = "APP_SELECTED_BUNDLE";
    public static final String APP_NAME_BUNDLE = "APP_NAME_BUNDLE";
    public static final String DIR_PREPARE_COPY_BUNDLE = "DIR_PREPARE_COPY_BUNDLE";
    public static final String SD_PREPARE_COPY_BUNDLE = "SD_PREPARE_COPY_BUNDLE";
    public static final String DTO_PREPARE_COPY_BUNDLE = "DTO_PREPARE_COPY_BUNDLE";

    /**
     * This string will uniquely identify our handler messages
     */
    public final static int MSG_PERFORME_COPY_PRE_EXECUTE = 1;
    public final static int MSG_PERFORME_COPY_RESOULT = 2;

    /**
     * This string will uniquely identify our bundle in onSaveInstanceState
     */
    public static final String DIRECTORY_LIST_MODELS_STATE = "DIRECTORY_LIST_MODELS_STATE";
    public static final String CURR_PERFORME_COPY_WORKING_STATE = "CURR_PERFORME_COPY_WORKING_STATE";
    public static final String CURRENT_NOTIFICATION_ID_STATE = "CURRENT_NOTIFICATION_ID_STATE";

    /**
     * This string will uniquely identify our AppInfoFragment.getTag()
     */
    public static final String TAG_APP_INFO_FRAGMENT = "TAG_APP_INFO_FRAGMENT";




//    public static enum target {
//        WHATSAPP("whatsapp"),
//        TELEGRAM("telegram"),
//        FACEBOOK("facebook");
//
//        private final String text;
//
//        private target(String text) {
//            this.text = text;
//        }
//
//        public String getName() {
//            return text;
//        }
//    }

    public static final List<String> APP_TARGET_LIST = Collections.unmodifiableList(
            new ArrayList<String>() {{
                add("whatsapp");
                add("telegram");
                add("facebook");
            }});

}
