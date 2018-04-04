package com.example.cheshta.smartcontrol

import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.cheshta.smartcontrol.connect.ConnectFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.Socket

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    object clientSocket {var clientSocket: Socket? = null}
    object objectInputStream {var objectInputStream: ObjectInputStream? = null}
    object objectOutputStream {var objectOutputStream: ObjectOutputStream? = null}
    lateinit var thisActivity: AppCompatActivity
    var doubleBackToExitPressedOnce: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        replaceFragment(R.id.nav_connect);

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkForPermission();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun checkForPermission(){
        if(thisActivity.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){

            if(thisActivity.shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)){
                Toast.makeText(thisActivity,"Read Permission is necessary to transfer",Toast.LENGTH_LONG).show()
            }

            else{
                thisActivity.requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1);
            }
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            if(doubleBackToExitPressedOnce){
                super.onBackPressed()
                return
            }

            doubleBackToExitPressedOnce = true
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

            Handler().postDelayed(Runnable {
                doubleBackToExitPressedOnce = false;
            }, 2000)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        replaceFragment(item.itemId);

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun replaceFragment(id: Int): Unit {
        var fragment: android.support.v4.app.Fragment? = null

        if(id == R.id.nav_connect){
            fragment = ConnectFragment()
        } /*else if (id == R.id.nav_touchpad) {
            fragment = TouchpadFragment()
        } else if (id == R.id.nav_keyboard) {
            fragment = KeyboardFragment()
        } else if (id == R.id.nav_file_transfer) {
            fragment = FileTransferFragment()
        } else if (id == R.id.nav_file_download) {
            fragment = FileDownloadFragment()
        } else if (id == R.id.nav_image_viewer) {
            fragment = ImageViewerFragment()
        } else if (id == R.id.nav_media_player) {
            fragment = MediaPlayerFragment()
        } else if (id == R.id.nav_presentation) {
            fragment = PresentationFragment()
        } else if (id == R.id.nav_power_off) {
            fragment = PowerOffFragment()
        } else if (id == R.id.action_help) {
            fragment = HelpFragment()
        } else if (id == R.id.action_live_screen) {
            fragment = LiveScreenFragment()
        }*/

        if(fragment != null){
            val fragmentTransaction: android.support.v4.app.FragmentTransaction? = supportFragmentManager.beginTransaction()
            fragmentTransaction?.replace(R.id.flContent, fragment)
            fragmentTransaction?.commit()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        try {
            if(clientSocket != null){
                clientSocket.clientSocket!!.close()
            }
            if(objectInputStream != null){
                objectInputStream.objectInputStream?.close()
            }
            if(objectOutputStream != null){
                objectOutputStream.objectOutputStream?.close()
            }
        } catch (e: Exception){
            e.printStackTrace()
        }

        //Server.closeServer();
    }

    fun sendMessageToServer(message: String){
        if(clientSocket != null){
            SendMessageToServer().execute(message.toString(), "STRING");
        }
    }

    fun sendMessageToServer(message: Int){
        if(clientSocket != null){
            SendMessageToServer().execute(message.toString(), "INT");
        }
    }

    fun sendMessageToServer(message: Float){
        if(clientSocket != null){
            SendMessageToServer().execute(message.toString(), "FLOAT");
        }
    }

    fun sendMessageToServer(message: Long){
        if(clientSocket != null){
            SendMessageToServer().execute(message.toString(), "LONG");
        }
    }

    object socketException {
        fun socketException(){
            if(clientSocket != null){
                try {
                    clientSocket.clientSocket!!.close()
                    objectOutputStream.objectOutputStream?.close()
                    clientSocket.clientSocket = null
                } catch (e: Exception){
                    e.printStackTrace();
                }
            }
        }

    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            2 -> {
                if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Click again to download", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(this, "Failed to download", Toast.LENGTH_SHORT).show();
                }
                return
            }

            1 -> {
                if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "Click again to download", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(this, "Failed to download", Toast.LENGTH_SHORT).show();
                }
                return
            }
        }
    }
}
