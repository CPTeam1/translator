package com.cp1.translator.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cp1.translator.models.Entry;

import java.util.List;

/**
 * Created by erioness1125(Hyunji Kim) on 3/15/2016.
 */
public abstract class EntriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected Context context;
    protected List<Entry> mEntriesList;

    // Define listener member variable
    protected OnClickItemListener listener;

    // Define the listener interface
    public interface OnClickItemListener {
        void onClickItem(View itemView, int position);
    }

    public void setOnClickItemListener(OnClickItemListener listener) {
        this.listener = listener;
    }

    // Add a list of questions
    public void addAll(List<Entry> entriesList) {
//        int sizeOfListBeforeAdding = mQuestionList.size();
        mEntriesList.addAll(entriesList);
        notifyDataSetChanged();
//        notifyItemRangeInserted(sizeOfListBeforeAdding, questionList.size());
    }

    // Add a question to the list
    public void add(Entry entry) {
        add(0, entry);
    }

    // Add a question to the list at specific position
    public void add(int idx, Entry question) {
        mEntriesList.add(idx, question);
        //notifyItemInserted(idx);
        notifyDataSetChanged();
    }

    public void clear() {
        mEntriesList.clear();
        notifyDataSetChanged();
    }

    public void setEntriesList(List<Entry> entriesList) {
        mEntriesList = entriesList;
    }

    @Override
    public int getItemCount() {
        return mEntriesList.size();
    }
}
