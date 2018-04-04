package com.example.cheshta.smartcontrol

import android.os.AsyncTask
import

/**
 * Created by chesh on 4/4/2018.
 */
abstract class FilesList: AsyncTask<String, Unit, Array<AvatarFile>>(), CallbackReceiver{

    override fun doInBackground(vararg p0: String?): Array<AvatarFile> {
        val path: String = p0[0].toString()
        val myFiles: Array<AvatarFile>

    }
}