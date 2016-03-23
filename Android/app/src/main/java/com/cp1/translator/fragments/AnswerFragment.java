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

import com.cp1.translator.activities.AnswerActivity;
import com.cp1.translator.adapters.AnswersAdapter;
import com.cp1.translator.models.Entry;
import com.cp1.translator.models.Post;
import com.cp1.translator.utils.Constants;
import com.parse.ParseException;
import com.parse.SaveCallback;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by erioness1125(Hyunji Kim) on 3/15/2016.
 */
public class AnswerFragment extends BaseFragment {

    private AnswersAdapter mAnswersAdapter;
    private Post mPost;
    private String emptyViewStr;

    public static AnswerFragment newInstance(Post post, String emptyViewStr) {
        AnswerFragment fragment = new AnswerFragment();
        fragment.mPost = post;
        fragment.emptyViewStr = emptyViewStr;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        /********************** RecyclerView **********************/
        mAnswersAdapter = new AnswersAdapter(new ArrayList<Entry>());
        // listener for the event of clicking an item in RecyclerView
        mAnswersAdapter.setOnClickItemListener(new AnswersAdapter.OnClickItemListener() {

            @Override
            public void onClickItem(View itemView, int position) {
                // create an intent to display the article
                Intent i = new Intent(getContext(), AnswerActivity.class);
                // get the article to display
                Entry answer = mAnswersAdapter.getAnswersList().get(position);
                // pass objects to the target activity
                i.putExtra(Constants.ENTRY_KEY, Parcels.wrap(answer));
                // launch the activity
                startActivity(i);
            }
        });
        mAnswersAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                showHideEmptyView();
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                showHideEmptyView();
            }
        });
        // Attach the adapter to the RecyclerView to populate items
        rvEntries.setAdapter(mAnswersAdapter);
        // Set layout manager to position the items
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvEntries.setLayoutManager(linearLayoutManager);
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
        pbLoadingEntries.setVisibility(View.VISIBLE);
        mPost.getAnswers(new Entry.EntriesListener() {
            @Override
            public void onEntries(List<Entry> answersList) {
                loadAnswers(answersList);
                pbLoadingEntries.setVisibility(View.GONE);
            }

            @Override
            public void onError(ParseException e) {
                e.printStackTrace();
                pbLoadingEntries.setVisibility(View.GONE);
            }
        });

        return view;
    }

    private void loadAnswers(List<Entry> answersList) {
        mAnswersAdapter.addAll(answersList);
    }

    private void showHideEmptyView() {
        // show emptyView message if answersList is empty
        if (mAnswersAdapter.getItemCount() == 0) {
            tvEmptyRvEntries.setText(emptyViewStr);
            tvEmptyRvEntries.setVisibility(View.VISIBLE);
            swipeContainer.setVisibility(View.GONE);
        } else {
            tvEmptyRvEntries.setVisibility(View.GONE);
            swipeContainer.setVisibility(View.VISIBLE);
        }
    }

    public void addAnswerToPost(final Entry answer) {
        pbLoadingEntries.setVisibility(View.VISIBLE);
        mPost.addAnswer(answer);
        mPost.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e("err", "in AnswerFragment: Error occured while adding the Parcelable<Entry>(answer) to the Post!");
                }
                else {
                    Log.i("info", "in AnswerFragment: Successfully added the Parcelable<Entry>(answer) to the Post!");

                    mAnswersAdapter.add(answer);
                }
                pbLoadingEntries.setVisibility(View.GONE);
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