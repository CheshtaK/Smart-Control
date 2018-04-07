package com.example.cheshta.smartcontrol

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by chesh on 4/4/2018.
 */
class Utility {

    fun getDate(date: String, dateFormat: String): String{
        val milliSeconds = date.toLong()

        val formatter = SimpleDateFormat(dateFormat)

        val calender = Calendar.getInstance()
        calender.timeInMillis = milliSeconds
        return formatter.format(calender.time)
    }

    fun getDate(milliSeconds: Long, dateFormat: String): String{
        val formatter = SimpleDateFormat(dateFormat)

        val calender = Calendar.getInstance()
        calender.timeInMillis = milliSeconds
        return formatter.format(calender.time)
    }

    fun decodeImageFile(path: String): Bitmap? {
        try {
            val o = BitmapFactory.Options()
            o.inJustDecodeBounds = true
            BitmapFactory.decodeFile(path,o)

            val REQUIRED_SIZE = 60
            var scale = 1

            while (o.outWidth/scale/2 >= REQUIRED_SIZE && o.outHeight/scale/2 >= REQUIRED_SIZE)
                scale *= 2

            val o2 = BitmapFactory.Options()
            o2.inSampleSize = scale
            return BitmapFactory.decodeFile(path,o2)
        }
        catch (e: Throwable){
            e.printStackTrace()
        }
        return null
    }

    fun getDuration(duration: Int): String{
        var time = ""

        var duration1 = duration
        duration1 /= 1000

        val minutes = duration1/60
        duration1 %= 60

        if(minutes > 0){
            time += "" + minutes + " mins"
        }

        time += "" + duration1 + " secs"
        return time
    }

    fun getAlbumArt(albumId: Int, context: Context): Bitmap? {
        var bm: Bitmap? = null

        try {
            val sArtworkUri = Uri.parse("content://media/external/audio/albumart")
            val uri = ContentUris.withAppendedId(sArtworkUri, albumId.toLong())
            val pfd = context.contentResolver.openFileDescriptor(uri,"r")

            if(pfd != null){
                val fd = pfd.fileDescriptor
                bm = BitmapFactory.decodeFileDescriptor(fd)
            }
        }

        catch (e: Exception){}
        return bm
    }

    fun getSize(size: Int): String{
        var size1 = size
        size1 /= 1024
        return "" + size + "KB"
    }

    fun getSize(size: Long): String{
        var size1 = size
        size1 /= 1024
        return "" + size + "KB"
    }
}