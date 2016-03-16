package com.cp1.translator.adapters;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
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

/**
 * Created by kimhy08 on 3/15/2016.
 */
public class AnswersAdapter extends EntriesAdapter {

    public AnswersAdapter(List<Entry> answersList) {
        mEntriesList = answersList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.item_answer, parent, false);

        // Return a new holder instance
        return new AnswersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // Get the data model based on position
        Entry answer = mEntriesList.get(position);
        populateAnswers((AnswersViewHolder) holder, answer);
    }

    private void populateAnswers(AnswersViewHolder holder, Entry answer) {
        try {
            holder.tvAnswerUser.setText(answer.getUser().fetchIfNeeded().getUsername().substring(0, 3));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // type of the answer
        Drawable typeDrawble = ContextCompat.getDrawable(context, R.drawable.shape_qs_txt);

        // Assume its a Text answer by default
        // As we don't want the switch case to crash :)
        String type = Types.TEXT;

        if(answer.getType() != null)
            type = answer.getType();

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
        holder.ivAnswerType.setBackground(typeDrawble);

        // answer title (=text)
        holder.tvAnswerTitle.setText(answer.getText());

        // TODO
        // set likes count
    }

    public class AnswersViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.llAnswerContainer) LinearLayout llAnswerContainer;
        @Bind(R.id.ivAnswerType) ImageView ivAnswerType;
        @Bind(R.id.ivAnswerLike) ImageView ivAnswerLike;
        @Bind(R.id.tvAnswerUser) TextView tvAnswerUser;
        @Bind(R.id.tvAnswerTitle) TextView tvAnswerTitle;
        @Bind(R.id.tvAnswerLikesCnt) TextView tvAnswerLikesCnt;

        public AnswersViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            // Setup the click listener
            llAnswerContainer.setOnClickListener(new View.OnClickListener() {
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
