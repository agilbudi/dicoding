package com.hide09.githubapp.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.hide09.githubapp.model.UserDetail

class UserDetailAdapter: BaseAdapter() {
    val listDetail= ArrayList<UserDetail>()

    fun updateUsers(newUsers : ArrayList<UserDetail>){
        listDetail.clear()
        listDetail.addAll(newUsers)
        notifyDataSetChanged()
    }
    override fun getCount(): Int = listDetail.size

    override fun getItem(position: Int) {
    }

    override fun getItemId(position: Int): Long {
        TODO("Not yet implemented")
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        TODO("Not yet implemented")
    }

}
