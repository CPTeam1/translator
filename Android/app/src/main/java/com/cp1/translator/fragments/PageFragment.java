package com.cp1.translator.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cp1.translator.R;

import butterknife.Bind;
import butterknife.ButterKnife;

// In this case, the fragment displays simple text based on the page
public class PageFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

//    private String mTitle;

//    private Button btAskQs;
    @Bind(R.id.tvQuestionTitle) TextView tvQuestionTitle;
    @Bind(R.id.tvRepliesCount) TextView tvRepliesCount;

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
//        mTitle = getArguments().getString(ARG_PAGE);
    }

    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = null;

        if (true) { // here if condition MUST CHANGE!! It must check if the question list is empty
            view = inflater.inflate(R.layout.fragment_empty, container, false);
        } else {
            view = inflater.inflate(R.layout.fragment_questions, container, false);
            ButterKnife.bind(this, view);

            // TODO
            /********************** ListView with Adapter **********************/

            /********************** end of ListView **********************/
        }

        return view;
    }
}
