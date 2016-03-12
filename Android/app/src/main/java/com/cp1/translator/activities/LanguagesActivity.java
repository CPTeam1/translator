package com.cp1.translator.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cp1.translator.R;
import com.cp1.translator.login.LoginUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LanguagesActivity extends AppCompatActivity {

    @Bind(R.id.lvAllLanguages) ListView lvAllLanguages;

    ArrayAdapter<String> adapter;
    List<String> languagesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LoginUtils.checkIfLoggedIn(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_languages);

        getSupportActionBar().setTitle(getString(R.string.languages));

        ButterKnife.bind(this);
        populateLanguages();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lang, menu);

        // set listener of each menu item
        // Search
        MenuItem miSearchLang = menu.findItem(R.id.miSearchLang);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(miSearchLang);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    lvAllLanguages.clearTextFilter();
                } else {
                    lvAllLanguages.setFilterText(newText.toString());
                }

                return true;
            }
        });

        return true;
    }

    private void populateLanguages() {
        languagesList = Languages.getLanguages();

        // initialize adapter
        adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, languagesList);
        lvAllLanguages.setAdapter(adapter);
        // set ListView item click listener
        lvAllLanguages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String language = ((TextView) view).getText().toString();

                // Prepare data intent
                Intent result = new Intent();

                // Pass selected language back as a result
                result.putExtra("language", language);

                // Activity finished ok, return the data
                setResult(RESULT_OK, result); // set result code and bundle data for response
                finish(); // closes the activity, pass data to parent
            }
        });
    }

    public static class Languages {

        private static final List<String> languagesList;

        static {
            String[] arr = new String[] { "Arabic", "Catalan", "Chinese", "Croatian", "Czech",
            "Danish", "Dutch", "English", "Finnish", "French", "German", "Greek", "Hebrew",
            "Hindi", "Hungarian", "Indonesian", "Italian", "Japanese", "Korean", "Malay",
            "Polish", "Portuguese", "Romanian", "Russian", "Slovak", "Spanish", "Swedish",
            "Thai", "Turkish", "Ukrainian", "Vietnamese" };

            languagesList = new ArrayList(Arrays.asList(arr));
        }

        public static List<String> getLanguages() {
            return languagesList;
        }
    }
}
