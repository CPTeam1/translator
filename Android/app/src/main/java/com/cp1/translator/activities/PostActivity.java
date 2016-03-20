package com.cp1.translator.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.cp1.translator.R;
import com.cp1.translator.fragments.AnswerFragment;
import com.cp1.translator.fragments.AudioPlayerFragment;
import com.cp1.translator.fragments.QsContentFragment;
import com.cp1.translator.fragments.VideoPlayerFragment;
import com.cp1.translator.models.Entry;
import com.cp1.translator.models.Post;
import com.cp1.translator.models.Types;
import com.cp1.translator.models.User;
import com.cp1.translator.utils.Constants;
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

    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        // set behavior of FAB
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAnswerDialog();
            }
        });

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

                    fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();

                    String imgUrl = null;
                    String qsType = object.getType();
                    if (qsType == null || qsType.trim().isEmpty())
                        qsType = Types.TEXT;
                    switch (qsType) {
                        case Types.AUDIO:
                            // AudioPlayerFragment
                            String audioUrl = object.getAudioUrl().getUrl();
                            ft.replace(R.id.flMediaContainer, AudioPlayerFragment.newInstance(audioUrl));

                            break;
                        case Types.VIDEO:
                            // VideoPlayerFragment
                            String videoUrl = object.getVideoUrl().getUrl();
                            ft.replace(R.id.flMediaContainer, VideoPlayerFragment.newInstance(videoUrl));

                            break;
                        case Types.PICTURE:
                            imgUrl = object.getImageUrl().getUrl();
                            break;
                    }

                    // question content
                    ft.replace(R.id.flQsContainer, QsContentFragment.newInstance(imgUrl, object.getText()));
                    // answers list
                    ft.replace(R.id.flAsContainer, AnswerFragment.newInstance(post));
                    ft.commit();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode ==  Constants.ASK_QS_REQ_CODE){
            Entry answer = data.getParcelableExtra("question");
            ParseQuery<Entry> query = ParseQuery.getQuery(Entry.class);
            query.whereEqualTo(Entry.TEXT_KEY, answer.getQuestionText());
            query.whereEqualTo(Entry.USER_KEY, (User) User.getCurrentUser());
            try {
                List<Entry> entriesList = query.find();
                if (entriesList != null && !entriesList.isEmpty()) {
                    answer = entriesList.get(0);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            updateAnswerFragment(answer);
        }
    }

    private void showAnswerDialog() {
        // start AskQuestion Activity
        Intent intent = new Intent(this, AskQuestion.class);
        intent.putExtra(Constants.IS_ANSWER_KEY, true);
        startActivityForResult(intent, Constants.ASK_QS_REQ_CODE);
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

    private void updateAnswerFragment(Entry answer) {
        Fragment fragment = fm.findFragmentById(R.id.flAsContainer);
        if (fragment instanceof AnswerFragment) {
            User currUser = (User) User.getCurrentUser();
            answer.setUser(currUser);
            // add answer to Post
            ((AnswerFragment) fragment).addAnswerToPost(answer);
            ((AnswerFragment) fragment).addEntry(answer);
            ((AnswerFragment) fragment).getRvEntries().getLayoutManager().scrollToPosition(0);
        }
    }
}
