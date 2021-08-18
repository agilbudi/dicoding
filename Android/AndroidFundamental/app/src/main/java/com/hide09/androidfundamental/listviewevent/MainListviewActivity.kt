package com.hide09.androidfundamental.listviewevent

import android.content.res.TypedArray
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.Toast
import com.hide09.androidfundamental.R
import com.hide09.androidfundamental.databinding.ActivityMainListviewBinding

class MainListviewActivity : AppCompatActivity() {
//    private val dataName = arrayOf("Cut Nyak Dien", "Ki Hajar Dewantara", "Moh Yamin", "Pattimura", "R A Kartini", "Sukarno")
    private lateinit var dataName: Array<String>
    private lateinit var dataDescription: Array<String>
    private lateinit var dataPhoto: TypedArray
    private var heroes = arrayListOf<Hero>()

    private lateinit var binding: ActivityMainListviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainListviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataName)
        val adapter = HeroAdapter(this)
        binding.lvList.adapter = adapter

        prepare()
        addItem(adapter)

        binding.lvList.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            Toast.makeText(this, heroes[position].name, Toast.LENGTH_SHORT).show()
        }
    }

    private fun addItem(adapter: HeroAdapter) {
        for (position in dataName.indices){
            val hero = Hero(
                    dataPhoto.getResourceId(position, -1),
                    dataName[position],
                    dataDescription[position]
            )
            heroes.add(hero)
        }
        adapter.heroes = heroes
    }

    private fun prepare() {
        dataName = resources.getStringArray(R.array.data_name)
        dataDescription = resources.getStringArray(R.array.data_description)
        dataPhoto = resources.obtainTypedArray(R.array.data_photo)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}