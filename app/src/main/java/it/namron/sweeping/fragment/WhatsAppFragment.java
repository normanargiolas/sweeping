package it.namron.sweeping.fragment;

import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.NotFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.namron.sweeping.activity.MainActivity;
import it.namron.sweeping.adapter.DirectoryAdapter;
import it.namron.sweeping.constant.PackageApp;
import it.namron.sweeping.sweeping.R;

/**
 * Created by norman on 09/05/17.
 */

public class WhatsAppFragment extends Fragment {

    private static final String LOG_TAG = WhatsAppFragment.class.getSimpleName();

    public static final int NUM_LIST_ITEMS = 100;

    //References to RecyclerView and Adapter to reset the list to its
    //"pretty" state when the reset menu item is clicked.
    private DirectoryAdapter mDirectoryAdapter;
    private RecyclerView mDirectoryList;

    public WhatsAppFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_whatsapp, container, false);

        mDirectoryList = (RecyclerView) rootView.findViewById(R.id.rv_numbers);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mDirectoryList.setLayoutManager(layoutManager);
        mDirectoryList.setHasFixedSize(true);

        //The GreenAdapter is responsible for displaying each item in the list.
        mDirectoryAdapter = new DirectoryAdapter(getContext());
        mDirectoryList.setAdapter(mDirectoryAdapter);

//        todo eseguirlo in un loader
        searchWhatsAppFolders();

        return rootView;
    }

    private void searchWhatsAppFolders() {
        //if there is no SD card, create new directory objects to make directory on device
        File whatsAppDirectory;
        File mediaDirectory;
        if (Environment.getExternalStorageState() == null) {
//            //create new file directory object
//            whatsAppDirectory = new File(Environment.getDataDirectory()
//                    + PackageApp.WHATSAPP_DIRECTORY);
//            mediaDirectory = new File(Environment.getDataDirectory()
//                    + PackageApp.WHATSAPP_MEDIA);
//            /*
//             * this checks to see if there are any previous test photo files
//             * if there are any photos, they are deleted for the sake of
//             * memory
//             */
//            if (mediaDirectory.exists()) {
//                File[] dirFiles = mediaDirectory.listFiles();
//                if (dirFiles.length != 0) {
//                    for (int ii = 0; ii <= dirFiles.length; ii++) {
////                        dirFiles[ii].delete();
//                    }
//                }
//            }
////            // if no directory exists, create new directory
////            if (!whatsAppDirectory.exists()) {
////                whatsAppDirectory.mkdir();
////            }
//
//            // if phone DOES have sd card
        } else if (Environment.getExternalStorageState() != null) {
            // search for directory on SD card
            File baseDirectory = new File(Environment.getExternalStorageDirectory().getPath());
            whatsAppDirectory = new File(baseDirectory, PackageApp.WHATSAPP_DIRECTORY);
            if (whatsAppDirectory.exists()) {
                mediaDirectory = new File(whatsAppDirectory, PackageApp.WHATSAPP_MEDIA);
                if (mediaDirectory.exists()) {
                    File[] subdirs = mediaDirectory.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY);

                    List<String> listDirectory = new ArrayList<String>();
//                    todo controllare use MatrixCursor, instead of addRow() which is not very handy, use builder method newRow()


                    for (int i = 0; i < subdirs.length; i++) {
                        String dir = subdirs[i].getPath();

                        Uri dirUri = Uri.parse(dir);
                        String folder = dirUri.getLastPathSegment();
                        if (!folder.startsWith(".")) {
                            listDirectory.add(folder);
                        }
                    }

                    String[] arreyListDir = listDirectory.toArray(new String[listDirectory.size()]);

                    MatrixCursor matrixCursor = new MatrixCursor(arreyListDir);

                    Log.d(LOG_TAG, "loadInBackground");


                    mDirectoryAdapter.swapCursor(matrixCursor);
                }
            }
        }// end of SD card checking
    }
}





















