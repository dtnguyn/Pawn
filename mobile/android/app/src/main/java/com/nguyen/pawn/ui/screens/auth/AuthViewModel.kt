package com.nguyen.pawn.ui.screens.auth

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nguyen.pawn.model.Token
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
    private val authRepo: AuthRepository
) : ViewModel() {

    private val _uiState = MutableLiveData<UIState>(UIState.Idle)
    val uiState: LiveData<UIState> = _uiState

//    private val _accessToken = MutableLiveData<String?>(null)
//    val accessToken: LiveData<String?> = _accessToken
//
//    private val _refreshToken = MutableLiveData<String?>(null)
//    val refreshToken: LiveData<String?> = _refreshToken

    private val _token = mutableStateOf<Token>(Token(null, null))
    val token: State<Token> = _token


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
            val response = authRepo.register(email, username, password, nativeLanguage)

            if (response != null) {
                withContext(Main) {
                    _token.value = response
                }
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
            val response = authRepo.login(emailOrUsername, password)
            if (response != null) {
                withContext(Main) {
                    _token.value = response
                }
            } else {
                emitError("Login unsuccessful! Please try again!")
            }
        }
    }

    fun initializeToken(accessToken: String?, refreshToken: String?){
        _token.value = Token(accessToken, refreshToken)
    }


//    fun checkAuthStatus(accessToken: String?, refreshToken: String?) {
//        viewModelScope.launch {
//            if (accessToken.isNullOrBlank() || refreshToken.isNullOrBlank()) {
//                return@launch
//            }
//            turnOnLoading()
//            val user = authRepo.checkAuthStatus(accessToken)
//            if (user != null) {
//                turnOffLoading()
//                withContext(Main) {
//                    _token.value = Token(accessToken, refreshToken)
//                }
//            } else {
//                val newAccessToken = authRepo.refreshAccessToken(refreshToken)
//                turnOffLoading()
//                withContext(Main) {
//                    _token.value = Token(newAccessToken, refreshToken)
//                }
//            }
//        }
//    }
//
//    fun logout(refreshToken: String?) {
//        viewModelScope.launch {
//            if (refreshToken != null) {
//                authRepo.logout(refreshToken)
//            }
//            withContext(Main) {
//                _user.value = null
//                _accessToken.value = null
//                _refreshToken.value = null
//            }
//        }
//    }

    fun clearError() {
        _uiState.value = UIState.Idle
    }


    private suspend fun turnOnLoading() {
        withContext(Main) {
            if (_uiState.value != UIState.Loading) _uiState.value = UIState.Loading
        }
    }

    private suspend fun turnOffLoading() {
        withContext(Main) {
            _uiState.value = UIState.Idle
        }
    }

    private suspend fun emitError(errMsg: String) {
        withContext(Main) {
            _uiState.value = UIState.Error(errMsg)
        }
    }


}