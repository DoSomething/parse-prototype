package org.dosomething.parseprototype;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;


public class NumberActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number);

        if (getIntent().hasExtra("com.parse.Data")) {
            String data = getIntent().getStringExtra("com.parse.Data");
            try {
                JSONObject json = new JSONObject(data);
                String strUri = json.getString("uri");
                Uri uri = Uri.parse(strUri);
                String number = uri.getQueryParameter("number");
                TextView view = (TextView) findViewById(R.id.number);
                view.setText(number);
            }
            catch (Exception e) {
                Toast.makeText(this, "Unable to get number :(", Toast.LENGTH_LONG);
            }
        }

    }
}
