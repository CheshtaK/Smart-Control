package com.example.cheshta.smartcontrol.filedownload


import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import com.example.cheshta.smartcontrol.MainActivity
import com.example.cheshta.smartcontrol.R
import kotlinx.android.synthetic.main.fragment_file_download.*
import java.util.*


class FileDownloadFragment : Fragment(), View.OnClickListener {

    private lateinit var pathStack: Stack<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        pathStack = Stack()
        pathStack.push("/")
        pathTextView.setText(pathStack.peek())
        backButton.isEnabled = false
        backButton.setOnClickListener(this)
        getFiles()
        fileTransferListView.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            val file = adapterView.getItemAtPosition(i) as AvatarFile
            val path = file.getPath()
            if(file.getType().equals("folder")){
                pathStack.push(path)
                val currentPath = pathStack.peek()
                pathTextView.setText(currentPath)
                backButton.isEnabled = true
                getFiles()
            }

            else{
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
                    downloadFile(file.getHeading(), file.getPath())
                }

                else{
                    checkForPermissionAndDownload(file.getHeading(), file.getPath())
                }
            }
        }
        return inflater.inflate(R.layout.fragment_file_download, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity.setTitle(resources.getString(R.string.file_download))
    }

    override fun onClick(p0: View?) {
        val id = p0?.id
        if(id == R.id.backButton){
            pathStack.pop()
            val currentPath = pathStack.peek()
            getFiles()
            pathTextView.setText(currentPath)
            if(!currentPath.equals("/")){}
            else{ backButton.isEnabled = false }
        }
    }

    private fun getFiles(){
        var message = "FILE_DOWNLOAD_LIST_FILES"
        MainActivity.sendMessageToServer.sendMessageToServer(message)
        message = pathStack.peek()
        MainActivity.sendMessageToServer.sendMessageToServer(message)
        GetFilesList(fileTransferListView, activity).execute(pathStack.peek())
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun checkForPermissionAndDownload(name: String, path: String){
        if(activity.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(activity.shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                Toast.makeText(activity, "Write Permission is necessary to download", Toast.LENGTH_LONG).show()
            }

            else activity.requestPermissions(arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 2)
        }

        else{
            downloadFile(name, path)
        }
    }

    fun downloadFile(name: String, path: String){
        if(MainActivity.clientSocket.clientSocket != null){
            MainActivity.sendMessageToServer.sendMessageToServer("FILE_DOWNLOAD_REQUEST")
            MainActivity.sendMessageToServer.sendMessageToServer(path)
            DownloadFileFromServer(activity).execute(name)
        }
        else{
            Toast.makeText(activity, "Not Connected", Toast.LENGTH_LONG).show()
        }
    }
}
