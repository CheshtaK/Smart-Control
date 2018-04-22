package com.example.cheshta.smartcontrol.filetransfer

import android.content.Context
import android.widget.ListView
import com.example.cheshta.smartcontrol.adapter.AvatarFileAdapter

class GetFilesList(fileTransferListView: ListView, context: Context): FilesList() {

    var fileTransferListView: ListView
    var context: Context

    init {
        this.fileTransferListView = fileTransferListView
        this.context = context
    }
    override fun receiveData(result: Any) {
        val filesInFolder: ArrayList<AvatarFile> = result as ArrayList<AvatarFile>
        fileTransferListView.adapter = AvatarFileAdapter(context, R.layout.music_image_avatar, filesInFolder)
    }
}