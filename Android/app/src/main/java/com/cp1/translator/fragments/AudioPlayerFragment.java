package com.cp1.translator.fragments;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cp1.translator.R;
import com.cp1.translator.utils.AudioPlayerUtils;
import com.cp1.translator.utils.Constants;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by erioness1125(Hyunji Kim) on 3/19/2016.
 */
public class AudioPlayerFragment extends Fragment implements MediaPlayer.OnCompletionListener, SeekBar.OnSeekBarChangeListener {

    @Bind(R.id.ibPlay) ImageButton ibPlay;
    @Bind(R.id.ibFastRewind) ImageButton ibFastRewind;
    @Bind(R.id.ibFastForward) ImageButton ibFastForward;
    @Bind(R.id.sbAudio) SeekBar sbAudio;
    @Bind(R.id.tvDurationTime) TextView tvDurationTime;
    @Bind(R.id.tvTotalTime) TextView tvTotalTime;

    private MediaPlayer mMediaPlayer;
    private AudioPlayerUtils utils;

    // Handler to update UI timer, progress bar etc,.
    private Handler mHandler = new Handler();

    private static final int SEEK_TIME = 1000; // 1000 milliseconds (=1 sec)

    public static AudioPlayerFragment newInstance(String audioUrl) {
        Bundle args = new Bundle();
        args.putString(Constants.AUDIO, audioUrl);
        AudioPlayerFragment fragment = new AudioPlayerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_audio_player, container, false);
        ButterKnife.bind(this, view);

        mMediaPlayer = new MediaPlayer();
        utils = new AudioPlayerUtils();

        sbAudio.setOnSeekBarChangeListener(this);
        mMediaPlayer.setOnCompletionListener(this);

        /**
         * Play button click event
         * plays a song and changes button to pause image
         * pauses a song and changes button to play image
         * */
        ibPlay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // check for already playing
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();

                    // Changing button image to play button
                } else {
                    // Resume song
                    mMediaPlayer.start();

                    // Changing button image to pause button
                }
            }
        });

        /**
         * Forward button click event
         * Forwards song specified seconds
         * */
        ibFastForward.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // get the current position
                int currentPosition = mMediaPlayer.getCurrentPosition();
                // get the duration of the file
                int duration = mMediaPlayer.getDuration();

                // check if (currentPosition + SEEK_TIME) is less than "duration"
                if (currentPosition + SEEK_TIME < duration) {
                    // forward song
                    mMediaPlayer.seekTo(currentPosition + SEEK_TIME);
                }
                else {
                    // forward to end position
                    mMediaPlayer.seekTo(duration);
                }
            }
        });

        /**
         * Backward button click event
         * Backward song to specified seconds
         * */
        ibFastRewind.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // get the current position
                int currentPosition = mMediaPlayer.getCurrentPosition();

                // check if (currentPosition - SEEK_TIME) is greater than 0 sec
                if (currentPosition - SEEK_TIME > 0) {
                    // forward song
                    mMediaPlayer.seekTo(currentPosition - SEEK_TIME);
                } else {
                    // back to the beginning
                    mMediaPlayer.seekTo(0);
                }
            }
        });

        try {
            String audioUrl = getArguments().getString(Constants.AUDIO);

            // load voice file
            mMediaPlayer.setDataSource(audioUrl);
            mMediaPlayer.prepare();
            mMediaPlayer.start();

            // set Progress bar values
            sbAudio.setProgress(0);
            sbAudio.setMax(100);

            // Updating progress bar
            updateProgressBar();
        } catch (IOException e) {
            Log.e("err", "in QsContentFragment: failed to start playing the audio");
        }

        return view;
    }

    private void updateProgressBar() {
        mHandler.postDelayed(UpdateTimeRunnable, 100);
    }

    /**
     * Background Runnable thread
     * */
    private Runnable UpdateTimeRunnable = new Runnable() {
        public void run() {
            long totalDuration = mMediaPlayer.getDuration();
            long currentDuration = mMediaPlayer.getCurrentPosition();

            // Displaying Total Duration time
            tvTotalTime.setText(String.valueOf( utils.milliSecondsToTimer(totalDuration) ));
            // Displaying time completed playing
            tvDurationTime.setText(String.valueOf( utils.milliSecondsToTimer(currentDuration) ));

            // Updating progress bar
            int progress = utils.getProgressPercentage(currentDuration, totalDuration);
            sbAudio.setProgress(progress);

            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
        }
    };

    /*********************************** OnCompletionListener ***********************************/
    @Override
    public void onCompletion(MediaPlayer mp) {

    }
    /*********************************** end of OnCompletionListener ***********************************/

    /*********************************** OnSeekBarChangeListener ***********************************/
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // remove message Handler from updating progress bar
        mHandler.removeCallbacks(UpdateTimeRunnable);
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        mHandler.removeCallbacks(UpdateTimeRunnable);
        int totalDuration = mMediaPlayer.getDuration();
        int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);

        // forward or backward to certain seconds
        mMediaPlayer.seekTo(currentPosition);

        // update timer progress again
        updateProgressBar();
    }
    /*********************************** end of OnSeekBarChangeListener ***********************************/
}
