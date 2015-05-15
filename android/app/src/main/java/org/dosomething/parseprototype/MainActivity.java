package org.dosomething.parseprototype;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class MainActivity extends Activity implements View.OnClickListener {

    // References to UI elements
    private EditText mEditNameView;
    private EditText mEditPokeView;
    private Button mNameButton;
    private Button mPokeButton;

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
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.name_button) {
            String name = mEditNameView.getText().toString();
            Toast.makeText(this, "TODO: Update server with name: " + name, Toast.LENGTH_SHORT).show();
        }
        else if (view.getId() == R.id.poke_button) {
            String from = mEditNameView.getText().toString();
            String to = mEditPokeView.getText().toString();
            Toast.makeText(this, "TODO: Send poke from " + from + " to " + to, Toast.LENGTH_SHORT).show();
        }
    }
}
