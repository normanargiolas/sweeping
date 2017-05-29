package it.namron.sweeping.utils;

import android.net.Uri;
import android.os.Environment;

import org.apache.commons.io.filefilter.DirectoryFileFilter;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by norman on 22/05/17.
 */

public class TelegramApp {

    public static enum folder {
        PACKAGE("com.telegram"),
        DIRECTORY("Telegram"),
        MEDIA("Telegram");

        private final String text;

        private folder(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }

    public TelegramApp() {
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
            telegramAppDirectory = new File(baseDirectory, folder.DIRECTORY.getText());
            if(telegramAppDirectory.exists()){
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
}
