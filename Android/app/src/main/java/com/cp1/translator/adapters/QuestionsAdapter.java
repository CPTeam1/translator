package com.cp1.translator.adapters;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cp1.translator.R;
import com.cp1.translator.models.Entry;
import com.cp1.translator.models.Types;
import com.parse.ParseException;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.cp1.translator.utils.Constants.APP_TAG;

/**
 * Created by pandeis on 3/9/16.
 * Changed ArrayAdapter to RecyclerView.Adapter by erioness1125(Hyunji Kim) on 03/12/16.
 */
public class QuestionsAdapter extends EntriesAdapter {

    private final int MY_QS = 1;
    private final int OTHERS_QS = 2;

    private String mMyUserName;

    public QuestionsAdapter(List<Entry> questionList, String myUserName) {
        mEntriesList = questionList;
        mMyUserName = myUserName;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        RecyclerView.ViewHolder viewHolder = null;
        View view;

        switch (viewType) {
            case MY_QS:
                // Inflate the custom layout
                view = inflater.inflate(R.layout.item_question, parent, false);

                // Return a new holder instance
                viewHolder = new MyQuestionViewHolder(view);
                break;
            case OTHERS_QS:
                // Inflate the custom layout
                view = inflater.inflate(R.layout.item_question_others, parent, false);

                // Return a new holder instance
                viewHolder = new OthersQuestionViewHolder(view);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // Get the data model based on position
        Entry question = mEntriesList.get(position);

        switch (holder.getItemViewType()) {
            case MY_QS:
                populateMyQuestions((MyQuestionViewHolder) holder, question);
                break;
            case OTHERS_QS:
                populateOthersQuestions((OthersQuestionViewHolder) holder, question);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        Entry question = mEntriesList.get(position);
        Log.d(APP_TAG,"Position: "+position + "Entry: "+question);
        Log.d(APP_TAG,"user: "+question.getUser());
        String askedBy = question.getUserID();
        if (mMyUserName.equals(askedBy)) {
            return MY_QS;
        }
        else {
            return OTHERS_QS;
        }
    }

    private void populateMyQuestions(MyQuestionViewHolder holder, Entry question) {
        // type of the question
        Drawable typeDrawble = ContextCompat.getDrawable(context, R.drawable.shape_qs_txt);

        // Asssume its a Text Question by default
        // As we dont want the switch case to crash :)
        String type = Types.TEXT;

        if(question.getType() != null)
            type = question.getType();

        switch (type) {
            case Types.PICTURE:
                typeDrawble = ContextCompat.getDrawable(context, R.drawable.shape_qs_pic);
                break;
            case Types.AUDIO:
                typeDrawble = ContextCompat.getDrawable(context, R.drawable.shape_qs_voice);
                break;
            case Types.VIDEO:
                typeDrawble = ContextCompat.getDrawable(context, R.drawable.shape_qs_video);
                break;
        }
        holder.ivQuestionType.setBackground(typeDrawble);

        // question title (=text)
        holder.tvQuestionTitle.setText(question.getQuestionText());

        // the number of replies to this question
        holder.tvRepliesCount.setText("[0]");
    }

    private void populateOthersQuestions(OthersQuestionViewHolder holder, Entry question) {
        // set user icon
        try {
            holder.tvOthersQsUser.setText(question.getUser().fetchIfNeeded().getUsername().substring(0, 3));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // type of the question
        Drawable typeDrawble = ContextCompat.getDrawable(context, R.drawable.shape_qs_txt);
        // Asssume its a Text Question by default
        // As we dont want the switch case to crash :)
        String type = Types.TEXT;

        if(question.getType() != null)
            type = question.getType();

        switch (type) {
            case Types.PICTURE:
                typeDrawble = ContextCompat.getDrawable(context, R.drawable.shape_qs_pic);
                break;
            case Types.AUDIO:
                typeDrawble = ContextCompat.getDrawable(context, R.drawable.shape_qs_voice);
                break;
            case Types.VIDEO:
                typeDrawble = ContextCompat.getDrawable(context, R.drawable.shape_qs_video);
                break;
        }
        holder.ivOthersQsType.setBackground(typeDrawble);

        // question title (=text)
        holder.tvOthersQsTitle.setText(question.getText());

        // the number of replies to this question
        holder.tvOthersRplCnt.setText("[0]");
    }

    // ViewHolder for ASK QS tab
    public class MyQuestionViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.llQsContainer) LinearLayout llQsContainer;
        @Bind(R.id.ivQuestionType) ImageView ivQuestionType;
        @Bind(R.id.tvQuestionTitle) TextView tvQuestionTitle;
        @Bind(R.id.tvRepliesCount) TextView tvRepliesCount;

        public MyQuestionViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            // Setup the click listener
            llQsContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Triggers click upwards to the adapter on click
                    if (listener != null)
                        listener.onClickItem(itemView, getLayoutPosition());
                }
            });
        }
    }

    // ViewHolder for ANSWER QS tab
    public class OthersQuestionViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.llOthersQsContainer) LinearLayout llOthersQsContainer;
        @Bind(R.id.ivOthersQsType) ImageView ivOthersQsType;
        @Bind(R.id.tvOthersQsUser) TextView tvOthersQsUser;
        @Bind(R.id.tvOthersQsTitle) TextView tvOthersQsTitle;
        @Bind(R.id.tvOthersRplCnt) TextView tvOthersRplCnt;

        public OthersQuestionViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            // Setup the click listener
            llOthersQsContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Triggers click upwards to the adapter on click
                    if (listener != null)
                        listener.onClickItem(itemView, getLayoutPosition());
                }
            });
        }
    }
}