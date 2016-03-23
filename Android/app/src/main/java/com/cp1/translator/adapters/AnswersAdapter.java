package com.cp1.translator.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cp1.translator.R;
import com.cp1.translator.models.Entry;
import com.cp1.translator.models.Types;
import com.cp1.translator.models.User;
import com.cp1.translator.utils.Constants;
import com.parse.ParseException;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by erioness1125(Hyunji Kim) on 3/15/2016.
 */
public class AnswersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected Context mContext;
    protected List<Entry> mAnswersList;

    // Define listener member variable
    protected OnClickItemListener listener;

    // Define the listener interface
    public interface OnClickItemListener {
        void onClickItem(View itemView, int position);
    }

    public void setOnClickItemListener(OnClickItemListener listener) {
        this.listener = listener;
    }

    public AnswersAdapter(List<Entry> answersList) {
        mAnswersList = answersList;
    }

    // Add a list of answers
    public void addAll(List<Entry> answersList) {
//        int sizeOfListBeforeAdding = mQuestionList.size();
        mAnswersList.addAll(answersList);
        notifyDataSetChanged();
//        notifyItemRangeInserted(sizeOfListBeforeAdding, questionList.size());
    }

    // Add an answer to the list
    public void add(Entry answer) {
        add(0, answer);
    }

    // Add an answer to the list at specific position
    public void add(int idx, Entry answer) {
        mAnswersList.add(idx, answer);
        notifyItemInserted(idx);
//        notifyDataSetChanged();
    }

    public void clear() {
        mAnswersList.clear();
        notifyDataSetChanged();
    }

    public void setEntriesList(List<Entry> entriesList) {
        mAnswersList = entriesList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);

        // Return a new holder instance
        return new AnswersViewHolder(
                inflater.inflate(R.layout.item_answer, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // Get the data model based on position
        Entry answer = mAnswersList.get(position);
        populateAnswers((AnswersViewHolder) holder, answer);
    }

    @Override
    public int getItemCount() {
        return mAnswersList.size();
    }

    private void populateAnswers(AnswersViewHolder holder, Entry answer) {
        // set time when the answer is created and added to the Post
        TextView tvTimeAnswered = (TextView) holder.includeTimeAnswered.findViewById(R.id.tvLabel);
        String relativeTimeStamp = Constants.getRelativeTimeAgo(answer.getCreatedAt());
        tvTimeAnswered.setText(relativeTimeStamp);

        // answered by
        TextView tvAnsweredBy = (TextView) holder.includeAnsweredBy.findViewById(R.id.tvLabel);
        // get User's nickname
        String nickname = "";
        try {
            User answerer = (User) answer.getUser().fetchIfNeeded();
            nickname = answerer.getNickname();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String askedBy = "From: " + nickname;
        tvAnsweredBy.setText(askedBy);

        View included = holder.includeAs;

        // clear ImageView(findMediaView)
        ImageView ivEntryMediaIcon = findMediaView(included);
        ivEntryMediaIcon.setImageResource(0);

        // clear ImageView(ivEntryPic)
        ImageView ivEntryPic = findImageView(included);
        ivEntryPic.setImageResource(0);

        // set TextView(tvEntryText)
        setEntryText(included, answer.getText());

        // Assume type=TEXT by default
        // As we don't want the switch case to crash :)
        String type = Types.TEXT;
        if (answer.getType() != null)
            type = answer.getType();
        switch (type) {
            case Types.PICTURE:
                String imgUrl = answer.getImageUrl().getUrl();

                // load img onto ivEntryPic
                Picasso.with(mContext)
                        .load(imgUrl)
                        .resize(600, 0)
                        .into(ivEntryPic);

                break;

            case Types.AUDIO:
                ivEntryMediaIcon.setImageResource(R.drawable.shape_qs_voice);

                break;

            case Types.VIDEO:
                ivEntryMediaIcon.setImageResource(R.drawable.shape_qs_video);

                break;
        }

    }

    private void setEntryText(View included, String text) {
        View vsTextAfterInflated;

        ViewStub vsText = (ViewStub) included.findViewById(R.id.vsText);
        if (vsText != null) {
            vsTextAfterInflated = ((ViewStub) included.findViewById(R.id.vsText)).inflate();
        }
        else {
            /*
            Once visible/inflated, the ViewStub element is no longer part of the view hierarchy.
            It is replaced by the inflated layout and the ID for the root view of that layout is the one
            specified by the android:inflatedId attribute of the ViewStub.
             */
            vsTextAfterInflated = included.findViewById(R.id.vsTextAfter);
        }

        // find TextView
        TextView tvEntryText = (TextView) vsTextAfterInflated.findViewById(R.id.tvEntryText);

        // set answer text
        tvEntryText.setText(text);
    }

    private ImageView findImageView(View included) {
        View vsPicAfterInflated;
        ViewStub vsPic = (ViewStub) included.findViewById(R.id.vsPic);
        if (vsPic != null) {
            vsPicAfterInflated = vsPic.inflate();
        }
        else {
            vsPicAfterInflated = included.findViewById(R.id.vsPicAfter);
        }

        // find ImageView
        ImageView ivEntryPic = (ImageView) vsPicAfterInflated.findViewById(R.id.ivEntryPic);
        return ivEntryPic;
    }

    private ImageView findMediaView(View included) {
        View vsMediaAfterInflated;
        ViewStub vsMedia = (ViewStub) included.findViewById(R.id.vsMedia);
        if (vsMedia != null) {
            vsMediaAfterInflated = vsMedia.inflate();
        }
        else {
            vsMediaAfterInflated = included.findViewById(R.id.vsMediaAfter);
        }

        // find ImageView(ivEntryMediaIcon)
        ImageView ivEntryMediaIcon = (ImageView) vsMediaAfterInflated.findViewById(R.id.ivEntryMediaIcon);
        return ivEntryMediaIcon;
    }

    public List<Entry> getAnswersList() {
        return mAnswersList;
    }

    public class AnswersViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.llAnswerContainer) LinearLayout llAnswerContainer;

        @Bind(R.id.includeAnsweredBy) View includeAnsweredBy;
        @Bind(R.id.includeAs) View includeAs;
        @Bind(R.id.includeTimeAnswered) View includeTimeAnswered;

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
