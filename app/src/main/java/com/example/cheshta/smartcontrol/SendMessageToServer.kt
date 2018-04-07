package com.example.cheshta.smartcontrol

import android.os.AsyncTask

/**
 * Created by chesh on 4/3/2018.
 */
class SendMessageToServer: AsyncTask<String, Unit, Unit>() {

    override fun doInBackground(vararg p0: String?) {
        var message = p0[0]
        val code = p0[1]

        var intMessage: Int
        var floatMessage: Float
        var longMessage: Long

        if(code.equals("STRING")){
            try {
                MainActivity.objectOutputStream.objectOutputStream?.writeObject(message)
                MainActivity.objectOutputStream.objectOutputStream?.flush()
            } catch (e: Exception){
                e.printStackTrace()
                    MainActivity.socketException.socketException()
            }
        }

        else if(code.equals("INT")){
            try {
                intMessage = message!!.toInt()
                MainActivity.objectOutputStream.objectOutputStream?.writeObject(intMessage)
                MainActivity.objectOutputStream.objectOutputStream?.flush()
            } catch (e: Exception){
                e.printStackTrace()
                MainActivity.socketException.socketException()
            }
        }

        else if(code.equals("FLOAT")){
            try {
                floatMessage = message!!.toFloat()
                MainActivity.objectOutputStream.objectOutputStream?.writeObject(floatMessage)
                MainActivity.objectOutputStream.objectOutputStream?.flush()
            } catch (e: Exception){
                e.printStackTrace()
                MainActivity.socketException.socketException()
            }
        }

        else if(code.equals("LONG")){
            try {
                longMessage = message!!.toLong()
                MainActivity.objectOutputStream.objectOutputStream?.writeObject(longMessage)
                MainActivity.objectOutputStream.objectOutputStream?.flush()
            } catch (e: Exception){
                e.printStackTrace()
                MainActivity.socketException.socketException()
            }
        }
    }
}