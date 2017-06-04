package it.namron.sweeping.fragment;

import android.content.Context;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import it.namron.sweeping.adapter.DirectoryItemAdapter;
import it.namron.sweeping.concurrency.FolderSizeAsyncTask;
import it.namron.sweeping.dialog.AlertMainFolderDialog;
import it.namron.sweeping.dialog.AlertSelectedFolderDialog;
import it.namron.sweeping.dialog.ExternalStorageCompatibilityDialog;
import it.namron.sweeping.dialog.PerformCopyDialog;
import it.namron.sweeping.dialog.EnoughtFreeMemoryDialog;
import it.namron.sweeping.dialog.parameter.PerformCopyDialogFromParameter;
import it.namron.sweeping.dialog.parameter.PerformCopyDialogToParameter;
import it.namron.sweeping.dto.AppItemDTO;
import it.namron.sweeping.dto.DirectoryItemDTO;
import it.namron.sweeping.exception.CustomException;
import it.namron.sweeping.listener.FolderSizeAsyncTaskListener;
import it.namron.sweeping.sweeping.R;
import it.namron.sweeping.utils.AppUtils;
import it.namron.sweeping.utils.LogUtils;
import it.namron.sweeping.utils.StorageUtils;
import it.namron.sweeping.wrapper.WrappedDirectorySize;

import static it.namron.sweeping.constant.Constant.ALERT_MAIN_FOLDER_DIALOG_TAG;
import static it.namron.sweeping.constant.Constant.ALERT_SELECTED_FOLDER_DIALOG_TAG;
import static it.namron.sweeping.constant.Constant.APP_FACEBOOK;
import static it.namron.sweeping.constant.Constant.APP_MESSENGER;
import static it.namron.sweeping.constant.Constant.APP_NAME_BUNDLE;
import static it.namron.sweeping.constant.Constant.APP_SELECTED_BUNDLE;
import static it.namron.sweeping.constant.Constant.APP_TELEGRAM;
import static it.namron.sweeping.constant.Constant.APP_WHATSAPP;
import static it.namron.sweeping.constant.Constant.DIALOG_FRAGMENT;
import static it.namron.sweeping.constant.Constant.ENOUGHT_FREE_MEMORY_DIALOG_TAG;
import static it.namron.sweeping.constant.Constant.EXTERNAL_STORAGE_COMPATIBILITY_DIALOG_TAG;
import static it.namron.sweeping.constant.Constant.MB_MARGIN;
import static it.namron.sweeping.constant.Constant.NOT_INITIALIZED_FOLDER_SIZE;
import static it.namron.sweeping.constant.Constant.PERFORM_COPY_DIALOG_PARAMETER_BUNDLE;
import static it.namron.sweeping.constant.Constant.PERFORM_COPY_DIALOG_PARAMETER_TAG;

/**
 * Created by norman on 09/05/17.
 */

public class AppInfoFragment extends Fragment implements
        DirectoryItemAdapter.DirectoryAdapterListener,
        PerformCopyDialog.ResoultPerformCopyDialogListener,
        AlertMainFolderDialog.ResoultAlertMainFolderDialogListener,
        ExternalStorageCompatibilityDialog.ResoultExternalStorageCompatibilityDialogListener,
        FolderSizeAsyncTaskListener {

    private static final String LOG_TAG = AppInfoFragment.class.getSimpleName();

    private FolderSizeAsyncTaskListener mAppInfofragmentListener;
    FolderSizeAsyncTask mFolderSizeAsyncTask;
    ExecutorService threadPoolExecutor = Executors.newFixedThreadPool(2);
    private int mCurrWorking = -1;

    private long mTotalSize = 0;

    private boolean isCompatible;

    private long mSDFreeMemory;
    private String mSDPath;


    //References to RecyclerView and Adapter to reset the list to its
    //"pretty" state when the reset menu item is clicked.
    private DirectoryItemAdapter mDirectoryAdapter;
    private RecyclerView mRecyclerView;

    private AppItemDTO mAppItem;

    private List<DirectoryItemDTO> mDirectoryListModels = new ArrayList<>();

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

    /**
     * This method is used to notify after a doInBackground that implement
     * FolderSizeAsyncTask has finished.
     */
    @Override
    public void notifyOnFolderSizeResoult(Long result, int index, String senderCode) {
        Log.d(LOG_TAG, "notifyOnFolderSizeResoult: " + result);
        mDirectoryAdapter.updateSize(result, index);
    }

    @Override
    public void notifyOnFolderSizeProgress(Integer progress, String senderCode) {

    }

    private LoaderManager.LoaderCallbacks<List<DirectoryItemDTO>> mAppInfoFolderLoaderCallback = new LoaderManager.LoaderCallbacks<List<DirectoryItemDTO>>() {

        @Override
        public Loader<List<DirectoryItemDTO>> onCreateLoader(int loaderId, Bundle args) {
            switch (loaderId) {
                case ID_APP_INFO_FOLDER_LOADER:
//                    return new FolderSizeAsyncTask(getContext());
                    return new AsyncTaskLoader<List<DirectoryItemDTO>>(getContext()) {

                        List<DirectoryItemDTO> mResponce;
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
                        public void deliverResult(List<DirectoryItemDTO> data) {
                            mResponce = data;
                            super.deliverResult(data);
                        }

                        @Override
                        public List<DirectoryItemDTO> loadInBackground() {

                            mDirectoryListModels.clear();
                            DirectoryItemDTO dirItem;
                            List<String> appDirList = null;

                            switch (mAppName) {
                                case APP_WHATSAPP:
                                    appDirList = AppUtils.listOfWhatsAppDirectory();
                                    break;
                                case APP_TELEGRAM:
                                    appDirList = AppUtils.listOfTelegramAppDirectory();
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
                                    dirItem = new DirectoryItemDTO();
                                    dirItem.setPath(dir);
                                    dirItem.setName(folder);
                                    dirItem.setSelected(true);
                                    dirItem.setSizeByte(NOT_INITIALIZED_FOLDER_SIZE);
                                    dirItem.setSizeString(WrappedDirectorySize.size(dirItem.getSizeByte()));

                                    mDirectoryListModels.add(dirItem);

                                    mCurrWorking++;
                                    mFolderSizeAsyncTask = new FolderSizeAsyncTask(getActivity(),
                                            mAppInfofragmentListener,
                                            mCurrWorking);
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
        public void onLoadFinished(Loader<List<DirectoryItemDTO>> loader, List<DirectoryItemDTO> data) {
            if (null == data) {
                LogUtils.LOGD_N(LOG_TAG, "data non deve essere nullo null");
                //todo gestire il caso
//                Toast.makeText(getActivity(), log, Toast.LENGTH_SHORT).show();
            } else {
                mDirectoryAdapter.populateDirectoryItem(data);
            }
        }

        @Override
        public void onLoaderReset(Loader<List<DirectoryItemDTO>> loader) {
            switch (loader.getId()) {
                case ID_APP_INFO_FOLDER_LOADER:
                    mDirectoryAdapter.populateDirectoryItem(null);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(LOG_TAG, "onAttach");
        try {
            mAppInfofragmentListener = (FolderSizeAsyncTaskListener) this;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement FolderSizeAsyncTaskListener");
        }
    }


    private boolean isSelected(List<DirectoryItemDTO> mDirectoryListModels) {
        for (DirectoryItemDTO directory : mDirectoryListModels) {
            if (directory.isSelected())
                return true;
        }
        return false;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!AppUtils.isExternalStorageCompatible())
            isCompatible = false;
        else
            isCompatible = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (isCompatible) {
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
        } else {
            LogUtils.LOGD_N(LOG_TAG, "Device non compatibile!");
            ExternalStorageCompatibilityDialog dialog = new ExternalStorageCompatibilityDialog();

            dialog.setTargetFragment(AppInfoFragment.this, DIALOG_FRAGMENT);
            dialog.show(getFragmentManager(), EXTERNAL_STORAGE_COMPATIBILITY_DIALOG_TAG);

            return null;
        }
    }

    /**
     * This method is used to notify from onBindViewHolder that implement
     * DirectoryAdapterListener has clicked.
     */
    @Override
    public void onIconDirectoryClicked(int position) {
        DirectoryItemDTO directoryItem = mDirectoryListModels.get(position);
        directoryItem.setSelected(!directoryItem.isSelected());
        mDirectoryListModels.set(position, directoryItem);
        mDirectoryAdapter.notifyDataSetChanged();


//        if (mToast != null) {
//            mToast.cancel();
//        }
//        String toastMessage = "Item #" + position + " clicked.";
//        mToast = Toast.makeText(this.getContext(), toastMessage, Toast.LENGTH_LONG);
//
//        mToast.show();
    }

    /**
     * This method is used to notify after a sendFeedback buttn that implement
     * ExternalStorageCompatibilityDialog has clicked.
     */
    @Override
    public void onSendFeedbackExternalStorageCompatibilityDialog(boolean resoult) {
        if (resoult)
            Toast.makeText(this.getContext(), "inviare feedbak", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this.getContext(), "non inviare", Toast.LENGTH_SHORT).show();

        getActivity().finish();
    }

    /**
     * This method is used to notify after a onClick positive buttn that implement
     * PerformCopyDialog has clicked.
     */
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

            if (isEnoughtFreeMemory(mDirectoryListModels) == true) {
                copySelectedFolder();
            }


//            Toast.makeText(this.getContext(), "Crea cartella ed inizia la procedura di copia", Toast.LENGTH_LONG).show();
        } else {
            AlertMainFolderDialog dialog = new AlertMainFolderDialog();
            dialog.setTargetFragment(AppInfoFragment.this, DIALOG_FRAGMENT);
            dialog.show(getFragmentManager(), ALERT_MAIN_FOLDER_DIALOG_TAG);
        }
    }

    private boolean isMainFolderJustPresent(String folder) {

        //String removibleSDPath = StorageUtils.getStorageDirectories(getContext());
        String removibleSDPath = getPathOfRemovibleSDWithMoreFreeMemory(folder);

        File baseDirectory = new File(removibleSDPath, folder);
        if (baseDirectory.exists() && baseDirectory.isDirectory()) {
            return true;
        } else {
            return false;
        }


//            String[] storageDirectories = StorageUtils.getStorageDirectories(getContext());
//
//
//            String app_root_dir = Environment.getExternalStorageDirectory().getPath();
//
//
//            File[] externalFilesDirs = ContextCompat.getExternalFilesDirs(getActivity(), null);
//            File[] externalCacheDirs = ContextCompat.getExternalCacheDirs(getActivity());
    }

    private String getPathOfRemovibleSDWithMoreFreeMemory(String folder) {
        String[] removibleSDPath = StorageUtils.getRemovibleSDStorageDirectory(getContext());

        if (removibleSDPath != null && removibleSDPath.length > 0) {
            String rootSD = removibleSDPath[0];
            long freeSpace;

            for (int n = 0; removibleSDPath.length - 1 > n; n++) {
                rootSD = removibleSDPath[n + 1];
                if (StorageUtils.getFreeMemorySize(removibleSDPath[n]) > StorageUtils.getFreeMemorySize(removibleSDPath[n + 1])) {
                    rootSD = removibleSDPath[n];
                }
            }

            mSDFreeMemory = StorageUtils.getFreeMemorySize(rootSD);
            mSDPath = rootSD;
            return rootSD;
        }
        //todo vedere meglio
        LogUtils.LOGD_N(LOG_TAG, "removible SD path error!");
        throw new CustomException("Errore inaspettato");
    }

    @Override
    public void onContinueAlertMainFolderDialog(boolean resoult) {
        mPerformeCopyDialog.dismiss();

        isEnoughtFreeMemory(mDirectoryListModels);
        if (isEnoughtFreeMemory(mDirectoryListModels) == true) {
            copySelectedFolder();
        }
    }

    private void copySelectedFolder() {
        Toast.makeText(this.getContext(), "Inizia la procedura di copia", Toast.LENGTH_LONG).show();

    }

    private boolean isEnoughtFreeMemory(List<DirectoryItemDTO> mDirectoryListModels) {
//        mDirectoryListModels contiene tutte le informazioni

        if (mDirectoryListModels != null) {
            mTotalSize = 0;
            for (DirectoryItemDTO directory : mDirectoryListModels) {
                if (directory.isSelected()) {
                    mTotalSize += directory.getSizeByte();
                }
            }
            //check if there is enought free memory
            if (mTotalSize + MB_MARGIN > mSDFreeMemory) {
                EnoughtFreeMemoryDialog dialog = new EnoughtFreeMemoryDialog();
                dialog.show(getFragmentManager(), ENOUGHT_FREE_MEMORY_DIALOG_TAG);
                return false;
            } else {
                return true;
            }
        }
        throw new CustomException();
    }

}





















