package it.namron.sweeping.utils;


import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.filefilter.DirectoryFileFilter;


/**
 * Created by norman on 13/05/17.
 */

public class WhatsApp {

    public static enum folder {
        PACKAGE("com.whatsapp"),
        WHATSAPP_DIRECTORY("WhatsApp"),
        WHATSAPP_MEDIA("Media");

        private final String text;

        private folder(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }

    public WhatsApp() {

    }


    public static List<String> listOfWhatsAppDirectory() {
        List<String> listDirectory = new ArrayList<>();

        File whatsAppDirectory;
        File mediaDirectory;

        if (Environment.getExternalStorageState() == null) {
//            todo da pensare come procedere
            //cercare nella memoria interna del dispositivo
        } else if (Environment.getExternalStorageState() != null) {
            // search for directory on SD card
            File baseDirectory = new File(Environment.getExternalStorageDirectory().getPath());
            whatsAppDirectory = new File(baseDirectory, folder.WHATSAPP_DIRECTORY.getText());
            if (whatsAppDirectory.exists()) {
                mediaDirectory = new File(whatsAppDirectory, folder.WHATSAPP_MEDIA.getText());
                if (mediaDirectory.exists()) {
                    File[] subdirs = mediaDirectory.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY);

                    if (subdirs != null) {  //todo controllare i permessi dell'app
                        for (int i = 0; i < subdirs.length; i++) {
                            String dir = subdirs[i].getPath();

//                            Uri dirUri = Uri.parse(dir);
//                            String folder = dirUri.getLastPathSegment();
                            if (!dir.startsWith(".")) {
                                listDirectory.add(dir);
                            }
                        }
                    }
                }
            }
        }
        return listDirectory;
    }

}
