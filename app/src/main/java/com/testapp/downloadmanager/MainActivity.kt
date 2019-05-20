package com.testapp.downloadmanager

import android.Manifest
import android.app.Activity
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
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.startActivity
import com.testapp.downloadmanager.async_download.AsyncDownload
import com.testapp.downloadmanager.manager.DownloadManager
import com.testapp.downloadmanager.service.DownloadService
import android.webkit.MimeTypeMap
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var REQUEST_CODE_WRITE_STORAGE_PERMISION = 105

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn?.setOnClickListener {
            checkStoragePermissions(this)
        }
    }

    private fun checkStoragePermissions(activity: Activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    ActivityCompat.requestPermissions(activity,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        REQUEST_CODE_WRITE_STORAGE_PERMISION)
                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(activity,
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        REQUEST_CODE_WRITE_STORAGE_PERMISION)
                }
            }
        } else {
            if (ContextCompat.checkSelfPermission(activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                if (ContextCompat.checkSelfPermission(activity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    } else {
                        // No explanation needed, we can request the permission.
                        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CODE_WRITE_STORAGE_PERMISION)
                    }
                }
            } else {
                val folder = File(Environment.getExternalStorageDirectory().toString() + "/" + "HelloWorld")
                if (!folder.exists()) {
                    folder.mkdirs()
                }
                val fileName = SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(Date()) + ".mp3"
                val urlOfTheFile = "https://sample-videos.com/audio/mp3/wave.mp3"
                DownloadManager.initDownload(this, urlOfTheFile, folder.absolutePath, fileName)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_WRITE_STORAGE_PERMISION -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    val folder = File(Environment.getExternalStorageDirectory().toString() + "/" + "HelloWorld")
                    if (!folder.exists()) {
                        folder.mkdirs()
                    }
                    val fileName = SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(Date()) + ".mp3"
                    val urlOfTheFile = "https://sample-videos.com/audio/mp3/wave.mp3"
                    DownloadManager.initDownload(this, urlOfTheFile, folder.absolutePath, fileName)
                }
            }

        }
    }
}
