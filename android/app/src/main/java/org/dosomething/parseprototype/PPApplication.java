package org.dosomething.parseprototype;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;

/**
 * Created by juy on 5/15/15.
 */
public class PPApplication extends Application {

    public void onCreate() {
        String appId = getResources().getString(R.string.parse_app_id);
        String clientKey = getResources().getString(R.string.parse_client_key);
        Parse.initialize(this, appId, clientKey);
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }

}
