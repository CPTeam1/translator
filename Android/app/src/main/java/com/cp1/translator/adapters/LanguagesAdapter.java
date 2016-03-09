package com.cp1.translator.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cp1.translator.R;
import com.cp1.translator.models.Skill;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by kimhy08 on 3/9/2016.
 */
public class LanguagesAdapter extends ArrayAdapter<Skill> {

    public LanguagesAdapter(Context context, List<Skill> skillList) {
        super(context, R.layout.item_skill, skillList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Skill skill = getItem(position);

        ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_skill, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvSkillLang.setText(skill.getLang().getName());
        viewHolder.tvSkillLevel.setText(String.valueOf(skill.getLevel()));

        return convertView;
    }

    class ViewHolder {
        @Bind(R.id.tvSkillLang) TextView tvSkillLang;
        @Bind(R.id.tvSkillLevel) TextView tvSkillLevel;

        public ViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }
}
