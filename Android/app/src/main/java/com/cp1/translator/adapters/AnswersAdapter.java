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
import com.cp1.translator.utils.Constants;
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

    // Add a list of questions
    public void addAll(List<Entry> entriesList) {
//        int sizeOfListBeforeAdding = mQuestionList.size();
        mAnswersList.addAll(entriesList);
        notifyDataSetChanged();
//        notifyItemRangeInserted(sizeOfListBeforeAdding, questionList.size());
    }

    // Add a question to the list
    public void add(Entry entry) {
        add(0, entry);
    }

    // Add a question to the list at specific position
    public void add(int idx, Entry question) {
        mAnswersList.add(idx, question);
        //notifyItemInserted(idx);
        notifyDataSetChanged();
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
        String askedBy = "From: " + answer.getUser().getNickname();
        tvAnsweredBy.setText(askedBy);

        View included = holder.includeAs;

        // Assume its a Text Question by default
        // As we don't want the switch case to crash :)
        String type = Types.TEXT;
        if (answer.getTypeLocally() != null)
            type = answer.getTypeLocally();
        switch (type) {
            case Types.TEXT:
                setEntryText(included, answer.getText());

                break;

            case Types.PICTURE:
                String imgUrl = answer.getImageUrl().getUrl();

                View vsPic = ((ViewStub) included.findViewById(R.id.vsPic)).inflate();
                // find ImageView
                ImageView ivEntryPic = (ImageView) vsPic.findViewById(R.id.ivEntryPic);
                // load img onto ivEntryPic
                Picasso.with(mContext)
                        .load(imgUrl)
                        .into(ivEntryPic);

                break;

            case Types.AUDIO:
            case Types.VIDEO:
                View vsMedia = ((ViewStub) included.findViewById(R.id.vsMedia)).inflate();
                // find ImageView
                ImageView ivEntryMediaIcon = (ImageView) vsMedia.findViewById(R.id.ivEntryMediaIcon);
                // load img onto ivEntryMediaIcon
//                Picasso.with(context)
//                        .load(imgUrl)
//                        .into(ivEntryPic);

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

        // set question text
        tvEntryText.setText(text);
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
