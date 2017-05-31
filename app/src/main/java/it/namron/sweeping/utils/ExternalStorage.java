package it.namron.sweeping.utils;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by norman on 15/05/17.
 */

public class ExternalStorage {

    /**
     * @return A String of SD storage location available
     */
    public static String getSDStorageLocation() {
        String strSDCardPath = System.getenv("SECONDARY_STORAGE");
        if ((strSDCardPath == null) || (strSDCardPath.length() == 0)) {
            strSDCardPath = System.getenv("EXTERNAL_SDCARD_STORAGE");
        }

        //If may get a full path that is not the right one, even if we don't have the SD Card there.
        //We just need the "/mnt/extSdCard/" i.e and check if it's writable
        if (strSDCardPath != null) {
            if (strSDCardPath.contains(":")) {
                strSDCardPath = strSDCardPath.substring(0, strSDCardPath.indexOf(":"));
            }
            File externalFilePath = new File(strSDCardPath);

            if (externalFilePath.exists() && externalFilePath.canWrite()) {
                return strSDCardPath;
            }
        }
        return null;
    }

    public static Map<Integer, String> listEnvironmentVariableStoreSDCardRootDirectory(){
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

        return listEnvironmentVariableStoreSDCardRootDirectory;
    }


    /**
     * @return A map of all storage locations available
     */
    public static List<String> getAllStorageLocations() {
        List<String> map = new ArrayList<String>();



        String removableStoragePath;
        File fileList[] = new File("/storage/").listFiles();
        for (File file : fileList) {
            if (!file.getAbsolutePath().equalsIgnoreCase(Environment.getExternalStorageDirectory().getAbsolutePath()) &&
                    !file.getAbsolutePath().equalsIgnoreCase(Environment.getExternalStorageDirectory().getParent()) &&
                    file.isDirectory() && file.canRead()) {
                removableStoragePath = file.getAbsolutePath();
                map.add(removableStoragePath);
            }

        }
        //If there is an SD Card, removableStoragePath will have it's path. If there isn't it will be an empty string.

        return map;
    }

//
//    public static final String SD_CARD = "sdCard";
//    public static final String EXTERNAL_SD_CARD = "externalSdCard";
//
//    /**
//     * @return True if the external storage is available. False otherwise.
//     */
//    public static boolean isAvailable() {
//        String state = Environment.getExternalStorageState();
//        if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
//            return true;
//        }
//        return false;
//    }
//
//    public static String getSdCardPath() {
//        return Environment.getExternalStorageDirectory().getPath() + "/";
//    }
//
//    /**
//     * @return True if the external storage is writable. False otherwise.
//     */
//    public static boolean isWritable() {
//        String state = Environment.getExternalStorageState();
//        if (Environment.MEDIA_MOUNTED.equals(state)) {
//            return true;
//        }
//        return false;
//
//    }
//
//    /**
//     * @return A map of all storage locations available
//     */
//    public static Map<String, File> getAllStorageLocations() {
//        Map<String, File> map = new HashMap<String, File>(10);
//
//        List<String> mMounts = new ArrayList<String>(10);
//        List<String> mVold = new ArrayList<String>(10);
//        mMounts.add("/mnt/sdcard");
//        mVold.add("/mnt/sdcard");
//
//        try {
//            File mountFile = new File("/proc/mounts");
//            if (mountFile.exists()) {
//                Scanner scanner = new Scanner(mountFile);
//                while (scanner.hasNext()) {
//                    String line = scanner.nextLine();
//                    if (line.startsWith("/dev/block/vold/")) {
//                        String[] lineElements = line.split(" ");
//                        String element = lineElements[1];
//
//                        // don't add the default mount path
//                        // it's already in the list.
//                        if (!element.equals("/mnt/sdcard"))
//                            mMounts.add(element);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        try {
//            File voldFile = new File("/system/etc/vold.fstab");
//            if (voldFile.exists()) {
//                Scanner scanner = new Scanner(voldFile);
//                while (scanner.hasNext()) {
//                    String line = scanner.nextLine();
//                    if (line.startsWith("dev_mount")) {
//                        String[] lineElements = line.split(" ");
//                        String element = lineElements[2];
//
//                        if (element.contains(":"))
//                            element = element.substring(0, element.indexOf(":"));
//                        if (!element.equals("/mnt/sdcard"))
//                            mVold.add(element);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//        for (int i = 0; i < mMounts.size(); i++) {
//            String mount = mMounts.get(i);
//            if (!mVold.contains(mount))
//                mMounts.remove(i--);
//        }
//        mVold.clear();
//
//        List<String> mountHash = new ArrayList<String>(10);
//
//        for (String mount : mMounts) {
//            File root = new File(mount);
//            if (root.exists() && root.isDirectory() && root.canWrite()) {
//                File[] list = root.listFiles();
//                String hash = "[";
//                if (list != null) {
//                    for (File f : list) {
//                        hash += f.getName().hashCode() + ":" + f.length() + ", ";
//                    }
//                }
//                hash += "]";
//                if (!mountHash.contains(hash)) {
//                    String key = SD_CARD + "_" + map.size();
//                    if (map.size() == 0) {
//                        key = SD_CARD;
//                    } else if (map.size() == 1) {
//                        key = EXTERNAL_SD_CARD;
//                    }
//                    mountHash.add(hash);
//                    map.put(key, root);
//                }
//            }
//        }
//
//        mMounts.clear();
//
//        if (map.isEmpty()) {
//            map.put(SD_CARD, Environment.getExternalStorageDirectory());
//        }
//        return map;
//    }


//----------------------------------------

//    public String isRemovableSDCardAvailable() {
//        final String FLAG = "mnt";
//        final String SECONDARY_STORAGE = System.getenv("SECONDARY_STORAGE");
//        final String EXTERNAL_STORAGE_DOCOMO = System.getenv("EXTERNAL_STORAGE_DOCOMO");
//        final String EXTERNAL_SDCARD_STORAGE = System.getenv("EXTERNAL_SDCARD_STORAGE");
//        final String EXTERNAL_SD_STORAGE = System.getenv("EXTERNAL_SD_STORAGE");
//        final String EXTERNAL_STORAGE = System.getenv("EXTERNAL_STORAGE");
//
//        Map<Integer, String> listEnvironmentVariableStoreSDCardRootDirectory = new HashMap<Integer, String>();
//        listEnvironmentVariableStoreSDCardRootDirectory.put(0, SECONDARY_STORAGE);
//        listEnvironmentVariableStoreSDCardRootDirectory.put(1, EXTERNAL_STORAGE_DOCOMO);
//        listEnvironmentVariableStoreSDCardRootDirectory.put(2, EXTERNAL_SDCARD_STORAGE);
//        listEnvironmentVariableStoreSDCardRootDirectory.put(3, EXTERNAL_SD_STORAGE);
//        listEnvironmentVariableStoreSDCardRootDirectory.put(4, EXTERNAL_STORAGE);
//
//        File externalStorageList[] = null;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
//            externalStorageList = getContext().getExternalFilesDirs(null);
//        }
//        String directory = null;
//        int size = listEnvironmentVariableStoreSDCardRootDirectory.size();
//        for (int i = 0; i < size; i++) {
//            if (externalStorageList != null && externalStorageList.length > 1 && externalStorageList[1] != null)
//                directory = externalStorageList[1].getAbsolutePath();
//            else
//                directory = listEnvironmentVariableStoreSDCardRootDirectory.get(i);
//
//            directory = canCreateFile(directory);
//            if (directory != null && directory.length() != 0) {
//                if (i == size - 1) {
//                    if (directory.contains(FLAG)) {
//                        Log.e(getClass().getSimpleName(), "SD Card's directory: " + directory);
//                        return directory;
//                    } else {
//                        return null;
//                    }
//                }
//                Log.e(getClass().getSimpleName(), "SD Card's directory: " + directory);
//                return directory;
//            }
//        }
//        return null;
//    }
//
//    /**
//     * Check if can create file on given directory. Use this enclose with method
//     * {@link BeginScreenFragement#isRemovableSDCardAvailable()} to check sd
//     * card is available on device or not.
//     *
//     * @param directory
//     * @return
//     */
//    public String canCreateFile(String directory) {
//        final String FILE_DIR = directory + File.separator + "hoang.txt";
//        File tempFlie = null;
//        try {
//            tempFlie = new File(FILE_DIR);
//            FileOutputStream fos = new FileOutputStream(tempFlie);
//            fos.write(new byte[1024]);
//            fos.flush();
//            fos.close();
//            Log.e(getClass().getSimpleName(), "Can write file on this directory: " + FILE_DIR);
//        } catch (Exception e) {
//            Log.e(getClass().getSimpleName(), "Write file error: " + e.getMessage());
//            return null;
//        } finally {
//            if (tempFlie != null && tempFlie.exists() && tempFlie.isFile()) {
//                // tempFlie.delete();
//                tempFlie = null;
//            }
//        }
//        return directory;
//    }
}
