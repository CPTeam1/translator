package com.cp1.translator.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.cp1.translator.R;
import com.cp1.translator.fragments.AskQuestionFragment;
import com.cp1.translator.fragments.PageFragment;
import com.cp1.translator.friends.FriendsActivity;
import com.cp1.translator.models.User;

import butterknife.ButterKnife;

import static com.cp1.translator.utils.Constants.APP_TAG;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toast.makeText(this, "Logged in as: " + User.getCurrentUser().getUsername(), Toast.LENGTH_SHORT).show();

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()));

        // Give the PagerSlidingTabStrip the ViewPager
        PagerSlidingTabStrip tabsStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        // Attach the view pager to the tab strip
        tabsStrip.setViewPager(viewPager);


        // from Hyunji: commented below as AppTheme color is set
//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#55acee")));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // set listener of each menu item
        // Search
        MenuItem searchItem = menu.findItem(R.id.miSearch);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here

                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    public void onNewQuestionClick(MenuItem item) {
        // start AskQuestion Activity

        FragmentManager fm = getSupportFragmentManager();
        AskQuestionFragment askQuestionFragment = AskQuestionFragment.newInstance();
//        askQuestionFragment.setTargetFragment(new PageFragment(),300);
        askQuestionFragment.show(fm,"AskQuestion");


//        Intent intent = new Intent(this, AskQuestion.class);
//        startActivity(intent);
    }

    public void onSettingsClick(MenuItem item) {
        // launch Settings View
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void onFriendsClick(MenuItem item) {
        Intent intent = new Intent(this, FriendsActivity.class);
        startActivity(intent);
    }

    public class FragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {
        private String tabTitles[] = new String[] { "Ask Qs", "Answer Qs","Top Users" };

        public FragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public Fragment getItem(int position) {
            Log.d(APP_TAG,"position: "+position);
            switch(position){
                case 0: {
                    return PageFragment.newInstance(tabTitles[0]);
                }
                case 1: {
                    return PageFragment.newInstance(tabTitles[1]);
                }
                case 2: {
                    return PageFragment.newInstance(tabTitles[2]);
                }
                default: return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return tabTitles[position];
        }
    }

}
