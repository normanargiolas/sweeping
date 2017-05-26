package it.namron.sweeping.fragment;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import it.namron.core.utility.TelegramApp;
import it.namron.core.utility.WhatsApp;
import it.namron.sweeping.adapter.DirectoryItemAdapter;
import it.namron.sweeping.dialog.PerformCopyDialog;
import it.namron.sweeping.model.AppItemModel;
import it.namron.sweeping.model.DirectoryItemModel;
import it.namron.sweeping.sweeping.R;

import static it.namron.sweeping.utils.Constant.APP_FACEBOOK;
import static it.namron.sweeping.utils.Constant.APP_MESSENGER;
import static it.namron.sweeping.utils.Constant.APP_NAME_BUNDLE;
import static it.namron.sweeping.utils.Constant.APP_SELECTED_BUNDLE;
import static it.namron.sweeping.utils.Constant.APP_TELEGRAM;
import static it.namron.sweeping.utils.Constant.APP_WHATSAPP;
import static it.namron.sweeping.utils.Constant.DIALOG_FOLDER_OUT;
import static it.namron.sweeping.utils.Constant.DIALOG_ICON_APP_ICON;
import static it.namron.sweeping.utils.Constant.DIALOG_TITLE_APP_NAME;
import static it.namron.sweeping.utils.Constant.DIALOG_FRAGMENT;

/**
 * Created by norman on 09/05/17.
 */

public class AppInfoFragment extends Fragment
        implements DirectoryItemAdapter.MessageAdapterListener, PerformCopyDialog.ResoultDialogListener {

    private static final String LOG_TAG = AppInfoFragment.class.getSimpleName();

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

    public AppInfoFragment() {

    }


    private LoaderManager.LoaderCallbacks<List<DirectoryItemModel>> mAppInfoFolderLoaderCallback = new LoaderManager.LoaderCallbacks<List<DirectoryItemModel>>() {

        @Override
        public Loader<List<DirectoryItemModel>> onCreateLoader(int loaderId, Bundle args) {
            switch (loaderId) {
                case ID_APP_INFO_FOLDER_LOADER:
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
                                    msg = new DirectoryItemModel();
                                    msg.setFolderName(dir);
                                    msg.setSelected(true);
                                    mDirectoryListModels.add(msg);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_app_info, container, false);

        FragmentManager fm = getFragmentManager();

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAppItem !=null){
                    PerformCopyDialog performeCopyDialog = new PerformCopyDialog(); //todo usare il bundle per passare i parametri: mAppItem
                    performeCopyDialog.setTargetFragment(AppInfoFragment.this, DIALOG_FRAGMENT);

                    Bundle bundleForDialog = new Bundle();
                    Bitmap bitmap = ((BitmapDrawable) mAppItem.getAppIcon()).getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    bundleForDialog.putByteArray(DIALOG_ICON_APP_ICON, stream.toByteArray());
                    bundleForDialog.putString(DIALOG_TITLE_APP_NAME, mAppItem.getAppName());
                    bundleForDialog.putString(DIALOG_FOLDER_OUT, mAppItem.getAppName().toLowerCase());


                    performeCopyDialog.setArguments(bundleForDialog);

                    // Show Alert DialogFragment
                    performeCopyDialog.show(fm, "Alert Dialog Fragment");


                    //Mostrare un menu dialog di conferma
                    //Recuperare i dati
                    //Avviare la procedura di copia
                    //Eventualmente cancellare gli originali
                }
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
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
        DirectoryItemModel message = mDirectoryListModels.get(position);
        message.setSelected(!message.isSelected());
        mDirectoryListModels.set(position, message);
        mDirectoryAdapter.notifyDataSetChanged();


        if (mToast != null) {
            mToast.cancel();
        }
        String toastMessage = "Item #" + position + " clicked.";
        mToast = Toast.makeText(this.getContext(), toastMessage, Toast.LENGTH_LONG);

        mToast.show();
    }

    @Override
    public void onResoultDialog(String inputText) {

        if (mToast != null) {
            mToast.cancel();
        }
        String toastMessage = "Hi, " + inputText + " clicked.";
        mToast = Toast.makeText(this.getContext(), toastMessage, Toast.LENGTH_LONG);
        mToast.show();
    }
}





















