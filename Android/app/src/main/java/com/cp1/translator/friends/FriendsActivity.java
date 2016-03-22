package com.cp1.translator.friends;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.cp1.translator.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FriendsActivity extends AppCompatActivity {

    @Bind(R.id.rvFriends)
    RecyclerView rvFriends;

    private FriendsAdapter friendsAdapter;
    private LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        ButterKnife.bind(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvFriends.setLayoutManager(layoutManager);

        friendsAdapter = new FriendsAdapter(this);
        rvFriends.setAdapter(friendsAdapter);

        loadContactEmails();
        getSupportLoaderManager().initLoader(78, new Bundle(), loaderCallbacks);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void loadContactEmails() {
        loaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader onCreateLoader(int id, Bundle args) {
                String[] projectionFields = new String[]{ContactsContract.CommonDataKinds.Email.ADDRESS};
                CursorLoader cursorLoader = new CursorLoader(FriendsActivity.this,
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        projectionFields, null, null, null);
                return cursorLoader;
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                ArrayList<String> emails = new ArrayList<>();
                data.moveToFirst();
                while (!data.isAfterLast()) {
                    emails.add(data.getString(0));
                    data.moveToNext();
                }
                if(!friendsAdapter.loadFriendsInContacts(emails)) {
                    Toast.makeText(getApplicationContext(), "This phone does not have any contacts to add as friends", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onLoaderReset(Loader loader) {

            }
        };
    }
}
