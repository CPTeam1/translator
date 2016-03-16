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
import com.cp1.translator.adapters.EntriesAdapter;
import com.cp1.translator.adapters.QuestionsAdapter;
import com.cp1.translator.models.Entry;
import com.cp1.translator.utils.Constants;
import com.cp1.translator.utils.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

// In this case, the fragment displays simple text based on the page
public abstract class PageFragment extends Fragment {

    private static final int ITEM_SPACE = 24;
    public static final String ARG_PAGE = "ARG_PAGE";
    public static final String MY_USER_NAME = "MY_USER_NAME";

    private String mMyUserName;

    protected EntriesAdapter mEntriesAdapter;
    protected List<Entry> mEntries;

    @Bind(R.id.rvEntries) RecyclerView rvEntries;
    @Bind(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;
    @Bind(R.id.tvEmptyRvEntries) TextView tvEmptyRvEntries;

    public RecyclerView getRvEntries() {
        return rvEntries;
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
        View view = inflater.inflate(R.layout.fragment_entries, container, false);
        ButterKnife.bind(this, view);

        mMyUserName = getArguments().getString(MY_USER_NAME);

        /********************** RecyclerView **********************/
        mEntries = new ArrayList<>();
        mEntriesAdapter = new QuestionsAdapter(mEntries, mMyUserName);
        // listener for the event of clicking an item in RecyclerView
        mEntriesAdapter.setOnClickItemListener(new EntriesAdapter.OnClickItemListener() {

            @Override
            public void onClickItem(View itemView, int position) {
                // create an intent to display the article
                Intent i = new Intent(getContext(), PostActivity.class);
                // get the article to display
                Entry entry = mEntries.get(position);
                String entryId = entry.getObjectId();
                // pass objects to the target activity
                i.putExtra(Constants.ENTRY_KEY, entryId);
                // launch the activity
                startActivity(i);
            }
        });
        // Attach the adapter to the RecyclerView to populate items
        rvEntries.setAdapter(mEntriesAdapter);
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

    public void addEntry(Entry question) {
        if (mEntries == null) {
            mEntries = new ArrayList<>();
            mEntriesAdapter.setEntriesList(mEntries);
        }
        mEntriesAdapter.add(question);
    }

    protected abstract void refreshEntries();

}
