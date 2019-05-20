# File Download Service

Download's files in background.
Written in kotlin.


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
	implementation 'com.github.naumanmir:filedownloadservice:1.0'
}

# Usage

First create a folder where you want your file to be downloaded and also create a file name for the file to download.
Also give internet and write storage permission in AndroidManifest.xml;

<uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>

NOTE: Don't forget to add runtime write storage permissions if you are running it on Marshmallow and above.

val folder = File(Environment.getExternalStorageDirectory().toString() + "/" + "HelloWorld")
        if (!folder.exists()) {
            folder.mkdirs()
        }
        val fileName = SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(Date()) + ".mp3"
	val urlOfTheFile = "https://sample-videos.com/audio/mp3/wave.mp3"
	DownloadManager.initDownload(
                this,
                "https://sample-videos.com/audio/mp3/wave.mp3",
                folder.absolutePath,
                fileName
            )

and your file will be downloaded to the specified path.


