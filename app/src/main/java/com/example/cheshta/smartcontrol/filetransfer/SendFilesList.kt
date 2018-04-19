package com.example.cheshta.smartcontrol.filetransfer

import java.io.ObjectOutputStream

/**
 * Created by chesh on 4/4/2018.
 */
class SendFilesList {

    fun sendFilesList(myFiles: Array<AvatarFile>, out: ObjectOutputStream){
        Thread(){
            fun run(){
                try {
                    out.writeObject(myFiles)
                    out.flush()
                }
                catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }.start()
    }
}