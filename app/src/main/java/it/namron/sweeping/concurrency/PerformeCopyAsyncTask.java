package it.namron.sweeping.concurrency;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.text.format.Time;
import android.util.Log;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import it.namron.sweeping.data.entity.History;
import it.namron.sweeping.data.service.ErrorLogService;
import it.namron.sweeping.data.service.HistoryService;
import it.namron.sweeping.dto.FromPerformCopyDTO;
import it.namron.sweeping.listener.PerformeCopyAsyncTaskListener;
import it.namron.sweeping.utils.LogUtils;
import it.namron.sweeping.utils.ResourceHashCode;

import static it.namron.sweeping.constant.Constant.DATETIME_FORMAT;

/**
 * Created by norman on 13/06/17.
 */

public class PerformeCopyAsyncTask extends AsyncTask<ArrayList<String>, Integer, Boolean> {
    private final String CLASS_NAME_HASH_CODE = getClass().getSimpleName() + super.hashCode();

    private final String LOG_TAG = getClass().getSimpleName();

    History mHistory = null;

    private Activity mActivity;
    private String mDestination;
    private FromPerformCopyDTO mInfo;

    private ArrayList<String> mSources;

    private PerformeCopyAsyncTaskListener mCallback;

    private HistoryService historyService;
    private ErrorLogService errorLogService;

    private int numberOfFiles;
    private long sizeOfFiles;

    public PerformeCopyAsyncTask(@NonNull Activity activity,
                                 @NonNull PerformeCopyAsyncTaskListener callback,
                                 @NonNull String destination,
                                 @NonNull FromPerformCopyDTO info) {
        try {
            ResourceHashCode.setPerformeCopyAsyncTaskCode(CLASS_NAME_HASH_CODE);
        } catch (NullPointerException e) {
            Log.e(LOG_TAG, "PerformeCopyAsyncTask: " + "ResourceHashCode not initialized");
            e.printStackTrace();
        }
        mActivity = activity;
        mDestination = destination;
        mInfo = info;
        mCallback = callback;

        historyService = new HistoryService();
        errorLogService = new ErrorLogService();
        numberOfFiles = 0;
        sizeOfFiles = 0;
    }

    @Override
    protected Boolean doInBackground(@NonNull ArrayList<String>... sources) {

        //todo remove this only for test
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        return null;


        mSources = sources[0];
        boolean res = inBackground();
        return res;
    }

    private boolean inBackground() {
        LogUtils.LOGD_N(LOG_TAG, "inBackground");

        mHistory = new History(mInfo.getFolder(), 0, 0);
        mHistory = historyService.insertHistory(mHistory);

//        //todo da provare, togliere successivamente
//        String m = "errore nella copia del file";
//        String t = "Stack trace";
//        insertErrorLog(m, t);


        try {
            for (String source : mSources) {

                File sourceLocation = new File(source);
                if (sourceLocation.isDirectory()) {
                    Uri uri = Uri.parse(source);
                    String token = uri.getLastPathSegment();
                    File destination = new File(mDestination, mInfo.getFolder());
                    if (!destination.exists()) {
                        //todo pensare se si vuole avvisare della directory già presente
                        destination.mkdir();
                    }
                    File destinationLocation = new File(mDestination, combine(mInfo.getFolder(), token));
                    copySelectedFolder(sourceLocation, destinationLocation);
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            String msg = "source o destination non validi";
            String trace = e.getMessage();
            insertErrorLog(msg, trace);
            return false;
        }
    }

    private void insertErrorLog(String msg, String trace) {
        String fullClassName = Thread.currentThread().getStackTrace()[3].getClassName();
        String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
        String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
        int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();
        errorLogService.insertErrorLog(fullClassName, methodName, lineNumber, msg, trace, null);
    }

    private void copySelectedFolder(@NonNull File source, @NonNull File destination) throws IOException {
        LogUtils.LOGD_N(LOG_TAG, "Inizia la procedura di copia per: ", source);
        if (source.isDirectory()) {
            if (!destination.exists()) {
                //todo pensare se si vuole avvisare della directory già presente
                destination.mkdir();
            }

            String[] children = source.list();
            for (int i = 0; i < source.listFiles().length; i++) {

                copySelectedFolder(new File(source, children[i]),
                        new File(destination, children[i]));
            }
        } else { //todo vedere come si comporta con i collegamenti
            if (destination.exists()) {
                if (!contentEquals(source, destination)) {
                    //file è con lo stesso nome ma diverso: rinominare
                    destination = getNewDestination(destination);
                    copy(source, destination);
                }

            } else {
                copy(source, destination);
            }

            //verifica integrità
            if (contentEquals(source, destination)) {
                //controlla se devo eliminare l'originale
                if (mInfo.isDelete()) {
                    FileUtils.forceDelete(source);
                }
            } else {
                //todo errore nella copia del file
                //mCallback.notifyOnErrorOccurred(source.toString(), 0, CLASS_NAME_HASH_CODE);

                String msg = "errore nella copia del file";
                String trace = null;
                insertErrorLog(msg, trace);
            }
        }
    }

    private File getNewDestination(File file) {
        /**
         Whereas you can have DateFormat patterns such as:
         "yyyy.MM.dd G 'at' HH:mm:ss z" ---- 2001.07.04 AD at 12:08:56 PDT
         "hh 'o''clock' a, zzzz" ----------- 12 o'clock PM, Pacific Daylight Time
         "EEE, d MMM yyyy HH:mm:ss Z"------- Wed, 4 Jul 2001 12:08:56 -0700
         "yyyy-MM-dd'T'HH:mm:ss.SSSZ"------- 2001-07-04T12:08:56.235-0700
         "yyMMddHHmmssZ"-------------------- 010704120856-0700
         "K:mm a, z" ----------------------- 0:08 PM, PDT
         "h:mm a" -------------------------- 12:08 PM
         "EEE, MMM d, ''yy" ---------------- Wed, Jul 4, '01
         **/
        DateFormat df = new SimpleDateFormat("_yyyy_MM_dd_sss");
        String time = df.format(Calendar.getInstance().getTime());

        String str = file.toString();
        String basename = FilenameUtils.getBaseName(str);
        String extension = FilenameUtils.getExtension(str);

        String newName = basename.concat(time).concat(extension);
        String parent = file.getParent();

        return new File(parent, newName);
    }

    private boolean contentEquals(File source, File destination) throws IOException {
        boolean isTwoEqual = FileUtils.contentEquals(source, destination);
        //todo verificare ulteriormente con md5 ???
        return isTwoEqual;
    }

    private void copy(File source, File destination) throws IOException {
        /**You can't use FileChannel if your file is bigger than 2GB though.**/
        FileInputStream inStream = new FileInputStream(source);
        FileOutputStream outStream = new FileOutputStream(destination);
        FileChannel inChannel = inStream.getChannel();
        FileChannel outChannel = outStream.getChannel();
        inChannel.transferTo(0, inChannel.size(), outChannel);
        inStream.close();
        outStream.close();

        numberOfFiles++;
        sizeOfFiles = sizeOfFiles + source.length();
    }

    private String combine(String path1, String path2) {
        File file1 = new File(path1);
        File file2 = new File(file1, path2);
        return file2.getPath();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        Log.d(LOG_TAG, "onProgressUpdate: " + "values[0] = " + values[0]);
        if (values.length > 0) {
            mCallback.notifyOnPerformeCopyProgress(values[0], ResourceHashCode.getPerformeCopyAsyncTaskCode());
        }
    }

    @Override
    protected void onPreExecute() {
        mCallback.notifyOnPerformeCopyPreExecute(ResourceHashCode.getPerformeCopyAsyncTaskCode());
    }

    @Override
    protected void onPostExecute(Boolean result) {
        mHistory.setFile_number(numberOfFiles);
        mHistory.setSize(sizeOfFiles);
        mHistory.setEnd_time(getCurrentTimeStamp());
        historyService.updateHistory(mHistory);

        mCallback.notifyOnPerformeCopyResoult(mActivity, mInfo.getFolder(), ResourceHashCode.getPerformeCopyAsyncTaskCode());
        ResourceHashCode.removePerformeCopyTask(this);
    }

    public Timestamp getCurrentTimeStamp(){
        try {
            Date date = new Date();
            Timestamp tsTemp = new Timestamp(date.getTime());
            final SimpleDateFormat parser = new SimpleDateFormat(DATETIME_FORMAT);
            tsTemp.valueOf(parser.format(tsTemp));
            return tsTemp;

        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }
}
