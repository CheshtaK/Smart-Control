package com.example.cheshta.smartcontrol.adapter

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.cheshta.smartcontrol.R
import com.example.cheshta.smartcontrol.Utility
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
            row = NavigationDrawerItemAdapter.scanForActivity.scanForActivity(context)!!.layoutInflater.
                    inflate(layoutResourceId, parent, false)
            holder = MusicImageAvatarHolder.MusicImageAvatarHolder()
            holder.icon = R.id.avatarImageView as ImageView
            holder.avatarHeading = R.id.avatarHeadingTextView as TextView
            holder.avatarSubheading = R.id.avatarSubheadingTextView as TextView
            row.tag = holder
        }

        else{
            holder = row.tag as MusicImageAvatarHolder.MusicImageAvatarHolder
        }

        var item: MusicImageAvatar = objects.get(position)
        var bitmap: Bitmap
        var utility = Utility()

        if(item.type.equals("image")){
            bitmap = utility.decodeImageFile(item.avatarData)!!
            holder.icon.setImageBitmap(bitmap)
        }

        else{
            bitmap = utility.getAlbumArt(item.icon, context)!!
            if(bitmap != null){
                holder.icon.setImageBitmap(bitmap)
            }

            else{
                holder.icon.setImageResource(R.mipmap.ic_launcher)
            }
        }
        holder.avatarHeading.setText(item.avatarHeading)
        holder.avatarSubheading.setText(item.avatarSubheading)
        return row!!
    }

    object MusicImageAvatarHolder{
        class MusicImageAvatarHolder{
            lateinit var icon: ImageView
            lateinit var avatarHeading: TextView
            lateinit var avatarSubheading: TextView
        }
    }
}

