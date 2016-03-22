package com.cp1.translator.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cp1.translator.R;
import com.cp1.translator.activities.PostActivity;
import com.cp1.translator.adapters.PostsAdapter;
import com.cp1.translator.models.Post;
import com.cp1.translator.utils.Constants;
import com.cp1.translator.utils.SpaceItemDecoration;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

// In this case, the fragment displays simple text based on the page
public abstract class PageFragment extends Fragment {

    private static final int ITEM_SPACE = 24;
    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String MY_USER_NAME = "MY_USER_NAME";

    private String mMyUserName;

    protected PostsAdapter mPostsAdapter;
    protected View mFragmentView;

    @Bind(R.id.rvEntries) RecyclerView rvEntries;
    @Bind(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;
    @Bind(R.id.tvEmptyRvEntries) TextView tvEmptyRvEntries;

    public RecyclerView getRvEntries() {
        return rvEntries;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_entries, container, false);
        ButterKnife.bind(this, view);

        mMyUserName = getArguments().getString(MY_USER_NAME);

        /********************** RecyclerView **********************/
        mPostsAdapter = new PostsAdapter(new ArrayList<Post>(), mMyUserName);
        // listener for the event of clicking an item in RecyclerView
        mPostsAdapter.setOnClickItemListener(new PostsAdapter.OnClickItemListener() {

            @Override
            public void onClickItem(View itemView, int position) {
                // create an intent to display the article
                Intent i = new Intent(getContext(), PostActivity.class);
                // get the article to display
                Post post = mPostsAdapter.getPostsList().get(position);
                String postObjectId = post.getObjectId();
                // pass objects to the target activity
                i.putExtra(Constants.POST_KEY, postObjectId);
                // launch the activity
                startActivity(i);
            }
        });
        mPostsAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                showHideEmptyView();
            }
        });
        // Attach the adapter to the RecyclerView to populate items
        rvEntries.setAdapter(mPostsAdapter);
        // add ItemDecoration
        rvEntries.addItemDecoration(new SpaceItemDecoration(ITEM_SPACE));
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

    public void addPost(Post post) {
        if (mPostsAdapter.getPostsList() == null) {
            mPostsAdapter.setPostsList(new ArrayList<Post>());
        }
        mPostsAdapter.add(post);
    }

    private void showHideEmptyView() {
        // show emptyView message if answersList is empty
        if (mPostsAdapter.getItemCount() == 0) {
            tvEmptyRvEntries.setText(getString(R.string.say_something_label));
            tvEmptyRvEntries.setVisibility(View.VISIBLE);
            swipeContainer.setVisibility(View.GONE);
        } else {
            tvEmptyRvEntries.setVisibility(View.GONE);
            swipeContainer.setVisibility(View.VISIBLE);
        }
    }

    protected abstract void refreshEntries();

}
