package com.cp1.translator.friends;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cp1.translator.R;
import com.cp1.translator.models.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by eelango on 3/7/16.
 */
public class FriendsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<User> users = new ArrayList<>();
    private ArrayList<Boolean> isFriends = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tvName)
        TextView tvName;

        @Bind(R.id.tvEmail)
        TextView tvEmail;

        @Bind(R.id.btAdd)
        Button btAdd;

        private boolean isFriend;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setUser(User user, boolean isFriend) {
            this.isFriend = isFriend;

            tvEmail.setText(user.getUsername());
            if (isFriend) {
                ViewHolder.removeFriendStyle(btAdd);
            } else {
                ViewHolder.addFriendStyle(btAdd);
            }
        }

        public static void addFriendStyle(Button bt) {
            bt.setBackground(new ColorDrawable(Color.parseColor("#ffffff")));
            bt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add_friend, 0, 0, 0);
            bt.setText("Add Friend");
        }

        public static void removeFriendStyle(Button bt) {
            bt.setBackground(new ColorDrawable(Color.parseColor("#55acee")));
            bt.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_current_friend, 0, 0, 0);
            bt.setText("Buddy");
        }

        @OnClick(R.id.btAdd)
        public void addRemoveFriend(View v) {
            if (isFriend) {
                ViewHolder.addFriendStyle(btAdd);
                isFriend = false;
            } else {
                ViewHolder.removeFriendStyle(btAdd);
                isFriend = true;
            }
        }
    }

    public FriendsAdapter(final FriendsActivity activity) {
        super();
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((FriendsAdapter.ViewHolder) holder).setUser(users.get(position), isFriends.get(position));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View tweetView = layoutInflater.inflate(R.layout.list_friend_item, parent, false);
        return new ViewHolder(tweetView);
    }

    public void loadFriendsInContacts(ArrayList<String> emails) {
        users.clear();
        isFriends.clear();

        ArrayList<ParseQuery<ParseUser>> userQueries = new ArrayList<>();
        for(String email: emails) {
            ParseQuery<ParseUser> query = ParseUser.getQuery().whereEqualTo(User.USERNAME_KEY, email);
            userQueries.add(query);
        }

        ParseQuery<ParseUser> mainQuery = ParseQuery.or(userQueries);
        mainQuery.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                Log.e("ELANLOG", "Queried all users in contacts");
                User currentUser = (User) ParseUser.getCurrentUser();
                List<User> friends = currentUser.getFriends();
                for (ParseUser u : objects) {
                    User user = (User) u;
                    users.add(user);
                    if (friends.contains(user)) {
                        isFriends.add(true);
                    } else {
                        isFriends.add(false);
                    }
                }
                notifyItemRangeChanged(0, users.size());
            }
        });
    }
}
