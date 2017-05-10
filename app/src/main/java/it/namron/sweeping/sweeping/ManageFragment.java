package it.namron.sweeping.sweeping;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.nononsenseapps.filepicker.FilePickerActivity;

import org.apache.commons.io.FileUtils;

import java.io.File;
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
     * This number will uniquely identify our Loader for get directory size
     */
    private static final int DIRECTORY_SIZE_LOADER = 20;
    /*
     * This number will uniquely identify our Loader for get directory size
     */
    private static final int FILES_NUMBER_LOADER = 21;

    /* A constant to save and restore the path file uri */
    private static final String SEARCH_FILE_PATH_URI_EXTRA = "filePathUri";

    public ManageFragment() {

    }

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
                TextView textView = (TextView) getView().findViewById(R.id.manage_from_size);
                textView.setText(String.valueOf(data / 1024));
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
                TextView textView = (TextView) getView().findViewById(R.id.manage_from_files);
                textView.setText(String.valueOf(data));
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
        /*
         * Initialize the loader
         */
        getLoaderManager().initLoader(DIRECTORY_SIZE_LOADER, null, mSizePathLoaderCallback);
        getLoaderManager().initLoader(FILES_NUMBER_LOADER, null, mFilesNumberLoaderCallback);

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

                        manageResoult(fileUri);

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

                        manageResoult(fileUri);

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

    private void manageResoult(Uri filePathUri) {

        TextView textView = (TextView) getView().findViewById(R.id.manage_from_folder);
        textView.setText(filePathUri.toString());

        LoaderManager loaderManager = getLoaderManager();
        Bundle filePathBundle = new Bundle();
        filePathBundle.putString(SEARCH_FILE_PATH_URI_EXTRA, filePathUri.toString());

        Loader<Long> searchSizeLoader = loaderManager.getLoader(DIRECTORY_SIZE_LOADER);
        if (searchSizeLoader == null) {
            loaderManager.initLoader(DIRECTORY_SIZE_LOADER, filePathBundle, mSizePathLoaderCallback);
        } else {
            loaderManager.restartLoader(DIRECTORY_SIZE_LOADER, filePathBundle, mSizePathLoaderCallback);
        }

        Loader<Long> searchFilesLoader = loaderManager.getLoader(FILES_NUMBER_LOADER);
        if (searchSizeLoader == null) {
            loaderManager.initLoader(FILES_NUMBER_LOADER, filePathBundle, mFilesNumberLoaderCallback);
        } else {
            loaderManager.restartLoader(FILES_NUMBER_LOADER, filePathBundle, mFilesNumberLoaderCallback);
        }
    }


}
