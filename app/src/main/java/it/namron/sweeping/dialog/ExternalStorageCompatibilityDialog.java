package it.namron.sweeping.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import it.namron.sweeping.sweeping.R;

/**
 * Created by norman on 27/05/17.
 */


public class ExternalStorageCompatibilityDialog extends DialogFragment {

    private ResoultExternalStorageCompatibilityDialogListener mListener;

    public interface ResoultExternalStorageCompatibilityDialogListener {
        void onSendFeedbackExternalStorageCompatibilityDialog(boolean resoult);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                // Set Dialog Icon
                .setIcon(R.drawable.androidhappy)
                // Set Dialog Title
                .setTitle(R.string.attenzione)
                // Set Dialog Message
                .setMessage(R.string.external_storage_compatibility_dialog_message)

                // Positive button
                .setPositiveButton(R.string.invia, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something else
                        dismiss();
                        mListener.onSendFeedbackExternalStorageCompatibilityDialog(true);
                    }
                })
                // Neutral button
                .setNeutralButton(R.string.annulla, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something else
                        mListener.onSendFeedbackExternalStorageCompatibilityDialog(false);
                        dismiss();
                    }
                })
                .create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the EditNameDialogListener so we can send events to the host
            mListener = (ExternalStorageCompatibilityDialog.ResoultExternalStorageCompatibilityDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement ResoultExternalStorageCompatibilityDialogListener");
        }
    }
}
