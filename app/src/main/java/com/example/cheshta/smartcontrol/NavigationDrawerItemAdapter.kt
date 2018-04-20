package com.example.cheshta.smartcontrol

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.cheshta.smartcontrol.model.NavigationDrawerItem
import android.content.ContextWrapper


class NavigationDrawerItemAdapter(context: Context, layoutResourceId: Int, objects: Array<NavigationDrawerItem>)
    : ArrayAdapter<NavigationDrawerItem>(context, layoutResourceId, objects) {

    var layoutResourceId: Int
    var objects: Array<NavigationDrawerItem>

    init {
        this.layoutResourceId = layoutResourceId
        this.objects = objects
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var row = convertView
        var holder: NavigationDrawerItemHolder.NavigationDrawerItemHolder? = null

        if(row == null){
            row = (scanForActivity(context))?.layoutInflater!!.inflate(layoutResourceId, parent, false)
            holder = NavigationDrawerItemHolder.NavigationDrawerItemHolder()
            holder.imgIcon = R.id.imgIcon as ImageView
            holder.textTitle = R.id.textTitle as TextView
            row.tag = holder
        }

        else{
            holder = row.tag as NavigationDrawerItemHolder.NavigationDrawerItemHolder
        }

        var item: NavigationDrawerItem = objects[position]
        holder.textTitle.setText(item.title)
        holder.imgIcon.setImageResource(item.icon)
        return row!!
    }

    object NavigationDrawerItemHolder{
        class NavigationDrawerItemHolder{
            lateinit var imgIcon: ImageView
            lateinit var textTitle: TextView
        }
    }

    fun scanForActivity(cont: Context?): Activity? {
        if (cont == null)
            return null
        else if (cont is Activity)
            return cont
        else if (cont is ContextWrapper)
            return scanForActivity(cont.baseContext)
        return null
    }
}