package com.millenialzdev.storyapp.remote.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.millenialzdev.storyapp.repository.StoryRepository
import com.millenialzdev.storyapp.data.pref.UserModel
import com.millenialzdev.storyapp.remote.response.ErrorResponse
import com.millenialzdev.storyapp.remote.response.LoginResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(private val repository: StoryRepository) : ViewModel() {

    private fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun loginUser(email: String, password: String, onLoginComplete: (LoginResponse) -> Unit) {
        viewModelScope.launch {
            try {
                val response = repository.login(email, password)
                val message = response.message
                onLoginComplete(LoginResponse(error = false, message = message))

                val token = response.loginResult?.token
                if (token != null) {
                    saveSession(UserModel(email, token, true))
                }

            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message
                onLoginComplete(LoginResponse(error = true, message = errorMessage))
                if (errorMessage != null) {
                    Log.e("LoginViewModel", errorMessage)
                }
            } catch (e: Exception) {
                val errorMessage = "Gagal masuk: ${e.message}"
                onLoginComplete(LoginResponse(error = true, message = errorMessage))
                Log.e("LoginViewModel", errorMessage)
            }
        }
    }

}