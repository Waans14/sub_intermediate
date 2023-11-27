package com.millenialzdev.storyapp.remote.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.millenialzdev.storyapp.repository.StoryRepository
import com.millenialzdev.storyapp.data.pref.UserModel
import kotlinx.coroutines.launch

class AddStoryViewModel(private val repository: StoryRepository) : ViewModel() {

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

}