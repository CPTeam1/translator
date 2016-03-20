package com.cp1.translator.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.cp1.translator.R;
import com.cp1.translator.login.LoginUtils;
import com.cp1.translator.models.Entry;
import com.cp1.translator.models.Post;
import com.cp1.translator.models.Question;
import com.cp1.translator.models.Types;
import com.cp1.translator.models.User;
import com.cp1.translator.push.EntryPusher;
import com.cp1.translator.utils.Constants;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.SaveCallback;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.cp1.translator.utils.Constants.ANS_HINT;
import static com.cp1.translator.utils.Constants.APP_TAG;
import static com.cp1.translator.utils.Constants.AUDIO;
import static com.cp1.translator.utils.Constants.AUDIO_EXT;
import static com.cp1.translator.utils.Constants.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE;
import static com.cp1.translator.utils.Constants.IMAGE;
import static com.cp1.translator.utils.Constants.PIC_EXT;
import static com.cp1.translator.utils.Constants.QS_HINT;
import static com.cp1.translator.utils.Constants.SEPARATOR;
import static com.cp1.translator.utils.Constants.VIDEO;
import static com.cp1.translator.utils.Constants.VIDEO_CAPTURE;
import static com.cp1.translator.utils.Constants.photoFileName;

public class AskQuestion extends AppCompatActivity  {
    // Question views
    @Bind(R.id.btAskQs) Button btAskQs;
    @Bind(R.id.etQs) EditText etQs;
    @Bind(R.id.tvChars) TextView tvCharsLeft;

    // Media recording buttons
    @Bind(R.id.ibClickPic) ImageButton ibClickPic;
    @Bind(R.id.ibRecAudio) ImageButton ibRecAudio;
    @Bind(R.id.ibRelAudio) ImageButton ibRelAudio;
    @Bind(R.id.ibRecVideo) ImageButton ibRecVideo;

    @Nullable @Bind(R.id.pbRecording) ProgressBar pbRecording;
    @Nullable @Bind(R.id.ibPlayAudio) ImageButton ibPlayAudio;
    @Nullable @Bind(R.id.ibStopAudio) ImageButton ibStopAudio;

    // Media Views
    @Nullable @Bind(R.id.rvMediaView) RelativeLayout rvMediaView;
    @Nullable @Bind(R.id.vvQsVideo) VideoView vvQsVideo;
    @Nullable @Bind(R.id.ivQsPic) ImageView ivQsPic;
    @Nullable @Bind(R.id.fabCancel) FloatingActionButton fabCancel;

    // Spinners
    @Bind(R.id.fromLang) Spinner spinFromLang;
    @Bind(R.id.toLang) Spinner spinToLang;

    private int textColor;

    private static int QS_CHAR_LIMIT = 140;
    private static boolean isRecReleased = false;
    private String imageURI;
    private String audioURI;
    private Uri videoURI;
    private String mAudioFileName;
    private MediaRecorder mediaRecorder;
    private MediaPlayer   mediaPlayer = null;
    private String fromLang = null;
    private String toLang = null;

    private boolean isMediaCaptured = false;

    private boolean isAnswer; // false by default

    // Defines the listener interface
    public interface AskQuestionDialogListener {
        void onFinishAsking(Entry newQuestion);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LoginUtils.checkIfLoggedIn(this);
        super.onCreate(savedInstanceState);

        // since both MainActivity and PostActivity start this AskQuestion Activity,
        // we should distinguish which is for creating a new question or answer.
        isAnswer = getIntent().getBooleanExtra(Constants.IS_ANSWER_KEY, false);

        setContentView(R.layout.activity_ask_question);
        ButterKnife.bind(this);

        etQs.addTextChangedListener(textWatcher);

        if(isAnswer){
            etQs.setHint(ANS_HINT);
        }
        else{
            etQs.setHint(QS_HINT);
        }

        textColor = tvCharsLeft.getCurrentTextColor();

        btAskQs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 1) save qs in DB
                // 2) pass qs back to main activity
                // 3) display in adapter
                if (etQs.getText() != null) {

                    String question = etQs.getText().toString();
                    fromLang = spinFromLang.getSelectedItem().toString();
                    toLang = spinToLang.getSelectedItem().toString();

                    if (toLang != null && fromLang != null) {


                        Question qsDB = saveLocally(question, User.getCurrentUser().getEmail());

                        Entry qsEntry = saveToParse(qsDB);
                        Log.d(APP_TAG, "Adding question here");
                        Intent displayQsIntent = new Intent(getApplicationContext(), MainActivity.class);
                        displayQsIntent.putExtra("question", qsEntry);
                        setResult(RESULT_OK, displayQsIntent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please choose which Language you want to translate to!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void onLaunchCamera(View view) {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // set the image file name
        intent.putExtra(MediaStore.EXTRA_OUTPUT, getPhotoFileUri(photoFileName + PIC_EXT));

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mediaRecorder!=null && !isRecReleased){
            Log.d(APP_TAG,"On Pause called. Stopping recording..");
            stopRecording();
        }

        if(mediaPlayer!=null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void stopRecording() {
        if(mediaRecorder!=null) {
            try {
                mediaRecorder.stop();
                mediaRecorder.reset();
                mediaRecorder.release();
                isRecReleased = true;
                Toast.makeText(getApplicationContext(), "Stopping recording..", Toast.LENGTH_SHORT).show();
            }catch(Exception e){
                Log.e(APP_TAG,"exception in stopping recording: "+e.getMessage());
            }
        }
    }

    public void onReleaseRecorder(View v){
        if(mediaRecorder!=null){
            ibRelAudio.setVisibility(View.GONE);
            pbRecording.setVisibility(ProgressBar.INVISIBLE);
            Log.d(APP_TAG, "Stopping recording..");
            stopRecording();
            //mediaRecorder = null;
            audioURI = mAudioFileName;
            hideMediaRecButtons();
            ibPlayAudio.setVisibility(View.VISIBLE);
            ibStopAudio.setVisibility(View.VISIBLE);
            spinFromLang.setVisibility(View.INVISIBLE);
            spinToLang.setVisibility(View.INVISIBLE);
        }
    }

    public void stopPlaying(View v) {
        if(mediaPlayer!=null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void onPlay(View view){
        if(mediaRecorder!=null){
            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

                mediaPlayer.setDataSource(mAudioFileName);
                mediaPlayer.prepare(); // must call prepare first
                mediaPlayer.start(); // then start
            }catch(IOException e){
                Log.e(APP_TAG,"Exception in playing media "+e.getMessage());
            }
        }
        else{
            Toast.makeText(this, "No playable media found!", Toast.LENGTH_LONG).show();
        }
    }

    public void onLaunchVideoRecorder(View view){
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
            rvMediaView.setVisibility(View.VISIBLE);
            vvQsVideo.setVisibility(View.VISIBLE);
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            File mediaFile = new File(
                    Environment.getExternalStorageDirectory().getAbsolutePath() + "/myvideo.mp4");
            videoURI = Uri.fromFile(mediaFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, videoURI);
            startActivityForResult(intent, Constants.VIDEO_CAPTURE);
        } else {
            Toast.makeText(this, "No camera on device", Toast.LENGTH_LONG).show();
        }
    }

    public void onCancelMedia(View view){
        showMediaRecButtons();
        rvMediaView.setVisibility(View.GONE);
    }


    public void onLaunchAudioRecorder(View v){

        try {
            // Verify that the device has a mic first
            PackageManager pmanager = this.getPackageManager();
            if (pmanager.hasSystemFeature(PackageManager.FEATURE_MICROPHONE)) {
                ibRecAudio.setVisibility(View.INVISIBLE);
                ibRelAudio.setVisibility(View.VISIBLE);
                pbRecording.setVisibility(ProgressBar.VISIBLE);
                // Set the file location for the audio
                mAudioFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
                mAudioFileName += "/audiorecordtest"+SEPARATOR + Long.toString(System.currentTimeMillis())+AUDIO_EXT;
                Log.d(APP_TAG,"Audio file for question: "+mAudioFileName);
                isRecReleased = false;

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
                Log.d(APP_TAG, "Recording in progress..");

            } else { // no mic on device
                Toast.makeText(this, "This device doesn't have a mic!", Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            Log.e(APP_TAG,"Exception in recording media"+e.getMessage());
            Toast.makeText(this, "Error in recording audio", Toast.LENGTH_LONG).show();
        }
    }

    public void hideMediaRecButtons(){
        ibClickPic.setVisibility(View.GONE);
        ibRecAudio.setVisibility(View.GONE);
        ibRecVideo.setVisibility(View.GONE);
    }

    public void showMediaRecButtons(){
        ibClickPic.setVisibility(View.VISIBLE);
        ibRecAudio.setVisibility(View.VISIBLE);
        ibRecVideo.setVisibility(View.VISIBLE);
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                hideMediaRecButtons();
                rvMediaView.setVisibility(View.VISIBLE);
                Uri takenPhotoUri = getPhotoFileUri(photoFileName + PIC_EXT);
                imageURI = takenPhotoUri.getPath();
                Log.d(APP_TAG,"qs image uri = " + imageURI + "Bitmap: "+takenPhotoUri.getPath());
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(takenPhotoUri.getPath());

                // Load the taken image into a preview
                ivQsPic.setImageBitmap(takenImage);

            } else { // Result was a failure
                showMediaRecButtons();
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }

        if(requestCode == VIDEO_CAPTURE){
            if (resultCode == RESULT_OK) {
                hideMediaRecButtons();

                rvMediaView.setVisibility(View.VISIBLE);
                vvQsVideo.setVisibility(View.VISIBLE);
                Log.d(APP_TAG,"Video saved at: "+data.getData().toString());

                Toast.makeText(this, "Video has been saved to:\n" + data.getData(), Toast.LENGTH_SHORT).show();
                playbackRecordedVideo();
            } else if (resultCode == RESULT_CANCELED) {
                showMediaRecButtons();
                Toast.makeText(this, "Video recording cancelled.",  Toast.LENGTH_SHORT).show();
            } else {
                showMediaRecButtons();
                Toast.makeText(this, "Failed to record video",  Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void playbackRecordedVideo() {
        Log.d(APP_TAG,"Video should play now!");
        Toast.makeText(this, "Video should play now",  Toast.LENGTH_SHORT).show();
        vvQsVideo.setVideoURI(videoURI);
        // TODO: remove this, its just a test video
//        vvQsVideo.setVideoURI(Uri.parse("android.resource://" + getPackageName() +"/"+R.raw.small_video));

        vvQsVideo.setMediaController(new MediaController(this));
        vvQsVideo.requestFocus();
        vvQsVideo.start();
    }


    // Returns the Uri for a photo stored on disk given the fileName
    public Uri getPhotoFileUri(String fileName) {
        // Only continue if the SD Card is mounted
        if (isExternalStorageAvailable()) {
            // Get safe storage directory for photos
            // Use `getExternalFilesDir` on Context to access package-specific directories.
            // This way, we don't need to request external read/write runtime permissions.
            File mediaStorageDir = new File(
                    getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

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

        if(q!=null) {
            q.setType(Types.TEXT);

            if (imageURI != null) {
                q.setImageURI(imageURI);
                q.setType(Types.PICTURE);
            }
            if (audioURI!=null) {
                q.setAudioURI(audioURI);
                q.setType(Types.AUDIO);
            }

            Log.d(APP_TAG,"Translate from: "+fromLang +" to: "+toLang);

            if(fromLang!=null)
                q.setFromLang(fromLang);
            if(toLang!=null)
                q.setToLang(toLang);

            if(videoURI!=null) {
                Log.d(APP_TAG,"URI: "+videoURI);
                Log.d(APP_TAG,"Path: "+videoURI.getPath());
                q.setVideoURI(videoURI.getPath());
                q.setType(Types.VIDEO);
            }

            q.save();
        }
        return q;
    }

    private Entry saveToParse(Question question) {
        //1. Save the multimedia as file objects to Parse
        Map<String,ParseFile> multiMediaMap = saveMultimedia(question);
        User currUser = (User) User.getCurrentUser();

        Entry qsEntry = new Entry();
        qsEntry.setText(question.getQuestion());
        if (!isAnswer)
            qsEntry.setAsQuestion();
        qsEntry.setUser(currUser);

        qsEntry.setType(question.getType());

        qsEntry.setFromLang(question.getFromLang());
        qsEntry.setToLang(question.getToLang());

        if(multiMediaMap!=null && multiMediaMap.size()>0) {
            if(multiMediaMap.containsKey(IMAGE))
                qsEntry.setImageUrl(multiMediaMap.get(IMAGE));
            if(multiMediaMap.containsKey(VIDEO))
                qsEntry.setVideoUrl(multiMediaMap.get(VIDEO));
            if(multiMediaMap.containsKey(AUDIO))
                qsEntry.setAudioUrl(multiMediaMap.get(AUDIO));
        }

        qsEntry.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(APP_TAG, "Error in saving to parse backend" + e.getMessage());
                } else {
                    Log.d(APP_TAG, "Saved successfully");

                }
            }
        });
        EntryPusher.pushEntryToFriends(qsEntry.getText());

        // create a new Post ONLY IF the Entry is a "question"
        Post qsPost = new Post();
        if (!isAnswer) {
            qsPost.setQuestion(qsEntry);
            qsPost.saveInBackground();
        }

        return qsEntry;
    }

    private ParseFile convertURIToParseFile(String path){
        ParseFile file = null   ;
        if(path!=null){
           try {
               FileInputStream fis = new FileInputStream(new File(path));
               byte[] byteArray   = IOUtils.toByteArray(fis);
               String fileName  = "question_"+Long.toString(System.currentTimeMillis());
               file = new ParseFile(fileName,byteArray);
           }catch (FileNotFoundException e) {
               Log.e(APP_TAG,"Exception while opening file "+ e.getMessage());
           } catch (IOException e) {
               Log.e(APP_TAG,"Exception while opening file "+ e.getMessage());
           }
        }
        return file;
    }

    private void saveFileInBackground(final ParseFile pfile){
        if(pfile!=null){
            // Upload the image into Parse Cloud
            pfile.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if(e!=null){
                        Log.e(APP_TAG,"Problem in saving File to parse backend: "+pfile.getName());
                    }
                    else
                        Log.d(APP_TAG,"File upload successful: "+pfile.getName());
                }
            });
        }
    }

    private Map<String,ParseFile> saveMultimedia(Question question) {
        Map<String,ParseFile> multiMediaMap = new HashMap<>();
        if(question!=null){
            if(question.getImageURI()!=null){
                ParseFile file = null;

                Bitmap bitmap = BitmapFactory.decodeFile(question.getImageURI());
                // Convert it to byte
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Compress image to lower quality scale 1 - 100
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] image = stream.toByteArray();
                // Create the ParseFile
                String picName = "question_"+Long.toString(System.currentTimeMillis());
                file = new ParseFile(picName,image);
                Log.d(APP_TAG,"Saving image question: "+file.getName());
                saveFileInBackground(file);
                if(file!=null)
                    multiMediaMap.put(IMAGE,file);
            }
            if(question.getVideoURI()!=null){
                ParseFile file = convertURIToParseFile(question.getVideoURI());
                Log.d(APP_TAG,"Saving video question: "+file.getName());
                saveFileInBackground(file);
                if(file!=null)
                    multiMediaMap.put(VIDEO,file);
            }
            if(question.getAudioURI()!=null){
                ParseFile file = convertURIToParseFile(question.getAudioURI());
                Log.d(APP_TAG,"Saving audio question: "+file.getName());
                saveFileInBackground(file);
                if(file!=null)
                    multiMediaMap.put(AUDIO,file);
            }
        }
        return multiMediaMap;
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
                isRecReleased = false;
            }
        }
    };
}
