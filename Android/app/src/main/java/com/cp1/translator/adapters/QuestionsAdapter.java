package com.cp1.translator.adapters;

import android.content.Context;
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

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by pandeis on 3/9/16.
 * Changed ArrayAdapter to RecyclerView.Adapter by erioness1125(Hyunji Kim) on 03/12/16.
 */
public class QuestionsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Entry> mQuestionList;

    // Define listener member variable
    private OnItemClickListener listener;

    // Define the listener interface
    public interface OnItemClickListener {
        void onQuestionClick(View itemView, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public QuestionsAdapter(List<Entry> questionList) {
        mQuestionList = questionList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        RecyclerView.ViewHolder viewHolder = null;
        View view = null;

        switch (viewType) {
            default:
                // Inflate the custom layout
                view = inflater.inflate(R.layout.item_question, parent, false);

                // Return a new holder instance
                viewHolder = new MyQuestionViewHolder(view);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // Get the data model based on position
        Entry question = mQuestionList.get(position);

        switch (holder.getItemViewType()) {
            default:
                populateMyQuestions((MyQuestionViewHolder) holder, question);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mQuestionList.size();
    }

    private void populateMyQuestions(MyQuestionViewHolder holder, Entry question) {
        // type of the question
        Drawable typeDrawble = ContextCompat.getDrawable(context, R.drawable.shape_qs_txt);
        switch (question.getType()) {
            case Types.PICTURE:
                typeDrawble = ContextCompat.getDrawable(context, R.drawable.shape_qs_pic);
                break;
            case Types.VOICE:
                typeDrawble = ContextCompat.getDrawable(context, R.drawable.shape_qs_voice);
                break;
            case Types.VIDEO:
                typeDrawble = ContextCompat.getDrawable(context, R.drawable.shape_qs_video);
                break;
        }
        holder.ivQuestionType.setBackground(typeDrawble);

        // question title (=text)
        holder.tvQuestionTitle.setText(question.getText());

        // the number of replies to this question
        holder.tvRepliesCount.setText("[0]");
    }

    // Clean all elements of the recycler
    public void clear() {
        int sizeOfListBeforeClearing = mQuestionList.size();
        mQuestionList.clear();
        notifyItemRangeRemoved(0, sizeOfListBeforeClearing);
    }

    // Add a list of questions
    public void addAll(List<Entry> questionList) {
        int sizeOfListBeforeAdding = mQuestionList.size();
        mQuestionList.addAll(questionList);
        notifyDataSetChanged();
//        notifyItemRangeInserted(sizeOfListBeforeAdding, questionList.size());
    }

    // Add a question to the list
    public void add(Entry question) {
        add(mQuestionList.size() - 1, question);
    }

    // Add a question to the list at specific position
    public void add(int idx, Entry question) {
        mQuestionList.add(idx, question);
        notifyItemInserted(idx);
    }

    public void setQuestionList(List<Entry> questionList) {
        mQuestionList = questionList;
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
                        listener.onQuestionClick(itemView, getLayoutPosition());
                }
            });
        }
    }
}
