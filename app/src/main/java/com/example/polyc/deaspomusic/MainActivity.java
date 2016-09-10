package com.example.polyc.deaspomusic;

import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.os.Bundle;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;

import android.media.MediaPlayer.OnPreparedListener;
import android.view.MotionEvent;
import android.widget.MediaController;
import android.widget.TextView;
import java.io.IOException;
import java.net.URI;


public class MainActivity extends Activity implements OnPreparedListener, MediaController.MediaPlayerControl{
    MediaMetadataRetriever metaRetriever;
    private static final String TAG = "AudioPlayer";

    public static final String AUDIO_FILE_NAME = "audioFileName";

    private MediaPlayer mediaPlayer;
    private MediaController mediaController;
    private String audioFile;
    private Uri myUri;
    private Handler handler = new Handler();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //audioFile = this.getIntent().getStringExtra("aspo");


        Uri myUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.itsnotmytime);
        /*metaRetriever.setDataSource(this, myUri);
        audioFile = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);*/
        audioFile = "To fix";
        ((TextView)findViewById(R.id.now_playing_text)).setText(audioFile);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);

        mediaController = new MediaController(this);

        try {
            //mediaPlayer.setDataSource(audioFile);
            mediaPlayer.setDataSource(this, myUri);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            Log.e(TAG, "Could not open file " + audioFile + " for playback.", e);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        mediaController.hide();
        mediaPlayer.stop();
        mediaPlayer.release();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //the MediaController will hide after 3 seconds - tap the screen to make it appear again
        mediaController.show();
        return false;
    }

    //--MediaPlayerControl methods----------------------------------------------------
    public void start() {
        mediaPlayer.start();
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    public int getAudioSessionId() {
        return mediaPlayer.getAudioSessionId();
    }

    public void seekTo(int i) {
        mediaPlayer.seekTo(i);
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public int getBufferPercentage() {
        return 0;
    }

    public boolean canPause() {
        return true;
    }

    public boolean canSeekBackward() {
        return true;
    }

    public boolean canSeekForward() {
        return true;
    }
    //--------------------------------------------------------------------------------

    public void onPrepared(MediaPlayer mediaPlayer) {
        Log.d(TAG, "onPrepared");
        mediaController.setMediaPlayer(this);
        mediaController.setAnchorView(findViewById(R.id.main_audio_view));

        handler.post(new Runnable() {
            public void run() {
                mediaController.setEnabled(true);
                mediaController.show();
            }
        });
    }
}