# Strap Kit JS Android Wear

## Version
0.2.0

## License

See [LICENSE](LICENSE)

## Dependencies

See [README-Dependencies.md](README-Dependencies.md)

## Create your Android Wear app from Strap Kit JS

From your strap kit project folder
```sh
$ strapkit platform add android-wear
```
And that't it!
#### Build
```sh
$ strapkit build android-wear
```
#### Deploy
This will install the debug version of your app on your mobile and wear devices. Make sure they are either emulators or connected through USB to your computer.
```sh
$ strapkit install android-wear
```

## Add to Existing Android Phone App
Minimum SDK 4.3
#### Create a Wear project within your App
1. Make sure you have the SDK for API 20 installed
2. You need to import an Android Wear module. Right click on the your project and select "Open Module Settings" Then click the "plus" button which should bring up a wizard to add a Wear Module.
3. Module name should be 'wear'
4. Make sure your 'wear' package name is the same as your app's package
5. Choose No Activity to create your module
6. In your mobile app's gradle file  
```sh
dependencies {
        wearApp project(':wear')
}
```

#### Add Strap Kit JS to your App


First you need to generate the necessary files from your root directory:
```sh
$ strapkit platform add android-wear
$ cd platforms/android-wear/libraries
```
In the libraries folder, you will see 4 aar files and an assets folder which need to be copied.
1. Copy the contents of the assets folder into your mobile apps "assets" folder.
2. Mobile: Copy both "*-mobile.aar" files into your mobile's /libs directory. In your build.gradle:
```
repositories {
    flatDir {
        dirs 'libs'
    }
}
dependencies {
    compile 'com.google.android.gms:play-services:6.5.87'
    compile 'com.google.code.gson:gson:2.2.+'
    compile 'com.squareup.okhttp:okhttp:2.2.0'
    compile(name: 'strapkit_lib-mobile', ext: 'aar')
    compile(name: 'strapmetrics-mobile', ext: 'aar')
}
```
3. Mobile: Add into your Application or Launch Activity:
```java
    import com.straphq.strapkit.strapkit_lib.messaging.StrapKitMessageService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This is all you need for an already developed app
        Intent serviceIntent = new Intent(this, StrapKitMessageService.class);
        startService(serviceIntent);
    }
```
4. Wear: Copy both "*-wear.aar" to your wear's /libs folder. In your build.gradle:
```
dependencies {
    compile 'com.google.android.support:wearable:1.1.0'
    compile 'com.google.android.gms:play-services-wearable:6.5.87'
    compile 'com.google.code.gson:gson:2.2.+'
    compile(name: 'strapkit-wear', ext: 'aar')
    compile(name: 'strapmetrics-wear', ext: 'aar')
}
```
5. In your wear's Android Manifest file add the following launcher activity under the "application" tag:
```xml
        <activity
            android:name="com.straphq.strapkit.framework.StrapKitSplashActivity"
            android:label="@string/app_name">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
```
6. In both your mobile and wear Manifest files, you must have the same permissions with the minimum as follows:
```xml
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.BROADCAST_STICKY"/>
    <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
```
#### Deploy
To test your app, [Android Wear docs](https://developer.android.com/training/wearables/apps/creating.html) are extremely useful for walking you through the steps.
