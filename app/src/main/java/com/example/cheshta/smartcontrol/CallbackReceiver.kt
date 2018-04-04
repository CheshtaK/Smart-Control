package com.example.cheshta.smartcontrol

/**
 * Created by chesh on 4/4/2018.
 */
interface CallbackReceiver {

    abstract fun receiveData(result: kotlin.Any): Unit
}