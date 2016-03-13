package com.cp1.translator.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cp1.translator.R;
import com.cp1.translator.adapters.QuestionsAdapter;
import com.cp1.translator.models.Entry;
import com.cp1.translator.utils.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

// In this case, the fragment displays simple text based on the page
public abstract class PageFragment extends Fragment implements AskQuestionFragment.AskQuestionDialogListener {

    private static final int ITEM_SPACE = 24;
    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String MY_USER_NAME = "MY_USER_NAME";

    private String mMyUserName;

    protected QuestionsAdapter mQuestionsAdapter;
    protected List<Entry> mQuestions;

    @Bind(R.id.rvQuestions) RecyclerView rvQuestions;
    @Bind(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        mTitle = getArguments().getString(ARG_PAGE);
    }

    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_questions, container, false);
        ButterKnife.bind(this, view);

        mMyUserName = getArguments().getString(MY_USER_NAME);

        /********************** RecyclerView **********************/
        mQuestions = new ArrayList<>();
        mQuestionsAdapter = new QuestionsAdapter(mQuestions, mMyUserName);
//        mQuestionsAdapter.setOnItemClickListener();
        // Attach the adapter to the RecyclerView to populate items
        rvQuestions.setAdapter(mQuestionsAdapter);
        // add ItemDecoration
        rvQuestions.addItemDecoration(new SpaceItemDecoration(ITEM_SPACE));
        /********************** end of RecyclerView **********************/

        /********************** SwipeRefreshLayout **********************/
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        /********************** end of SwipeRefreshLayout **********************/

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void addQuestion(Entry question) {
        if (mQuestions == null) {
            mQuestions = new ArrayList<>();
            mQuestionsAdapter.setQuestionList(mQuestions);
        }
        mQuestionsAdapter.add(question);
    }

    @Override
    public void onFinishAsking(Entry newQuestion) {
        addQuestion(newQuestion);
    }

    protected abstract void refreshQuestions();
}
