package com.cp1.translator.fragments;

import android.os.Bundle;
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
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

import java.util.List;

/**
 * Created by erioness1125(Hyunji Kim) on 3/12/2016.
 */
public class OthersPageFragment extends PageFragment {

    public static OthersPageFragment newInstance(String title, String myUserName) {
        Bundle args = new Bundle();
        args.putString(ARG_PAGE, title);
        args.putString(MY_USER_NAME, myUserName);
        OthersPageFragment fragment = new OthersPageFragment();
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

        // first request: load questions by others(friends)
        loadBuddyQuestions();

        return view;
    }

    @Override
    protected void refreshEntries() {
        loadBuddyQuestions();
    }

    private void loadBuddyQuestions() {
        User me = (User) User.getCurrentUser();
        // All Posts with questions by friends
        ParseQuery<Post> buddyQsQuery = ParseQuery.getQuery(Post.class);

        /*
        equivalent SQL query (to help you to understand):

        select * from Post
        where Post.question in (select * from Entry where Entry.user in (me.getFriendsRelation) )
         */
        // filter by questions from current user's friends
        ParseRelation<User> friendsRelation = me.getFriendsRelation();
        ParseQuery<User> friendsQuery = friendsRelation.getQuery();
        ParseQuery<Entry> innerQuery = ParseQuery.getQuery(Entry.class);
        innerQuery.whereMatchesQuery(Entry.USER_KEY, friendsQuery);
        buddyQsQuery.whereMatchesQuery(Post.QUESTION_KEY, innerQuery);
        buddyQsQuery.include(Post.QUESTION_KEY);
        buddyQsQuery.include(Post.QUESTION_KEY + "." + Entry.USER_KEY);
        buddyQsQuery.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> postsList, ParseException e) {
                if (e != null) {
                    Log.e("err", "in OthersPageFragment: failed to load Post!");
                    e.printStackTrace();
                } else {
                    mPostsAdapter.addAll(postsList);
                    swipeContainer.setRefreshing(false);
                }
            }
        });
    }
}