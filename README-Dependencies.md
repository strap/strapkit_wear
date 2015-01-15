# Strap Kit JS Android Wear Dependencies

## JAVA_HOME
#### Linux
Check if already set
```sh
$ echo $JAVA_HOME
```
If nothing there simply make this command: 
```sh
$ sudo apt-get install openjdk-7-jdk
```
#### Mac
Follow this link: http://javatechig.com/java/core-java/set-java_home-environment-variable-mac-os-x

## ADB
#### Linux
To install:
```sh
$ sudo apt-get install android-tools-adb
```
#### Mac
Should be included with Android SDK. Make sure you path is set up. Follow the Android SDK instructions

## ANDROID_HOME and Android SDK
If Android SDK already installed:
```sh
$ export ANDROID_HOME=/path/to/sdk
$ export PATH=$PATH:$ANDROID_HOME/tools
```
#### Linux Installation
1. Option 1 with IDE: Download [Android Studio](http://developer.android.com/sdk/index.html). Check that ANDROID_HOME is in the path following the steps above.
2. Option 2 without IDE: Follow these instructions: 
```sh
$ wget http://dl.google.com/android/android-sdk_r24.0.2-linux.tgz
$ mdkir -p ~/android-sdk
$ tar zxvf android-sdk_r24.0.2-linux.tgz -C ~/android-sdk/
$ rm android-sdk_r24.0.2-linux.tgz
$ export ANDROID_HOME=~/android-sdk/android-sdk-linux/
$ export PATH=$PATH:$ANDROID_HOME/tools
```

Note: For linux 64-bit machines, make sure you run the command: 
```sh
$ sudo apt-get install lib32stdc++6 lib32z1
```

#### Mac Installation
1. Option 1 with IDE: Download [Android Studio](http://developer.android.com/sdk/index.html)
2. Option 2 without IDE: Instructions coming soon...

## Install appropriate SDK tools
Assuming you set the ANDROID_HOME environmental variable and added ANDROID_HOME/tools to your PATH do the command to open up the SDK Manager: 
```sh
$ android sdk
```
You need to install or update the following packages from the SDK Manager:
1. Tools > Android SDK Tools
2. Tools > Android SDK Platform-tools
3. Tools > Android SDK Build-tools Rev. 21.1.1
4. Android 5.0.1 (API 21) > SDK Platform
5. Android 4.4W.2 (API 20) > SDK Platform
6. Android 4.4W.2 (API 20) > Android Wear ARM EABI v7a System Image
7. Android 4.3.1 (API 18) > SDK Platform
8. Extras > Android Support Repository
9. Extras > Android Support Library
10. Extras > Google Play Services
11. Extras > Google Repository












