package com.cp1.translator.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.cp1.translator.R;
import com.cp1.translator.fragments.AnswerFragment;
import com.cp1.translator.fragments.QsContentFragment;
import com.cp1.translator.models.Entry;
import com.cp1.translator.models.Post;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

import static com.cp1.translator.utils.Constants.APP_TAG;
import static com.cp1.translator.utils.Constants.ENTRY_KEY;

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
        String entryId = getIntent().getStringExtra(ENTRY_KEY);
        ParseQuery<Entry> query = ParseQuery.getQuery(Entry.class);
        query.getInBackground(entryId, new GetCallback<Entry>() {
            @Override
            public void done(Entry object, ParseException e) {
                if (e != null) {
                    Log.e(APP_TAG, "in PostActivity: Error in fetching ParseObject<Entry> from backend!");
                } else {
                    List<Post> postList = queryPost(object);
                    Post post = null;
                    if (postList != null && !postList.isEmpty()) {
                        post = postList.get(0);
                    } else {
                        // if post is null, create new one and save it
                        post = new Post();
                        post.setQuestion(object);
                        post.saveInBackground();
                    }

                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    // question content
                    ft.replace(R.id.flQsContainer, QsContentFragment.newInstance(object));
                    // answers list
                    ft.replace(R.id.flAsContainer, AnswerFragment.newInstance(post));
                    ft.commit();
                }
            }
        });
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
