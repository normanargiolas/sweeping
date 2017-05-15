package it.namron.core;

import android.content.Context;
import android.os.Environment;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    // Context of the app under test.
    Context appContext = InstrumentationRegistry.getTargetContext();

    @Test
    public void useAppContext() throws Exception {
        Environment.getExternalStorageDirectory().getPath();

        File mediaDirectory;
        mediaDirectory = new File("/storage/");



//        String removableStoragePath;
//        File fileList[] = new File("/storage/").listFiles();
//        for (File file : fileList) {
//            if (!file.getAbsolutePath().equalsIgnoreCase(Environment.getExternalStorageDirectory().getAbsolutePath()) && file.isDirectory() && file.canRead())
//                removableStoragePath = file.getAbsolutePath();
//        }
        //If there is an SD Card, removableStoragePath will have it's path. If there isn't it will be an empty string.

        isRemovableSDCardAvailable();


        assertEquals("it.namron.core.test", appContext.getPackageName());
    }

    public String isRemovableSDCardAvailable() {
        final String FLAG = "mnt";
        final String SECONDARY_STORAGE = System.getenv("SECONDARY_STORAGE");
        final String EXTERNAL_STORAGE_DOCOMO = System.getenv("EXTERNAL_STORAGE_DOCOMO");
        final String EXTERNAL_SDCARD_STORAGE = System.getenv("EXTERNAL_SDCARD_STORAGE");
        final String EXTERNAL_SD_STORAGE = System.getenv("EXTERNAL_SD_STORAGE");
        final String EXTERNAL_STORAGE = System.getenv("EXTERNAL_STORAGE");

        Map<Integer, String> listEnvironmentVariableStoreSDCardRootDirectory = new HashMap<Integer, String>();
        listEnvironmentVariableStoreSDCardRootDirectory.put(0, SECONDARY_STORAGE);
        listEnvironmentVariableStoreSDCardRootDirectory.put(1, EXTERNAL_STORAGE_DOCOMO);
        listEnvironmentVariableStoreSDCardRootDirectory.put(2, EXTERNAL_SDCARD_STORAGE);
        listEnvironmentVariableStoreSDCardRootDirectory.put(3, EXTERNAL_SD_STORAGE);
        listEnvironmentVariableStoreSDCardRootDirectory.put(4, EXTERNAL_STORAGE);

        File externalStorageList[] = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            externalStorageList = appContext.getApplicationContext().getExternalFilesDirs(null);
        }
        String directory = null;
        int size = listEnvironmentVariableStoreSDCardRootDirectory.size();
        for (int i = 0; i < size; i++) {
            if (externalStorageList != null && externalStorageList.length > 1 && externalStorageList[1] != null)
                directory = externalStorageList[1].getAbsolutePath();
            else
                directory = listEnvironmentVariableStoreSDCardRootDirectory.get(i);

            directory = canCreateFile(directory);
            if (directory != null && directory.length() != 0) {
                if (i == size - 1) {
                    if (directory.contains(FLAG)) {
                        Log.e(getClass().getSimpleName(), "SD Card's directory: " + directory);
                        return directory;
                    } else {
                        return null;
                    }
                }
                Log.e(getClass().getSimpleName(), "SD Card's directory: " + directory);
                return directory;
            }
        }
        return null;
    }

    //    /**
//     * //     * Check if can create file on given directory. Use this enclose with method
//     * //     * {@link BeginScreenFragement#isRemovableSDCardAvailable()} to check sd
//     * //     * card is available on device or not.
//     * //     *
//     * //     * @param directory
//     * //     * @return
//     * //
//     */
    public String canCreateFile(String directory) {
        final String FILE_DIR = directory + File.separator + "hoang.txt";
        File tempFlie = null;
        try {
            tempFlie = new File(FILE_DIR);
            FileOutputStream fos = new FileOutputStream(tempFlie);
            fos.write(new byte[1024]);
            fos.flush();
            fos.close();
            Log.e(getClass().getSimpleName(), "Can write file on this directory: " + FILE_DIR);
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Write file error: " + e.getMessage());
            return null;
        } finally {
            if (tempFlie != null && tempFlie.exists() && tempFlie.isFile()) {
                // tempFlie.delete();
                tempFlie = null;
            }
        }
        return directory;
    }


}
