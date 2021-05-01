package com.hide09.androidfundamental.listviewevent

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.hide09.androidfundamental.R
import com.hide09.androidfundamental.databinding.ItemHeroBinding
import de.hdodenhof.circleimageview.CircleImageView

class HeroAdapter internal constructor(private val context: Context): BaseAdapter(){
    internal var heroes = arrayListOf<Hero>()

    override fun getCount(): Int = heroes.size

    override fun getItem(position: Int): Any = heroes[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var itemView = convertView
        if (itemView == null){
            itemView = LayoutInflater.from(context).inflate(R.layout.item_hero, parent, false)
        }
        val viewHolder = ViewHolder(itemView as View)
        val hero = getItem(position) as Hero
        viewHolder.bind(hero)
        return itemView
    }

    private inner class ViewHolder(view: View) {
//        private val txtName: TextView = view.findViewById(R.id.txt_name)
//        private val txtDescripion: TextView = view.findViewById(R.id.txt_description)
//        private val imgPhoto: CircleImageView = view.findViewById(R.id.img_photo)
        private val binding =  ItemHeroBinding.bind(view)

        fun bind(hero: Hero){
            binding.txtName.text = hero.name
            binding.txtDescription.text = hero.description
            binding.imgPhoto.setImageResource(hero.photo)
        }
    }
}