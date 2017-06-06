package it.namron.sweeping.concurrency;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;
import android.webkit.MimeTypeMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import it.namron.sweeping.dto.FromPerformCopyDTO;
import it.namron.sweeping.listener.PerformeCopyListener;
import it.namron.sweeping.utils.LogUtils;

/**
 * Created by norman on 05/06/17.
 */

public class PerformeCopyLoader extends AsyncTaskLoader<Boolean> {
    public static final String LOG_TAG = PerformeCopyLoader.class.getSimpleName();


    private Context mContext;
    private ArrayList<String> mSources;
    private String mDestination;
    private PerformeCopyListener mCallback;
    FromPerformCopyDTO mInfo;

    private Boolean mResponce;
    private String currentTime;

    public PerformeCopyLoader(Context context, ArrayList<String> sources, String destination, FromPerformCopyDTO info, PerformeCopyListener callback) {
        super(context);
        mContext = context;
        mSources = sources;
        mDestination = destination;
        mInfo = info;
        mCallback = callback;
    }


    /**
     * Handles a request to start the Loader.
     */
    @Override
    protected void onStartLoading() {
        Boolean responce;
        if (mSources == null) {
            return;
        }
        /**
         * If we already have cached results, just deliver them now. If we don't have any
         * cached results, force a load.
         **/
        if (mResponce != null) {
            deliverResult(mResponce);
        } else {
            forceLoad();
        }
    }

    @Override
    public Boolean loadInBackground() {
        LogUtils.LOGD_N(LOG_TAG, "loadInBackground");
        try {
            for (String source : mSources) {

                File sourceLocation = new File(source);
                if (sourceLocation.isDirectory()) {
                    Uri uri = Uri.parse(source);
                    String token = uri.getLastPathSegment();
                    File destination = new File(mDestination, mInfo.getFolder());
                    if (!destination.exists()) {
                        //pensare se si vuole avvisare della directory già presente
                        destination.mkdir();
                    }
                    File destinationLocation = new File(mDestination, combine(mInfo.getFolder(), token));
                    copySelectedFolder(sourceLocation, destinationLocation);
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Called when there is new data to deliver to the client.  The
     * super class will take care of delivering it; the implementation
     * here just adds a little more logic.
     */
    @Override
    public void deliverResult(Boolean res) {
        mResponce = res;
        super.deliverResult(res);
    }

    /**
     * Handles a request to stop the Loader.
     */
    @Override
    protected void onStopLoading() {
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }

    /**
     * Handles a request to completely reset the Loader.
     */
    @Override
    protected void onReset() {
        super.onReset();
        // Ensure the loader is stopped
        onStopLoading();
        // At this point we can release the resources associated with 'apps'
        // if needed.
        if (mResponce != null) {
            onReleaseResources(mResponce);
            mResponce = null;
        }
    }

    private String combine(String path1, String path2) {
        File file1 = new File(path1);
        File file2 = new File(file1, path2);
        return file2.getPath();
    }

    private void copySelectedFolder(@NonNull File source, @NonNull File destination) throws IOException {
        LogUtils.LOGD_N(LOG_TAG, "Inizia la procedura di copia per: ", source);
        if (source.isDirectory()) {
            if (!destination.exists()) {
                //pensare se si vuole avvisare della directory già presente
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
                //errore nella copia del file
                mCallback.notifyOnErrorOccurred(source.toString(), 0, "copySelectedFolder");
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
    }

    /**
     * Helper function to take care of releasing resources associated
     * with an actively loaded data set.
     */
    protected void onReleaseResources(Boolean res) {
        // For a simple List<> there is nothing to do.  For something
        // like a Cursor, we would close it here.
    }

}
