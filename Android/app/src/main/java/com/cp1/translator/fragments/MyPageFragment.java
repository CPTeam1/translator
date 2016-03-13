package com.cp1.translator.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cp1.translator.listeners.EndlessRecyclerViewScrollListener;
import com.cp1.translator.models.Entry;
import com.cp1.translator.models.User;
import com.parse.ParseException;

import java.util.List;

/**
 * Created by kimhy08 on 3/12/2016.
 */
public class MyPageFragment extends PageFragment {

    public static MyPageFragment newInstance(String title, String myUserName) {
        Bundle args = new Bundle();
        args.putString(ARG_PAGE, title);
        args.putString(MY_USER_NAME, myUserName);
        MyPageFragment fragment = new MyPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        /********************** SwipeRefreshLayout **********************/
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // TODO
            }
        });
        /********************** end of SwipeRefreshLayout **********************/

        /********************** RecyclerView **********************/
        // Set layout manager to position the items
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvQuestions.setLayoutManager(linearLayoutManager);
        // Add the scroll listener
        rvQuestions.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // TODO
            }
        });
        /********************** end of RecyclerView **********************/

        // first request: load questions
        loadQuestions();

        return view;
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
