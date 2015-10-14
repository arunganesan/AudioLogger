package edu.umich.eecs.audiologger;

import android.app.AlarmManager;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.List;

public class MainActivity extends ActionBarActivity implements GroundTruthDialog.DialogListener {
    Button toggleButton;
    TextView statusText;
    DoSample sampler;

    final String TAG = "MainA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        sampler = new DoSample();
        wireUI();
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
        toggleButton.setOnClickListener(doExperiment);
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
    View.OnClickListener doExperiment = new View.OnClickListener() {
        @Override
        public void onClick (View v) {
            statusText.setText("Running experiment");
            toggleButton.setEnabled(false);
            sampler.doOne(MainActivity.this);
        }
    };

    public void doneExperiment () {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                toggleButton.setEnabled(true);
                updateText();
            }
        });
    }


    @Override
    public void onClick(DialogFragment dialog, String item) {
        String tempname = Environment.getExternalStorageDirectory() + File.separator + "radiologger" + File.separator + "temp.m4a";
        String newname = Environment.getExternalStorageDirectory() + File.separator + "radiologger" + File.separator + System.currentTimeMillis() + "-" + item + ".m4a";
        File from = new File(tempname);
        File to = new File(newname);
        from.renameTo(to);
        doneExperiment();
    }
}
