package it.namron.sweeping.sweeping;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nononsenseapps.filepicker.FilePickerActivity;
import com.nononsenseapps.filepicker.Utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Created by norman on 09/05/17.
 */

public class ManageFragment extends Fragment {

    public static final String LOG_TAG = "ManageFragment";
    private static final int PICKFILE_REQUEST_CODE = 1;

    public ManageFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_manage, container, false);

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                try {

//                    Utils.makeHepticFeedback(getActivity());

                    Intent selectDirectoyIntent = new Intent(getActivity(), FilePickerActivity.class);
                    selectDirectoyIntent.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
                    selectDirectoyIntent.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, true);
                    selectDirectoyIntent.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_DIR);
                    selectDirectoyIntent.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());
                    startActivityForResult(selectDirectoyIntent, PICKFILE_REQUEST_CODE);

                } catch (Exception e) {
                    Log.e(LOG_TAG, "exception", e);
                    e.printStackTrace();

                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                }


//                Uri selectedUri = Uri.parse(Environment.getExternalStorageDirectory() + "/");
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//                intent.setDataAndType(selectedUri, "resource/folder");
//                startActivityForResult(intent, PICKFILE_REQUEST_CODE);


            }
        });

        return rootView;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == PICKFILE_REQUEST_CODE) {
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

        }
    }

    private void manageResoult(Uri fileUri) {

        TextView textView = (TextView) getView().findViewById(R.id.manage_from_folder);
        textView.setText(fileUri.toString());

        //TODO implementare un loader per il calcolo
        long folder_size_bytes = FileUtils.sizeOfDirectory(new File(fileUri.getPath()));
        Log.d(LOG_TAG, String.valueOf(folder_size_bytes / 1024));


        //TODO implementare un loader per il calcolo
        int number_of_file = countFiles(new File(fileUri.getPath()));

        Log.d(LOG_TAG, String.valueOf(number_of_file));


    }

    public static int countFiles(File directory) {
        int count = 0;
        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                count += countFiles(file);
            }
            count++;
        }
        return count;
    }
}
