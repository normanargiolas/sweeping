package it.namron.sweeping.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import it.namron.sweeping.concurrency.FolderSizeAsyncTask;
import it.namron.sweeping.dialog.AlertMainFolderDialog;
import it.namron.sweeping.dialog.AlertSelectedFolderDialog;
import it.namron.sweeping.listener.FolderSizeAsyncTaskListener;
import it.namron.sweeping.utils.ExternalStorage;
import it.namron.sweeping.utils.Folder;
import it.namron.sweeping.utils.TelegramApp;
import it.namron.sweeping.utils.WhatsApp;
import it.namron.sweeping.adapter.DirectoryItemAdapter;
import it.namron.sweeping.dialog.PerformCopyDialog;
import it.namron.sweeping.dialog.parameter.PerformCopyDialogFromParameter;
import it.namron.sweeping.dialog.parameter.PerformCopyDialogToParameter;
import it.namron.sweeping.model.AppItemModel;
import it.namron.sweeping.model.DirectoryItemModel;
import it.namron.sweeping.sweeping.R;

import static it.namron.sweeping.utils.Constant.ALERT_MAIN_FOLDER_DIALOG_TAG;
import static it.namron.sweeping.utils.Constant.ALERT_SELECTED_FOLDER_DIALOG_TAG;
import static it.namron.sweeping.utils.Constant.APP_FACEBOOK;
import static it.namron.sweeping.utils.Constant.APP_MESSENGER;
import static it.namron.sweeping.utils.Constant.APP_NAME_BUNDLE;
import static it.namron.sweeping.utils.Constant.APP_SELECTED_BUNDLE;
import static it.namron.sweeping.utils.Constant.APP_TELEGRAM;
import static it.namron.sweeping.utils.Constant.APP_WHATSAPP;
import static it.namron.sweeping.utils.Constant.DIALOG_FRAGMENT;
import static it.namron.sweeping.utils.Constant.PERFORM_COPY_DIALOG_PARAMETER_BUNDLE;
import static it.namron.sweeping.utils.Constant.PERFORM_COPY_DIALOG_PARAMETER_TAG;

/**
 * Created by norman on 09/05/17.
 */

public class AppInfoFragment extends Fragment implements
        DirectoryItemAdapter.DirectoryAdapterListener,
        PerformCopyDialog.ResoultPerformCopyDialogListener,
        AlertMainFolderDialog.ResoultAlertMainFolderDialogListener,
        FolderSizeAsyncTaskListener {

    private static final String LOG_TAG = AppInfoFragment.class.getSimpleName();

    private FolderSizeAsyncTaskListener mAppInfofragmentListener;
    FolderSizeAsyncTask mFolderSizeAsyncTask;
    ExecutorService threadPoolExecutor = Executors.newFixedThreadPool(2);
    private int mCurrWorking = 0;


    //References to RecyclerView and Adapter to reset the list to its
    //"pretty" state when the reset menu item is clicked.
    private DirectoryItemAdapter mDirectoryAdapter;
    private RecyclerView mRecyclerView;

    private AppItemModel mAppItem;

    private List<DirectoryItemModel> mDirectoryListModels = new ArrayList<>();

    /*
     * This number will uniquely identify our Loader
     */
    private static final int ID_APP_INFO_FOLDER_LOADER = 20;

    private static final int ID_WHATSAPP_FOLDER_LOADER = 30;

    private LoaderManager mLoaderManager;

    private Toast mToast;
    PerformCopyDialog mPerformeCopyDialog;

    public AppInfoFragment() {

    }

    @Override
    public void notifyOnFolderSizeResoult(Integer result, String senderCode) {
        Log.d(LOG_TAG, "notifyOnFolderSizeResoult: " + result);

    }

    @Override
    public void notifyOnFolderSizeProgress(Integer progress, String senderCode) {

    }

    private LoaderManager.LoaderCallbacks<List<DirectoryItemModel>> mAppInfoFolderLoaderCallback = new LoaderManager.LoaderCallbacks<List<DirectoryItemModel>>() {

        @Override
        public Loader<List<DirectoryItemModel>> onCreateLoader(int loaderId, Bundle args) {
            switch (loaderId) {
                case ID_APP_INFO_FOLDER_LOADER:
//                    return new FolderSizeAsyncTask(getContext());
                    return new AsyncTaskLoader<List<DirectoryItemModel>>(getContext()) {

                        List<DirectoryItemModel> mResponce;
                        String mAppName;


                        @Override
                        protected void onStartLoading() {
                            if (args != null && args.getString(APP_NAME_BUNDLE) != null) {
                                mAppName = args.getString(APP_NAME_BUNDLE).toLowerCase();
                            } else {
                                return;
                            }

                            if (mResponce != null) {
                                deliverResult(mResponce);
                            } else {
                                forceLoad();
                            }
                        }

                        @Override
                        public void deliverResult(List<DirectoryItemModel> data) {
                            mResponce = data;
                            super.deliverResult(data);
                        }

                        @Override
                        public List<DirectoryItemModel> loadInBackground() {

                            mDirectoryListModels.clear();
                            DirectoryItemModel msg;
                            List<String> appDirList = null;

                            switch (mAppName) {
                                case APP_WHATSAPP:
                                    appDirList = WhatsApp.listOfWhatsAppDirectory();
                                    break;
                                case APP_TELEGRAM:
                                    appDirList = TelegramApp.listOfTelegramAppDirectory();
                                    break;
                                case APP_FACEBOOK:
                                    break;
                                case APP_MESSENGER:
                                    break;
                                default:
                                    Log.d(LOG_TAG, "Start new Fragment-->" + mAppName);
                                    throw new RuntimeException("App non valida: " + mAppName);
                            }

                            if (appDirList != null) {
                                for (String dir : appDirList) {
                                    Uri dirUri = Uri.parse(dir);
                                    String folder = dirUri.getLastPathSegment();
                                    msg = new DirectoryItemModel();
                                    msg.setPath(dir);
                                    msg.setFolderName(folder);
                                    msg.setSelected(true);
                                    mDirectoryListModels.add(msg);

                                    mCurrWorking++;
                                    mFolderSizeAsyncTask = new FolderSizeAsyncTask(getActivity(), mAppInfofragmentListener);
                                    mFolderSizeAsyncTask.executeOnExecutor(threadPoolExecutor, dirUri);
                                }
                            }

                            return mDirectoryListModels;
                        }
                    };
                default:
                    throw new RuntimeException("Loader Not Implemented: " + loaderId);
            }
        }


        @Override
        public void onLoadFinished(Loader<List<DirectoryItemModel>> loader, List<DirectoryItemModel> data) {
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
        public void onLoaderReset(Loader<List<DirectoryItemModel>> loader) {
            switch (loader.getId()) {
                case ID_APP_INFO_FOLDER_LOADER:
                    mDirectoryAdapter.swapFolder(null);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(LOG_TAG, "onAttach");
        try {
            mAppInfofragmentListener = (FolderSizeAsyncTaskListener) this;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement FolderSizeAsyncTaskListener");
        }
    }



    private boolean isSelected(List<DirectoryItemModel> mDirectoryListModels) {
        for (DirectoryItemModel directory : mDirectoryListModels) {
            if (directory.isSelected())
                return true;
        }
        return false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_app_info, container, false);

        FragmentManager fm = getFragmentManager();

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAppItem != null && mDirectoryListModels != null) {
                    if (isSelected(mDirectoryListModels) == true) {
                        mPerformeCopyDialog = new PerformCopyDialog();
                        mPerformeCopyDialog.setTargetFragment(AppInfoFragment.this, DIALOG_FRAGMENT);

                        Bundle bundleForDialog = createPerformCopyBundle();
                        mPerformeCopyDialog.setArguments(bundleForDialog);
                        // Show Alert DialogFragment
                        mPerformeCopyDialog.show(fm, PERFORM_COPY_DIALOG_PARAMETER_TAG);
                    } else {
                        AlertSelectedFolderDialog dialog = new AlertSelectedFolderDialog();
                        dialog.show(getFragmentManager(), ALERT_SELECTED_FOLDER_DIALOG_TAG);
                    }


                    //Mostrare un menu dialog di conferma
                    //Recuperare i dati
                    //Avviare la procedura di copia
                    //Eventualmente cancellare gli originali


                }


//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }

            private Bundle createPerformCopyBundle() {
                PerformCopyDialogToParameter param = new PerformCopyDialogToParameter();
                param.setIcon(mAppItem.getAppIcon());
                param.setTitle(mAppItem.getAppName());
                param.setFolder(mAppItem.getAppName().toLowerCase());
                Bundle bundleForDialog = new Bundle();
                bundleForDialog.putParcelable(PERFORM_COPY_DIALOG_PARAMETER_BUNDLE, param);
                return bundleForDialog;
            }

        });

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_numbers);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        //The DirectoryItemAdapter is responsible for displaying each item in the list.
        mDirectoryAdapter = new DirectoryItemAdapter(getContext(), this, mDirectoryListModels);
        mRecyclerView.setAdapter(mDirectoryAdapter);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mAppItem = bundle.getParcelable(APP_SELECTED_BUNDLE);
            getActivity().setTitle(mAppItem.getAppName());

            if (mAppItem != null) {
                /*
                * Initialize the loader
                */
                mLoaderManager = getLoaderManager();
                Bundle appInfoBundle = new Bundle();
                appInfoBundle.putString(APP_NAME_BUNDLE, mAppItem.getAppName());

                getLoaderManager().initLoader(ID_APP_INFO_FOLDER_LOADER, appInfoBundle, mAppInfoFolderLoaderCallback);
            }
        }

        return rootView;
    }

    @Override
    public void onIconDirectoryClicked(int position) {
        DirectoryItemModel directoryItem = mDirectoryListModels.get(position);
        directoryItem.setSelected(!directoryItem.isSelected());
        mDirectoryListModels.set(position, directoryItem);
        mDirectoryAdapter.notifyDataSetChanged();


        if (mToast != null) {
            mToast.cancel();
        }
        String toastMessage = "Item #" + position + " clicked.";
        mToast = Toast.makeText(this.getContext(), toastMessage, Toast.LENGTH_LONG);

        mToast.show();
    }


    @Override
    public void onResoultPerformCopyDialog(@NonNull PerformCopyDialogFromParameter parameter) {

//        if (mToast != null) {
//            mToast.cancel();
//        }
//        String toastMessage = "Folder:" + parameter.getFolder() + " Mantieni originali: " + parameter.getOriginal().toString();
//        mToast = Toast.makeText(this.getContext(), toastMessage, Toast.LENGTH_LONG);
//        mToast.show();


        if (isMainFolderJustPresent(parameter.getFolder()) == false) {
            mPerformeCopyDialog.dismiss();

            searchFolderToCopy(mDirectoryListModels);
            //inizia la porcedura di copia

//            Toast.makeText(this.getContext(), "Crea cartella ed inizia la procedura di copia", Toast.LENGTH_LONG).show();
        } else {
            AlertMainFolderDialog dialog = new AlertMainFolderDialog();
            dialog.setTargetFragment(AppInfoFragment.this, DIALOG_FRAGMENT);
            dialog.show(getFragmentManager(), ALERT_MAIN_FOLDER_DIALOG_TAG);
        }
    }

    private boolean isMainFolderJustPresent(String folder) {
        //todo da implementare
        return true;
    }

    @Override
    public void onContinueAlertMainFolderDialog(boolean resoult) {
        mPerformeCopyDialog.dismiss();

        searchFolderToCopy(mDirectoryListModels);
//        Toast.makeText(this.getContext(), "Cartella già presente ma inizia la procedura di copia", Toast.LENGTH_LONG).show();
    }

    private void searchFolderToCopy(List<DirectoryItemModel> mDirectoryListModels) {
//        mDirectoryListModels contiene tutte le informazioni


        List<String> all = ExternalStorage.getAllStorageLocations();
        String sd = ExternalStorage.getSDStorageLocation();
        Map<Integer, String> list = ExternalStorage.listEnvironmentVariableStoreSDCardRootDirectory();


        if (mDirectoryListModels != null) {
            List<Folder> foldersToCopy = new ArrayList<>();

            for (DirectoryItemModel directory : mDirectoryListModels) {
                if (directory.isSelected()) {


                    Folder folder = getFoldersInfo(foldersToCopy);
                    if (folder != null) {
                        folder.setName(directory.getFolderName());
                        foldersToCopy.add(folder);
                    }
                }
            }


        }
    }

    private Folder getFoldersInfo(List<Folder> foldersToCopy) {
        return null;
    }


}





















