package com.example.cheshta.smartcontrol.filetransfer

import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import com.example.cheshta.smartcontrol.CallbackReceiver
import com.example.cheshta.smartcontrol.MainActivity
import java.io.File
import java.io.FileInputStream
import java.io.ObjectOutputStream

open class TransferFileToServer(context: Context): AsyncTask<String, String, Unit>(), CallbackReceiver {

    var context: Context
    lateinit var progressDialog: ProgressDialog

    init {
        this.context = context
    }

    override fun onPreExecute() {
        progressDialog = ProgressDialog(context)
        progressDialog.setTitle("Transfering File");
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setProgress(0);
        progressDialog.show();
    }

    override fun doInBackground(vararg p0: String?) {
        val name = p0[0]
        var path = p0[1]
        progressDialog.setMessage(name)
        var fis: FileInputStream? = null
        try {
            if(MainActivity.clientSocket.clientSocket != null){
                if(MainActivity.objectOutputStream.objectOutputStream == null){
                    MainActivity.objectOutputStream.objectOutputStream =
                            ObjectOutputStream(MainActivity.clientSocket.clientSocket!!.getOutputStream())
                }

                val file = File(path)
                val fileSize: Long = file.length()

                MainActivity.objectOutputStream.objectOutputStream!!.writeObject(fileSize)
                MainActivity.objectOutputStream.objectOutputStream!!.flush()

                fis = FileInputStream(file)
                val buffer = ByteArray(4096)
                var read: Int = 0
                var totalRead: Long = 0
                var remaining = fileSize as Int
                while (totalRead < fileSize && fis.read(buffer, 0, Math.min(buffer.size, remaining)) > 0){
                    read = fis.read(buffer, 0, Math.min(buffer.size, remaining))
                    totalRead += read
                    remaining -= read
                    publishProgress("" + (((totalRead * 100)/ fileSize) as Int))
                    MainActivity.objectOutputStream.objectOutputStream!!.write(buffer, 0, read)
                    MainActivity.objectOutputStream.objectOutputStream!!.flush()
                }
                MainActivity.objectOutputStream.objectOutputStream!!.flush()
            }
        }
        catch (e: Exception){
            e.printStackTrace()
        }

        finally {
            try {
                if(fis != null){
                    fis.close()
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

        receiveData(result!!)
    }

    override fun receiveData(result: Any) {
    }
}