package com.cp1.translator.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * Created by kimhy08 on 3/13/2016.
 */
public class NicknameDialogFragment extends DialogFragment {

    private static final String NICKNAME = "nickname";
    private static final String TITLE = "title";

    private EditText input;
    private String originalNickname;

    public interface NicknameDialogListener {
        void onFinishEditDialog(String newNickName);
    }

    public NicknameDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    public static NicknameDialogFragment newInstance(String nickname) {
        NicknameDialogFragment fragment = new NicknameDialogFragment();
        Bundle args = new Bundle();
        args.putString(NICKNAME, nickname);
        args.putString(TITLE, "Nickname");
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString(TITLE);
        Activity activity = getActivity();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setTitle(title);

        // create an editable textfield
        input = new EditText(activity);
        originalNickname = getArguments().getString(NICKNAME);
        input.setText(originalNickname);
        input.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        input.setSelection(input.getText().length());
        LinearLayout layout = new LinearLayout(activity);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(25, 0, 25, 0);
        layout.addView(input, params);
        alertDialogBuilder.setView(layout);

        alertDialogBuilder.setPositiveButton("OK",  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // on success
                // Return input text to activity
                NicknameDialogListener listener = (NicknameDialogListener) getActivity();
                listener.onFinishEditDialog(input.getText().toString());
                dialog.dismiss();
            }
        });

        return alertDialogBuilder.create();
    }
}
