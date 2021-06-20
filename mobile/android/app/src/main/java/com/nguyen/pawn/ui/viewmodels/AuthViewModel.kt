package com.nguyen.pawn.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nguyen.pawn.repo.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel
    @Inject constructor(
        private val repo: AuthRepository
    ) : ViewModel() {

    private val _errorMsg = MutableLiveData<String?>(null)


    fun registerAccount(
        email: String,
        username: String,
        password: String,
        passwordVerify: String,
        nativeLanguage: String
    ) {
//        if (email.isBlank() || username.isBlank() || password.isBlank() || passwordVerify.isBlank() || nativeLanguage.isBlank()) {
//            _errorMsg.value = "Please enter all the required information!"
//            return
//        }
//        if (password != passwordVerify) {
//            _errorMsg.value = "Enter password again not match registered password!"
//            return
//        }

        viewModelScope.launch(IO) {
            repo.register(email, username, password, nativeLanguage)
        }

    }


}