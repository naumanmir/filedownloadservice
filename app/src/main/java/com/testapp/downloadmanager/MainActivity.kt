package com.testapp.downloadmanager

import android.content.Context
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import java.net.URL
import java.net.URLConnection
import java.nio.file.Files.exists
import java.io.File.separator
import android.os.Environment.getExternalStorageDirectory
import android.util.Log
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.text.SimpleDateFormat
import java.util.*
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.support.v4.content.ContextCompat.startActivity
import com.testapp.downloadmanager.async_download.AsyncDownload
import com.testapp.downloadmanager.manager.DownloadManager
import com.testapp.downloadmanager.service.DownloadService
import android.webkit.MimeTypeMap


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val folder = File(Environment.getExternalStorageDirectory().toString() + "/" + "HelloWorld")
        if (!folder.exists()) {
            folder.mkdirs()
        }
        val fileName = SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(Date()) + ".mp3"

        for (i in 1..2) {
            DownloadManager.initDownload(
                this,
                "https://sample-videos.com/audio/mp3/wave.mp3",
                folder.absolutePath,
                fileName
            )
        }

    }
}
