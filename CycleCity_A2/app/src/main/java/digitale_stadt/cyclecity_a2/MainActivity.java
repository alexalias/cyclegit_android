package digitale_stadt.cyclecity_a2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity {
    Intent trackerService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    //Specifies actions to be performed when the play/stop button is clicked
    //Our play/stop button is a toggle button!
    public void startTracking(View view){
        boolean on = ((ToggleButton) view).isChecked();
        if (on) {
            Log.i("info", "Tracking is on!");
            Toast.makeText(MainActivity.this, "Tacking started", Toast.LENGTH_SHORT).show();
            trackerService = new Intent(this, digitale_stadt.cyclecity_a2.TrackerService.class);
            Log.d("MAIN", "Intent wurde erzeugt");
            this.startService(trackerService);
            Log.d("MAIN", "TrackerService wurde gestartet");

        }
        else {
            Log.i("info", "Tracking is off!");
            Toast.makeText(MainActivity.this, "Tacking stopped", LENGTH_SHORT).show();
            this.stopService(trackerService);
        }

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
}
