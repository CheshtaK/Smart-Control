package com.example.cheshta.smartcontrol.poweroff


import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cheshta.smartcontrol.MainActivity
import com.example.cheshta.smartcontrol.R


/**
 * A simple [Fragment] subclass.
 */
class PowerOffFragment : Fragment(), View.OnClickListener {

    lateinit var dialogClickListener: DialogInterface.OnClickListener
    lateinit var builder: AlertDialog.Builder
    lateinit var action: String

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        dialogClickListener = DialogInterface.OnClickListener { dialogInterface, i ->
            when(i){
                DialogInterface.BUTTON_POSITIVE -> {
                    sendActionToServer(action.toUpperCase())
                    dialogInterface.dismiss()
                }
                DialogInterface.BUTTON_NEGATIVE -> {
                    dialogInterface.dismiss()
                }
            }
        }

        builder = AlertDialog.Builder(activity)
        return inflater!!.inflate(R.layout.fragment_power_off, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity.setTitle(resources.getString(R.string.power_off))
    }

    override fun onClick(p0: View?) {
        val id = p0!!.id

        when(id){
            R.id.shutdownButton -> action = "Shutdown_PC"
            R.id.restartButton -> action = "Restart_PC"
            R.id.sleepButton -> action = "Sleep_PC"
            R.id.lockButton -> action = "Lock_PC"
        }
        showConfirmDialog()
    }

    fun showConfirmDialog(){
        builder.setTitle(action)
                .setMessage("Are you sure?")
                .setPositiveButton("Yes",dialogClickListener)
                .setNegativeButton("No",dialogClickListener)
                .show()
    }

    fun sendActionToServer(action: String){
        MainActivity.sendMessageToServer.sendMessageToServer(action)
    }

}// Required empty public constructor
