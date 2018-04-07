package com.example.cheshta.smartcontrol.connect

import android.content.Context
import android.os.AsyncTask
import com.example.cheshta.smartcontrol.CallbackReceiver
import com.example.cheshta.smartcontrol.MainActivity
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketAddress
import javax.security.auth.callback.Callback

/**
 * Created by chesh on 4/4/2018.
 */
abstract class MakeConnection: AsyncTask<Unit, Unit, Socket>, CallbackReceiver {

    var ipAddress: String
    var port: String
    var context: Context
    var clientSocket: Socket? = null

    constructor(ipAddress: String, port: String, context: Context){
        this.ipAddress = ipAddress
        this.port = port;
        this.context = context;
    }

    override fun doInBackground(vararg p0: Unit?): Socket? {
        try {
            val portNumber = port.toInt()
            val socketAddress = InetSocketAddress(ipAddress,portNumber)
            clientSocket = Socket()
            clientSocket!!.connect(socketAddress,3000)
            MainActivity.objectInputStream.objectInputStream = ObjectInputStream(clientSocket!!.getInputStream())
            MainActivity.objectOutputStream.objectOutputStream = ObjectOutputStream(clientSocket!!.getOutputStream())
        } catch (e: Exception){
            e.printStackTrace()
            clientSocket = null
        }
        return clientSocket
    }

    override fun onPostExecute(result: Socket?) {
        receiveData(this!!.clientSocket!!)
    }

    override fun receiveData(result: kotlin.Any) {

    }
}