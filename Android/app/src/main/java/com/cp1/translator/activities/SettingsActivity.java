package com.cp1.translator.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.cp1.translator.R;
import com.cp1.translator.models.Lang;
import com.cp1.translator.models.User;

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

    private List<Lang> languageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        // set title
        getSupportActionBar().setTitle(getString(R.string.settings));

        loadUserSettings();
    }

    private void loadUserSettings() {
        User me = (User) User.getCurrentUser();
        tvUserEmail.setText(me.getEmail());
        tvUserName.setText(me.getNickname());

        lvLanguages.setEmptyView(tvEmptyLanguage);
        // load languages
        languageList = new ArrayList<>();
        ArrayAdapter<Lang> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, languageList);
        lvLanguages.setAdapter(adapter);
    }

    @OnClick(R.id.ibAddLanguage)
    public void onClickAddLanguage() {
        // launch Languages View
        Intent intent = new Intent(this, LanguagesActivity.class);
        startActivity(intent);
    }
}
