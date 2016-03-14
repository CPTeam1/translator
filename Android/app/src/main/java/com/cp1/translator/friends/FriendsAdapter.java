package com.cp1.translator.friends;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cp1.translator.R;
import com.cp1.translator.models.User;
import com.cp1.translator.push.EntryPusher;
import com.cp1.translator.utils.SaveListener;
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
public class FriendsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements User.FriendshipListener {

    private ArrayList<User> mUsers = new ArrayList<>();
    private ArrayList<User> mFriends = new ArrayList<>();

    @Override
    public void changedFriendship(User user, boolean isFriend) {
        if (isFriend) {
            if(!mFriends.contains(user)) {
                mFriends.add(user);
            }
        } else {
            if(mFriends.contains(user)) {
                mFriends.remove(user);
            }
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tvName)
        TextView tvName;

        @Bind(R.id.tvEmail)
        TextView tvEmail;

        @Bind(R.id.btAdd)
        Button btAdd;

        private boolean isFriend;
        private User user;
        private User.FriendshipListener friendshipListener;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setUser(User user, boolean isFriend, User.FriendshipListener listener) {
            this.user = user;
            this.isFriend = isFriend;
            friendshipListener = listener;

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
            User currUser = (User) ParseUser.getCurrentUser();
            if (isFriend) {
                currUser.removeFriend(user, new SaveListener() {
                    @Override
                    public void saved() {
                        isFriend = false;
                        ViewHolder.addFriendStyle(btAdd);
                        friendshipListener.changedFriendship(user, false);
                    }

                    @Override
                    public void onError(ParseException e) {
                        e.printStackTrace();
                    }
                });
            } else {
                currUser.addFriend(user, new SaveListener() {
                    @Override
                    public void saved() {
                        isFriend = true;
                        ViewHolder.removeFriendStyle(btAdd);
                        friendshipListener.changedFriendship(user, true);
                    }

                    @Override
                    public void onError(ParseException e) {
                        e.printStackTrace();
                    }
                });
            }
            EntryPusher.pushTestToEveryone();
        }
    }

    public FriendsAdapter(final FriendsActivity activity) {
        super();
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        User user = mUsers.get(position);
        ((FriendsAdapter.ViewHolder) holder).setUser(user, mFriends.contains(user), this);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View tweetView = layoutInflater.inflate(R.layout.list_friend_item, parent, false);
        return new ViewHolder(tweetView);
    }

    public void loadFriendsInContacts(ArrayList<String> emails) {
        mUsers.clear();

        ArrayList<ParseQuery<User>> userQueries = new ArrayList<>();
        for(String email: emails) {
            if(!email.equals(ParseUser.getCurrentUser().getEmail())) {
                ParseQuery<User> query = ParseQuery.getQuery(User.class).whereEqualTo(User.USERNAME_KEY, email);
                userQueries.add(query);
            }
        }

        ParseQuery<User> mainQuery = ParseQuery.or(userQueries);
        mainQuery.findInBackground(new FindCallback<User>() {
            @Override
            public void done(final List<User> contacts, ParseException e) {
                Log.e("ELANLOG", "Queried all users in contacts");
                mUsers.addAll(contacts);
                User currentUser = (User) ParseUser.getCurrentUser();
                currentUser.getFriends(new User.UsersListener() {
                    @Override
                    public void onUsers(List<User> friends) {
                        mFriends.addAll(friends);
                        for (User friend: friends) {
                            if (!mUsers.contains(friend))
                                mUsers.add(friend);
                        }
                        notifyItemRangeChanged(0, mUsers.size());
                    }

                    @Override
                    public void onError(ParseException e) {
                        e.printStackTrace();
                    }
                });
            }
        });
    }
}
