package com.cp1.translator.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cp1.translator.R;
import com.cp1.translator.activities.AskQuestion;

import butterknife.ButterKnife;

// In this case, the fragment displays simple text based on the page
public class PageFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    private String mTitle;

    private Button btAskQs;

    public static PageFragment newInstance(String title) {
        Bundle args = new Bundle();
        args.putString(ARG_PAGE, title);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTitle = getArguments().getString(ARG_PAGE);
    }

    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = null;

        if (true) { // here if condition MUST CHANGE!! It must check if the question list is empty
            view = inflater.inflate(R.layout.fragment_empty, container, false);
        } else {
            view = inflater.inflate(R.layout.fragment_page, container, false);
            ButterKnife.bind(view);
            TextView tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            btAskQs = (Button) view.findViewById(R.id.btQs);

            tvTitle.setText("Fragment " + mTitle);

            btAskQs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent askQsIntent = new Intent(getActivity(), AskQuestion.class);
                    startActivity(askQsIntent);
                }
            });
        }

        return view;
    }
}
