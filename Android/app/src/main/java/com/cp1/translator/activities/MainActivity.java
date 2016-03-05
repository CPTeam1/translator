package com.cp1.translator.activities;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.cp1.translator.R;
import com.cp1.translator.fragments.PageFragment;
import com.cp1.translator.models.User;
import com.cp1.translator.utils.Constants;

import butterknife.ButterKnife;

import static com.cp1.translator.utils.Constants.*;

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

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#55acee")));
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
