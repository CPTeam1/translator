package com.cp1.translator.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cp1.translator.R;
import com.cp1.translator.activities.AnswerActivity;
import com.cp1.translator.adapters.AnswersAdapter;
import com.cp1.translator.adapters.EntriesAdapter;
import com.cp1.translator.models.Entry;
import com.cp1.translator.models.Post;
import com.cp1.translator.utils.Constants;
import com.cp1.translator.utils.SpaceItemDecoration;
import com.parse.ParseException;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by kimhy08 on 3/15/2016.
 */
public class AnswerFragment extends PageFragment {

    private static final int ITEM_SPACE = 12;

    private Post mPost;

    public static AnswerFragment newInstance(Post post) {
        AnswerFragment fragment = new AnswerFragment();
        fragment.mPost = post;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_entries, container, false);
        ButterKnife.bind(this, view);

        /********************** RecyclerView **********************/
        mEntries = new ArrayList<>();
        mEntriesAdapter = new AnswersAdapter(mEntries);
        // listener for the event of clicking an item in RecyclerView
        mEntriesAdapter.setOnClickItemListener(new EntriesAdapter.OnClickItemListener() {

            @Override
            public void onClickItem(View itemView, int position) {
                // create an intent to display the article
                Intent i = new Intent(getContext(), AnswerActivity.class);
                // get the article to display
                Entry answer = mEntries.get(position);
                // pass objects to the target activity
                i.putExtra(Constants.ENTRY_KEY, Parcels.wrap(answer));
                // launch the activity
                startActivity(i);
            }
        });
        mEntriesAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                showHideEmptyView();
            }
        });
        // Attach the adapter to the RecyclerView to populate items
        rvEntries.setAdapter(mEntriesAdapter);
        // Set layout manager to position the items
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvEntries.setLayoutManager(linearLayoutManager);
        // add ItemDecoration
        rvEntries.addItemDecoration(new SpaceItemDecoration(ITEM_SPACE));
        /********************** end of RecyclerView **********************/

        /********************** SwipeRefreshLayout **********************/
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshEntries();
            }
        });
        /********************** end of SwipeRefreshLayout **********************/

        // load answers
        mPost.getAnswers(new Entry.EntriesListener() {
            @Override
            public void onEntries(List<Entry> answersList) {
                loadAnswers(answersList);
            }

            @Override
            public void onError(ParseException e) {
                e.printStackTrace();
            }
        });

        return view;
    }

    private void loadAnswers(List<Entry> answersList) {
        mEntriesAdapter.addAll(answersList);
    }

    private void showHideEmptyView() {
        // show emptyView message if answersList is empty
        if (mEntriesAdapter.getItemCount() == 0) {
            tvEmptyRvEntries.setText(getString(R.string.add_first_answer_label));
            tvEmptyRvEntries.setVisibility(View.VISIBLE);
            swipeContainer.setVisibility(View.GONE);
        } else {
            tvEmptyRvEntries.setVisibility(View.GONE);
            swipeContainer.setVisibility(View.VISIBLE);
        }
    }

    public void addAnswerToPost(Entry answer) {
        mPost.addAnswer(answer);
        mPost.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e("err", "in AnswerFragment: Error occured while adding the Parcelable<Entry>(answer) to the Post!");
                }
                else {
                    Log.i("info", "in AnswerFragment: Successfully added the Parcelable<Entry>(answer) to the Post!");
                }
            }
        });
    }

    @Override
    protected void refreshEntries() {
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeContainer.setRefreshing(false);
            }
        }, 1000);
    }
}