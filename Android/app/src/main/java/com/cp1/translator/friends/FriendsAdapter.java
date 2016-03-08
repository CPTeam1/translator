package com.cp1.translator.friends;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cp1.translator.R;
import com.cp1.translator.models.User;
import com.parse.ParseUser;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by eelango on 3/7/16.
 */
public class FriendsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<String> emails = new ArrayList<>();
    private LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tvName)
        TextView tvName;

        @Bind(R.id.tvEmail)
        TextView tvEmail;

        @Bind(R.id.btAdd)
        Button btAdd;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setUser(String email) {
            tvEmail.setText(email);
        }
    }

    public FriendsAdapter(final FriendsActivity activity) {
        super();
        getContactEmails(activity);
        activity.getSupportLoaderManager().initLoader(78, new Bundle(), loaderCallbacks);
        User currentUser = (User) ParseUser.getCurrentUser();

    }

    public void getContactEmails(final FriendsActivity activity) {
        loaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader onCreateLoader(int id, Bundle args) {
                String[] projectionFields = new String[]{ContactsContract.CommonDataKinds.Email.ADDRESS};
                CursorLoader cursorLoader = new CursorLoader(activity,
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        projectionFields, null, null, null);
                return cursorLoader;
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                emails.clear();
                data.moveToFirst();
                while (!data.isAfterLast()) {
                    emails.add(data.getString(0));
                    data.moveToNext();
                }
                notifyItemRangeRemoved(0, emails.size());
            }

            @Override
            public void onLoaderReset(Loader loader) {
                emails.clear();
            }
        };
    }

    @Override
    public int getItemCount() {
        return emails.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((FriendsAdapter.ViewHolder) holder).setUser(emails.get(position));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View tweetView = layoutInflater.inflate(R.layout.list_friend_item, parent, false);
        return new ViewHolder(tweetView);
    }
}
