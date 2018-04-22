package com.example.cheshta.smartcontrol.filetransfer


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import com.example.cheshta.smartcontrol.FileAPI
import com.example.cheshta.smartcontrol.MainActivity
import kotlinx.android.synthetic.main.fragment_file_transfer.*
import java.io.File

/**
 * A simple [Fragment] subclass.
 *
 */
class FileTransferFragment : Fragment(), View.OnClickListener {

    lateinit var currentDirectory: File
    lateinit var currentPath: String
    lateinit var rootPath: String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        currentPath = FileAPI().getExternalStoragePath()
        rootPath = currentPath
        currentDirectory = File(currentPath)
        backButton.isEnabled = false
        backButton.setOnClickListener(this)

        GetFilesList(fileTransferListView, activity).execute(currentPath)
        fileTransferListView.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            val file = adapterView.getItemAtPosition(i) as AvatarFile
            val path = file.getPath()
            val tempDirectoryOrFile = File(path)

            if(tempDirectoryOrFile.isDirectory()){
                val tempArray = tempDirectoryOrFile.listFiles()
                if(tempArray != null && tempArray.size > 0){
                    backButton.isEnabled = true
                    currentPath = path
                    currentDirectory = tempDirectoryOrFile
                    pathTextView.setText(currentPath)
                    GetFilesList(fileTransferListView, activity).execute(currentPath)
                }
            }

            else{
                transferFile(file.heading, file.path)
            }
        }
        pathTextView.setText(currentPath)
        return inflater.inflate(R.layout.fragment_file_transfer, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity.setTitle(resources.getString(R.string.file_transfer))
    }

    override fun onClick(p0: View?) {
        val id = p0!!.id
        if(id == R.id.backButton){
            currentPath = currentDirectory.parent
            currentDirectory = File(currentPath)
            GetFilesList(fileTransferListView, activity).execute(currentPath)
            pathTextView.setText(currentPath)
            if(!currentPath.equals(rootPath)){}
            else{
                backButton.isEnabled = false
            }
        }
    }

    private fun transferFile(name: String, path: String){
        if(MainActivity.clientSocket.clientSocket != null){
            MainActivity.sendMessageToServer.sendMessageToServer("FILE_TRANSFER_REQUEST")
            MainActivity.sendMessageToServer.sendMessageToServer(name)
            object : TransferFileToServer(activity){
                override fun receiveData(result: Any) {
                }
            }.execute(arrayOf(name, path).toString())
        }
        else{
            Toast.makeText(activity, "Not Connected", Toast.LENGTH_LONG).show()
        }
    }
}
