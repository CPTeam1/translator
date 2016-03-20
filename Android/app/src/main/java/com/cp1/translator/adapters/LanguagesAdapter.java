package com.cp1.translator.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cp1.translator.R;
import com.cp1.translator.models.Lang;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by erioness1125(Hyunji Kim) on 3/9/2016.
 */
public class LanguagesAdapter extends ArrayAdapter<Lang> {

    public LanguagesAdapter(Context context, List<Lang> skillList) {
        super(context, R.layout.item_skill, skillList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Lang lang = getItem(position);

        ViewHolder viewHolder; // view lookup cache stored in tag

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_skill, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvSkillLang.setText(lang.getName());
        return convertView;
    }

    class ViewHolder {
        @Bind(R.id.tvSkillLang) TextView tvSkillLang;

        public ViewHolder(View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }
}
