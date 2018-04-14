package com.example.cheshta.smartcontrol.connect

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.cheshta.smartcontrol.MainActivity
import com.example.cheshta.smartcontrol.R
import com.example.cheshta.smartcontrol.server.Server
import kotlinx.android.synthetic.main.fragment_connect.*
import java.net.Socket

class ConnectFragment : Fragment() {

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        sharedPreferences = activity.getSharedPreferences("lastConnectionDetails", Context.MODE_PRIVATE)
        val lastConnectionDetails = getLastConnectionDetails()
        etIpAddress.setText(lastConnectionDetails[0])
        etPortNumber.setText(lastConnectionDetails[1])

        if(MainActivity.clientSocket.clientSocket != null){
            btnConnect.setText("Connected")
            btnConnect.isEnabled = false
        }

        btnConnect.setOnClickListener(View.OnClickListener {
            makeConnection()
        })

        return inflater!!.inflate(R.layout.fragment_connect, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity.setTitle(resources.getString(R.string.connect))
    }

    fun makeConnection(){
        val ipAddress = etIpAddress.text.toString()
        val port = etPortNumber.text.toString()

        if(ValidateIP.validateIP(ipAddress) && ValidateIP.validatePort(port)){
            setLastConnectionDetails(arrayOf(ipAddress,port))
            btnConnect.setText("Connecting....")
            btnConnect.isEnabled = false

            object : MakeConnection(ipAddress, port, activity){
                override fun receiveData(result: kotlin.Any){
                    MainActivity.clientSocket.clientSocket = result as Socket

                    if(MainActivity.clientSocket.clientSocket == null){
                        Toast.makeText(activity,"Server is not listening", Toast.LENGTH_SHORT).show()
                        btnConnect.setText("Connect")
                        btnConnect.isEnabled = true
                    }

                    else{
                        btnConnect.setText("Connected")
                        Thread(Runnable { Server(activity).startServer(port.toInt())}).start()
                    }
                }
            }.execute()
        }

        else{
            Toast.makeText(activity, "Invalid IP Address or port", Toast.LENGTH_SHORT).show()
        }
    }

    fun getLastConnectionDetails(): Array<String>{
        var arr = arrayOf<String>()
        arr[0] = sharedPreferences.getString("lastConnectedIP", "")
        arr[1] = sharedPreferences.getString("lastConnectedPort","3000")
        return arr
    }

    fun setLastConnectionDetails(arr: Array<String>){
        val editor = sharedPreferences.edit()
        editor.putString("lastConnectedIP",arr[0])
        editor.putString("lastConnectedPort",arr[1])
        editor.apply()
    }
}
