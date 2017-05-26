package it.namron.sweeping.dialog;

/**
 * Created by norman on 24/05/17.
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import it.namron.sweeping.model.AppItemModel;
import it.namron.sweeping.sweeping.R;

import static it.namron.sweeping.utils.Constant.DIALOG_FOLDER_OUT;
import static it.namron.sweeping.utils.Constant.DIALOG_ICON_APP_ICON;
import static it.namron.sweeping.utils.Constant.DIALOG_TITLE_APP_NAME;

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
        Bundle mArgs = getArguments();
        String title = mArgs.getString(DIALOG_TITLE_APP_NAME);
        String folder = mArgs.getString(DIALOG_FOLDER_OUT);

        byte[] b = mArgs.getByteArray(DIALOG_ICON_APP_ICON);
        Drawable iconDrawable = new BitmapDrawable(getResources(), BitmapFactory.decodeByteArray(b, 0, b.length));

        View rootView = inflater.inflate(R.layout.performe_copy_dialog, container, false);

        EditText performeEditText = (EditText) rootView.findViewById(R.id.performe_edit_text);
        performeEditText.setText(folder, TextView.BufferType.EDITABLE);

        TextView titleTextView = (TextView) rootView.findViewById(R.id.title_text_view);
        titleTextView.setText(title);
        ImageView dialogIcon = (ImageView) rootView.findViewById(R.id.dialog_icon);
        dialogIcon.setImageDrawable(iconDrawable);

        RadioGroup performeRadioGroup = (RadioGroup) rootView.findViewById(R.id.performe_radio_group);
        RadioButton performeRdBtnSposta = (RadioButton) rootView.findViewById(R.id.performe_rdBtn_sposta);
        RadioButton performeRdBtnTieni = (RadioButton) rootView.findViewById(R.id.performe_rdBtn_tieni);
        performeRdBtnSposta.setChecked(true);


        performeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup performeRadioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.performe_rdBtn_sposta:
                        // 'Incident' checked
//                    fall.setVisibility(View.GONE);
//                    trip.setVisibility(View.GONE);
//                    illness.setVisibility(View.GONE);
                        mListener.onResoultDialog("sposta");

                        break;
                    case R.id.performe_rdBtn_tieni:
                        mListener.onResoultDialog("tieni");

                        // 'Accident' checked
                        break;
                }
            }
        });


        Button performeBtnOkay = (Button) rootView.findViewById(R.id.performe_btn_okay);
        performeBtnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onResoultDialog("OK");
            }
        });

        Button performeBtnCancel = (Button) rootView.findViewById(R.id.performe_btn_cancel);
        performeBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onResoultDialog("Cancell");
            }
        });


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