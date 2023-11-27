package com.millenialzdev.storyapp.remote.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.millenialzdev.storyapp.data.pref.UserModel
import com.millenialzdev.storyapp.remote.response.StoryResponse
import com.millenialzdev.storyapp.repository.StoryRepository

class MapsViewModel(private val repository: StoryRepository) : ViewModel() {

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }


}