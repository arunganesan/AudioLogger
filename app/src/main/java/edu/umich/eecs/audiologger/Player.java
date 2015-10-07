package edu.umich.eecs.audiologger;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

/**
 * Created by Arun on 11/21/2014.
 */
public class Player {
    MainActivity myActivity;


    public Player(MainActivity myActivity) {
        AudioManager am = (AudioManager) myActivity.getSystemService(Context.AUDIO_SERVICE);
        am.setStreamVolume(am.STREAM_MUSIC, am.getStreamMaxVolume(am.STREAM_MUSIC), 0);
        this.myActivity = myActivity;
    }


    public void startPlaying () {

    }

}
