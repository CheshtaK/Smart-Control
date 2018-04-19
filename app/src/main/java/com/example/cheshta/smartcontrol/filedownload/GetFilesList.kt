package com.example.cheshta.smartcontrol.filedownload

import android.content.Context
import android.widget.ListView
import android.widget.Toast
import com.example.cheshta.smartcontrol.R

class GetFilesList: GetFilesListFromServer {

    lateinit var fileDownloadListView: ListView
    lateinit var context: Context

    constructor(fileDownloadListView: ListView, context: Context){
        this.fileDownloadListView = fileDownloadListView
        this.context = context
    }

    override fun receiveData(result: Any) {
        val filesInFolder: ArrayList<AvatarFile> = result as ArrayList<AvatarFile>
        if(filesInFolder != null){
            fileDownloadListView.adapter = AvatarFileAdapter(context, R.layout.music_image_avatar, filesInFolder)
        }

        else{
            Toast.makeText(context, "Not Connected To PC", Toast.LENGTH_LONG).show()
        }
    }
}