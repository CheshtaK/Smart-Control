package com.example.cheshta.smartcontrol.keyboard


import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.example.cheshta.smartcontrol.MainActivity

import com.example.cheshta.smartcontrol.R
import kotlinx.android.synthetic.main.fragment_keyboard.*


/**
 * A simple [Fragment] subclass.
 */
class KeyboardFragment : Fragment(), View.OnTouchListener, View.OnClickListener, TextWatcher {

    var previousText = ""

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_keyboard, container, false)
        /*initialization(rootView)*/
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity.setTitle(resources.getString(R.string.keyboard))
    }

    /*fun initialization(rootView: View){
        ctrlButton.setOnTouchListener(this)
    }*/ //set all ontouch/onclick listeners

    override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
        var action = "KEY_PRESS"
        if(p1!!.action == MotionEvent.ACTION_DOWN){
            action = "KEY_PRESS"
        }
        else if(p1.action == MotionEvent.ACTION_UP){
            action = "KEY_RELEASE"
        }

        var keyCode = 17
        when(p0!!.id){
            R.id.ctrlButton -> keyCode = 17
            R.id.altButton -> keyCode = 18
            R.id.shiftButton -> keyCode = 16
        }

        sendKeyCodeToServer(action,keyCode)
        return false
    }

    override fun onClick(p0: View?) {
        val id = p0!!.id
        if(id == R.id.clearTextButton){
            typeHereEditText.setText("")
        }
        else if(id == R.id.ctrlAltTButton || id == R.id.ctrlShiftZButton || id == R.id.altF4Button){
            var message = "CTRL_SHIFT_Z"
            when(id){
                R.id.ctrlAltTButton -> message = "CTRL_ALT_T"
                R.id.ctrlShiftZButton -> message = "CTRL_SHIFT_Z"
                R.id.altF4Button -> message = "ALT_F4"
            }

            MainActivity.sendMessageToServer.sendMessageToServer(message)
        }
        else{
            var keyCode = 17
            var action = "TYPE_KEY"
            when(id){
                R.id.enterButton -> keyCode = 10
                R.id.tabButton -> keyCode = 9
                R.id.escButton -> keyCode = 27
                R.id.printScrButton -> keyCode = 154
                R.id.deleteButton -> keyCode = 127
                R.id.nButton -> keyCode = 78
                R.id.tButton -> keyCode = 84
                R.id.wButton -> keyCode = 87
                R.id.rButton -> keyCode = 82
                R.id.fButton -> keyCode = 70
                R.id.zButton -> keyCode = 90
                R.id.cButton -> keyCode = 67
                R.id.xButton -> keyCode = 88
                R.id.vButton -> keyCode = 86
                R.id.aButton -> keyCode = 65
                R.id.oButton -> keyCode = 79
                R.id.sButton -> keyCode = 83
                R.id.backspaceButton -> keyCode = 8
            }
            sendKeyCodeToServer(action,keyCode)
        }
    }

    fun sendKeyCodeToServer(action: String,keyCode: Int){
        MainActivity.sendMessageToServer.sendMessageToServer(action)
        MainActivity.sendMessageToServer.sendMessageToServer(keyCode)
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        var ch = newCharacter(p0!!, previousText)
        if(ch == '0'){
            return
        }
        MainActivity.sendMessageToServer.sendMessageToServer("TYPE_CHARACTER")
        MainActivity.sendMessageToServer.sendMessageToServer(Character.toString(ch))
        previousText = p0.toString()
    }

    override fun afterTextChanged(p0: Editable?) {}

    fun newCharacter(currentText: CharSequence, previousText: CharSequence): Char{
        var ch: Char = 0.toChar()
        var currentTextLength = currentText.length
        var previousTextLength = previousText.length
        var difference = currentTextLength - previousTextLength
        if(currentTextLength > previousTextLength){
            if(1 == difference){
                ch = currentText.get(previousTextLength)
            }
            else if(currentTextLength < previousTextLength){
                if(-1 == difference){
                    ch = '\b'
                }
                else{
                    ch = ' '
                }
            }
        }
        return ch
    }
}// Required empty public constructor

/**
 * ctrl: 17
 * alt: 18
 * shift: 16
 * enter: 10
 * tab: 9
 * esc: 27
 * prntScr: 154
 * backspace: 524
 * delete: 127
 * backspace: 8
 */
/**
 * N: 78
 * T: 84
 * W: 87
 * R: 82
 * F: 70
 * Z: 90
 * C: 67
 * X: 88
 * V: 86
 * A: 65
 * O: 79
 * S: 83
 */