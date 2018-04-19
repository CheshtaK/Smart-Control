package com.example.cheshta.smartcontrol


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_file_download.*
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
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
        fileTransferListView.setOnClickListener(this)
        return inflater.inflate(R.layout.fragment_file_download, container, false)
    }


    override fun onClick(p0: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
