package it.namron.sweeping.dialog;

/**
 * Created by norman on 24/05/17.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.namron.sweeping.dialog.parameter.PerformCopyDialogFromParameter;
import it.namron.sweeping.dialog.parameter.PerformCopyDialogToParameter;
import it.namron.sweeping.sweeping.R;

import static it.namron.sweeping.constant.Constant.ALERT_FOLDER_DIALOG_TAG;
import static it.namron.sweeping.constant.Constant.PERFORM_COPY_DIALOG_PARAMETER_BUNDLE;

public class PerformCopyDialog extends DialogFragment{

    private ResoultPerformCopyDialogListener mListener;

    RadioButton mPerformeRdBtnSposta;
    RadioButton mPerformeRdBtnTieni;

    EditText mPerformeEditText;
    PerformCopyDialogFromParameter mParameterFromObj;

    public interface ResoultPerformCopyDialogListener {
        void onResoultPerformCopyDialog(PerformCopyDialogFromParameter parameter);
    }

    public PerformCopyDialog() {
        // Empty constructor required for DialogFragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle mArgs = getArguments();
        if (mArgs != null) {
            PerformCopyDialogToParameter parameterObj = mArgs.getParcelable(PERFORM_COPY_DIALOG_PARAMETER_BUNDLE);
            if (parameterObj != null) {
                String title = parameterObj.getTitle();
                String folder = parameterObj.getFolder();
                Drawable iconDrawable = parameterObj.getIcon();

//            byte[] b = mArgs.getByteArray(DIALOG_ICON_APP_ICON);
//            Drawable iconDrawable = new BitmapDrawable(getResources(), BitmapFactory.decodeByteArray(b, 0, b.length));

                View rootView = inflater.inflate(R.layout.performe_copy_dialog, container, false);

                mPerformeEditText = (EditText) rootView.findViewById(R.id.performe_edit_text);
                mPerformeEditText.setText(folder, TextView.BufferType.EDITABLE);

                TextView titleTextView = (TextView) rootView.findViewById(R.id.title_text_view);
                titleTextView.setText(title);
                ImageView dialogIcon = (ImageView) rootView.findViewById(R.id.dialog_icon);
                dialogIcon.setImageDrawable(iconDrawable);

                RadioGroup performeRadioGroup = (RadioGroup) rootView.findViewById(R.id.performe_radio_group);
                mPerformeRdBtnSposta = (RadioButton) rootView.findViewById(R.id.performe_rdBtn_sposta);
                mPerformeRdBtnTieni = (RadioButton) rootView.findViewById(R.id.performe_rdBtn_tieni);
                mPerformeRdBtnSposta.setChecked(true);

                performeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup performeRadioGroup, int checkedId) {
                        switch (checkedId) {
                            case R.id.performe_rdBtn_sposta:
                                // 'Incident' checked
//                    fall.setVisibility(View.GONE);
//                    trip.setVisibility(View.GONE);
//                    illness.setVisibility(View.GONE);
//                                mListener.onResoultPerformCopyDialog("sposta");

                                break;
                            case R.id.performe_rdBtn_tieni:
//                                mListener.onResoultPerformCopyDialog("tieni");

                                // 'Accident' checked
                                break;
                        }
                    }
                });

                Button performeBtnOkay = (Button) rootView.findViewById(R.id.performe_btn_okay);
                performeBtnOkay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hideKeyboard(getActivity(), mPerformeEditText.getWindowToken());


                        if (validFormatFolder(mPerformeEditText.getText().toString())) {
                            mParameterFromObj = new PerformCopyDialogFromParameter();
                            mParameterFromObj.setFolder(mPerformeEditText.getText().toString());
                            mParameterFromObj.setOriginal(mPerformeRdBtnTieni.isChecked());

                            mListener.onResoultPerformCopyDialog(mParameterFromObj);
                        } else {
                            AlertFolderNameDialog alertFolderDialog = new AlertFolderNameDialog();
                            // Show DialogFragment
                            alertFolderDialog.show(getFragmentManager(), ALERT_FOLDER_DIALOG_TAG);
                        }
                    }

                    private boolean validFormatFolder(String file) {
                        //String regex = "^[a-zA-Z\\s]*$"; //letters and spaces
                        String regex = new String("^.*[^a-zA-Z\\s0-9._-].*$");
//                    String fileName = file.replaceAll("[\\/:*?\"<>|]", "_");
                        Pattern pattern = Pattern.compile(regex);
                        Matcher matcher = pattern.matcher(file);
                        if (matcher.matches()) {
                            //name folder not valid
                            return false;
                        } else {
                            //valid folder name
                            return true;
                        }
                    }
                });

                Button performeBtnCancel = (Button) rootView.findViewById(R.id.performe_btn_cancel);
                performeBtnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hideKeyboard(getActivity(), mPerformeEditText.getWindowToken());

//                        mListener.onResoultPerformCopyDialog("Cancell");
                        dismiss();
                    }
                });
                return rootView;
            }
        }
        return null;
    }

    /**
     * This method is used to hide a keyboard after a user has
     * finished typing the url.
     */
    public void hideKeyboard(Activity activity,
                             IBinder windowToken) {
        InputMethodManager mgr =
                (InputMethodManager) activity.getSystemService
                        (Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(windowToken,
                0);
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
//                        mListener.onResoultPerformCopyDialog("OK");
//                    }
//                })
//
//                // Negative Button
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog,	int which) {
//                        // Do something else
//                        mListener.onResoultPerformCopyDialog("Cancel");
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
            mListener = (ResoultPerformCopyDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement ResoultPerformCopyDialogListener");
        }
    }

}