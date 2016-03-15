package com.cp1.translator.activities;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.cp1.translator.R;
import com.cp1.translator.fragments.AnswerFragment;
import com.cp1.translator.fragments.QsContentFragment;
import com.cp1.translator.models.Entry;
import com.cp1.translator.models.Post;
import com.cp1.translator.utils.Constants;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.parceler.Parcels;

import java.util.List;

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

        // query Post object
        Parcelable qsParcelable = getIntent().getParcelableExtra(Constants.ENTRY_KEY);
        Entry question = (Entry) Parcels.unwrap(qsParcelable);
        List<Post> postList = queryPost(question);
        Post post = null;
        if (postList != null && !postList.isEmpty()) {
            post = postList.get(0);
        }

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        // question content
        ft.replace(R.id.flQsContainer, QsContentFragment.newInstance(qsParcelable));
        // answers list
        ft.replace(R.id.flAsContainer, AnswerFragment.newInstance(Parcels.wrap(post)));
        ft.commit();
    }

    private List<Post> queryPost(Entry question) {
        List<Post> postList = null;
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.whereEqualTo(Post.QUESTION_KEY, question);
        try {
            postList = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return postList;
    }
}
