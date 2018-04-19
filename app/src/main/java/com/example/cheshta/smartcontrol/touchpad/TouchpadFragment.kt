package com.example.cheshta.smartcontrol.touchpad


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.example.cheshta.smartcontrol.MainActivity
import com.example.cheshta.smartcontrol.R
import kotlinx.android.synthetic.main.fragment_touchpad.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class TouchpadFragment : Fragment() {

    var initX: Int = 0
    var initY: Int = 0
    var disX: Int = 0
    var disY: Int = 0

    var mouseMoved = false
    var multiTouch = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        leftClickButton.setOnClickListener { simulateLeftClick() }
        rightClickButton.setOnClickListener { simulateRightClick() }
        touchPadTextView.setOnTouchListener { view, motionEvent ->
            if(MainActivity.clientSocket.clientSocket != null){
                when (motionEvent.action){
                    MotionEvent.ACTION_DOWN -> {
                        initX = motionEvent.x as Int
                        initY = motionEvent.y as Int
                        mouseMoved = false
                    }

                    MotionEvent.ACTION_MOVE -> {
                        if(multiTouch == false){
                            disX = motionEvent.x as Int - initX
                            disY = motionEvent.y as Int - initY

                            initX = motionEvent.x as Int
                            initY = motionEvent.y as Int

                            if(disX != 0 || disY != 0){
                                MainActivity.sendMessageToServer.sendMessageToServer("MOUSE_MOVE")
                                MainActivity.sendMessageToServer.sendMessageToServer(disX)
                                MainActivity.sendMessageToServer.sendMessageToServer(disY)
                                mouseMoved = true
                            }
                        }

                        else{
                            disY = motionEvent.y as Int - initY
                            disY = disY / 2
                            initY = motionEvent.y as Int
                            if(disY != 0){
                                MainActivity.sendMessageToServer.sendMessageToServer("MOUSE_WHEEL")
                                MainActivity.sendMessageToServer.sendMessageToServer(disY)
                                mouseMoved = true
                            }
                        }
                    }

                    MotionEvent.ACTION_CANCEL -> {
                        if(!mouseMoved){
                            MainActivity.sendMessageToServer.sendMessageToServer("LEFT_CLICK")
                        }
                    }

                    MotionEvent.ACTION_UP -> {
                        if(!mouseMoved){
                            MainActivity.sendMessageToServer.sendMessageToServer("LEFT_CLICK")
                        }
                    }

                    MotionEvent.ACTION_POINTER_DOWN -> {
                        initY = motionEvent.y as Int
                        mouseMoved = false
                        multiTouch = true
                    }

                    MotionEvent.ACTION_POINTER_UP -> {
                        if(!mouseMoved){
                            MainActivity.sendMessageToServer.sendMessageToServer("LEFT_CLICK")
                        }
                        multiTouch = false
                    }
                }
            }
            return@setOnTouchListener true
        }
        return inflater.inflate(R.layout.fragment_touchpad, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity.setTitle(resources.getString(R.string.touchpad))
    }

    private fun simulateLeftClick(){
        val message = "LEFT_CLICK"
        MainActivity.sendMessageToServer.sendMessageToServer(message)
    }

    private fun simulateRightClick(){
        val message = "RIGHT_CLICK"
        MainActivity.sendMessageToServer.sendMessageToServer(message)
    }
}
