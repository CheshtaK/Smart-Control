package com.example.cheshta.smartcontrol.livescreen

import android.os.AsyncTask
import com.example.cheshta.smartcontrol.CallbackReceiver
import com.example.cheshta.smartcontrol.FileAPI
import com.example.cheshta.smartcontrol.MainActivity
import java.io.File
import java.io.FileOutputStream
import java.io.ObjectInputStream

/**
 * Created by chesh on 4/7/2018.
 */
abstract class UpdateScreenshot: AsyncTask<Unit, Unit, String>(), CallbackReceiver {

    override fun doInBackground(vararg p0: Unit?): String {
        var fos: FileOutputStream? = null
        var path = FileAPI().getExternalStoragePath()
        path = path + "/RemoteControlPC/screenshot.png"

        var file = File(path)
        var dirs = File(file.parent)
        if(!dirs.exists()){
            dirs.mkdirs()
        }

        try {
            if(MainActivity.clientSocket.clientSocket != null){
                if(MainActivity.objectInputStream.objectInputStream == null){
                    MainActivity.objectInputStream.objectInputStream = ObjectInputStream(
                            MainActivity.clientSocket.clientSocket!!.getInputStream())
                }

                fos = FileOutputStream(file)
                var buffer = ByteArray(4096)
                var fileSize: Int = MainActivity.objectInputStream.objectInputStream!!.readObject() as Int
                var read: Int = 0
                var remaining = fileSize
                while ((MainActivity.objectInputStream.objectInputStream!!.read(buffer,0,Math.min(buffer.size,remaining))) > 0){
                    read = MainActivity.objectInputStream.objectInputStream!!.read(buffer,0,Math.min(buffer.size,remaining))
                    remaining -= read
                    fos.write(buffer,0,read)
                }
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
        return path
    }

    override fun receiveData(path: Any) {
        receiveData(path)
    }

    override fun onPostExecute(path: String?) {
        receiveData(path!!)
    }
}