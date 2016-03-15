package com.cp1.translator.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.cp1.translator.R;
import com.cp1.translator.models.Entry;
import com.cp1.translator.models.Types;
import com.cp1.translator.utils.Constants;

import org.parceler.Parcels;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by kimhy08 on 3/13/2016.
 */
public class QsContentFragment extends Fragment {

    // this always shows up on the screen regardless of the question type
    @Bind(R.id.tvQsContentTxt) TextView tvQsContentTxt;

    // only one of three shows up based on the question type
    @Bind(R.id.ivQsMediaImg) ImageView ivQsMediaImg;
    @Bind(R.id.mcQsMediaVoice) MediaController mcQsMediaVoice;
    @Bind(R.id.vvQsMediaVideo) VideoView vvQsMediaVideo;

    public static QsContentFragment newInstance(Parcelable qsParcelable) {
        Bundle args = new Bundle();
        args.putParcelable(Constants.ENTRY_KEY, qsParcelable);
        QsContentFragment fragment = new QsContentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qs_content, container, false);
        ButterKnife.bind(this, view);

        Entry question = Parcels.unwrap(getArguments().getParcelable(Constants.ENTRY_KEY));
        question.fetchIfNeededInBackground();
        String qsType = question.getType();
        if (qsType == null)
            qsType = Types.TEXT;
        switch (qsType) {
            default:
                tvQsContentTxt.setText(question.getText());
            case Types.PICTURE:
                // load img onto ivQsMediaImg

                // set visible
                break;
            case Types.AUDIO:
                // load voice file

                // set visible
                break;
            case Types.VIDEO:
                // load video file

                // set visible
                break;
        }

        return view;
    }
}
