package com.cp1.translator.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cp1.translator.R;
import com.cp1.translator.adapters.LanguagesAdapter;
import com.cp1.translator.fragments.NicknameDialogFragment;
import com.cp1.translator.login.LoginUtils;
import com.cp1.translator.models.Lang;
import com.cp1.translator.models.User;
import com.parse.ParseException;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends AppCompatActivity implements NicknameDialogFragment.NicknameDialogListener {

    @Bind(R.id.ibAddLanguage) ImageButton ibAddLanguage;
    @Bind(R.id.lvLanguages) ListView lvLanguages;
    @Bind(R.id.tvUserEmail) TextView tvUserEmail;
    @Bind(R.id.tvUserNickname) TextView tvUserNickname;
    @Bind(R.id.tvEmptyLanguage) TextView tvEmptyLanguage;

    @Nullable
    @Bind(R.id.pbLoadingLangs) ProgressBar pbLoadingLangs;

    private ArrayAdapter<Lang> mAdapter;
    private List<Lang> mLangList;
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
        if (resultCode == RESULT_OK && requestCode == LANG_REQ_CODE) {
            String langName = data.getStringExtra("language");
            Lang lang = Lang.getOrCreate(langName);
            me.addLang(lang);
            try {
                me.save();
                mLangList.add(lang);
                mAdapter.notifyDataSetChanged();
            } catch (ParseException e) {
                e.printStackTrace();
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

        String nickname = me.getNickname();
        if (nickname == null || nickname.isEmpty())
            nickname = me.getEmail();
        tvUserNickname.setText(nickname);

        // set empty view
        lvLanguages.setEmptyView(tvEmptyLanguage);

        // load languages
        mLangList = new ArrayList<>();
        mAdapter = new LanguagesAdapter(this, mLangList);
        lvLanguages.setAdapter(mAdapter);

        // OnItemLongClickListener to delete a language from the ListView
        lvLanguages.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final Lang lang = mLangList.get(position);

                // save to parse
                me.removeLang(lang);
                me.saveInBackground();

                // remove Lang from mLangList
                mLangList.remove(position);
                mAdapter.notifyDataSetChanged();

                Snackbar.make(lvLanguages, R.string.snackbar_text_del_lang, Snackbar.LENGTH_LONG)
                        .setAction(R.string.snackbar_action_undo, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mLangList.add(position, lang);
                                mAdapter.notifyDataSetChanged();
                            }
                        })
                        .show(); // Don’t forget to show!

                return true;
            }
        });

        // show progress bar while loading the languages
        pbLoadingLangs.setVisibility(View.VISIBLE);
        me.getLangs(new Lang.LangsListener() {
            @Override
            public void onLangs(List<Lang> langs) {
                mLangList.addAll(langs);
                mAdapter.notifyDataSetChanged();

                pbLoadingLangs.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError(ParseException e) {
                e.printStackTrace();
            }
        });
    }

    @OnClick(R.id.ibAddLanguage)
    public void onClickAddLanguage() {
        // launch Languages View
        Intent intent = new Intent(this, LanguagesActivity.class);
        startActivityForResult(intent, LANG_REQ_CODE);
    }

    @OnClick(R.id.tvUserNickname)
    public void onClickSetNickname() {
        FragmentManager fm = getSupportFragmentManager();
        NicknameDialogFragment editNameDialog = NicknameDialogFragment.newInstance(tvUserNickname.getText().toString());
        editNameDialog.show(fm, "fragment_nickname");
    }

    @Override
    public void onFinishEditDialog(String newNickName) {
        tvUserNickname.setText(newNickName);

        // update user
        me.setNickname(newNickName);
        me.saveInBackground(new SaveCallback() {

            @Override
            public void done(ParseException e) {
                if (e != null) {
                    // There was an error
                    Log.d("Settings", "Failed to update the nickname");
                } else {
                    Log.i("Settings", "Successfully updated the nickname");
                }
            }
        });
    }
}