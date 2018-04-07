package com.example.cheshta.smartcontrol

/**
 * Created by chesh on 4/7/2018.
 */
class FileAPI {

    fun getExternalStoragePath(): String {
        val internal = System.getenv("EXTERNAL_STORAGE")
        val sdCard = System.getenv("SECONDARY_STORAGE")
        return if (sdCard == null || sdCard == "")
        { internal }
        else sdCard
    }
}