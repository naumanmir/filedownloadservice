package com.testapp.downloadmanager.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import com.testapp.downloadmanager.R
import com.testapp.downloadmanager.async_download.AsyncDownload
import com.testapp.downloadmanager.manager.DownloadManager
import kotlin.collections.ArrayList

class DownloadService : Service() {
    private var notificationBuilder: NotificationCompat.Builder? = null
    private var notificationManager: NotificationManager? = null
    private val NOTIFICATION_CHANNEL = "downloads"
    private var NOTIFICATION_ID = -1

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onLowMemory() {
        super.onLowMemory()
        stopSelf()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        initDownload()
        createPendingNotification()
        return START_STICKY
    }

    private fun initDownload() {
        val list = DownloadManager.listOfFilesToBeDownloaded
        if (list != null && list.size > 0) {
            if (DownloadManager.indexToDownload < list.size) {
                val fileToDownload = list.get(DownloadManager.indexToDownload)
                if (fileToDownload != null && !fileToDownload.isDownloaded) {
                    fileToDownload.isDownloaded = true
                    NOTIFICATION_ID = DownloadManager.indexToDownload
                    notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL)
                        .setSmallIcon(R.drawable.download)
                        .setContentText("please wait...")
                        .setContentTitle("Downloading")
                        .setAutoCancel(false)
                        .setOngoing(true)
                        .setChannelId(NOTIFICATION_CHANNEL)

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val channel = NotificationChannel(
                            NOTIFICATION_CHANNEL,
                            NOTIFICATION_CHANNEL,
                            NotificationManager.IMPORTANCE_LOW
                        )
                        channel.description = "no sound"
                        channel.setSound(null, null)
                        channel.enableLights(false);
                        channel.setLightColor(Color.BLUE);
                        channel.enableVibration(false);
                        notificationManager?.createNotificationChannel(channel)
                    }
                    notificationManager?.notify(NOTIFICATION_ID, notificationBuilder?.build())
                    AsyncDownload(progress = { i ->
                        notificationBuilder?.setOngoing(true)
                        notificationBuilder?.setProgress(100, i, false)
                        notificationBuilder?.setContentTitle(fileToDownload.fileName)
                        notificationBuilder?.setContentText(i.toString() + "%")
                        notificationManager?.notify(NOTIFICATION_ID, notificationBuilder?.build())
                    }, success = { b ->
                        notificationManager?.cancel(999)
                        if (b) {
                            notificationBuilder?.setOngoing(false)
                            notificationBuilder?.setContentTitle("Download Complete")
                            notificationManager?.notify(NOTIFICATION_ID, notificationBuilder?.build())
                            notificationManager = null
                            notificationBuilder = null
                            val newIndexToDownload = DownloadManager.indexToDownload + 1
                            if (newIndexToDownload < DownloadManager.listOfFilesToBeDownloaded?.size!!) {
                                DownloadManager.indexToDownload = newIndexToDownload
                                initDownload()
                            } else {
                                DownloadManager.indexToDownload = 0
                                DownloadManager.listOfFilesToBeDownloaded = ArrayList()
                                stopSelf()
                            }

                        } else {
                            notificationManager?.cancel(999)
                            notificationBuilder?.setOngoing(false)
                            notificationBuilder?.setContentText("")
                            notificationBuilder?.setContentTitle("Download Error")
                            notificationManager?.notify(NOTIFICATION_ID, notificationBuilder?.build())
                            notificationManager = null
                            notificationBuilder = null
                            DownloadManager.indexToDownload = 0
                            DownloadManager.listOfFilesToBeDownloaded = ArrayList()
                            stopSelf()
                        }
                    }).execute(fileToDownload.url, fileToDownload.filePath, fileToDownload.fileName)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        notificationBuilder?.setOngoing(false)
        notificationBuilder?.setContentText("")
        notificationBuilder?.setContentTitle("Download Error")
        notificationManager?.notify(NOTIFICATION_ID, notificationBuilder?.build())
        notificationManager = null
        notificationBuilder = null
    }

    // when any error occurs stop the service by calling stop self
    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        notificationManager?.cancel(999)
        notificationBuilder?.setOngoing(false)
        notificationBuilder?.setContentText("")
        notificationBuilder?.setContentTitle("Download Error")
        notificationManager?.notify(NOTIFICATION_ID, notificationBuilder?.build())
        notificationManager = null
        notificationBuilder = null
        DownloadManager.indexToDownload = 0
        DownloadManager.listOfFilesToBeDownloaded = ArrayList()
        stopSelf()
    }


    fun createPendingNotification() {
        if (DownloadManager.isNotificationShowing) {
            notificationBuilder?.setOngoing(false)
            notificationBuilder?.setContentText("Added in queue for download")
            notificationBuilder?.setContentTitle("Pending")
            notificationBuilder?.setProgress(0, 0, false)
            notificationManager?.notify(999, notificationBuilder?.build())
        }
    }
}