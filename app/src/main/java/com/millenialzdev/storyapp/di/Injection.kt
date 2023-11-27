package com.millenialzdev.storyapp.di

import android.content.Context
import com.millenialzdev.storyapp.repository.StoryRepository
import com.millenialzdev.storyapp.data.pref.UserPreference
import com.millenialzdev.storyapp.data.pref.dataStore
import com.millenialzdev.storyapp.remote.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return StoryRepository.getInstance(apiService, pref)
    }
}