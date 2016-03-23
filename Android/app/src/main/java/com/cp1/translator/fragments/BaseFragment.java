package com.cp1.translator.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cp1.translator.R;
import com.cp1.translator.utils.SpaceItemDecoration;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by erioness1125(Hyunji Kim) on 3/22/2016.
 */
public abstract class BaseFragment extends Fragment {

    private static final int ITEM_SPACE = 24;

    @Bind(R.id.rvEntries) RecyclerView rvEntries;
    @Bind(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;
    @Bind(R.id.tvEmptyRvEntries) TextView tvEmptyRvEntries;

    public RecyclerView getRvEntries() {
        return rvEntries;
    }

    // Inflate the fragment layout we defined above for this fragment
    // Set the associated text for the title
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_entries, container, false);
        ButterKnife.bind(this, view);

        /********************** RecyclerView **********************/
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

    protected abstract void refreshEntries();
}
