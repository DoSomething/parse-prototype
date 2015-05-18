# Parse Prototype

### Purpose

This prototype is intended to answer how we could use Parse for mobile app push notifications.

Questions we intended to answer:

- What's the bare minimum of data we'd need to log from mobile app users to send targeted push notifications?
- How can we customize the title and message of push notifications?
- How can we customize the behavior of opening a push notification?


### How to use this

#### Server

A live server already exists here: http://ds-parse-prototype.herokuapp.com

The sample mobile apps are configured to send requests there. If you want to run the server instead on your local dev box, you'll need to:

- [Install Node](https://nodejs.org/)
- Go to the project folder: `$ cd <path to project folder>`
- Install dependencies: `$ npm install`
- Start the server: `$ node server`

You'll also then need to update your Android and iOS app configurations to point to this server.

#### Android

The sample app allows you to:

- Create a user: A user's name gets saved to the database. Additionally Parse provides the device type ("android" or "ios") and installation ID to identify this device.
- Specify a target to send notifications to by name.
  - Puppet: Notification sent to the user that opens up to a picture of Puppet Sloth
  - Poke: Generic notification that opens up to the main activity
  - Number: Notification sent to the user with a random number and opens up to an activity displaying that number

Values for the server location and the Parse id and keys can be changed in `values.xml`.

#### iOS

TBD


### Findings

For our purposes, using Parse seems like a feasible approach. Parse is implemented on the Android app to identify the unique devices for push and to handle the receiving of push notifications. On the server side, for sending push notifications we can use the Parse Rest API for configuring and triggering targeted push notifications.

What the notifications look like in the Android notification drawer:

![Android Notification Drawer](https://raw.githubusercontent.com/DoSomething/parse-prototype/master/notifications.png)

> What's the bare minimum of data we'd need to log from mobile app users to send targeted push notifications?

For each user we'll want to save the device type and the installation ID:

**Android:**

```
ParseInstallation parseInstall = ParseInstallation.getCurrentInstallation();
String installationId = parseInstall.getString("installationId");
String deviceType = parseInstall.getString("deviceType");
```

> How can we customize the title and message and behavior of opening a push notifications?

**Android:**

On requests made to the Rest API to send out a push, the device-specific installation id is used to identify who to push to. The `title` and `alert` can be set in the request body to customize the title and message that show up in the notification drawer. And the `uri` can be set to indicate how the app should handle opening a notification.

An example request made to the Rest API could look like:

```
body = {
  where: {
    installationId: doc.installation_id
  },
  data: {
    alert: 'Here\'s a puppet!',
    title: 'A Puppet!',
    uri: 'parseprototype://puppet'
  }
};

options = {
  url: 'https://api.parse.com/1/push',
  headers: {
    'X-Parse-Application-Id': PARSE_APP_ID,
    'X-Parse-REST-API-Key': PARSE_API_KEY
  },
  json: true,
  body: body
};

request.post(options, callback);
```

On the client side, a lot of the customization can just happen in the `AndroidManifest.xml`.

- To set a custom icon:
```
<meta-data
    android:name="com.parse.push.notification_icon"
    android:resource="@drawable/ic_puppetsloth" />
```

- To open up a specific activity when push data includes a URI, set the `<intent-filter>` for that Activity:
```
<activity
    android:name=".PuppetSlothActivity"
    android:label="@string/title_activity_puppet_sloth"
    android:parentActivityName=".MainActivity" >
    <intent-filter>
        <action android:name="android.intent.action.VIEW" />
        <category android:name="android.intent.category.DEFAULT" />
        <data android:scheme="parseprototype" android:host="puppet"/>
    </intent-filter>
</activity>
```

- For an Activity to pull data from a URI to further customize what happens when the Activity gets opened, get the Extra "com.parse.Data" from the Intent. This example is used for URIs that look like: `parseprototype://number?number=123123`.
```
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
```

**iOS:**

TBD