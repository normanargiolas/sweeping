package it.namron.sweeping.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import it.namron.sweeping.sweeping.R;

/**
 * Created by norman on 27/05/17.
 */

public class EnoughtFreeMemoryDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                // Set Dialog Icon
                .setIcon(R.drawable.androidhappy)
                // Set Dialog Title
                .setTitle(R.string.attenzione)
                // Set Dialog Message
                .setMessage(R.string.enought_free_memory_dialog_message)

                // Positive button
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something else
                        dismiss();
                    }
                }).create();
    }

//    public void show(FragmentManager fragmentManager, String s) {
//
//    }
}
