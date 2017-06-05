package it.namron.sweeping.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import it.namron.sweeping.dialog.parameter.PerformCopyDialogFromParameter;

import static it.namron.sweeping.constant.Constant.ALERT_SELECTED_FOLDER_DIALOG_TAG;
import static it.namron.sweeping.constant.Constant.DIALOG_FRAGMENT;
import static it.namron.sweeping.constant.Constant.ENOUGHT_FREE_MEMORY_DIALOG_TAG;
import static it.namron.sweeping.constant.Constant.EXTERNAL_STORAGE_COMPATIBILITY_DIALOG_TAG;
import static it.namron.sweeping.constant.Constant.PERFORM_COPY_DIALOG_PARAMETER_TAG;

/**
 * Created by norman on 05/06/17.
 */

public class DialogHandler extends Fragment implements
        PerformCopyDialog.ResoultPerformCopyDialogListener,
        ExternalStorageCompatibilityDialog.ResoultExternalStorageCompatibilityDialogListener {

    private static final String LOG_TAG = DialogHandler.class.getSimpleName();

    private Context mContext;
    private FragmentManager mFragmentManager;

    private EnoughtFreeMemoryDialog enoughtFreeMemoryDialog = new EnoughtFreeMemoryDialog();
    private ExternalStorageCompatibilityDialog externalStorageCompatibilityDialog = new ExternalStorageCompatibilityDialog();
    private AlertSelectedFolderDialog alertSelectedFolderDialog = new AlertSelectedFolderDialog();
    private PerformCopyDialog performCopyDialog = new PerformCopyDialog();


    private DialogHandlerListener mListener;

    public void initialize(Context context, FragmentManager fragmentManager) {
        mContext = context;
        mFragmentManager = fragmentManager;
        onAttach(context);
    }

    public interface DialogHandlerListener {
        void onDialogResoult(Object resoult, String tag);
    }

    public DialogHandler() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the EditNameDialogListener so we can send events to the host
            mListener = (DialogHandler.DialogHandlerListener) getTargetFragment();
            if (mListener == null)
                throw new NullPointerException(context.toString()
                        + " listener in DialogHandlerListener must not be null!");
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement DialogHandlerListener!");
        }
    }


    public void showExternalStorageCompatibility() {
        externalStorageCompatibilityDialog.setTargetFragment(this, DIALOG_FRAGMENT);
        externalStorageCompatibilityDialog.show(mFragmentManager, EXTERNAL_STORAGE_COMPATIBILITY_DIALOG_TAG);
    }

    public void showEnoughtFreeMemory() {
        enoughtFreeMemoryDialog.show(mFragmentManager, ENOUGHT_FREE_MEMORY_DIALOG_TAG);
    }

    public void showPerformCopyDialog(Bundle bundle) {
        performCopyDialog.setArguments(bundle);
        performCopyDialog.setTargetFragment(this, DIALOG_FRAGMENT);
        performCopyDialog.show(mFragmentManager, PERFORM_COPY_DIALOG_PARAMETER_TAG);
    }

    public void dismissPerformCopyDialog() {
        performCopyDialog.dismiss();
    }

    public void showAlertSelectedFolderDialog() {
        alertSelectedFolderDialog.show(mFragmentManager, ALERT_SELECTED_FOLDER_DIALOG_TAG);
    }

    /**
     * This method is used to notify after a sendFeedback buttn that implement
     * ExternalStorageCompatibilityDialog has clicked.
     */
    @Override
    public void onSendFeedbackExternalStorageCompatibilityDialog(boolean resoult) {
        mListener.onDialogResoult(resoult, EXTERNAL_STORAGE_COMPATIBILITY_DIALOG_TAG);
    }

    /**
     * This method is used to notify after a onClick positive buttn that implement
     * PerformCopyDialog has clicked.
     */
    @Override
    public void onResoultPerformCopyDialog(PerformCopyDialogFromParameter resoult) {
        mListener.onDialogResoult(resoult, PERFORM_COPY_DIALOG_PARAMETER_TAG);
    }
}
