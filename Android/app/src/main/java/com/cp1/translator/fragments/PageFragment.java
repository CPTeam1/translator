package com.cp1.translator.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.cp1.translator.R;
import com.cp1.translator.activities.AskQuestion;
import com.cp1.translator.adapters.QuestionsAdapter;
import com.cp1.translator.models.Question;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

// In this case, the fragment displays simple text based on the page
public class PageFragment extends Fragment implements AskQuestionFragment.AskQuestionDialogListener {
    public static final String ARG_PAGE = "ARG_PAGE";
    private static QuestionsAdapter questionsAdapter;
    private static ArrayList<Question> questions;

//    private String mTitle;

//    private Button btAskQs;
    @Nullable @Bind(R.id.tvQuestionTitle) TextView tvQuestionTitle;
    @Nullable @Bind(R.id.tvRepliesCount) TextView tvRepliesCount;
    @Nullable @Bind(R.id.lvQuestions) ListView lvQuestions;

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
        questionsAdapter = new QuestionsAdapter(getActivity(),questions);
        questions = new ArrayList<>(10);

//        mTitle = getArguments().getString(ARG_PAGE);
    }



    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = null;

        view = inflater.inflate(R.layout.fragment_questions, container, false);
        ButterKnife.bind(this, view);

        lvQuestions = (ListView) view.findViewById(R.id.lvQuestions);

        // TODO
        /********************** ListView with Adapter **********************/
        /********************** end of ListView **********************/
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //lvQuestions.setAdapter(questionsAdapter);
    }

    public void addQuestion(Question q){
        if(questions==null)
            questions = new ArrayList<>();
        questions.add(q);
        questionsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFinishAsking(Question newQuestion) {
        addQuestion(newQuestion);
    }
}
