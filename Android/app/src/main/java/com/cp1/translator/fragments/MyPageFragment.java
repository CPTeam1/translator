package com.cp1.translator.fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cp1.translator.listeners.EndlessRecyclerViewScrollListener;
import com.cp1.translator.models.Entry;
import com.cp1.translator.models.Post;
import com.cp1.translator.models.User;
import com.cp1.translator.utils.ParseErrorConverter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by erioness1125(Hyunji Kim) on 3/12/2016.
 */
public class MyPageFragment extends PageFragment {

    private static final String LOG_TAG = "MyPageFragment";

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
                refreshEntries();
            }
        });
        /********************** end of SwipeRefreshLayout **********************/

        /********************** RecyclerView **********************/
        // Set layout manager to position the items
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvEntries.setLayoutManager(linearLayoutManager);
        // Add the scroll listener
        rvEntries.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // TODO
            }
        });
        /********************** end of RecyclerView **********************/

        // first request: load questions
        loadPosts(true);

        mFragmentView = view;
        return view;
    }

    private void loadPosts(final boolean isRefresh) {
        User me = (User) User.getCurrentUser();

        /*
        equivalent SQL query (to help you to understand):

        select * from Post
        where Post.question in (select * from Entry where Entry.user = me)
         */
        ParseQuery<Entry> innerQuery = ParseQuery.getQuery(Entry.class);
        innerQuery.whereEqualTo(Entry.USER_KEY, me);
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.whereMatchesQuery(Post.QUESTION_KEY, innerQuery);
        query.include(Post.QUESTION_KEY);
        query.include(Post.QUESTION_KEY + "." + Entry.USER_KEY);
        query.orderByDescending(Post.CREATED_AT);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> postsList, ParseException e) {
                if (e != null) {
                    String errMsg = ParseErrorConverter.getErrMsg(e.getCode());
                    Log.e(LOG_TAG, "Failed to load Post! " + errMsg, e);
                    Snackbar.make(mFragmentView, errMsg, Snackbar.LENGTH_LONG).show();
                } else {
                    if (isRefresh) {
                        mPostsAdapter.setPostsList(postsList);
                        mPostsAdapter.notifyDataSetChanged();
                    }
                    else {
                        mPostsAdapter.addAll(postsList);
                    }
                }

                swipeContainer.setRefreshing(false);
            }
        });
    }

    @Override
    protected void refreshEntries() {
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadPosts(true);
            }
        }, 1000);
    }

    @Override
    protected String getClassName() {
        return MyPageFragment.class.getName();
    }
}
