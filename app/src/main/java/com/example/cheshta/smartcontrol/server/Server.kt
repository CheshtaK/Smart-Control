package com.example.cheshta.smartcontrol.server

import android.app.Activity
import android.widget.Toast
import com.example.cheshta.smartcontrol.FilesList
import com.example.cheshta.smartcontrol.MainActivity
import com.example.cheshta.smartcontrol.SendFilesList
import java.io.InputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.OutputStream
import java.net.ServerSocket
import java.net.Socket
import com.example.cheshta.smartcontrol.Server.outputStream
import com.example.cheshta.smartcontrol.Server.outputStream
import com.example.cheshta.smartcontrol.Server.inputStream
import com.example.cheshta.smartcontrol.Server.inputStream
import com.example.cheshta.smartcontrol.Server.serverSocket
import com.example.cheshta.smartcontrol.Server.serverSocket


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
            val sendFilesList = SendFilesList()

            while (true){
                val message = objectInputStream.objectInputStream.readObject() as String
                if(message == null){
                    closeServer.closeServer()
                    break
                }

                when(message){
                    "FILE_DOWNLOAD_LIST_FILES" -> {
                        filepath = objectInputStream.objectInputStream.readObject() as String

                        object : FilesList(){
                            override fun receiveData(result: Any) {
                                val filesInFolder = result as Array<AvatarFile>
                                sendFilesList.sendFilesList(filesInFolder, objectOutputStream.objectOutputStream)
                            }
                        }.execute(filepath)
                    }

                    "FILE_DOWNLOAD" -> {
                        filepath = objectInputStream.objectInputStream.readObject() as String
                        TransferFileToPC().tranferFile(filepath, objectOutputStream.objectOutputStream)
                    }

                    else -> {}
                }
            }
        }

        catch (e: Exception){
            e.printStackTrace()
        }
    }

    object closeServer{
        fun closeServer(){
            try {
                serverSocket?.serverSocket.close()
                clientSocket.clientSocket.close()
                inputStream?.inputStream.close()
                outputStream?.outputStream.close()
                objectOutputStream.objectOutputStream.close()
                objectInputStream.objectInputStream.close()
            } catch (e: Exception) {
                println(e)
            }
        }
    }

    object sendMessageToServer{
        fun sendMessageToServer(message: Long){
            if (clientSocket.clientSocket != null){
                try {
                    objectOutputStream.objectOutputStream.writeObject(message)
                    objectOutputStream.objectOutputStream.flush()
                }

                catch (e: Exception){
                    e.printStackTrace()
                    MainActivity.socketException.socketException()
                }
            }
        }
    }

    private fun socketException(){
        Toast.makeText(activity.activity, "Connect", Toast.LENGTH_LONG).show()
        if(clientSocket.clientSocket != null){
            try {
                clientSocket.clientSocket.close()
                objectOutputStream.objectOutputStream.close()
                clientSocket.clientSocket = null
            }

            catch (e2: Exception){
                e2.printStackTrace()
            }
        }
    }
}