package com.hide09.githubapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hide09.githubapp.adapter.UserDetailAdapter
import com.hide09.githubapp.databinding.ActivityDetailUserBinding
import com.hide09.githubapp.model.UserDetail
import com.hide09.githubapp.viewmodel.UserDetailViewModel
import kotlinx.android.synthetic.main.activity_detail_user.*

class DetailUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding
    lateinit var userDetailVM: UserDetailViewModel

    companion object{
        const val EXTRA_USER = "extra_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val username = intent.getStringExtra(EXTRA_USER)
        setData(username)
    }

    private fun setData(username: String?) {
        userDetailVM = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(UserDetailViewModel::class.java)
        userDetailVM.setDataUserDetail(username)
        userDetailVM.getUserDetail().observe(this, {items ->
            with(binding){
                Glide.with(this@DetailUserActivity)
                        .load(items.detailFoto)
                        .apply {
                            RequestOptions().centerCrop()
                            RequestOptions().override(130, 130)
                        }
                        .into(civDetailUser)
                tvDetailUsername.text = items.detailUserName
                tvDetailNama.text = items.detailNama
                tvDetailRepo.text = items.detailRepo
                tvDetailFollowing.text = items.detailFollowing
                tvDetailFollowers.text = items.detailFollowers
            }
        })
    }

}