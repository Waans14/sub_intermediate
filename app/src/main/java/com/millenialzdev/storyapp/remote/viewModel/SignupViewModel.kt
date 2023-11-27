package com.millenialzdev.storyapp.remote.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.millenialzdev.storyapp.remote.response.ErrorResponse
import com.millenialzdev.storyapp.repository.StoryRepository
import com.millenialzdev.storyapp.remote.response.RegisterResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SignupViewModel(private val repository: StoryRepository) : ViewModel() {
    fun registerUser(name: String, email: String, password: String, onRegisterComplete: (RegisterResponse) -> Unit) {
        viewModelScope.launch {
            try {
                val response = repository.registerUser(name, email, password)
                val message = response.message
                onRegisterComplete(RegisterResponse(error = false, message = message))
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
                onRegisterComplete(RegisterResponse(error = true, message = errorMessage))
                if (errorMessage != null) {
                    Log.e("SignupViewModel", errorMessage)
                }
            } catch (e: Exception) {
                val errorMessage = "Gagal mendaftarkan pengguna: ${e.message}"
                onRegisterComplete(RegisterResponse(error = true, message = errorMessage))
                Log.e("SignupViewModel", errorMessage)
            }
        }
    }
}