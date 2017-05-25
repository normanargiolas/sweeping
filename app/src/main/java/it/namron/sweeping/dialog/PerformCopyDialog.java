package it.namron.sweeping.dialog;

/**
 * Created by norman on 24/05/17.
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.namron.sweeping.sweeping.R;

public class PerformCopyDialog extends DialogFragment {

    private ResoultDialogListener mListener;

    public interface ResoultDialogListener {
        void onResoultDialog(String inputText);
    }

    public PerformCopyDialog() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.performe_copy_dialog, container, false);

        // Do something else
        return rootView;
    }

//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {

//        Dialog dialog = new Dialog(getActivity());
//
//        dialog.setContentView(R.layout.performe_copy_dialog);
//        dialog.setTitle(R.string.title_performe_copy_dialog);
//
//        return dialog;


//        return new AlertDialog.Builder(getActivity())
//                // Set Dialog Icon
//                .setIcon(R.drawable.androidhappy)
//                // Set Dialog Title
//                .setTitle("Alert DialogFragment")
//                // Set Dialog Message
//                .setMessage("Alert DialogFragment Tutorial")
//
//                // Positive button
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        // Do something else
//                        mListener.onResoultDialog("OK");
//                    }
//                })
//
//                // Negative Button
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog,	int which) {
//                        // Do something else
//                        mListener.onResoultDialog("Cancel");
//                        PerformCopyDialog.this.getDialog().cancel();
//                    }
//                }).create();
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the EditNameDialogListener so we can send events to the host
            mListener = (ResoultDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement ResoultDialogListener");
        }
    }

}