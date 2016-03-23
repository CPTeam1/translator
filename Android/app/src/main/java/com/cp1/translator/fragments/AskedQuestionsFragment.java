package com.cp1.translator.fragments;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cp1.translator.listeners.EndlessRecyclerViewScrollListener;
import com.cp1.translator.models.Entry;
import com.cp1.translator.models.Lang;
import com.cp1.translator.models.Post;
import com.cp1.translator.models.User;
import com.cp1.translator.utils.Constants;
import com.cp1.translator.utils.ParseErrorConverter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.cp1.translator.utils.Constants.*;

/**
 * Created by erioness1125(Hyunji Kim) on 3/12/2016.
 */
public class AskedQuestionsFragment extends PageFragment {

    public static final String ARG_PAGE = "ARG_PAGE";
    private List<String> mLangList;

    public static AskedQuestionsFragment newInstance(String title, String myUserName) {
        Bundle args = new Bundle();
        args.putString(ARG_PAGE, title);
        args.putString(MY_USER_NAME, myUserName);
        AskedQuestionsFragment fragment = new AskedQuestionsFragment();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        // load languages
        mLangList = new ArrayList<>();

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
        final User me = (User) User.getCurrentUser();

        // Get my skills
        me.getLangs(new Lang.LangsListener() {
            @Override
            public void onLangs(List<Lang> langs) {
                mLangList.addAll(convertLangsToStrings(langs));
                Log.d(APP_TAG,"Skills: "+mLangList.toString());
                // All Posts with questions by friends
                ParseQuery<Post> buddyQsQuery = ParseQuery.getQuery(Post.class);

                // filter by questions from current user's friends
                ParseRelation<User> friendsRelation = me.getFriendsRelation();
                ParseQuery<User> friendsQuery = friendsRelation.getQuery();
                ParseQuery<Entry> innerQuery = ParseQuery.getQuery(Entry.class);
                innerQuery.whereMatchesQuery(Entry.USER_KEY, friendsQuery);
                buddyQsQuery.whereMatchesQuery(Post.QUESTION_KEY, innerQuery);

                // Check this query
                buddyQsQuery.whereContainedIn(Post.TO_LANG_KEY,mLangList);

                buddyQsQuery.include(Post.QUESTION_KEY);
                buddyQsQuery.include(Post.QUESTION_KEY + "." + Entry.USER_KEY);

                buddyQsQuery.orderByDescending(Post.CREATED_AT);
                buddyQsQuery.findInBackground(new FindCallback<Post>() {
                    @Override
                    public void done(List<Post> postsList, ParseException e) {
                        if (e != null) {
                            String errMsg = ParseErrorConverter.getErrMsg(e.getCode());
                            Log.e(APP_TAG, "Failed to load Post! " + errMsg, e);
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
            public void onError(ParseException e) {
                e.printStackTrace();
            }
        });

    }

    private List<String> convertLangsToStrings(List<Lang> langs) {
        if(langs == null)
            return null;
        else{
            List<String> langStr = new ArrayList<>();
            for(Lang l: langs){
                langStr.add(l.getName());
            }
            return langStr;
        }
    }


    @Override
    protected String getClassName() {
        return AskedQuestionsFragment.class.getName();
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
}
