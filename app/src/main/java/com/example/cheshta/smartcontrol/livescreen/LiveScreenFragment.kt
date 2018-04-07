package com.example.cheshta.smartcontrol.livescreen


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.Toast
import com.example.cheshta.smartcontrol.MainActivity
import com.example.cheshta.smartcontrol.R
import kotlinx.android.synthetic.main.fragment_live_screen.*
import java.util.*
import kotlin.concurrent.timerTask


/**
 * A simple [Fragment] subclass.
 */
class LiveScreenFragment : Fragment() {

    private var screenshotImageViewX: Int = 0
    private var screenshotImageViewY: Int = 0

    private var xCord: Int = 0
    private var yCord: Int = 0
    private var initX: Int = 0
    private var initY: Int = 0

    var mouseMoved = false
    var multiTouch = false

    private var currentPressTime: Long = 0
    private var lastPressTime: Long = 0

    private lateinit var timer: Timer

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val vto = screenshotImageView.viewTreeObserver

        vto.addOnGlobalLayoutListener { object : ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {
                screenshotImageViewX = screenshotImageView.height
                screenshotImageViewY = screenshotImageView.width

                val obs = screenshotImageView.viewTreeObserver
                obs.removeGlobalOnLayoutListener {context}
            }
        }}

        screenshotImageView.setOnTouchListener( View.OnTouchListener { view, motionEvent ->
            if(MainActivity.clientSocket.clientSocket != null){
                when(motionEvent.action){                 //ACTION_MASK

                    MotionEvent.ACTION_DOWN -> {
                        xCord = screenshotImageViewX - (motionEvent.y as Int)
                        yCord = motionEvent.x as Int
                        initX = xCord
                        initY = yCord
                        MainActivity.sendMessageToServer.sendMessageToServer("MOUSE_MOVE_LIVE")
                        MainActivity.sendMessageToServer.sendMessageToServer((xCord / screenshotImageViewX) as Float)
                        MainActivity.sendMessageToServer.sendMessageToServer((yCord / screenshotImageViewY) as Float)
                        mouseMoved = false
                    }

                    MotionEvent.ACTION_MOVE -> {
                        if(multiTouch == false){
                            xCord = screenshotImageViewX - (motionEvent.y as Int)
                            yCord = motionEvent.x as Int
                            if((xCord - initX) != 0 && (yCord - initY) != 0){
                                initX = xCord
                                initY = yCord
                                MainActivity.sendMessageToServer.sendMessageToServer("MOUSE_MOVE_LIVE")
                                MainActivity.sendMessageToServer.sendMessageToServer((xCord / screenshotImageViewX) as Float)
                                MainActivity.sendMessageToServer.sendMessageToServer((yCord / screenshotImageViewY) as Float)
                                mouseMoved = true
                            }
                        }
                    }

                    MotionEvent.ACTION_UP -> {
                        currentPressTime = System.currentTimeMillis()
                        val interval = currentPressTime - lastPressTime

                        if(interval >= 500 && !mouseMoved){
                            MainActivity.sendMessageToServer.sendMessageToServer("LEFT_CLICK")
                            delayedUpdateScreenshot()
                        }
                        lastPressTime = currentPressTime
                    }
                }
            }
            return@OnTouchListener true
        });

        timer = Timer()
        updateScreenshot()

        return inflater!!.inflate(R.layout.fragment_live_screen, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity.setTitle(resources.getString(R.string.live_screen))
    }

    private fun updateScreenshot(){

        if(MainActivity.clientSocket.clientSocket != null){
            MainActivity.sendMessageToServer.sendMessageToServer("SCREENSHOT_REQUEST")

            object : UpdateScreenshot(){
                override fun receiveData(path: Any) {
                    var bitmap = BitmapFactory.decodeFile(path as String?)
                    var matrix = Matrix()
                    matrix.postRotate((-90).toFloat())
                    try {
                        var rotated = Bitmap.createBitmap(bitmap,0,0,bitmap.width,bitmap.height,matrix,true)
                        screenshotImageView.setImageBitmap(rotated)
                    }
                    catch (e:Exception){
                        Toast.makeText(activity,"Error",Toast.LENGTH_SHORT).show()
                    }
                }
            }.execute()
        }
    }

    fun delayedUpdateScreenshot(){
        timer.schedule(timerTask { updateScreenshot() },500)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timer.cancel()
        timer.purge()
    }
}// Required empty public constructor
