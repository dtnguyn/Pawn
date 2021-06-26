package com.nguyen.pawn.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nguyen.pawn.model.User
import com.nguyen.pawn.repo.AuthRepository
import com.nguyen.pawn.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AuthViewModel
@Inject constructor(
    private val repo: AuthRepository
) : ViewModel() {

    private val _uiState = MutableLiveData<UIState>(UIState.Success)

    private val _user = MutableLiveData<User?>(null)

    private val _accessToken = MutableLiveData<String?>(null)

    private val _refreshToken = MutableLiveData<String?>(null)

    val uiState: LiveData<UIState> = _uiState
    val user: LiveData<User?> = _user
    val accessToken: LiveData<String?> = _accessToken
    val refreshToken: LiveData<String?> = _refreshToken


    fun registerAccount(
        email: String,
        username: String,
        password: String,
        passwordVerify: String,
        nativeLanguage: String
    ) {

        viewModelScope.launch {
            if (email.isBlank() || username.isBlank() || password.isBlank() || passwordVerify.isBlank() || nativeLanguage.isBlank()) {
                emitError("Please enter all the required information!")
                return@launch
            }
            if (password != passwordVerify) {
                emitError("Enter password again not match registered password!")
                return@launch
            }
            turnOnLoading()
            val registerResponse = repo.register(email, username, password, nativeLanguage)

            if (registerResponse) {
                login(email, password)
            } else {
                emitError("Register unsuccessful! Please try again!")
            }
        }
    }

    fun login(emailOrUsername: String, password: String) {

        viewModelScope.launch {
            if (emailOrUsername.isBlank() || password.isBlank()) {
                emitError("Please enter all the required information!")
                return@launch
            }
            turnOnLoading()
            val loginResponse = repo.login(emailOrUsername, password)
            if (loginResponse != null) {
                checkAuthStatus(loginResponse.accessToken, loginResponse.refreshToken)
            } else {
                emitError("Login unsuccessful! Please try again!")
            }
        }
    }


    fun checkAuthStatus(accessToken: String?, refreshToken: String?) {
        viewModelScope.launch {
            if (accessToken.isNullOrBlank() || refreshToken.isNullOrBlank()) {
                return@launch
            }
            turnOnLoading()
            val user = repo.checkAuthStatus(accessToken)
            Log.d("Auth", "Here ${user}")
            if (user != null) {

                turnOffLoading()
                println(user)
                withContext(Main) {
                    _user.value = user
                    _accessToken.value = accessToken
                    _refreshToken.value = refreshToken
                }
            } else {
                val newAccessToken = repo.refreshAccessToken(refreshToken)
                val newUser = repo.checkAuthStatus(newAccessToken)
                turnOffLoading()
                withContext(Main) {
                    _user.value = newUser
                    _accessToken.value = newAccessToken
                }
            }
        }
    }

    fun logout(refreshToken: String?) {
        viewModelScope.launch {
            if (refreshToken != null) {
                repo.logout(refreshToken)
            }
            withContext(Main) {
                _user.value = null
                _accessToken.value = null
                _refreshToken.value = null
            }
        }
    }

    fun clearError() {
        _uiState.value = UIState.Success
    }


    private suspend fun turnOnLoading() {
        withContext(Main) {
            if (_uiState.value != UIState.Loading) _uiState.value = UIState.Loading
        }
    }

    private suspend fun turnOffLoading() {
        withContext(Main) {
            _uiState.value = UIState.Success

        }
    }

    private suspend fun emitError(errMsg: String) {
        withContext(Main) {
            _uiState.value = UIState.Error(errMsg)
        }
    }


}