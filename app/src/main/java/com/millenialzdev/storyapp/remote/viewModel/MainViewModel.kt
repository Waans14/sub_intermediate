package com.millenialzdev.storyapp.remote.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.millenialzdev.storyapp.repository.StoryRepository
import com.millenialzdev.storyapp.data.pref.UserModel
import com.millenialzdev.storyapp.remote.response.ListStoryItem
import kotlinx.coroutines.launch


class MainViewModel(private val repository: StoryRepository) : ViewModel() {

    fun getStory(token: String?): LiveData<PagingData<ListStoryItem>> {
        if (token.isNullOrEmpty()) {
            Log.e("MainViewModel", "Token is null or empty")
            return MutableLiveData()
        }

        return repository.getStory(token).cachedIn(viewModelScope)
    }

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

}