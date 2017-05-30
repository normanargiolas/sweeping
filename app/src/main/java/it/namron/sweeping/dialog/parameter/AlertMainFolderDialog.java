package it.namron.sweeping.dialog.parameter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import it.namron.sweeping.sweeping.R;

/**
 * Created by norman on 27/05/17.
 */

public class AlertMainFolderDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                // Set Dialog Icon
                .setIcon(R.drawable.androidhappy)
                // Set Dialog Title
                .setTitle("Attenzione")
                // Set Dialog Message
                .setMessage("Nome della cartella non valido!")

                // Positive button
                .setPositiveButton("Continua", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something else
                        dismiss();
                    }
                })
                // Neutral button
                .setNeutralButton("Rinomina", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something else
                        dismiss();
                    }
                })
                // Negative button
                .setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Do something else
                        dismiss();
                    }
                })
                .create();
    }

//    public void show(FragmentManager fragmentManager, String s) {
//
//    }
}
