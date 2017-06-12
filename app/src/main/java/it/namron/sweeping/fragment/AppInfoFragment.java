package it.namron.sweeping.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import it.namron.sweeping.adapter.DirectoryItemAdapter;
import it.namron.sweeping.concurrency.FolderSizeAsyncTask;
import it.namron.sweeping.concurrency.PerformeCopyLoader;
import it.namron.sweeping.dialog.DialogHandler;
import it.namron.sweeping.dto.ToPerformCopyDTO;
import it.namron.sweeping.dto.FromPerformCopyDTO;
import it.namron.sweeping.dto.AppItemDTO;
import it.namron.sweeping.dto.DirectoryItemDTO;
import it.namron.sweeping.exception.CustomException;
import it.namron.sweeping.listener.FolderSizeAsyncTaskListener;
import it.namron.sweeping.listener.PerformeCopyListener;
import it.namron.sweeping.sweeping.R;
import it.namron.sweeping.utils.AppUtils;
import it.namron.sweeping.utils.LogUtils;
import it.namron.sweeping.utils.StorageUtils;
import it.namron.sweeping.wrapper.WrappedDirectorySize;

import static it.namron.sweeping.constant.Constant.ALERT_MAIN_FOLDER_DIALOG_TAG;
import static it.namron.sweeping.constant.Constant.APP_FACEBOOK;
import static it.namron.sweeping.constant.Constant.APP_MESSENGER;
import static it.namron.sweeping.constant.Constant.APP_NAME_BUNDLE;
import static it.namron.sweeping.constant.Constant.APP_SELECTED_BUNDLE;
import static it.namron.sweeping.constant.Constant.APP_TELEGRAM;
import static it.namron.sweeping.constant.Constant.APP_WHATSAPP;
import static it.namron.sweeping.constant.Constant.DIALOG_FRAGMENT;
import static it.namron.sweeping.constant.Constant.DTO_PREPARE_COPY_BUNDLE;
import static it.namron.sweeping.constant.Constant.EXTERNAL_STORAGE_COMPATIBILITY_DIALOG_TAG;
import static it.namron.sweeping.constant.Constant.ID_APP_INFO_FOLDER_LOADER;
import static it.namron.sweeping.constant.Constant.ID_PREPARE_COPY_LOADER;
import static it.namron.sweeping.constant.Constant.MB_MARGIN;
import static it.namron.sweeping.constant.Constant.MSG_PERFORME_COPY_POST_BACKGROUND;
import static it.namron.sweeping.constant.Constant.MSG_PERFORME_COPY_PRE_BACKGROUND;
import static it.namron.sweeping.constant.Constant.NOT_INITIALIZED_FOLDER_SIZE;
import static it.namron.sweeping.constant.Constant.PERFORM_COPY_DIALOG_PARAMETER_BUNDLE;
import static it.namron.sweeping.constant.Constant.PERFORM_COPY_DIALOG_PARAMETER_TAG;
import static it.namron.sweeping.constant.Constant.DIR_PREPARE_COPY_BUNDLE;
import static it.namron.sweeping.constant.Constant.SD_PREPARE_COPY_BUNDLE;

/**
 * Created by norman on 09/05/17.
 */

public class AppInfoFragment extends Fragment implements
        DirectoryItemAdapter.DirectoryAdapterListener,
        DialogHandler.DialogHandlerListener,
        FolderSizeAsyncTaskListener,
        PerformeCopyListener {

    private static final String LOG_TAG = AppInfoFragment.class.getSimpleName();

    private DialogHandler mDialogHandler;

    private PerformeCopyListener performeCopyListener;
    private FolderSizeAsyncTaskListener mAppInfofragmentListener;
    FolderSizeAsyncTask mFolderSizeAsyncTask;
    ExecutorService threadPoolExecutor = Executors.newFixedThreadPool(2);
    private int mCurrWorking = -1;

    private long mTotalSize = 0;

    private boolean isCompatible;

    private long mSDFreeMemory;
    private String mSDPath;

    private ProgressBar mProgressBar;

    private FromPerformCopyDTO mPerformCopyDTO;

    //References to RecyclerView and Adapter to reset the list to its
    //"pretty" state when the reset menu item is clicked.
    private DirectoryItemAdapter mDirectoryAdapter;
    private RecyclerView mRecyclerView;

    private AppItemDTO mAppItem;

    private List<DirectoryItemDTO> mDirectoryListModels = new ArrayList<>();
    private List<DirectoryItemDTO> mSelectedDirectoryList = new ArrayList<>();

    private LoaderManager mLoaderManager;

    private Toast mToast;


    static class UIHandler extends Handler {
        WeakReference<AppInfoFragment> mParent;

        public UIHandler(WeakReference<AppInfoFragment> parent) {
            mParent = parent;
        }

        @Override
        public void handleMessage(Message msg) {
            AppInfoFragment parent = mParent.get();
            if (null != parent) {
                switch (msg.what) {
                    case MSG_PERFORME_COPY_PRE_BACKGROUND: {
                        parent.mProgressBar.setVisibility(View.VISIBLE);
                        parent.mProgressBar.bringToFront();
                        break;
                    }
                    case MSG_PERFORME_COPY_POST_BACKGROUND: {
                        parent.mProgressBar.setVisibility(View.GONE);
                        break;
                    }
                }
            }
        }
    }

    Handler handler;


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

    /**
     * This method is used to notify after a doInBackground that implement
     * PerformeCopyListener has finished.
     */
    @Override
    public void notifyOnFolderCopied(String folder, int index, String senderCode) {
        Toast.makeText(getActivity(), "copia di " + folder, Toast.LENGTH_SHORT).show();
    }

    /**
     * This method is used to notify an error during the copy of files in the object that implement
     * PerformeCopyListener has finished.
     */
    @Override
    public void notifyOnErrorOccurred(String folder, int index, String senderCode) {
        Toast.makeText(getActivity(), "errore nella copia di " + folder, Toast.LENGTH_SHORT).show();
    }

    /**
     * This method is used to notify OnPostBackground during the copy of files in the object that implement
     * PerformeCopyListener has finished.
     */
    @Override
    public void notifyOnPostBackground(String senderCode) {
        if (handler != null) {
            Message msg = handler.obtainMessage(MSG_PERFORME_COPY_POST_BACKGROUND);
            handler.sendMessage(msg);
        }
    }

    /**
     * This method is used to notify OnPreBackground during the copy of files in the object that implement
     * PerformeCopyListener has finished.
     */
    @Override
    public void notifyOnPreBackground(String senderCode) {
        if (handler != null) {
            Message msg = handler.obtainMessage(MSG_PERFORME_COPY_PRE_BACKGROUND);
            handler.sendMessage(msg);
        }
    }

    private LoaderManager.LoaderCallbacks<Boolean> mPerformeCopyLoader = new LoaderManager.LoaderCallbacks<Boolean>() {

        @Override
        public Loader<Boolean> onCreateLoader(int loaderId, Bundle args) {
            switch (loaderId) {
                case ID_PREPARE_COPY_LOADER:
                    // This is called when a new Loader needs to be created.  This
                    // sample only has one Loader with no arguments, so it is simple.
                    if (args != null &&
                            args.getStringArrayList(DIR_PREPARE_COPY_BUNDLE) != null &&
                            args.getString(SD_PREPARE_COPY_BUNDLE) != null &&
                            args.getParcelable(DTO_PREPARE_COPY_BUNDLE) != null) {

                        ArrayList<String> dirs = args.getStringArrayList(DIR_PREPARE_COPY_BUNDLE);
                        String sd = args.getString(SD_PREPARE_COPY_BUNDLE);
                        FromPerformCopyDTO dto = args.getParcelable(DTO_PREPARE_COPY_BUNDLE);

                        return new PerformeCopyLoader(getContext(), dirs, sd, dto, performeCopyListener);
                    }

                default:
                    throw new RuntimeException("Loader Not Implemented: " + loaderId);
            }
        }

        @Override
        public void onLoadFinished(Loader<Boolean> loader, Boolean data) {
            switch (loader.getId()) {
                case ID_PREPARE_COPY_LOADER:
                    LogUtils.LOGD_N(LOG_TAG, "onLoadFinished");
                    if (null == data) {
                        LogUtils.LOGD_N(LOG_TAG, "data non deve essere nullo null");
                        //todo gestire il caso
//                Toast.makeText(getActivity(), log, Toast.LENGTH_SHORT).show();
                    } else {
                        if (data)
                            Toast.makeText(getActivity(), "copia terminata con successo", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getActivity(), "copia terminata con errori", Toast.LENGTH_SHORT).show();
                        //todo inviare il feedback errorlog
                    }
            }

        }

        @Override
        public void onLoaderReset(Loader<Boolean> loader) {
            LogUtils.LOGD_N(LOG_TAG, "onLoaderReset");
            //todo gestire il caso

        }
    };


    private LoaderManager.LoaderCallbacks<List<DirectoryItemDTO>> mAppInfoFolderLoader = new LoaderManager.LoaderCallbacks<List<DirectoryItemDTO>>() {

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
                                    LogUtils.LOGD_N(LOG_TAG, "Start new Fragment-->" + mAppName);
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

        handler = new UIHandler(new WeakReference<AppInfoFragment>(this));

        try {
            mAppInfofragmentListener = (FolderSizeAsyncTaskListener) this;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement FolderSizeAsyncTaskListener");
        }
        try {
            performeCopyListener = (PerformeCopyListener) this;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement PerformeCopyListener");
        }
        //todo aggiungere le restanti interfacce
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

        mDialogHandler = new DialogHandler();
        mDialogHandler.setTargetFragment(AppInfoFragment.this, DIALOG_FRAGMENT);
        mDialogHandler.initialize(getContext(), getFragmentManager());

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

            mProgressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
            mProgressBar.setVisibility(View.GONE);

            FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mAppItem != null && mDirectoryListModels != null) {
                        if (isSelected(mDirectoryListModels) == true) {
                            Bundle bundleForDialog = createPerformCopyBundle();
                            mDialogHandler.showPerformCopyDialog(bundleForDialog);
                        } else {
                            mDialogHandler.showAlertSelectedFolderDialog();
                        }
                    }
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                }

                private Bundle createPerformCopyBundle() {
                    ToPerformCopyDTO param = new ToPerformCopyDTO();
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
                    /**
                     * Initialize the loader
                     **/
                    mLoaderManager = getLoaderManager();
                    Bundle appInfoBundle = new Bundle();
                    appInfoBundle.putString(APP_NAME_BUNDLE, mAppItem.getAppName());

                    getLoaderManager().initLoader(ID_APP_INFO_FOLDER_LOADER, appInfoBundle, mAppInfoFolderLoader);
                }
            }
            return rootView;
        } else {
            LogUtils.LOGD_N(LOG_TAG, "Device non compatibile!");
            mDialogHandler.showExternalStorageCompatibility();
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
//            String app_root_dir = Environment.getExternalStorageDirectory().getPath();
//
//            File[] externalFilesDirs = ContextCompat.getExternalFilesDirs(getActivity(), null);
//            File[] externalCacheDirs = ContextCompat.getExternalCacheDirs(getActivity());
    }

    private String getPathOfRemovibleSDWithMoreFreeMemory(String folder) {
        String[] removibleSDPath = StorageUtils.getRemovibleSDStorageDirectory(getContext());

        removibleSDPath = new String[1];
        removibleSDPath[0] = Environment.getExternalStorageDirectory().getPath(); //todo only for test on nexus

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

    private boolean isEnoughtFreeMemory(List<DirectoryItemDTO> directoryListModels) {
        //  directoryListModels = mDirectoryListModels
        // contiene tutte le informazioni riguardo le directory da copiare

        if (directoryListModels != null) {
            mSelectedDirectoryList.clear();
            mTotalSize = 0;
            for (DirectoryItemDTO directory : directoryListModels) {
                if (directory.isSelected()) {
                    mSelectedDirectoryList.add(directory);
                    mTotalSize += directory.getSizeByte();
                }
            }
            //check if there is enought free memory
//            if (mTotalSize + MB_MARGIN > 104857600) {  //todo for test
            if (mTotalSize + MB_MARGIN > mSDFreeMemory) {
                mDialogHandler.showEnoughtFreeMemory();
                return false;
            } else {
                return true;
            }
        }
        throw new CustomException();
    }

    /**
     * This method is used to notify a dialog resoult from DialogHandler that implement DialogHandlerListener
     *
     * @param tag     rappresent the dialog identifier
     * @param resoult the dialog resoult to cast
     */
    @Override
    public void onDialogResoult(Object resoult, String tag) {
        switch (tag) {
            case EXTERNAL_STORAGE_COMPATIBILITY_DIALOG_TAG:
                boolean res = (boolean) resoult;
                if (res)
                    Toast.makeText(this.getContext(), "inviare feedbak", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this.getContext(), "non inviare", Toast.LENGTH_SHORT).show();
                getActivity().finish();
                break;
            case PERFORM_COPY_DIALOG_PARAMETER_TAG:
                mPerformCopyDTO = (FromPerformCopyDTO) resoult;

                if (isMainFolderJustPresent(mPerformCopyDTO.getFolder()) == true)
                    mDialogHandler.showAlertMainFolder();
                else
                    prepareTheCopy();
                break;
            case ALERT_MAIN_FOLDER_DIALOG_TAG:
                /**
                 * "continue" buttn that implement ContinueAlertMainFolderDialog has clicked
                 **/
                prepareTheCopy();
                break;
            default:
                LogUtils.LOGD_N(LOG_TAG, "Alert Dialog sconosciuto");
                break;
        }
    }

    /**
     * If we haven't enought free memory, we waiting in AppInfoFragment directory list
     * At this point we have:
     * mTotalSize = total size of file to copy in byte
     * mSDPath = path of SD card
     * mSDFreeMemory = free memory of mSDPath
     * mDirectoryListModels = contains all directory nformation
     * mSelectedDirectoryList = contains all directory selected information, not be null at this point
     * mPerformCopyDTO = contains name of folder where to put the files and if delete files after the copy
     * mLoaderManager = deve essere diverso da null
     **/
    private void prepareTheCopy() {
        //todo lanciare un asynck task all'interno di un loader
        mDialogHandler.dismissPerformCopyDialog();
        if (isEnoughtFreeMemory(mDirectoryListModels) == true) {

            final ArrayList<String> dir = new ArrayList<>();
            for (DirectoryItemDTO s : mSelectedDirectoryList) {
                dir.add(s.getPath());
            }

            Bundle directoryBundle = new Bundle();
            directoryBundle.putStringArrayList(DIR_PREPARE_COPY_BUNDLE, dir);
            directoryBundle.putString(SD_PREPARE_COPY_BUNDLE, mSDPath);
            directoryBundle.putParcelable(DTO_PREPARE_COPY_BUNDLE, mPerformCopyDTO);

            Loader<Boolean> performeCopyLoader = mLoaderManager.getLoader(ID_PREPARE_COPY_LOADER);
            if (performeCopyLoader == null) {
                mLoaderManager.initLoader(ID_PREPARE_COPY_LOADER, directoryBundle, mPerformeCopyLoader);
            } else {
                mLoaderManager.restartLoader(ID_PREPARE_COPY_LOADER, directoryBundle, mPerformeCopyLoader);
            }

        }
    }


}





















