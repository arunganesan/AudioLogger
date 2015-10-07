package edu.umich.eecs.audiologger;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity {
    Button toggleButton;
    TextView statusText;
    AlarmManager am;
    PendingIntent pendingIntent;
    Recorder recorder;

    final String TAG = "MainA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        wireUI();
        am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(MainActivity.this, DoSample.class);
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
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


    /**
     * Function finds the UI elements and sets callback functions
     */
    void wireUI () {
        toggleButton = (Button)findViewById(R.id.toggleButton);
        statusText = (TextView) findViewById(R.id.textView);
        toggleButton.setOnClickListener(setupAlarm);
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
                am.cancel(pendingIntent);
                pendingIntent.cancel();
            } else {
                toggleButton.setText("Stop Experiment");
                Toast.makeText(MainActivity.this, "Started intent.", Toast.LENGTH_SHORT).show();
                Intent alarmIntent = new Intent(MainActivity.this, DoSample.class);
                pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                am.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 8000, pendingIntent);
            }
        }
    };


    boolean alarmIsRunning () {

        return (PendingIntent.getBroadcast(MainActivity.this, 0,
                new Intent(MainActivity.this, DoSample.class),
                PendingIntent.FLAG_NO_CREATE) != null);
    }
}
