package com.example.cheshta.smartcontrol.presentation


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cheshta.smartcontrol.MainActivity
import com.example.cheshta.smartcontrol.R
import kotlinx.android.synthetic.main.fragment_presentation.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class PresentationFragment : Fragment(), View.OnClickListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        downArrowButton.setOnClickListener(this)
        leftArrowButton.setOnClickListener(this)
        upArrowButton.setOnClickListener(this)
        rightArrowButton.setOnClickListener(this)
        f5Button.setOnClickListener(this)
        return inflater.inflate(R.layout.fragment_presentation, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity.setTitle(resources.getString(R.string.presentation))
    }

    override fun onClick(p0: View?) {
        val id = p0?.id
        var action = "F5_KEY"
        when(id){
            R.id.downArrowButton -> { action = "DOWN_ARROW_KEY"}
            R.id.leftArrowButton -> { action = "LEFT_ARROW_KEY"}
            R.id.upArrowButton -> { action = "UP_ARROW_KEY"}
            R.id.rightArrowButton -> { action = "RIGHT_ARROW_KEY"}
            R.id.f5Button -> { action = "F5_KEY"}
        }
        sendActionToServer(action)
    }

    fun sendActionToServer(action: String){
        MainActivity.sendMessageToServer.sendMessageToServer(action)
    }
}
