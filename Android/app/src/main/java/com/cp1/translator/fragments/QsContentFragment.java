package com.cp1.translator.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cp1.translator.R;
import com.cp1.translator.utils.Constants;
import com.squareup.picasso.Picasso;

import butterknife.ButterKnife;

/**
 * Created by erioness1125(Hyunji Kim) on 3/13/2016.
 */
public class QsContentFragment extends Fragment {

    public static QsContentFragment newInstance(String imgUrl, String text) {
        Bundle args = new Bundle();
        args.putString(Constants.IMAGE, imgUrl);
        args.putString(Constants.TEXT, text);
        QsContentFragment fragment = new QsContentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_qs_content, container, false);
        ButterKnife.bind(this, view);

        String imgUrl = getArguments().getString(Constants.IMAGE);
        if (imgUrl != null && !(imgUrl.trim().isEmpty()) ) {
            // find ImageView
            ImageView ivQsMediaImg = (ImageView) view.findViewById(R.id.ivQsMediaImg);
            // load img onto ivQsMediaImg
            Picasso.with(getContext())
                    .load(imgUrl)
                    .resize(600, 0)
                    .into(ivQsMediaImg);
            ivQsMediaImg.setVisibility(View.VISIBLE);
        }

        String qsContent = getArguments().getString(Constants.TEXT);
        if (qsContent == null || qsContent.trim().isEmpty())
            qsContent = "< No Caption Available >";
        // find TextView and set text
        TextView tvQsContentTxt = (TextView) view.findViewById(R.id.tvQsContentTxt);
        tvQsContentTxt.setText(qsContent);

        return view;
    }
}