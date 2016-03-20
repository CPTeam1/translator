package com.cp1.translator.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by erioness1125(Hyunji Kim) on 3/12/2016.
 */
public class UsersFragment extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";

    public static UsersFragment newInstance(String title) {
        Bundle args = new Bundle();
        args.putString(ARG_PAGE, title);
        UsersFragment fragment = new UsersFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
