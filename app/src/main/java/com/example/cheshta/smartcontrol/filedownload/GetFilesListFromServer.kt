package com.example.cheshta.smartcontrol.filedownload

import android.os.AsyncTask
import com.example.cheshta.smartcontrol.CallbackReceiver
import com.example.cheshta.smartcontrol.MainActivity
import java.io.ObjectInputStream

open class GetFilesListFromServer : AsyncTask<String, Unit, ArrayList<AvatarFile>>(), CallbackReceiver {

    override fun receiveData(result: Any){}

    override fun doInBackground(vararg p0: String?): ArrayList<AvatarFile> {
        val path = p0[0]
        var myFiles: ArrayList<AvatarFile> = null
        try {
            if(MainActivity.clientSocket.clientSocket != null){
                if(MainActivity.objectInputStream.objectInputStream == null){
                    MainActivity.objectInputStream.objectInputStream = ObjectInputStream(
                            MainActivity.clientSocket.clientSocket!!.getInputStream()
                    )

                    myFiles = MainActivity.objectInputStream.objectInputStream!!.readObject() as ArrayList<AvatarFile>
                }
            }
        }

        catch (e: Exception){
            e.printStackTrace()
        }

        return myFiles
    }

    override fun onPostExecute(result: ArrayList<AvatarFile>?) {
        receiveData(result!!)
    }
}