package edu.umich.eecs.audiologger;

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
public class DoSample extends BroadcastReceiver {
    final String TAG = "DoSampler";

    @Override
    public void onReceive(final Context context, Intent intent) {
        // For our recurring task, we'll just display a message
        Toast.makeText(context, "I'm running", Toast.LENGTH_SHORT).show();


        new Thread(new Runnable() {
            @Override
            public void run() {

                // Start the recorder
                final MediaRecorder mrecorder = new MediaRecorder();
                mrecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mrecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                String filename = Environment.getExternalStorageDirectory() + File.separator + "radiologger" + File.separator + System.currentTimeMillis() + ".m4a";
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
                AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                am.setStreamVolume(am.STREAM_MUSIC, am.getStreamMaxVolume(am.STREAM_MUSIC), 0);

                // Play the script.
                final MediaPlayer mplayer = MediaPlayer.create(context, R.raw.script);
                mplayer.start();
                mplayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            mrecorder.stop();
                            mrecorder.release();
                            mplayer.release();
                        }
                    }
                );
            }
        }).start();
    }
}
