package com.example.cheshta.smartcontrol.adapter

import android.content.Context
import android.graphics.Bitmap
import android.provider.ContactsContract
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.cheshta.smartcontrol.R
import com.example.cheshta.smartcontrol.Utility
import com.example.cheshta.smartcontrol.model.MusicImageAvatar
import kotlinx.android.synthetic.main.music_image_avatar.view.*

open class AvatarFileAdapter(context: Context, layoutResourceId: Int, objects: ArrayList<AvatarFile>) :
        ArrayAdapter<AvatarFile>(context, layoutResourceId, objects) {

    var layoutResourceId: Int
    var objects: ArrayList<MusicImageAvatar>

    init {
        this.layoutResourceId = layoutResourceId
        this.objects = objects
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var row = convertView
        var holder: AvatarFileHolder.AvatarFileHolder? = null
        if(row == null){
            row = NavigationDrawerItemAdapter.scanForActivity.scanForActivity(context)!!.layoutInflater.
                    inflate(layoutResourceId, parent, false)
            holder = AvatarFileHolder.AvatarFileHolder()
            holder.icon = R.id.avatarImageView as ImageView
            holder.avatarHeading = R.id.avatarHeadingTextView as TextView
            holder.avatarSubheading = R.id.avatarSubheadingTextView as TextView
            row.tag = holder
        }

        else holder = row.tag as AvatarFileHolder.AvatarFileHolder

        var item: AvatarFile = objects.get(position)
        var bitmap: Bitmap
        var utility = Utility()
        var type: String = item.type

        if(type.equals("image")){
            if(item.icon != -1){
                bitmap = utility.decodeImageFile(item.getPath())!!
                holder.icon.setImageBitmap(bitmap)
            }

            else{
                holder.icon.setImageResource(R.mipmap.ic_launcher)
            }
        }

        else if(type.equals("mp3")){
            holder.icon.setImageResource(R.mipmap.ic_launcher)
        }

        else if(type.equals("pdf")){
            holder.icon.setImageResource(R.mipmap.ic_launcher)
        }

        else if(type.equals("file")){
            holder.icon.setImageResource(R.mipmap.ic_launcher)
        }

        else if(type.equals("folder")){
            holder.icon.setImageResource(R.mipmap.ic_launcher)
        }

        holder.avatarHeading.setText(item.avatarHeading)
        holder.avatarSubheading.setText(item.avatarSubheading)
        return row!!
    }

    object AvatarFileHolder{
        class AvatarFileHolder{
            lateinit var icon: ImageView
            lateinit var avatarHeading: TextView
            lateinit var avatarSubheading: TextView
        }
    }
}