# File Download Service

[![](https://jitpack.io/v/naumanmir/filedownloadservice.svg)](https://jitpack.io/#naumanmir/filedownloadservice)

Download's files in background.
Written in kotlin.

Demo video is showing two files which are downloaded one after the other.

![ezgif com-video-to-gif](https://user-images.githubusercontent.com/29778659/58005671-9b2a4a80-7aff-11e9-8579-f1ab466b2e5e.gif)


# Prerequisites

Add this in your project root build.gradle file:

	allprojects {
		repositories {
				...
				maven { url "https://jitpack.io" }
				}
			}

# Dependency

Add this to your app's level build.gradle file :

	dependencies {
	...
	implementation 'com.github.naumanmir:filedownloadservice:1.2'
	}

# Usage

Edit your AndroidManifest.xml as below : 

	<uses-permission android:name="android.permission.INTERNET"/>
    	<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
	
	<application
            android:allowBackup="true"
            android:icon="@mipmap/ic_launcher"
            android:label="@string/app_name"
            android:roundIcon="@mipmap/ic_launcher_round"
            android:supportsRtl="true"
            android:theme="@style/AppTheme">
        <activity android:name=".MainActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN"/>

                <category android:name="android.intent.category.LAUNCHER"/>
            </intent-filter>
        </activity>
	
		<service android:name="com.downloadservice.filedownloadservice.service.DownloadService"/>
	
    </application>

    

NOTE: Don't forget to add runtime write storage permissions if you are running it on Marshmallow and above otherwise your app can crash or it can give download error.

First create a folder where you want your file to be downloaded and also create a file name for the file to download.

	val folder = File(Environment.getExternalStorageDirectory().toString() + "/" + "HelloWorld")
        if (!folder.exists()) {
            folder.mkdirs()
        }
        val fileName = SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(Date()) + ".mp3"
	val urlOfTheFile = "https://sample-videos.com/audio/mp3/wave.mp3"

Now just call
	
	FileDownloadManager.initDownload(
                this,
                urlOfTheFile,
                folder.absolutePath,
                fileName
            )

initDownload method takes four paramters i.e. context of your activity or fragment, url of the file, path of the folder where the file is to be downloaded and file name with correct extension.

Now, your file will be downloaded to the specified path.

