package com.cp1.translator.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cp1.translator.R;
import com.cp1.translator.adapters.QuestionsAdapter;
import com.cp1.translator.models.Entry;
import com.cp1.translator.models.User;
import com.cp1.translator.utils.SpaceItemDecoration;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

// In this case, the fragment displays simple text based on the page
public class PageFragment extends Fragment implements AskQuestionFragment.AskQuestionDialogListener {

    private static final int ITEM_SPACE = 24;

    public static final String ARG_PAGE = "ARG_PAGE";

    private QuestionsAdapter mQuestionsAdapter;
    private List<Entry> mQuestions;

    @Bind(R.id.rvQuestions) RecyclerView rvQuestions;
    @Bind(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;

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
        View view = inflater.inflate(R.layout.fragment_questions, container, false);
        ButterKnife.bind(this, view);

        /********************** RecyclerView **********************/
        mQuestions = new ArrayList<>();
        mQuestionsAdapter = new QuestionsAdapter(mQuestions);
//        mQuestionsAdapter.setOnItemClickListener(new TimelineAdapter.OnItemClickListener() {
//            @Override
//            public void onProfilePicClick(View itemView, int position) {
//                Intent i = new Intent(getContext(), ProfileActivity.class);
//                // get the article to display
//                Tweet tweet = mTweetList.get(position);
//                // pass objects to the target activity
//                i.putExtra(getString(R.string.userid), tweet.getUser().getIdStr());
//                // launch the activity
//                startActivity(i);
//            }
//
//            @Override
//            public void onTweetContainerClick(View itemView, int position) {
//                // create an intent to display the article
//                Intent i = new Intent(getContext(), TweetActivity.class);
//                // get the article to display
//                Tweet tweet = mTweetList.get(position);
//                // pass objects to the target activity
//                i.putExtra("tweet", Parcels.wrap(tweet));
//                i.putExtra("user", Parcels.wrap(tweet.getUser()));
//                i.putExtra("myProfileImgUrl", me[0].getProfileImageUrl());
//                // launch the activity
//                startActivity(i);
//            }
//
//            @Override
//            public void onReplyClick(int position) {
//                // create an intent to display the article
//                Intent i = new Intent(getActivity(), ReplyActivity.class);
//                // get the article to display
//                Tweet tweet = mTweetList.get(position);
//                // pass objects to the target activity
//                i.putExtra("inReplyToStatusId", tweet.getIdStr());
//                i.putExtra("toName", tweet.getUser().getName());
//                i.putExtra("toScreenName", tweet.getUser().getScreenName());
//                i.putExtra("myProfileImgUrl", me[0].getProfileImageUrl());
//                // launch the activity
//                startActivityForResult(i, RequestCodes.REQUEST_CODE_REPLY);
//            }
//        });
        // Attach the adapter to the RecyclerView to populate items
        rvQuestions.setAdapter(mQuestionsAdapter);
//        RecyclerView.ItemDecoration itemDecoration =
//                new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST);
//        rvQuestions.addItemDecoration(itemDecoration);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvQuestions.setLayoutManager(linearLayoutManager);

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

        // first request: load questions
        loadQuestions();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //lvQuestions.setAdapter(questionsAdapter);
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

    private void loadQuestions() {
        User me = (User) User.getCurrentUser();
        me.getQuestions(new Entry.EntriesListener() {

            @Override
            public void onError(ParseException e) {
                e.printStackTrace();
            }

            @Override
            public void onEntries(List<Entry> questions) {
                mQuestionsAdapter.addAll(questions);
            }
        });
    }
}
