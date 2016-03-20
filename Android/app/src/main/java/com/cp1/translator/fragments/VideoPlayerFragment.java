package com.cp1.translator.fragments;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

import com.cp1.translator.R;
import com.cp1.translator.utils.Constants;

/**
 * Created by erioness1125(Hyunji Kim) on 3/19/2016.
 */
public class VideoPlayerFragment extends Fragment {

    public static VideoPlayerFragment newInstance(String videoUrl) {
        Bundle args = new Bundle();
        args.putString(Constants.VIDEO, videoUrl);
        VideoPlayerFragment fragment = new VideoPlayerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_player, container, false);

        final VideoView vvQsMediaVideo = (VideoView) view.findViewById(R.id.vvQsMediaVideo);
        vvQsMediaVideo.setVideoPath(getArguments().getString(Constants.VIDEO));
        MediaController mediaController = new MediaController(getContext());
        mediaController.setAnchorView(vvQsMediaVideo);
        vvQsMediaVideo.setMediaController(mediaController);
        vvQsMediaVideo.requestFocus();
        vvQsMediaVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                vvQsMediaVideo.start();
            }
        });

        return view;
    }
}
