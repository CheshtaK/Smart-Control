package com.example.cheshta.smartcontrol.server

import com.example.cheshta.smartcontrol.server.Server
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.ObjectOutputStream

/**
 * Created by chesh on 4/15/2018.
 */
class TransferFileToPC {

    fun tranferFile(path: String, objectOutputStream: ObjectOutputStream){
        Thread{ Runnable {
            if (objectOutputStream == null){
                return@Runnable
            }

            var fis: FileInputStream? = null
            try {
                val file = File(path)
                fis = FileInputStream(file)
                val fileSize = file.length()
                Server.sendMessageToServer.sendMessageToServer(fileSize)
                val buffer = ByteArray(4096)
                var read: Int
                var totalRead: Float = 0F
                var remaining = fileSize as Int
                while (totalRead < fileSize && (fis.read(buffer, 0, Math.min(buffer.size, remaining))) > 0){
                    read = fis.read(buffer, 0, Math.min(buffer.size, remaining))
                    totalRead += read
                    remaining -= read
                    objectOutputStream.write(buffer, 0, read)
                    objectOutputStream.flush()
                }
                objectOutputStream.flush()
            }

            catch (eof: FileNotFoundException){
                Server.sendMessageToServer.sendMessageToServer(0)
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
        }}.start()
    }
}