package com.cp1.translator.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cp1.translator.R;
import com.cp1.translator.models.Question;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by pandeis on 3/9/16.
 */
public class QuestionsAdapter extends ArrayAdapter<Question> {
    public QuestionsAdapter(Context context, List<Question> questions) {
        super(context, 0, questions);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //1. get view
        Question question = getItem(position);
        // 2. find andinflate the template
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_question,parent,false);
        }


        TextView tvQuestions = (TextView) convertView.findViewById(R.id.tvQuestionTitle);
        TextView tvReplies = (TextView) convertView.findViewById(R.id.tvRepliesCount);


        tvQuestions.setText(question.getQuestion());
        if(question.getAnswers()!=null)
            tvReplies.setText(question.getAnswers().length());
        else
            tvReplies.setText("0");

//        ivProfileImage.setImageResource(android.R.color.transparent); // clear out an old image for recycled view
//        Picasso.with(getContext()).load(question.getProfileImage()).fit().into(ivProfileImage);
        return convertView;

    }
}
