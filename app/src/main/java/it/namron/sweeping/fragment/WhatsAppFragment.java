package it.namron.sweeping.fragment;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.apache.commons.io.filefilter.DirectoryFileFilter;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import it.namron.library.WhatsApp;
import it.namron.sweeping.adapter.DirectoryAdapter;
import it.namron.sweeping.constant.PackageApp;
import it.namron.sweeping.model.Message;
import it.namron.sweeping.sweeping.R;

/**
 * Created by norman on 09/05/17.
 */

public class WhatsAppFragment extends Fragment implements DirectoryAdapter.MessageAdapterListener {

    private static final String LOG_TAG = WhatsAppFragment.class.getSimpleName();

    public static final int NUM_LIST_ITEMS = 100;

    //References to RecyclerView and Adapter to reset the list to its
    //"pretty" state when the reset menu item is clicked.
    private DirectoryAdapter mDirectoryAdapter;
    private RecyclerView mDirectoryList;

    private List<Message> messages = new ArrayList<>();

    /*
     * This number will uniquely identify our Loader
     */
    private static final int ID_WHATSAPP_FOLDER_LOADER = 20;

    private LoaderManager mLoaderManager;
//    private Loader<Cursor> mFolderLoader;


    private Toast mToast;

    public WhatsAppFragment() {

    }

    private LoaderManager.LoaderCallbacks<List<Message>> mWhatsAppFolderLoaderCallback = new LoaderManager.LoaderCallbacks<List<Message>>() {

        @Override
        public Loader<List<Message>> onCreateLoader(int loaderId, Bundle args) {
            switch (loaderId) {
                case ID_WHATSAPP_FOLDER_LOADER:
                    return new AsyncTaskLoader<List<Message>>(getContext()) {

                        List<Message> mResponce;

                        @Override
                        protected void onStartLoading() {
                            Cursor responce;

                            if (args == null) {
                                return;
                            }
                            if (mResponce != null) {
                                deliverResult(mResponce);
                            } else {
                                forceLoad();
                            }
                        }

                        @Override
                        public void deliverResult(List<Message> data) {
                            mResponce = data;
                            super.deliverResult(data);
                        }

                        @Override
                        public List<Message> loadInBackground() {
                            File whatsAppDirectory;
                            File mediaDirectory;
                            if (Environment.getExternalStorageState() == null) {

                            } else if (Environment.getExternalStorageState() != null) {
                                // search for directory on SD card
                                File baseDirectory = new File(Environment.getExternalStorageDirectory().getPath());
                                whatsAppDirectory = new File(baseDirectory, PackageApp.WHATSAPP_DIRECTORY);
                                if (whatsAppDirectory.exists()) {
                                    mediaDirectory = new File(whatsAppDirectory, PackageApp.WHATSAPP_MEDIA);
                                    if (mediaDirectory.exists()) {

                                        File[] subdirs = mediaDirectory.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY);

//                                        List<String> listDirectory = new ArrayList<String>();

                                        messages.clear();
                                        Message msg = new Message();

                                        for (int i = 0; i < subdirs.length; i++) {
                                            String dir = subdirs[i].getPath();

                                            Uri dirUri = Uri.parse(dir);
                                            String folder = dirUri.getLastPathSegment();
                                            if (!folder.startsWith(".")) {
//                                                listDirectory.add(folder);

                                                msg.setFolderName(folder);
                                                msg.setSelected(false);
                                                messages.add(msg);
                                            }
                                        }

                                        return messages;


//                                        String[] arreyListDir = listDirectory.toArray(new String[listDirectory.size()]);
//                                        MatrixCursor matrixCursor = new MatrixCursor(arreyListDir);
//                                        Log.d(LOG_TAG, "loadInBackground");
//
//                                        return matrixCursor;
                                    }
                                }
                            }
                            return null;
                        }
                    };
                default:
                    throw new RuntimeException("Loader Not Implemented: " + loaderId);
            }
        }

        @Override
        public void onLoadFinished(Loader<List<Message>> loader, List<Message> data) {
            if (null == data) {
                int currentLine = Thread.currentThread().getStackTrace()[0].getLineNumber();
                String mthd = Thread.currentThread().getStackTrace()[0].getMethodName();
                String log = mthd.concat(": ").concat(String.valueOf(currentLine));
                Log.d(LOG_TAG, log);

                Toast.makeText(getActivity(), log, Toast.LENGTH_SHORT).show();
            } else {
                mDirectoryAdapter.swapFolder(data);
            }
        }

        @Override
        public void onLoaderReset(Loader<List<Message>> loader) {
            switch (loader.getId()) {
                case ID_WHATSAPP_FOLDER_LOADER:
                    mDirectoryAdapter.swapFolder(null);
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_whatsapp, container, false);

        mDirectoryList = (RecyclerView) rootView.findViewById(R.id.rv_numbers);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mDirectoryList.setLayoutManager(layoutManager);
        mDirectoryList.setHasFixedSize(true);

        //The GreenAdapter is responsible for displaying each item in the list.
        mDirectoryAdapter = new DirectoryAdapter(getContext(), this, messages);
        mDirectoryList.setAdapter(mDirectoryAdapter);

        /*
         * Initialize the loader
         */
        mLoaderManager = getLoaderManager();

        Bundle pathBundle = new Bundle();
        pathBundle.putString(PackageApp.WHATSAPP, PackageApp.WHATSAPP_DIRECTORY);
//        WhatsApp a = new WhatsApp().get {};

        String a = WhatsApp.folder.PACKAGE.getText();
        pathBundle.putString(WhatsApp.folder.PACKAGE.name(), WhatsApp.folder.PACKAGE.getText());


        getLoaderManager().initLoader(ID_WHATSAPP_FOLDER_LOADER, pathBundle, mWhatsAppFolderLoaderCallback);

////        todo eseguirlo in un loader
//        searchWhatsAppFolders();

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


//                    mDirectoryAdapter.swapCursor(matrixCursor);
                }
            }
        }// end of SD card checking
    }

    @Override
    public void onIconDirectoryClicked(int position) {
        Message message = messages.get(position);
        message.setSelected(!message.isSelected());
        messages.set(position, message);
        mDirectoryAdapter.notifyDataSetChanged();


        if (mToast != null) {
            mToast.cancel();
        }
        String toastMessage = "Item #" + position + " clicked.";
        mToast = Toast.makeText(this.getContext(), toastMessage, Toast.LENGTH_LONG);

        mToast.show();
    }
}





















