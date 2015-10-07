package edu.umich.eecs.audiologger;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button toggleButton;
    TextView statusText;
    AlarmManager am;

    final String TAG = "MainA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        wireUI();
        am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume () {
        super.onResume();
        updateText();
   }

    /**
     * Function finds the UI elements and sets callback functions
     */
    void wireUI () {
        toggleButton = (Button)findViewById(R.id.toggleButton);
        statusText = (TextView) findViewById(R.id.textView);
        toggleButton.setOnClickListener(setupAlarm);

        if (alarmIsRunning()) toggleButton.setText("Stop Experiment");
        else toggleButton.setText("Start Experiment");

        updateText();
    }


    void updateText (){
        String filename = Environment.getExternalStorageDirectory() + File.separator + "radiologger";
        File savedir = new File(filename);
        File[] files = savedir.listFiles();
        statusText.setText("Collected " + files.length + " samples");

    }

    /**
     * If the alarmmanager already has regular alarm, this turns on
     * Otherwise, it turns off
     */
    View.OnClickListener setupAlarm = new View.OnClickListener() {
        @Override
        public void onClick (View v) {
            if (alarmIsRunning()) {
                Toast.makeText(MainActivity.this, "Stopped intent.", Toast.LENGTH_SHORT).show();
                toggleButton.setText("Start Experiment");

                PendingIntent pi = PendingIntent.getBroadcast(MainActivity.this,
                        0, new Intent(MainActivity.this, DoSample.class), PendingIntent.FLAG_NO_CREATE);

                am.cancel(pi);
                pi.cancel();
            } else {
                toggleButton.setText("Stop Experiment");
                Toast.makeText(MainActivity.this, "Started intent.", Toast.LENGTH_SHORT).show();
                PendingIntent pi = PendingIntent.getBroadcast(MainActivity.this,
                        0, new Intent(MainActivity.this, DoSample.class), PendingIntent.FLAG_UPDATE_CURRENT);
                am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000*60*1, pi);
            }
        }
    };


    boolean alarmIsRunning () {

        return (PendingIntent.getBroadcast(MainActivity.this, 0,
                new Intent(MainActivity.this, DoSample.class),
                PendingIntent.FLAG_NO_CREATE) != null);
    }
}
