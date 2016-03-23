package com.cp1.translator.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cp1.translator.R;
import com.cp1.translator.models.Entry;
import com.cp1.translator.models.Post;
import com.cp1.translator.models.Types;
import com.cp1.translator.utils.Constants;
import com.cp1.translator.utils.DeviceDimensionsHelper;
import com.parse.ParseException;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by erioness1125(Hyunji Kim) on 3/20/2016.
 */
public class PostsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mContext;
    List<Post> mPostsList;

    private String mMyUserName;

    // Define listener member variable
    protected OnClickItemListener listener;

    // Define the listener interface
    public interface OnClickItemListener {
        void onClickItem(View itemView, int position);
    }

    public void setOnClickItemListener(OnClickItemListener listener) {
        this.listener = listener;
    }

    public PostsAdapter(List<Post> postsList, String myUserName) {
        mPostsList = postsList;
        mMyUserName = myUserName;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // Get the data model based on position
        Post post = mPostsList.get(position);
        populatePosts((PostViewHolder) holder, post);
    }

    @Override
    public int getItemCount() {
        return mPostsList.size();
    }

    private void populatePosts(final PostViewHolder holder, Post post) {

        // set original and target languages
        TextView tvFromLang = (TextView) holder.includeQsLang.findViewById(R.id.tvLabel);
        tvFromLang.setText(post.getFromLang());
        TextView tvToLang = (TextView) holder.includeAsLang.findViewById(R.id.tvLabel);
        tvToLang.setText(post.getToLang());

        // set Post created time
        TextView tvTimeAsked = (TextView) holder.includeTimeAsked.findViewById(R.id.tvLabel);
        String relativeTimeStamp = Constants.getRelativeTimeAgo(post.getCreatedAt());
        tvTimeAsked.setText(relativeTimeStamp);

        Entry question = post.getQuestion();
        // load answers to choose the top one
        post.getAnswers(new Entry.EntriesListener() {
            @Override
            public void onEntries(List<Entry> answersList) {
                if (answersList != null && !answersList.isEmpty()) {
                    Entry answer = answersList.get(0);
                    bindView(holder, answer);
                }
                else {
                    // no answer yet
                    setEntryText(holder.includeAs, "No Answer");
                }
            }

            @Override
            public void onError(ParseException e) {
                e.printStackTrace();
                // failed to load answers
                setEntryText(holder.includeAs, "Failed to load answers");
            }
        });

        // show TextView(tvLabel in item_label.xml) ONLY IF the question is written by others!
        TextView tvAskedBy = (TextView) holder.includeAskedBy.findViewById(R.id.tvLabel);
        if (!mMyUserName.equals(question.getUser().getUsername())) {
            // get User's nickname
            String askedBy = "From: " + question.getUser().getNickname();
            tvAskedBy.setText(askedBy);
        }
        else
            tvAskedBy.setVisibility(View.GONE);

        bindView(holder, question);
    }

    private void bindView(PostViewHolder holder, Entry entry) {
        View included;
        if (entry.isQuestion())
            included = holder.includeQs;
        else
            included = holder.includeAs;

        if (included != null) {
            // clear ImageView(findMediaView)
            ImageView ivEntryMediaIcon = findMediaView(included);
            ivEntryMediaIcon.setImageResource(0);

            // clear ImageView(ivEntryPic)
            ImageView ivEntryPic = findImageView(included);
            ivEntryPic.setImageResource(0);

            // set TextView(tvEntryText)
            setEntryText(included, entry.getText());

            // Assume its a Text Question by default
            // As we don't want the switch case to crash :)
            String type = Types.TEXT;
            if (entry.getType() != null)
                type = entry.getType();
            switch (type) {
                case Types.PICTURE:
                    String imgUrl = entry.getImageUrl().getUrl();

                    // load img onto ivEntryPic
                    Picasso.with(mContext)
                            .load(imgUrl)
                            .resize(DeviceDimensionsHelper.getDisplayWidth(mContext), 0)
                            .into(ivEntryPic);

                    break;

                case Types.AUDIO:
                    ivEntryMediaIcon.setImageResource(R.drawable.shape_qs_voice);

                    break;

                case Types.VIDEO:
                    ivEntryMediaIcon.setImageResource(R.drawable.shape_qs_video);

                    break;
            }
        }
    }

    private void setEntryText(View included, String text) {
        View vsTextAfterInflated;

        ViewStub vsText = (ViewStub) included.findViewById(R.id.vsText);
        if (vsText != null) {
            vsTextAfterInflated = ((ViewStub) included.findViewById(R.id.vsText)).inflate();
        }
        else {
            /*
            Once visible/inflated, the ViewStub element is no longer part of the view hierarchy.
            It is replaced by the inflated layout and the ID for the root view of that layout is the one
            specified by the android:inflatedId attribute of the ViewStub.
             */
            vsTextAfterInflated = included.findViewById(R.id.vsTextAfter);
        }

        // find TextView
        TextView tvEntryText = (TextView) vsTextAfterInflated.findViewById(R.id.tvEntryText);

        // set question text
        tvEntryText.setText(text);

        if (included.getId() == R.id.includeQs)
            tvEntryText.setTextColor(Color.parseColor("#727272"));
    }

    private ImageView findImageView(View included) {
        View vsPicAfterInflated;
        ViewStub vsPic = (ViewStub) included.findViewById(R.id.vsPic);
        if (vsPic != null) {
            vsPicAfterInflated = vsPic.inflate();
        }
        else {
            vsPicAfterInflated = included.findViewById(R.id.vsPicAfter);
        }

        // find ImageView
        ImageView ivEntryPic = (ImageView) vsPicAfterInflated.findViewById(R.id.ivEntryPic);
        return ivEntryPic;
    }

    private ImageView findMediaView(View included) {
        View vsMediaAfterInflated;
        ViewStub vsMedia = (ViewStub) included.findViewById(R.id.vsMedia);
        if (vsMedia != null) {
            vsMediaAfterInflated = vsMedia.inflate();
        }
        else {
            vsMediaAfterInflated = included.findViewById(R.id.vsMediaAfter);
        }

        // find ImageView(ivEntryMediaIcon)
        ImageView ivEntryMediaIcon = (ImageView) vsMediaAfterInflated.findViewById(R.id.ivEntryMediaIcon);
        return ivEntryMediaIcon;
    }

    // Add a list of questions
    public void addAll(List<Post> postsList) {
//        int sizeOfListBeforeAdding = mQuestionList.size();
        mPostsList.addAll(postsList);
        notifyDataSetChanged();
//        notifyItemRangeInserted(sizeOfListBeforeAdding, questionList.size());
    }

    // Add a question to the list
    public void add(Post post) {
        add(0, post);
    }

    // Add a question to the list at specific position
    public void add(int idx, Post post) {
        mPostsList.add(idx, post);
        //notifyItemInserted(idx);
        notifyDataSetChanged();
    }

    public void setPostsList(List<Post> postsList) {
        mPostsList = postsList;
    }

    public List<Post> getPostsList() {
        return mPostsList;
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.llPostContainer) LinearLayout llPostContainer;

        @Bind(R.id.includeAskedBy) View includeAskedBy;
        @Bind(R.id.includeQsLang) View includeQsLang;
        @Bind(R.id.includeQs) View includeQs;
        @Bind(R.id.includeAsLang) View includeAsLang;
        @Bind(R.id.includeAs) View includeAs;
        @Bind(R.id.includeTimeAsked) View includeTimeAsked;


        public PostViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            // Setup the click listener
            llPostContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Triggers click upwards to the adapter on click
                    if (listener != null)
                        listener.onClickItem(itemView, getLayoutPosition());
                }
            });
        }
    }
}
