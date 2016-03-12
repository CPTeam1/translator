package com.cp1.translator.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.cp1.translator.R;
import com.cp1.translator.adapters.LanguagesAdapter;
import com.cp1.translator.login.LoginUtils;
import com.cp1.translator.models.Lang;
import com.cp1.translator.models.Skill;
import com.cp1.translator.models.User;
import com.cp1.translator.utils.Constants;
import com.parse.ParseException;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends AppCompatActivity {

    @Bind(R.id.ibAddLanguage) ImageButton ibAddLanguage;
    @Bind(R.id.lvLanguages) ListView lvLanguages;
    @Bind(R.id.tvUserEmail) TextView tvUserEmail;
    @Bind(R.id.tvUserName) TextView tvUserName;
    @Bind(R.id.tvEmptyLanguage) TextView tvEmptyLanguage;

    private ArrayAdapter<Skill> adapter;
    private List<Skill> skillList;
    private User me;

    private static final int LANG_REQ_CODE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LoginUtils.checkIfLoggedIn(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        // set title
        getSupportActionBar().setTitle(getString(R.string.settings));

        loadUserSettings();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == LANG_REQ_CODE) {
                Lang lang = new Lang();
                lang.setName(data.getStringExtra("language"));
                Skill skill = new Skill();
                skill.setLang(lang);
                skillList.add(skill);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    private void loadUserSettings() {
        me = (User) User.getCurrentUser();
        tvUserEmail.setText(me.getEmail());
        tvUserName.setText(me.getNickname());

        lvLanguages.setEmptyView(tvEmptyLanguage);
        // load languages
        skillList = new ArrayList<>();
        if (me.getSkills() != null)
            skillList.addAll(me.getSkills());
        adapter = new LanguagesAdapter(this, skillList);
        lvLanguages.setAdapter(adapter);
    }

    @OnClick(R.id.ibAddLanguage)
    public void onClickAddLanguage() {
        // launch Languages View
        Intent intent = new Intent(this, LanguagesActivity.class);
        startActivityForResult(intent, LANG_REQ_CODE);
    }

    // TODO
    public void onSaveSettings(MenuItem item) {
        for (Skill skill : skillList)
            me.addSkill(skill);

        me.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e!=null){
                    Log.e(Constants.APP_TAG,"Error in uploading skills"+e.getMessage());
                }
                else
                    Log.d(Constants.APP_TAG,"skills uploaded successfully");
            }
        });
    }
}
