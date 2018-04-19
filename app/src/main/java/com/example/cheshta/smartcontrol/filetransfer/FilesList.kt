package com.example.cheshta.smartcontrol.filetransfer

import android.os.AsyncTask
import com.example.cheshta.smartcontrol.CallbackReceiver
import com.example.cheshta.smartcontrol.R
import com.example.cheshta.smartcontrol.Utility
import java.io.File
import

/**
 * Created by chesh on 4/4/2018.
 */
abstract class FilesList: AsyncTask<String, Unit, Array<AvatarFile>>(), CallbackReceiver {

    override fun doInBackground(vararg p0: String?): Array<AvatarFile> {
        val path = p0[0].toString()
        val myFiles: Array<AvatarFile> = Array<AvatarFile>()
        val utility = Utility()
        val file = File(path)

        val files = file.listFiles()
        if(files != null && files.size > 0){
            for (i in 0..files.size){
                val avatarHeading = files[i].name
                val lastModified = files[i].lastModified()
                val lastModifiedDate = utility.getDate(lastModified, "dd MM yyyy hh:mm a")

                val icon = R.mipmap.ic_launcher //put folder image
                var itemsOrSize: String
                var filePath: String
                var type: String

                if(files[i].isDirectory){
                    type = "folder"
                    val tempArray = files[i].listFiles()

                    if(tempArray != null){
                        itemsOrSize = "" + files[i].listFiles().size + "items"
                    }

                    else{
                        itemsOrSize = "" + 0 + "item"
                    }
                }

                else{
                    itemsOrSize = utility.getSize(files[i].length())
                    type = "file"

                    if(avatarHeading.length > 3){
                        val extension = avatarHeading.substring(avatarHeading.length - 3).toLowerCase()

                        if(extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png") || extension.equals("svg")){
                            type = "image"
                        }
                        else if (extension.equals("mp3")){
                            type = "mp3"
                        }
                        else if(extension.equals("pdf")){
                            type = "pdf"
                        }
                    }
                }

                filePath = files[i].absolutePath
                val subHeading = itemsOrSize + "" + lastModifiedDate
                val avatarFile = AvatarFile(icon, avatarHeading, subHeading, filePath, type)
                myFiles.add(avatarFile)
            }
        }

        return myFiles
    }

    override fun onPostExecute(myFiles: Array<AvatarFile>?) {
        receiveData(myFiles!!)
    }

    override fun receiveData(result: Any) {

    }
}