package com.example.cheshta.smartcontrol

import android.app.Activity
import android.widget.Toast
import java.io.InputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.OutputStream
import java.net.ServerSocket
import java.net.Socket

/**
 * Created by chesh on 4/4/2018.
 */
class Server {

    object serverSocket{ lateinit var serverSocket: ServerSocket}
    object clientSocket{ lateinit var clientSocket: Socket }
    object inputStream{ lateinit var inputStream: InputStream }
    object outputStream{ lateinit var outputStream: OutputStream }
    object objectInputStream{ lateinit var objectInputStream: ObjectInputStream }
    object objectOutputStream{ lateinit var objectOutputStream: ObjectOutputStream }
    object activity{ lateinit var activity: Activity}

    constructor(activity: Activity){
        Server.activity.activity = activity
    }

    fun startServer(port: Int){
        try {
            serverSocket.serverSocket = ServerSocket(port)
        }
        catch (e: Exception){
            activity.run { Runnable { Toast.makeText(activity,"Unable to start server",Toast.LENGTH_LONG).show() } }
            e.printStackTrace()
            return
        }

        try{
            clientSocket.clientSocket = serverSocket.serverSocket.accept()
            inputStream.inputStream = clientSocket.clientSocket.getInputStream()
            outputStream.outputStream = clientSocket.clientSocket.getOutputStream()
            objectInputStream.objectInputStream = ObjectInputStream(inputStream.inputStream)
            objectOutputStream.objectOutputStream = ObjectOutputStream(outputStream.outputStream)

            var filepath: String
            val sendFilesList: SendFilesList = SendFilesList()

            while (true){
                val message: String = objectInputStream.objectInputStream.readObject() as String
                if(message == null){
                    closeServer()
                    break
                }

                when(message){
                    "FILE_DOWNLOAD_LIST_FILES" -> {
                        filepath = objectInputStream.objectInputStream.readObject() as String
                        FilesList()
                    }

                }
            }
        }
    }
}