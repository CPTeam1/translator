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
import android.widget.TextView;

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

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.cp1.translator.utils.Constants.APP_TAG;
import static com.cp1.translator.utils.Constants.POST_KEY;

/**
 * Created by erioness1125(Hyunji Kim) on 3/13/2016.
 *
 * PostActivity is for showing the details of a 'Post', which is the model that represents one question and multiple answers.
 * This is created after clicking an item in RecyclerView(the list of questions)
 */
public class PostActivity extends AppCompatActivity {

    private FragmentManager fm;

    @Bind(R.id.tvQuestionUser)
    TextView tvQuestionUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        ButterKnife.bind(this);

        setTitle("Question");

        // set behavior of FAB
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAnswerDialog();
            }
        });

        // query Post object
        String postObjectId = getIntent().getStringExtra(POST_KEY);
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.QUESTION_KEY);
        query.include(Post.QUESTION_KEY + "." + Entry.USER_KEY);
        query.getInBackground(postObjectId, new GetCallback<Post>() {
            @Override
            public void done(Post post, ParseException e) {
                if (e != null) {
                    Log.e(APP_TAG, "in PostActivity: Error in fetching ParseObject<Post> from backend!");
                } else {
                    Log.d(APP_TAG, "in PostActivity: Got Post!");

                    fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();

                    Entry question = post.getQuestion();
                    String imgUrl = null;
                    String qsType = question.getType();
                    if (qsType == null || qsType.trim().isEmpty())
                        qsType = Types.TEXT;
                    switch (qsType) {
                        case Types.AUDIO:
                            // AudioPlayerFragment
                            String audioUrl = question.getAudioUrl().getUrl();
                            ft.replace(R.id.flMediaContainer, AudioPlayerFragment.newInstance(audioUrl));

                            break;
                        case Types.VIDEO:
                            // VideoPlayerFragment
                            String videoUrl = question.getVideoUrl().getUrl();
                            ft.replace(R.id.flMediaContainer, VideoPlayerFragment.newInstance(videoUrl));

                            break;
                        case Types.PICTURE:
                            imgUrl = question.getImageUrl().getUrl();
                            break;
                    }

                    User qsUser = question.getUser();
                    tvQuestionUser.setText(qsUser.getNickname());

                    // question content
                    ft.replace(R.id.flQsContainer, QsContentFragment.newInstance(imgUrl, question.getText()));
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

    private void updateAnswerFragment(Entry answer) {
        Fragment fragment = fm.findFragmentById(R.id.flAsContainer);
        if (fragment instanceof AnswerFragment) {
            User currUser = (User) User.getCurrentUser();
            answer.setUser(currUser);
            // add answer to Post
            ((AnswerFragment) fragment).addAnswerToPost(answer);
//            ((AnswerFragment) fragment).addEntry(answer);
            ((AnswerFragment) fragment).getRvEntries().getLayoutManager().scrollToPosition(0);
        }
    }
}
