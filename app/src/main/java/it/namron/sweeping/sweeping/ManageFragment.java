package it.namron.sweeping.sweeping;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.nononsenseapps.filepicker.FilePickerActivity;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.StringBuilderWriter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by norman on 09/05/17.
 */

public class ManageFragment extends Fragment {

    public static final String LOG_TAG = "ManageFragment";

    /*
     * This number will uniquely identify our FilePickerActivity Intent for get directory path
     */
    private static final int PICKFILE_SOURCE_CODE_INTENT = 1;
    private static final int PICKFILE_DESTINATION_CODE_INTENT = 2;


    /*
     * This number will uniquely identify our Loader
     */
    private static final int DIRECTORY_SOURCE_SIZE_LOADER = 20;
    private static final int DIRECTORY_DESTINATION_SIZE_LOADER = 21;
    private static final int FILES_SOURCE_NUMBER_LOADER = 30;
    private static final int FILES_DESTINATION_NUMBER_LOADER = 31;
    private static final int COPY_FOLDER_LOADER = 40;
    private static final int DELETE_FOLDER_LOADER = 41;


    /* A constant to save and restore the path file uri */
    private static final String SEARCH_FILE_PATH_URI_EXTRA = "filePathUri";
    private static final String SOURCE_PATH_URI_EXTRA = "sourcePath";
    private static final String DESTINATION_PATH_URI_EXTRA = "destinationPath";


    private LoaderManager mLoaderManager;

    private TextView mTextViewManageFromFolder;
    private TextView mTextViewManageFromFiles;
    private TextView mTextViewManageFromSize;
    private TextView mTextViewManageToFolder;
    private TextView mTextViewManageToFiles;
    private TextView mTextViewManageToSize;

    private Switch mSwitchManage;

    private Uri mSourceFolder = null;
    private Uri mDestinationFolder = null;


    public ManageFragment() {

    }

    private LoaderManager.LoaderCallbacks<Boolean> mCopyFolderLoaderCallback = new LoaderManager.LoaderCallbacks<Boolean>() {

        @Override
        public Loader<Boolean> onCreateLoader(int id, Bundle args) {
            return new AsyncTaskLoader<Boolean>(getContext()) {

                Boolean mResponce;

                @Override
                protected void onStartLoading() {
                    Boolean responce;

                    if (args == null) {
                        return;
                    }
                /*
                 * If we already have cached results, just deliver them now. If we don't have any
                 * cached results, force a load.
                 */
                    if (mResponce != null) {
                        deliverResult(mResponce);
                    } else {
                        forceLoad();
                    }
                }

                @Override
                public Boolean loadInBackground() {
                    Log.d(LOG_TAG, "loadInBackground");

                    String sourcePath = args.getString(SOURCE_PATH_URI_EXTRA);
                    Uri sourceUri = Uri.parse(sourcePath);
                    String destPath = args.getString(DESTINATION_PATH_URI_EXTRA);
                    Uri destUri = Uri.parse(destPath);

                    try {
                        File source = new File(sourceUri.getPath());
                        File desc = new File(destUri.getPath());

                        FileUtils.copyDirectory(source, desc);
                        Log.d(LOG_TAG, "performeCopyFolder cartella copiata");
                    } catch (IOException e) {
                        Log.d(LOG_TAG, "performeCopyFolder error...");
                        e.printStackTrace();
                        return false;
                    }
                    return true;
                }

                @Override
                public void deliverResult(Boolean data) {
                    mResponce = data;
                    super.deliverResult(data);
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<Boolean> loader, Boolean data) {
            if (null == data) {
                Log.d(LOG_TAG, "onLoadFinished error");
                String msg = "Errore nella copia della cartella!";
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            } else {
                String msg = "Cartella copiata correttamente!";
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onLoaderReset(Loader<Boolean> loader) {

        }
    };

    private LoaderManager.LoaderCallbacks<Long> mSizePathLoaderCallback = new LoaderManager.LoaderCallbacks<Long>() {
        @Override
        public Loader<Long> onCreateLoader(int id, Bundle args) {
            return new AsyncTaskLoader<Long>(getContext()) {

                Long mDataByte;

                @Override
                protected void onStartLoading() {
                    Long dataByte;

                    if (args == null) {
                        return;
                    }
                /*
                 * If we already have cached results, just deliver them now. If we don't have any
                 * cached results, force a load.
                 */
                    if (mDataByte != null) {
                        deliverResult(mDataByte);
                    } else {
                        forceLoad();
                    }
                }

                @Override
                public Long loadInBackground() {
                    Log.d(LOG_TAG, "loadInBackground");

                    String filePath = args.getString(SEARCH_FILE_PATH_URI_EXTRA);
                    Uri filePathUri = Uri.parse(filePath);

                             /* If the user didn't enter anything, there's nothing to search for */
                    if (filePathUri == null) {
                        return null;
                    }

                /* Perform the search */
                    long folder_size_bytes = FileUtils.sizeOfDirectory(new File(filePathUri.getPath()));
                    Log.d(LOG_TAG, String.valueOf(folder_size_bytes / 1024));

                    return folder_size_bytes;
                }

                @Override
                public void deliverResult(Long dataByte) {
                    mDataByte = dataByte;
                    super.deliverResult(dataByte);
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<Long> loader, Long data) {
            Log.d(LOG_TAG, "onLoadFinished");

            if (null == data) {
                Log.d(LOG_TAG, "onLoadFinished error");
            } else {
                switch (loader.getId()) {
                    case DIRECTORY_SOURCE_SIZE_LOADER:
                        mTextViewManageFromSize.setText(String.valueOf(data / 1024));
                        break;
                    case DIRECTORY_DESTINATION_SIZE_LOADER:
                        mTextViewManageToSize.setText(String.valueOf(data / 1024));
                        break;
                    default:
                        Log.d(LOG_TAG, "onLoadFinished loader sconosciuto");
                        break;
                }
                Log.d(LOG_TAG, String.valueOf(data / 1024));
            }
        }


        @Override
        public void onLoaderReset(Loader<Long> loader) {
        }
    };

    private LoaderManager.LoaderCallbacks<Integer> mFilesNumberLoaderCallback = new LoaderManager.LoaderCallbacks<Integer>() {
        @Override
        public Loader<Integer> onCreateLoader(int id, Bundle args) {
            return new AsyncTaskLoader<Integer>(getContext()) {

                Integer mFiles;

                @Override
                protected void onStartLoading() {
                    Integer files;

                    if (args == null) {
                        return;
                    }
                /*
                 * If we already have cached results, just deliver them now. If we don't have any
                 * cached results, force a load.
                 */
                    if (mFiles != null) {
                        deliverResult(mFiles);
                    } else {
                        forceLoad();
                    }

                }

                @Override
                public Integer loadInBackground() {
                    Log.d(LOG_TAG, "loadInBackground");

                    String filePath = args.getString(SEARCH_FILE_PATH_URI_EXTRA);
                    Uri filePathUri = Uri.parse(filePath);
                    /* If the user didn't enter anything, there's nothing to search for */
                    if (filePathUri == null) {
                        return null;
                    }

                /* Perform the search */
                    int number_of_file = countFiles(new File(filePathUri.getPath()));
                    Log.d(LOG_TAG, String.valueOf(number_of_file));

                    return number_of_file;
                }

                @Override
                public void deliverResult(Integer files) {
                    mFiles = files;
                    super.deliverResult(files);
                }
            };
        }

        private int countFiles(File directory) {
            int count = 0;
            for (File file : directory.listFiles()) {
                if (file.isDirectory()) {
                    count += countFiles(file);
                }
                count++;
            }
            return count;
        }

        @Override
        public void onLoadFinished(Loader<Integer> loader, Integer data) {
            Log.d(LOG_TAG, "onLoadFinished");

            if (null == data) {
                Log.d(LOG_TAG, "onLoadFinished error");
            } else {
                switch (loader.getId()) {
                    case FILES_SOURCE_NUMBER_LOADER:
                        mTextViewManageFromFiles.setText(String.valueOf(data));
                        break;
                    case FILES_DESTINATION_NUMBER_LOADER:
                        mTextViewManageToFiles.setText(String.valueOf(data));
                        break;
                    default:
                        Log.d(LOG_TAG, "onLoadFinished loader sconosciuto");
                        break;
                }
                Log.d(LOG_TAG, String.valueOf(data));
            }
        }

        @Override
        public void onLoaderReset(Loader<Integer> loader) {

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_manage, container, false);

        mTextViewManageFromFolder = (TextView) rootView.findViewById(R.id.manage_from_folder);
        mTextViewManageFromFiles = (TextView) rootView.findViewById(R.id.manage_from_files);
        mTextViewManageFromSize = (TextView) rootView.findViewById(R.id.manage_from_size);
        mTextViewManageToFolder = (TextView) rootView.findViewById(R.id.manage_to_folder);
        mTextViewManageToFiles = (TextView) rootView.findViewById(R.id.manage_to_files);
        mTextViewManageToSize = (TextView) rootView.findViewById(R.id.manage_to_size);

        mSwitchManage = (Switch) rootView.findViewById(R.id.switch_manage);

        FloatingActionButton fab_source_manage = (FloatingActionButton) rootView.findViewById(R.id.fab_source_manage);
        fab_source_manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                try {
                    Intent selectDirectoyIntent = new Intent(getActivity(), FilePickerActivity.class);
                    selectDirectoyIntent.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
                    selectDirectoyIntent.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, true);
                    selectDirectoyIntent.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_DIR);
                    selectDirectoyIntent.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());
                    startActivityForResult(selectDirectoyIntent, PICKFILE_SOURCE_CODE_INTENT);

                } catch (Exception e) {
                    Log.e(LOG_TAG, "exception", e);
                    e.printStackTrace();

                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        FloatingActionButton fab_destination_manage = (FloatingActionButton) rootView.findViewById(R.id.fab_destination_manage);
        fab_destination_manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                try {
                    Intent selectDirectoyIntent = new Intent(getActivity(), FilePickerActivity.class);
                    selectDirectoyIntent.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
                    selectDirectoyIntent.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, true);
                    selectDirectoyIntent.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_DIR);
                    selectDirectoyIntent.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());
                    startActivityForResult(selectDirectoyIntent, PICKFILE_DESTINATION_CODE_INTENT);

                } catch (Exception e) {
                    Log.e(LOG_TAG, "exception", e);
                    e.printStackTrace();

                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        FloatingActionButton fab_performe_manage = (FloatingActionButton) rootView.findViewById(R.id.fab_performe_manage);
        fab_performe_manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //copy folder
                performeCopyFolder(mSourceFolder, mDestinationFolder);

                if (mSwitchManage.isChecked()) {
                    performeDeleteFolder();
                }
            }
        });

        /*
         * Initialize the loader
         */
        mLoaderManager = getLoaderManager();
        getLoaderManager().initLoader(DIRECTORY_SOURCE_SIZE_LOADER, null, mSizePathLoaderCallback);
        getLoaderManager().initLoader(DIRECTORY_DESTINATION_SIZE_LOADER, null, mSizePathLoaderCallback);

        getLoaderManager().initLoader(FILES_SOURCE_NUMBER_LOADER, null, mFilesNumberLoaderCallback);
        getLoaderManager().initLoader(FILES_DESTINATION_NUMBER_LOADER, null, mFilesNumberLoaderCallback);

        getLoaderManager().initLoader(COPY_FOLDER_LOADER, null, mCopyFolderLoaderCallback);
//        getLoaderManager().initLoader(DELETE_FOLDER_LOADER, null, mDeleteFolderLoaderCallback);

        return rootView;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case PICKFILE_SOURCE_CODE_INTENT:
                    if (null != data && !data.getBooleanExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false)) {
                        Log.d(LOG_TAG, "EXTRA_ALLOW_MULTIPLE");

                        // The URI will now be something like content://PACKAGE-NAME/root/path/to/file
                        Uri uri = data.getData();
                        // A utility method is provided to transform the URI to a File object
                        File file = com.nononsenseapps.filepicker.Utils.getFileForUri(uri);
                        // If you want a URI which matches the old return value, you can do
                        Uri fileUri = Uri.fromFile(file);
                        // Do something with the result...

                        mSourceFolder = fileUri;
                        manageSourceResoult(fileUri);

                        String msg = "Selected dir" + fileUri.getPath();

                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                        Log.d(LOG_TAG, msg);
                    } else {
                        Log.d(LOG_TAG, "NOT EXTRA_ALLOW_MULTIPLE");
                        // Handling multiple results is one extra step
                        ArrayList<String> paths = data.getStringArrayListExtra(FilePickerActivity.EXTRA_PATHS);
                        if (paths != null) {
                            for (String path : paths) {
                                Uri uri = Uri.parse(path);
                                // Do something with the URI
                                File file = com.nononsenseapps.filepicker.Utils.getFileForUri(uri);
                                // If you want a URI which matches the old return value, you can do
                                Uri fileUri = Uri.fromFile(file);
                                // Do something with the result...
                                String msg = "Selected dir" + fileUri.getPath();
                                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                Log.d(LOG_TAG, msg);

                            }
                        }
                    }
                    break;
                case PICKFILE_DESTINATION_CODE_INTENT:
                    if (null != data && !data.getBooleanExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false)) {
                        Log.d(LOG_TAG, "EXTRA_ALLOW_MULTIPLE");

                        // The URI will now be something like content://PACKAGE-NAME/root/path/to/file
                        Uri uri = data.getData();
                        // A utility method is provided to transform the URI to a File object
                        File file = com.nononsenseapps.filepicker.Utils.getFileForUri(uri);
                        // If you want a URI which matches the old return value, you can do
                        Uri fileUri = Uri.fromFile(file);
                        // Do something with the result...

                        mDestinationFolder = fileUri;
                        manageDestinationResoult(fileUri);

                        String msg = "Selected dir" + fileUri.getPath();

                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                        Log.d(LOG_TAG, msg);
                    } else {
                        Log.d(LOG_TAG, "NOT EXTRA_ALLOW_MULTIPLE");
                        // Handling multiple results is one extra step
                        ArrayList<String> paths = data.getStringArrayListExtra(FilePickerActivity.EXTRA_PATHS);
                        if (paths != null) {
                            for (String path : paths) {
                                Uri uri = Uri.parse(path);
                                // Do something with the URI
                                File file = com.nononsenseapps.filepicker.Utils.getFileForUri(uri);
                                // If you want a URI which matches the old return value, you can do
                                Uri fileUri = Uri.fromFile(file);
                                // Do something with the result...
                                String msg = "Selected dir" + fileUri.getPath();
                                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                Log.d(LOG_TAG, msg);

                            }
                        }
                    }
                    break;
                default:
                    Log.d(LOG_TAG, "Intent sconosciuto");
            }
        }
    }

    private boolean performeCopyFolder(Uri sourceUri, Uri descUri) {

        Bundle pathBundle = new Bundle();
        pathBundle.putString(SOURCE_PATH_URI_EXTRA, mSourceFolder.toString());
        pathBundle.putString(DESTINATION_PATH_URI_EXTRA, mDestinationFolder.toString());

        Loader<Long> copyFolderLoader = mLoaderManager.getLoader(COPY_FOLDER_LOADER);
        if (copyFolderLoader == null) {
            mLoaderManager.initLoader(COPY_FOLDER_LOADER, pathBundle, mCopyFolderLoaderCallback);
        } else {
            mLoaderManager.restartLoader(COPY_FOLDER_LOADER, pathBundle, mCopyFolderLoaderCallback);
        }


        return true;
    }

    private boolean performeDeleteFolder() {

        return true;
    }

    private void manageDestinationResoult(Uri filePathUri) {
        mTextViewManageToFolder.setText(filePathUri.toString());

        Bundle filePathBundle = new Bundle();
        filePathBundle.putString(SEARCH_FILE_PATH_URI_EXTRA, filePathUri.toString());

        Loader<Long> searchSizeLoader = mLoaderManager.getLoader(DIRECTORY_DESTINATION_SIZE_LOADER);
        if (searchSizeLoader == null) {
            mLoaderManager.initLoader(DIRECTORY_DESTINATION_SIZE_LOADER, filePathBundle, mSizePathLoaderCallback);
        } else {
            mLoaderManager.restartLoader(DIRECTORY_DESTINATION_SIZE_LOADER, filePathBundle, mSizePathLoaderCallback);
        }

        Loader<Long> searchFilesLoader = mLoaderManager.getLoader(FILES_DESTINATION_NUMBER_LOADER);
        if (searchSizeLoader == null) {
            mLoaderManager.initLoader(FILES_DESTINATION_NUMBER_LOADER, filePathBundle, mFilesNumberLoaderCallback);
        } else {
            mLoaderManager.restartLoader(FILES_DESTINATION_NUMBER_LOADER, filePathBundle, mFilesNumberLoaderCallback);
        }

    }


    private void manageSourceResoult(Uri filePathUri) {
        mTextViewManageFromFolder.setText(filePathUri.toString());

        Bundle filePathBundle = new Bundle();
        filePathBundle.putString(SEARCH_FILE_PATH_URI_EXTRA, filePathUri.toString());

        Loader<Long> searchSizeLoader = mLoaderManager.getLoader(DIRECTORY_SOURCE_SIZE_LOADER);
        if (searchSizeLoader == null) {
            mLoaderManager.initLoader(DIRECTORY_SOURCE_SIZE_LOADER, filePathBundle, mSizePathLoaderCallback);
        } else {
            mLoaderManager.restartLoader(DIRECTORY_SOURCE_SIZE_LOADER, filePathBundle, mSizePathLoaderCallback);
        }

        Loader<Long> searchFilesLoader = mLoaderManager.getLoader(FILES_SOURCE_NUMBER_LOADER);
        if (searchSizeLoader == null) {
            mLoaderManager.initLoader(FILES_SOURCE_NUMBER_LOADER, filePathBundle, mFilesNumberLoaderCallback);
        } else {
            mLoaderManager.restartLoader(FILES_SOURCE_NUMBER_LOADER, filePathBundle, mFilesNumberLoaderCallback);
        }
    }


}
