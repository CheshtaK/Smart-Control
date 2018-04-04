package com.example.cheshta.smartcontrol.connect

import java.util.regex.Pattern

/**
 * Created by chesh on 4/4/2018.
 */
object ValidateIP {
    private val PATTERN = Pattern.compile(
            "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$")//to validate ip address

    fun validateIP(ip: String): Boolean {
        return PATTERN.matcher(ip).matches()
    }

    fun validatePort(portNumber: String?): Boolean {
        if (portNumber != null && portNumber.length == 4 && portNumber.matches(".*\\d.*".toRegex())) {
            return portNumber.toInt() > 1023
        } else
            return false
    }
}
