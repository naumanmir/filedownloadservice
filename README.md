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
	implementation 'com.github.naumanmir:filedownloadservice:1.1'
	}

# Usage

First create a folder where you want your file to be downloaded and also create a file name for the file to download.
Also give internet and write storage permission in AndroidManifest.xml;

	<uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>

NOTE: Don't forget to add runtime write storage permissions if you are running it on Marshmallow and above otherwise your app can crash.

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

and your file will be downloaded to the specified path.

