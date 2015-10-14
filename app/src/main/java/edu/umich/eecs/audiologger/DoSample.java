package edu.umich.eecs.audiologger;

import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

/**
 * Created by Arun on 10/7/2015.
 *
 * The receiver wakes up, plays the script, records audio for the duration,
 * and then goes back to sleep. Very very simple.
 */
public class DoSample {
    final String TAG = "DoSampler";
    MainActivity mainActivity;

    public void doOne (final MainActivity mainActivity) {
        // For our recurring task, we'll just display a message
        this.mainActivity = mainActivity;

        new Thread(new Runnable() {
            @Override
            public void run() {

                // Start the recorder
                final MediaRecorder mrecorder = new MediaRecorder();
                mrecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mrecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                String filename = Environment.getExternalStorageDirectory() + File.separator + "radiologger" + File.separator + "temp.m4a";
                Log.v(TAG, "Saving to file: " + filename);
                mrecorder.setOutputFile(filename);
                mrecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

                try {
                    mrecorder.prepare();
                } catch (IOException e) {
                    Log.e(TAG, "Prepare() failed");
                }
                mrecorder.start();

                // Set maximum volume
                AudioManager am = (AudioManager) mainActivity.getSystemService(Context.AUDIO_SERVICE);
                am.setStreamVolume(am.STREAM_MUSIC, am.getStreamMaxVolume(am.STREAM_MUSIC), 0);


                /* Record for 5 seconds */
                try { Thread.sleep(5000); } catch (Exception e) {}

                mrecorder.stop();
                mrecorder.release();
                DialogFragment newFragment = new GroundTruthDialog();
                newFragment.show(mainActivity.getFragmentManager(), "Stations");
            }
        }).start();
    }




}
