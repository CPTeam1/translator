package com.cp1.translator.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.cp1.translator.R;
import com.cp1.translator.fragments.QsContentFragment;
import com.cp1.translator.models.Entry;

import org.parceler.Parcels;

/**
 * Created by erioness1125(Hyunji Kim) on 3/13/2016.
 *
 * PostActivity is for showing the details of a 'Post', which is the model that represents one question and multiple answers.
 * This is created after clicking an item in RecyclerView(the list of questions)
 */
public class PostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.flQsContainer, QsContentFragment.newInstance(
                Parcels.<Entry>unwrap(getIntent().getParcelableExtra(QsContentFragment.ENTRY))
        ));
        ft.addToBackStack(null); // activities are automatically added to the backstack, but fragments must be manually added.
        ft.commit();
    }
}
