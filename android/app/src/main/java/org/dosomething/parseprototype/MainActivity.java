package org.dosomething.parseprototype;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.parse.Parse;
import com.parse.ParseInstallation;

import org.json.JSONObject;

import java.util.Random;


public class MainActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "ParsePrototype";

    // Server url
    private String mServerUrl;

    // References to UI elements
    private EditText mEditNameView;
    private EditText mEditNotifyView;
    private Button mNameButton;
    private Button mPuppetButton;
    private Button mPokeButton;
    private Button mNumberButton;

    // Queue for Volley requests
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditNameView = (EditText)findViewById(R.id.name_edit);
        mEditNotifyView = (EditText)findViewById(R.id.notify_edit);
        mNameButton = (Button)findViewById(R.id.name_button);
        mPuppetButton = (Button)findViewById(R.id.puppet_button);
        mPokeButton = (Button)findViewById(R.id.poke_button);
        mNumberButton = (Button)findViewById(R.id.number_button);

        mNameButton.setOnClickListener(this);
        mPuppetButton.setOnClickListener(this);
        mPokeButton.setOnClickListener(this);
        mNumberButton.setOnClickListener(this);

        mQueue = Volley.newRequestQueue(this);

        mServerUrl = getResources().getString(R.string.server_url);
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        if (id == R.id.name_button) {
            String url = mServerUrl + "/users";
            String name = mEditNameView.getText().toString();

            ParseInstallation parseInstall = ParseInstallation.getCurrentInstallation();
            String installationId = parseInstall.getString("installationId");
            String deviceType = parseInstall.getString("deviceType");

            String deviceToken = parseInstall.getString("deviceToken");
            String gcmSenderId = parseInstall.getString("GCMSenderId");
            Log.d("Parse", "deviceToken: " + deviceToken + " // GCMSenderId: " + gcmSenderId);

            JSONObject body = new JSONObject();
            try {
                body.put("name", name);
                body.put("installation_id", installationId);
                body.put("device_type", deviceType);
            }
            catch (Exception e) {
                // eh, let's just not let this happen...
            }

            Response.Listener<JSONObject> onSuccess = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                }
            };

            Response.ErrorListener onError = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, error.getMessage());
                }
            };

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, body, onSuccess, onError);
            mQueue.add(request);
        }
        else if (id == R.id.puppet_button || id == R.id.poke_button || id == R.id.number_button) {
            String url = mServerUrl + "/notify";
            String from = mEditNameView.getText().toString();
            String to = mEditNotifyView.getText().toString();

            JSONObject body = new JSONObject();
            try {
                body.put("from", from);
                body.put("to", to);

                if (id == R.id.puppet_button) {
                    body.put("notificationType", "puppet");
                }
                else if (id == R.id.poke_button) {
                    body.put("notificationType", "poke");
                }
                else if (id == R.id.number_button) {
                    body.put("notificationType", "number");
                    Random random = new Random();
                    body.put("number", random.nextInt());
                }
            }
            catch (Exception e) {
                // eh, let's also just not let this happen..
            }

            Response.Listener<JSONObject> onSuccess = new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Toast.makeText(MainActivity.this, "Notification sent", Toast.LENGTH_SHORT).show();
                }
            };

            Response.ErrorListener onError = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, error.getMessage());
                }
            };

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, body, onSuccess, onError);
            mQueue.add(request);
        }

    }
}
