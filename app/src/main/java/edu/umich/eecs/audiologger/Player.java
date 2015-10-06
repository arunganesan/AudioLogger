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

    //public short [] sound = SHORTCHIRP;// = WN;

    int buffsize, spaceCounter;
    final static String TAG = "Player";
    public AudioTrack audioTrack;
    boolean isRunning = false, silenceOverride = false;
    int chirpPlayCount = 0;
    int tweakBy = 0;

    boolean scheduleAligner = false, scheduleTurnOn = false;

    //int SPACE = (int)(CHIRP.length * 3);
    double softwareVolume = 0;
    int SPACE;

    MainActivity myActivity;


    public Player(MainActivity myActivity) {
        resetSpace();
        AudioManager am = (AudioManager) myActivity.getSystemService(Context.AUDIO_SERVICE);
        am.setStreamVolume(am.STREAM_MUSIC, am.getStreamMaxVolume(am.STREAM_MUSIC), 0);
        this.myActivity = myActivity;
    }


    public void resetSpace () {
        spaceCounter = SPACE + tweakBy;
        tweakBy = 0;
    }

    public void setSpace (int new_space) { SPACE = new_space; }
    public int getSpace () { return SPACE; }

    public void turnOnSound () {
        scheduleTurnOn = true;
    }

    public void turnOffSound () {
        silenceOverride = true;
    }
    public boolean isSoundOn () { return !silenceOverride; }


    public void tweak (int amount) {
        //pauseSound = true;
        this.tweakBy = amount;
    }

    public void setSoftwareVolume (double softwareVolume) {
        //this.softwareVolume = 0;
        //this.softwareVolume = softwareVolume;
        AudioManager am = (AudioManager) myActivity.getSystemService(Context.AUDIO_SERVICE);
        int maxVol = am.getStreamMaxVolume(am.STREAM_MUSIC);
        int setVolume = (int) (((float)maxVol) * softwareVolume);
        Log.v(TAG, "Setting volume to " + setVolume + " out of " + maxVol);
        am.setStreamVolume(am.STREAM_MUSIC, setVolume, 0);
    }

    public double getSoftwareVolume () { return softwareVolume; }

    public void startPlaying () {
        // Set up audiotrack, start playing on loop until time to stop

        buffsize = AudioTrack.getMinBufferSize(44100, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
        //buffsize = 22304;
        //buffsize + 8000;
        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 44100, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, buffsize, AudioTrack.MODE_STREAM);
        audioTrack.play();
        isRunning = true;

       // XXX: Create a MediaPlayer to open a file and run it
    }

    public void stopPlaying() {
        isRunning = false;
        if (audioTrack != null) {
            audioTrack.stop();
            audioTrack.release();
        }
    }
}
