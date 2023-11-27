package com.millenialzdev.storyapp.view.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.millenialzdev.storyapp.databinding.ActivityDetailBinding
import com.millenialzdev.storyapp.remote.response.ListStoryItem

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupData()
    }

    private fun setupData() {
        val storyItem = intent.getParcelableExtra<ListStoryItem>("Story") as ListStoryItem
        Glide.with(applicationContext)
            .load(storyItem.photoUrl)
            .into(binding.ivImage)
        binding.tvJudul.text = storyItem.name
        binding.tvDesc.text = storyItem.description
    }
}