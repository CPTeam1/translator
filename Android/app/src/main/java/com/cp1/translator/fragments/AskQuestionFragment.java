package com.cp1.translator.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cp1.translator.R;
import com.cp1.translator.models.Entry;
import com.cp1.translator.models.Post;
import com.cp1.translator.models.Question;
import com.cp1.translator.models.User;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.cp1.translator.utils.Constants.APP_TAG;
import static com.cp1.translator.utils.Constants.AUDIO_EXT;
import static com.cp1.translator.utils.Constants.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE;
import static com.cp1.translator.utils.Constants.PIC_EXT;
import static com.cp1.translator.utils.Constants.QNO;
import static com.cp1.translator.utils.Constants.SEPARATOR;
import static com.cp1.translator.utils.Constants.photoFileName;

public class AskQuestionFragment extends DialogFragment {
    @Bind(R.id.btAskQs) Button btAskQs;
    @Bind(R.id.etQs) EditText etQs;
    @Bind(R.id.tvChars) TextView tvCharsLeft;
    @Nullable @Bind(R.id.ivQsPic) ImageView ivQsPic;
    @Nullable @Bind(R.id.ibRecAudio) ImageButton ibRecAudio;
    @Nullable @Bind(R.id.ibRelAudio) ImageButton ibRelAudio;
    @Nullable @Bind(R.id.ibClickPic) ImageButton ibClickPic;
    @Nullable @Bind(R.id.ibPlayAudio) ImageButton ibPlayAudio;

    private int textColor;
    private int currQuesNo;
    private SharedPreferences mSettings;

    private static int QS_CHAR_LIMIT = 140;
    private static boolean qsPosted = false;
    private String imageURI;
    private String audioURI;
    private String videoURI;
    private String mAudioFileName;
    private MediaRecorder mediaRecorder;

    // Defines the listener interface
    public interface AskQuestionDialogListener {
        void onFinishAsking(Entry newQuestion);
    }


    public AskQuestionFragment() {
        // Required empty public constructor
    }

    public static AskQuestionFragment newInstance() {
        Bundle args = new Bundle();
        AskQuestionFragment fragment = new AskQuestionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etQs.addTextChangedListener(textWatcher);
        textColor = tvCharsLeft.getCurrentTextColor();
        mSettings = PreferenceManager.getDefaultSharedPreferences(getActivity());

        btAskQs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                // 1) save qs in DB
                // 2) pass qs back to main activity
                // 3) display in adapter
                if (etQs.getText() != null) {

                    String question = etQs.getText().toString();

                    Question qsDB = saveLocally(question, User.getCurrentUser().getEmail());

                    // build new Entry object for qsDB
                    Entry qsEntry = buildQuestionEntry(qsDB);

                    // TODO uncomment once model is fixed
                    //saveToParse(question);

                    AskQuestionDialogListener listener = (AskQuestionDialogListener) getTargetFragment();
                    if(listener!=null) {
                            listener.onFinishAsking(qsEntry);
                        dismiss();
                    }
//                    Intent displayQsIntent = new Intent(getApplicationContext(), MainActivity.class);
//                    displayQsIntent.putExtra("question", Parcels.wrap(qsDB));
//                    startActivity(displayQsIntent);
                }
            }
        });

        ibClickPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLaunchCamera(v);
            }
        });

        ibRecAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLaunchRecorder(v);
            }
        });

        ibRelAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onReleaseRecorder(v);
            }
        });

        ibPlayAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlay(v);
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onResume() {
        super.onResume();
        currQuesNo = mSettings.getInt(QNO,0);
        currQuesNo++;
    }

    public void onLaunchCamera(View view) {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // set the image file name
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getPhotoFileUri(photoFileName + SEPARATOR+ Integer.toString(currQuesNo) + PIC_EXT));

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mediaRecorder!=null){
            Log.d(APP_TAG, "On Pause called. Stopping recording..");
            mediaRecorder.stop();
            mediaRecorder.reset();
            mediaRecorder.release();
        }
    }

    public void onReleaseRecorder(View view){
        if(mediaRecorder!=null){
            Log.d(APP_TAG,"Stopping recording..");
            mediaRecorder.stop();
            mediaRecorder.reset();
            mediaRecorder.release();
            audioURI = mAudioFileName;
        }
    }

    public void onPlay(View view){
        if(mediaRecorder!=null){
            try {
                MediaPlayer mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setDataSource(mAudioFileName);
                mediaPlayer.prepare(); // must call prepare first
                mediaPlayer.start(); // then start
            }catch(Exception e){
                Log.e(APP_TAG,"Exception in playing media "+e.getMessage());
            }
        }
        else{
            Toast.makeText(getActivity(), "No playable media found!", Toast.LENGTH_LONG).show();
        }
    }

    public void onLaunchRecorder(View view){
        try {
            // Verify that the device has a mic first
            PackageManager pmanager = getActivity().getPackageManager();
            if (pmanager.hasSystemFeature(PackageManager.FEATURE_MICROPHONE)) {
                // Set the file location for the audio
                mAudioFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
                mAudioFileName += "/audiorecordtest"+SEPARATOR + Integer.toString(currQuesNo)+AUDIO_EXT;
                Log.d(APP_TAG,"Audio file for question: "+mAudioFileName);
                // Create the recorder
                mediaRecorder = new MediaRecorder();
                // Set the audio format and encoder
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                // Setup the output location
                mediaRecorder.setOutputFile(mAudioFileName);
                // Start the recording
                mediaRecorder.prepare();
                mediaRecorder.start();
                Log.d(APP_TAG,"Recording in progress..");
            } else { // no mic on device
                Toast.makeText(getActivity(), "This device doesn't have a mic!", Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            Log.e(APP_TAG,"Exception in recording media"+e.getMessage());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == getActivity().RESULT_OK) {
                Uri takenPhotoUri = getPhotoFileUri(photoFileName + SEPARATOR+ Integer.toString(currQuesNo) + PIC_EXT);
                imageURI = takenPhotoUri.toString();
                Log.d(APP_TAG,"qs image uri = " + imageURI);
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(takenPhotoUri.getPath());

                // Load the taken image into a preview
                ivQsPic.setImageBitmap(takenImage);

            } else { // Result was a failure
                Toast.makeText(getActivity(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }




    // Returns the Uri for a photo stored on disk given the fileName
    public Uri getPhotoFileUri(String fileName) {
        // Only continue if the SD Card is mounted
        if (isExternalStorageAvailable()) {
            // Get safe storage directory for photos
            // Use `getExternalFilesDir` on Context to access package-specific directories.
            // This way, we don't need to request external read/write runtime permissions.
            File mediaStorageDir = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
                Log.d(APP_TAG, "failed to create directory");
            }

            // Return the file target for the photo based on filename
            return Uri.fromFile(new File(mediaStorageDir.getPath() + File.separator + fileName));
        }
        return null;
    }

    // Returns true if external storage for photos is available
    private boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }



    private Question saveLocally(String question, String userName) {
        Question q = Question.toQuestion(question,userName);
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putInt(QNO, currQuesNo);
        editor.commit();
        if(q!=null) {
            if (imageURI != null)
                q.setImageURI(imageURI);
            if (audioURI!=null)
                q.setAudioURI(audioURI);
        }
        return q;
    }

    private void saveToParse(String question) {
        Entry qsEntry = new Entry();
//        qsEntry.setQuestion();
        qsEntry.setText(question);
//        qsEntry.setUser((User) User.getCurrentUser());

        Post qsPost = new Post();
        qsPost.setQuestion(qsEntry);

//        qsEntry.setPost(qsPost);

        qsEntry.saveInBackground();
        qsPost.saveInBackground();
    }


    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            int charsRemaining = QS_CHAR_LIMIT - s.length();
            tvCharsLeft.setText(Integer.toString(charsRemaining));

            if (charsRemaining >= 0 && charsRemaining < QS_CHAR_LIMIT) {
                btAskQs.setEnabled(true);
                tvCharsLeft.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
            } else {
                btAskQs.setEnabled(false);
                if (charsRemaining < 0)
                    tvCharsLeft.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                qsPosted = false;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ask_question, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    public Entry buildQuestionEntry(Question question) {
        Entry qsEntry = new Entry();
        qsEntry.setAsQuestion();
        qsEntry.setType(question.getType());

        if (question.getQuestion() != null)
            qsEntry.setText(question.getQuestion());

        return qsEntry;
    }
}
