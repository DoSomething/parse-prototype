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


public class MainActivity extends Activity implements View.OnClickListener {

    // Server url
    private String mServerUrl;

    // References to UI elements
    private EditText mEditNameView;
    private EditText mEditPokeView;
    private Button mNameButton;
    private Button mPokeButton;

    // Queue for Volley requests
    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditNameView = (EditText)findViewById(R.id.name_edit);
        mEditPokeView = (EditText)findViewById(R.id.poke_edit);
        mNameButton = (Button)findViewById(R.id.name_button);
        mPokeButton = (Button)findViewById(R.id.poke_button);

        mNameButton.setOnClickListener(this);
        mPokeButton.setOnClickListener(this);

        mQueue = Volley.newRequestQueue(this);

        mServerUrl = getResources().getString(R.string.server_url);
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.name_button) {
            String name = mEditNameView.getText().toString();
            String url = mServerUrl + "/users";

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

            Response.Listener<JSONObject> onSuccess = new Response.Listener<JSONObject>(){
                @Override
                public void onResponse(JSONObject response) {
                    Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                }
            };

            Response.ErrorListener onError = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            };

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, body, onSuccess, onError);
            mQueue.add(request);
        }
        else if (view.getId() == R.id.poke_button) {
            String from = mEditNameView.getText().toString();
            String to = mEditPokeView.getText().toString();
            Toast.makeText(this, "TODO: Send poke from " + from + " to " + to, Toast.LENGTH_SHORT).show();
        }

    }
}
