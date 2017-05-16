package it.namron.sweeping.fragment;

import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

import it.namron.core.utility.WhatsApp;
import it.namron.sweeping.adapter.DirectoryAdapter;
import it.namron.sweeping.constant.PackageApp;
import it.namron.sweeping.model.DirectoryItemModel;
import it.namron.sweeping.sweeping.R;

/**
 * Created by norman on 09/05/17.
 */

public class WhatsAppFragment extends Fragment implements DirectoryAdapter.MessageAdapterListener {

    private static final String LOG_TAG = WhatsAppFragment.class.getSimpleName();

    //References to RecyclerView and Adapter to reset the list to its
    //"pretty" state when the reset menu item is clicked.
    private DirectoryAdapter mDirectoryAdapter;
    private RecyclerView mDirectoryList;

    private List<DirectoryItemModel> messages = new ArrayList<>();

    /*
     * This number will uniquely identify our Loader
     */
    private static final int ID_WHATSAPP_FOLDER_LOADER = 20;

    private LoaderManager mLoaderManager;

    private Toast mToast;

    public WhatsAppFragment() {

    }

    private LoaderManager.LoaderCallbacks<List<DirectoryItemModel>> mWhatsAppFolderLoaderCallback = new LoaderManager.LoaderCallbacks<List<DirectoryItemModel>>() {

        @Override
        public Loader<List<DirectoryItemModel>> onCreateLoader(int loaderId, Bundle args) {
            switch (loaderId) {
                case ID_WHATSAPP_FOLDER_LOADER:
                    return new AsyncTaskLoader<List<DirectoryItemModel>>(getContext()) {

                        List<DirectoryItemModel> mResponce;

                        @Override
                        protected void onStartLoading() {

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
                        public void deliverResult(List<DirectoryItemModel> data) {
                            mResponce = data;
                            super.deliverResult(data);
                        }

                        @Override
                        public List<DirectoryItemModel> loadInBackground() {

                            messages.clear();
                            DirectoryItemModel msg;

                            List<String> whatsAppDir = WhatsApp.listOfWhatsAppDirectory();
                            for (String dir : whatsAppDir) {
                                msg = new DirectoryItemModel();
                                msg.setFolderName(dir);
                                msg.setSelected(true);
                                messages.add(msg);
                            }

                            return messages;
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

        //The DirectoryAdapter is responsible for displaying each item in the list.
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

    @Override
    public void onIconDirectoryClicked(int position) {
        DirectoryItemModel message = messages.get(position);
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





















