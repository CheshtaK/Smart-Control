package com.example.cheshta.smartcontrol

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.cheshta.smartcontrol.model.MusicImageAvatar
import kotlin.collections.ArrayList

open class MusicImageAvatarAdapter(context: Context, layoutResourceId: Int, objects: ArrayList<MusicImageAvatar>) :
        ArrayAdapter<MusicImageAvatar>(context, layoutResourceId, objects) {

    var layoutResourceId: Int
    var objects: ArrayList<MusicImageAvatar>

    init {
        this.layoutResourceId = layoutResourceId
        this.objects = objects
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View{
        var row = convertView
        var holder: MusicImageAvatarHolder.MusicImageAvatarHolder? = null
        if(row == null){

        }
        return row
    }

    object MusicImageAvatarHolder{
        class MusicImageAvatarHolder{
            lateinit var icon: ImageView
            lateinit var avatarHeading: TextView
            lateinit var avatarSubheading: TextView
        }
    }
}

