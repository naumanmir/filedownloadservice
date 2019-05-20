package com.downloadservice.filedownloadservice.async_download

import android.os.AsyncTask
import android.util.Log
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

class AsyncDownload(progress: (Int) -> Unit, success: (Boolean) -> Unit) : AsyncTask<String, Int, String>() {
    val progress1 = progress
    var success1 = success

    override fun onPreExecute() {
        super.onPreExecute()
    }

    override fun doInBackground(vararg p0: String?): String {
        initDownload(p0)
        return ""
    }

    override fun onProgressUpdate(vararg values: Int?) {
        super.onProgressUpdate(*values)
        progress1(values[0]!!)
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
    }

    override fun onCancelled() {
        super.onCancelled()
    }

    private fun initDownload(p0: Array<out String?>) {
        var count: Int = 0
        try {
            val url =
                URL(p0[0])
            val connection: HttpURLConnection? = url.openConnection() as HttpURLConnection?
            connection?.requestMethod = "GET"
            connection?.readTimeout = 20000
            connection?.connectTimeout = 20000
            connection?.setRequestProperty("Accept-Encoding", "identity")
            connection?.useCaches = false
            connection?.connect()
            // getting file length

            if (connection != null && connection.responseCode == 200) {
                val lengthOfFile: Int? = connection.contentLength

                // input stream to read file - with 8k buffer
                val input: BufferedInputStream? = BufferedInputStream(url.openStream());

                // Output stream to write file
                val output = FileOutputStream(p0[1] + "/" + p0[2])
                val outputStream = BufferedOutputStream(output)

                val data = ByteArray(1024)

                var total: Long = 0

                while ({ count = input?.read(data)!!;count }() != -1) {
                    total += count.toLong()
                    // publishing the progress....
                    // After this onProgressUpdate will be called
//                print("" + (total * 100 / lengthOfFile!!).toInt())
                    Log.d("Main Activity", "Progress: " + (total * 100 / lengthOfFile!!).toInt())
                    onProgressUpdate((total * 100 / lengthOfFile!!).toInt()).toString()
                    // writing data to file
                    outputStream.write(data, 0, count)
                }

                // flushing output
                outputStream.flush()

                // closing streams
                outputStream.close()
                input?.close()
                connection.disconnect()
                success1(true)
            }

        } catch (e: Exception) {
//            Log.e("Error: ", e.message)
            print("Error: " + e.message)
            success1(false)
        }
    }
}