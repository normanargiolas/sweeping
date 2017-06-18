package it.namron.sweeping.fragment;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NotificationCompat;
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

import it.namron.sweeping.activity.AppInfoActivity;
import it.namron.sweeping.adapter.DirectoryItemAdapter;
import it.namron.sweeping.concurrency.FolderSizeAsyncTask;
import it.namron.sweeping.concurrency.PerformeCopyAsyncTask;
import it.namron.sweeping.dialog.DialogHandler;
import it.namron.sweeping.dto.AppItemDTO;
import it.namron.sweeping.dto.DirectoryItemDTO;
import it.namron.sweeping.dto.FromPerformCopyDTO;
import it.namron.sweeping.dto.ToPerformCopyDTO;
import it.namron.sweeping.exception.CustomException;
import it.namron.sweeping.listener.FolderSizeAsyncTaskListener;
import it.namron.sweeping.listener.PerformeCopyAsyncTaskListener;
import it.namron.sweeping.sweeping.R;
import it.namron.sweeping.utils.AppUtils;
import it.namron.sweeping.utils.LogUtils;
import it.namron.sweeping.utils.ResourceHashCode;
import it.namron.sweeping.utils.StorageUtils;
import it.namron.sweeping.wrapper.WrappedDirectorySize;

import static it.namron.sweeping.constant.Constant.ALERT_MAIN_FOLDER_DIALOG_TAG;
import static it.namron.sweeping.constant.Constant.APP_FACEBOOK;
import static it.namron.sweeping.constant.Constant.APP_MESSENGER;
import static it.namron.sweeping.constant.Constant.APP_NAME_BUNDLE;
import static it.namron.sweeping.constant.Constant.APP_SELECTED_BUNDLE;
import static it.namron.sweeping.constant.Constant.APP_TELEGRAM;
import static it.namron.sweeping.constant.Constant.APP_WHATSAPP;
import static it.namron.sweeping.constant.Constant.CURRENT_NOTIFICATION_ID_STATE;
import static it.namron.sweeping.constant.Constant.CURR_PERFORME_COPY_WORKING_STATE;
import static it.namron.sweeping.constant.Constant.DIALOG_FRAGMENT;
import static it.namron.sweeping.constant.Constant.DIRECTORY_LIST_MODELS_STATE;
import static it.namron.sweeping.constant.Constant.EXTERNAL_STORAGE_COMPATIBILITY_DIALOG_TAG;
import static it.namron.sweeping.constant.Constant.ID_APP_INFO_FOLDER_LOADER;
import static it.namron.sweeping.constant.Constant.MB_MARGIN;
import static it.namron.sweeping.constant.Constant.MSG_PERFORME_COPY_PRE_EXECUTE;
import static it.namron.sweeping.constant.Constant.MSG_PERFORME_COPY_RESOULT;
import static it.namron.sweeping.constant.Constant.NOT_INITIALIZED_FOLDER_SIZE;
import static it.namron.sweeping.constant.Constant.PERFORM_COPY_DIALOG_PARAMETER_BUNDLE;
import static it.namron.sweeping.constant.Constant.PERFORM_COPY_DIALOG_PARAMETER_TAG;

/**
 * Created by norman on 09/05/17.
 */

public class AppInfoFragment extends Fragment implements
        DirectoryItemAdapter.DirectoryAdapterListener,
        DialogHandler.DialogHandlerListener,
        FolderSizeAsyncTaskListener,
        PerformeCopyAsyncTaskListener {

    private static final String LOG_TAG = AppInfoFragment.class.getSimpleName();

    boolean isPause;

    private DialogHandler mDialogHandler;

    private FolderSizeAsyncTaskListener mFolderSizeAsyncTaskListener;
    private FolderSizeAsyncTask mFolderSizeAsyncTask;

    //    private PerformeCopyLoaderListener performeCopyListener;
    private PerformeCopyAsyncTask mPerformeCopyAsyncTask;
    private PerformeCopyAsyncTaskListener mPerformeCopyAsyncTaskListener;

    private ExecutorService mThreadPoolExecutor = Executors.newFixedThreadPool(2);
    private int mCurrFolderSizeWorking = -1;
    private int mCurrPerformeCopyWorking = -1;

    private long mTotalSize = 0;

    private boolean isCompatible;

    private long mSDFreeMemory;
    private String mSDPath;

    private ProgressBar mProgressBar;

    private FloatingActionButton mFab;

    private FromPerformCopyDTO mPerformCopyDTO;

    /**
     * Notification parameters
     **/
    private NotificationCompat.Builder mNotificationBuilder;
    private static NotificationManager mNotificationManager;
    private Bitmap mIcon;
    private String notificationTitle;
    private String notificationText;
    private int mCurrentNotificationID = 0;


    //References to RecyclerView and Adapter to reset the list to its
    //"pretty" state when the reset menu item is clicked.
    private DirectoryItemAdapter mDirectoryAdapter;
    private RecyclerView mRecyclerView;

    private AppItemDTO mAppItem;

    private ArrayList<DirectoryItemDTO> mDirectoryListDTO = new ArrayList<DirectoryItemDTO>();
    private ArrayList<DirectoryItemDTO> mSelectedDirectoryListDTO = new ArrayList<DirectoryItemDTO>();

    private LoaderManager mLoaderManager;

    private Toast mToast;

    static class UIHandler extends Handler {
        private WeakReference<AppInfoFragment> mParent;

        public UIHandler(WeakReference<AppInfoFragment> parent) {
            mParent = parent;
        }

        @Override
        public void handleMessage(Message msg) {
            AppInfoFragment parent = mParent.get();
            if (null != parent) {
                switch (msg.what) {
                    case MSG_PERFORME_COPY_PRE_EXECUTE: {
                        parent.mProgressBar.setVisibility(View.VISIBLE);
                        parent.mProgressBar.bringToFront();

                        parent.mFab.setVisibility(View.GONE);
                        break;
                    }
                    case MSG_PERFORME_COPY_RESOULT: {
                        parent.mProgressBar.setVisibility(View.GONE);

                        parent.mFab.setVisibility(View.VISIBLE);

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
        if (!isPause) {
            mDirectoryAdapter.updateSize(result, index);
        }
    }

    @Override
    public void notifyOnFolderSizeProgress(Integer progress, String senderCode) {

    }

    /**
     * This method is used to notify PROGRESS during the copy of files in the object that implement
     * PerformeCopyAsyncTaskListener has finished.
     */
    @Override
    public void notifyOnPerformeCopyProgress(Integer progress, String senderCode) {
        LogUtils.LOGD_N(LOG_TAG, "notifyOnPerformeCopyProgress");
    }

    /**
     * This method is used to notify PRE EXECUTE during the copy of files in the object that implement
     * PerformeCopyAsyncTaskListener has finished.
     */
    @Override
    public void notifyOnPerformeCopyPreExecute(String senderCode) {
        LogUtils.LOGD_N(LOG_TAG, "notifyOnPerformeCopyPreExecute");
        if (!isPause) {
            if (handler != null) {
                Message msg = handler.obtainMessage(MSG_PERFORME_COPY_PRE_EXECUTE);
                handler.sendMessage(msg);
            }
        }
    }

    /**
     * This method is used to notify RESOULT during the copy of files in the object that implement
     * PerformeCopyAsyncTaskListener has finished.
     **/
    @Override
    public void notifyOnPerformeCopyResoult(Activity activity, String folder, String senderCode) {
        LogUtils.LOGD_N(LOG_TAG, "notifyOnPerformeCopyResoult");
        mCurrPerformeCopyWorking = -1;

        //alternativa perchè nel UI thread
        //mProgressBar.setVisibility(View.GONE);
        //mFab.setVisibility(View.VISIBLE);
        if (!isPause) {
            if (handler != null) {
                Message msg = handler.obtainMessage(MSG_PERFORME_COPY_RESOULT);
                handler.sendMessage(msg);
            }
            Toast.makeText(getActivity(), "Copia cartella " + folder + " terminata!", Toast.LENGTH_SHORT).show();
        } else {
            if (isForegroundRunning(activity)) {
                LogUtils.LOGD_N(LOG_TAG, "foreground");
                Toast.makeText(activity, "Copia cartella " + folder + " terminata!", Toast.LENGTH_SHORT).show();
            } else {
                LogUtils.LOGD_N(LOG_TAG, "background");
                sendNotification(activity, folder);
            }
        }
    }

    public boolean isForegroundRunning(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
            if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                for (String activeProcess : processInfo.pkgList) {
                    if (activeProcess.equals(context.getPackageName())) {
                        //If your app is the process in foreground, then it's not in running in background
                        return true;
                    }
                }
            }
        }
        return false;
    }


    //todo da sistemare per non aprire due istanze dell'app con la notifica
    private void clearAllNotifications() {
        if (mNotificationManager != null) {
            mCurrentNotificationID = 0;
            mNotificationManager.cancelAll();
        }
    }

    private void sendNotification(Activity activity, String folder) {
        mNotificationManager = (NotificationManager) activity.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        mIcon = BitmapFactory.decodeResource(activity.getResources(),
                R.drawable.app_icon);

        notificationTitle = activity.getApplicationContext().getString(R.string.app_name);
        notificationText = "...copia cartella " + folder + " terminata!";
        mNotificationBuilder = new NotificationCompat.Builder(activity.getApplicationContext())
                .setSmallIcon(R.drawable.folder_selected)
                .setLargeIcon(mIcon)
                .setContentTitle(notificationTitle)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(notificationText))
                .setPriority(Notification.PRIORITY_MAX)
                .setContentText(notificationText);

        Intent notificationIntent = new Intent(activity, AppInfoActivity.class);

        Bundle bundle = new Bundle();
        bundle.putParcelable(APP_SELECTED_BUNDLE, mAppItem);
        notificationIntent.putExtras(bundle);

        PendingIntent contentIntent = PendingIntent.getActivity(activity, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        mNotificationBuilder.setContentIntent(contentIntent);
        Notification notification = mNotificationBuilder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND;

        mCurrentNotificationID++;
        int notificationId = mCurrentNotificationID;
        if (notificationId == Integer.MAX_VALUE - 1)
            notificationId = 0;

        mNotificationManager.notify(notificationId, notification);
    }

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

                            mDirectoryListDTO.clear();
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

                                    mDirectoryListDTO.add(dirItem);

                                    mCurrFolderSizeWorking++;
                                    mFolderSizeAsyncTask = new FolderSizeAsyncTask(getActivity(),
                                            mFolderSizeAsyncTaskListener,
                                            mCurrFolderSizeWorking);
                                    mFolderSizeAsyncTask.executeOnExecutor(mThreadPoolExecutor, dirUri);
                                }
                            }

                            return mDirectoryListDTO;
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
            mFolderSizeAsyncTaskListener = (FolderSizeAsyncTaskListener) this;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement FolderSizeAsyncTaskListener");
        }
        try {
            mPerformeCopyAsyncTaskListener = (PerformeCopyAsyncTaskListener) this;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement PerformeCopyAsyncTaskListener");
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

        // Reset instance state on reconfiguration
//        if (null != savedInstanceState) {
//            restoreValue(savedInstanceState);
//        }


        if (!AppUtils.isExternalStorageCompatible())
            isCompatible = false;
        else
            isCompatible = true;

        // Preserve across reconfigurations
        setRetainInstance(true);
    }

//    private void restoreValue(Bundle savedInstanceState) {
//
//        mDirectoryListDTO = savedInstanceState.getParcelableArrayList(DIRECTORY_LIST_MODELS_STATE);
//
//        mCurrPerformeCopyWorking = savedInstanceState.getInt(CURR_PERFORME_COPY_WORKING_STATE);
//    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        LogUtils.LOGD_N(LOG_TAG, "onSaveInstanceState");

        savedInstanceState.putInt(CURR_PERFORME_COPY_WORKING_STATE, mCurrPerformeCopyWorking);

        if (null != mDirectoryListDTO) {
            savedInstanceState.putParcelableArrayList(DIRECTORY_LIST_MODELS_STATE, mDirectoryListDTO);
        }

        if (0 != mCurrentNotificationID) {
            savedInstanceState.putInt(CURRENT_NOTIFICATION_ID_STATE, mCurrentNotificationID);
        }

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        clearAllNotifications();
        isPause = false;
        if (isCompatible) {
            mDialogHandler = new DialogHandler();
            mDialogHandler.setTargetFragment(AppInfoFragment.this, DIALOG_FRAGMENT);
            mDialogHandler.initialize(getContext(), getFragmentManager());

            View rootView = inflater.inflate(R.layout.fragment_app_info, container, false);

//            FragmentManager fm = getFragmentManager();

            mProgressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);

            mFab = (FloatingActionButton) rootView.findViewById(R.id.fab);
            mFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mAppItem != null && mDirectoryListDTO != null) {
                        PerformeCopyAsyncTask task = ResourceHashCode.getPerformeCopyTask(mAppItem.getAppName());
                        if (task == null || task.getStatus() != AsyncTask.Status.RUNNING) {
                            if (isSelected(mDirectoryListDTO) == true) {
                                Bundle bundleForDialog = createPerformCopyBundle();
                                mDialogHandler.showPerformCopyDialog(bundleForDialog);
                            } else {
                                mDialogHandler.showAlertSelectedFolderDialog();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Copia ancora in esecuzione!", Toast.LENGTH_SHORT).show();
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
            mDirectoryAdapter = new DirectoryItemAdapter(getContext(), this, mDirectoryListDTO);
            mRecyclerView.setAdapter(mDirectoryAdapter);

            Bundle bundle = getArguments();
            if (bundle != null) {
                mAppItem = bundle.getParcelable(APP_SELECTED_BUNDLE);
                getActivity().setTitle(mAppItem.getAppName());

                if (mAppItem != null) {
                    Bundle appInfoBundle = new Bundle();
                    appInfoBundle.putString(APP_NAME_BUNDLE, mAppItem.getAppName());
                    /**
                     * Initialize the loader
                     **/
                    mLoaderManager = getLoaderManager();
//                    Loader<Boolean> appInfoFolderLoader = mLoaderManager.getLoader(ID_APP_INFO_FOLDER_LOADER);
//                    if (appInfoFolderLoader == null) {
//                        mLoaderManager.initLoader(ID_APP_INFO_FOLDER_LOADER, appInfoBundle, mAppInfoFolderLoader);
//                    } else {
//                        mLoaderManager.restartLoader(ID_APP_INFO_FOLDER_LOADER, appInfoBundle, mAppInfoFolderLoader);
//                    }
                    if (null == savedInstanceState) {
//                        Bundle appInfoBundle = new Bundle();
//                        appInfoBundle.putString(APP_NAME_BUNDLE, mAppItem.getAppName());
                        getLoaderManager().initLoader(ID_APP_INFO_FOLDER_LOADER, appInfoBundle, mAppInfoFolderLoader);
                    }
                }
            }

            restoreState(savedInstanceState);

            return rootView;
        } else {
            LogUtils.LOGD_N(LOG_TAG, "Device non compatibile!");
            mDialogHandler.showExternalStorageCompatibility();
            return null;
        }
    }

    private void restoreState(Bundle savedInstanceState) {
//        if (null != savedInstanceState) {
//            mDirectoryAdapter.populateDirectoryItem(mDirectoryListDTO);
//        }
        if (null != savedInstanceState) {
            mCurrentNotificationID = savedInstanceState.getInt(CURRENT_NOTIFICATION_ID_STATE);
        }

        if (mCurrPerformeCopyWorking == 1) {
            mProgressBar.setVisibility(View.VISIBLE);
            mProgressBar.bringToFront();
            mFab.setVisibility(View.GONE);
        } else {
            mProgressBar.setVisibility(View.GONE);
            mProgressBar.bringToFront();
            mFab.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        isPause = true;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * This method is used to notify from onBindViewHolder that implement
     * DirectoryAdapterListener has clicked.
     */
    @Override
    public void onIconDirectoryClicked(int position) {
        DirectoryItemDTO directoryItem = mDirectoryListDTO.get(position);
        directoryItem.setSelected(!directoryItem.isSelected());
        mDirectoryListDTO.set(position, directoryItem);
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
        //  directoryListModels = mDirectoryListDTO
        // contiene tutte le informazioni riguardo le directory da copiare

        if (directoryListModels != null) {
            mSelectedDirectoryListDTO.clear();
            mTotalSize = 0;
            for (DirectoryItemDTO directory : directoryListModels) {
                if (directory.isSelected()) {
                    mSelectedDirectoryListDTO.add(directory);
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
     * mDirectoryListDTO = contains all directory nformation
     * mSelectedDirectoryListDTO = contains all directory selected information, not be null at this point
     * mPerformCopyDTO = contains name of folder where to put the files and if delete files after the copy
     * mLoaderManager = deve essere diverso da null
     **/
    private void prepareTheCopy() {
        //todo lanciare un asynck task all'interno di un loader
        mDialogHandler.dismissPerformCopyDialog();
        if (isEnoughtFreeMemory(mDirectoryListDTO) == true) {

            final ArrayList<String> dirs = new ArrayList<>();
            for (DirectoryItemDTO s : mSelectedDirectoryListDTO) {
                dirs.add(s.getPath());
            }

            mCurrPerformeCopyWorking = 1;
            mPerformeCopyAsyncTask = new PerformeCopyAsyncTask(getActivity(),
                    mPerformeCopyAsyncTaskListener, mSDPath, mPerformCopyDTO);
            ResourceHashCode.addPerformeCopyTask(mAppItem.getAppName(), mPerformeCopyAsyncTask);
            mPerformeCopyAsyncTask.executeOnExecutor(mThreadPoolExecutor, dirs);
        }
    }


}





















