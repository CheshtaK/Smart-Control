package com.example.cheshta.smartcontrol.filedownload

import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import com.example.cheshta.smartcontrol.FileAPI
import com.example.cheshta.smartcontrol.MainActivity
import java.io.File
import java.io.FileOutputStream
import java.io.ObjectInputStream

class DownloadFileFromServer: AsyncTask<String, String, Unit> {

    lateinit var context: Context
    lateinit var progressDialog: ProgressDialog

    constructor(context: Context){
        this.context = context
    }

    override fun onPreExecute() {
        progressDialog = ProgressDialog(context)
        progressDialog.setTitle("Downloading File");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setProgress(0);
        progressDialog.show();
    }

    override fun doInBackground(vararg p0: String?) {
        val name = p0[0]
        progressDialog.setMessage(name)
        var fos: FileOutputStream? = null

        var path = FileAPI().getExternalStoragePath()
        path = path + "/SmartControl2/" + name

        val file = File(path)
        val dirs = File(file.parent)
        if(!dirs.exists()){
            dirs.mkdirs()
        }

        try {
            if(MainActivity.clientSocket.clientSocket != null){
                if(MainActivity.objectInputStream.objectInputStream == null) MainActivity.objectInputStream.objectInputStream =
                        ObjectInputStream(MainActivity.clientSocket.clientSocket!!.getInputStream())
            }

            fos = FileOutputStream(file)
            val buffer = ByteArray(4096)
            val fileSize = MainActivity.objectInputStream.objectInputStream!!.readObject()
            var read: Int
            var totalRead: Long = 0
            var remaining = fileSize as Int
            while (MainActivity.objectInputStream.objectInputStream!!.read(buffer, 0, Math.min(buffer.size, remaining)) > 0){
                read = MainActivity.objectInputStream.objectInputStream!!.read(buffer, 0, Math.min(buffer.size, remaining))
                totalRead += read
                remaining -= read
                publishProgress("" + ((totalRead * 100) / fileSize) as Int)
                fos.write(buffer, 0, read)
            }
        }

        catch (e: Exception){
            e.printStackTrace()
        }

        finally {
            try {
                if(fos != null){
                    fos.close()
                }
            }

            catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    override fun onProgressUpdate(vararg values: String?) {
        progressDialog.progress = values[0] as Int
    }

    override fun onPostExecute(result: Unit?) {
        if(progressDialog.isShowing){
            progressDialog.dismiss()
        }
    }
}