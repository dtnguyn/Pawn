package com.nguyen.pawn.ui.screens.auth

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nguyen.pawn.model.Token
import com.nguyen.pawn.repo.AuthRepository
import com.nguyen.pawn.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/** This viewModel contains states
 *  for Auth screen */

@HiltViewModel
class AuthViewModel
@Inject constructor(
    private val authRepo: AuthRepository
) : ViewModel() {

    /** ---STATES--- */

    /** This state is used for pushing the user to home screen after login */
    private val _tokenUIState: MutableState<UIState<Token>> = mutableStateOf(UIState.Loaded(Token(null, null)))
    val tokenUIState: State<UIState<Token>> = _tokenUIState


    /** ---INTENTS--- */

    /** Check all the required inputs, emit errors if necessary,
     * if not then create a new account, then if get null response then
     * emit errors */
    fun registerAccount(
        email: String,
        username: String,
        password: String,
        passwordVerify: String,
        nativeLanguage: String
    ) {
        viewModelScope.launch {
            if (email.isBlank() || username.isBlank() || password.isBlank() || passwordVerify.isBlank() || nativeLanguage.isBlank()) {
                _tokenUIState.value = UIState.Error("Please enter required all fields!")
                return@launch
            }
            if (password != passwordVerify) {
                _tokenUIState.value = UIState.Error("Your password verify does not match your password!")
                return@launch
            }
            authRepo.register(email, username, password, nativeLanguage).collectLatest {
                _tokenUIState.value = it
            }
        }
    }

    /** Check required inputs, emit error if necessary,
     * if not then login to account, if the token is null
     * then emit error */
    fun login(emailOrUsername: String, password: String) {
        viewModelScope.launch {
            if (emailOrUsername.isBlank() || password.isBlank()) {
                _tokenUIState.value = UIState.Error("Please enter required all fields!")
                return@launch
            }
            authRepo.login(emailOrUsername, password).collectLatest {
                _tokenUIState.value = it
            }
        }
    }

    /** Update the current state of auth token */
    fun initializeToken(accessToken: String?, refreshToken: String?){
        _tokenUIState.value = UIState.Loaded(Token(accessToken, refreshToken))
    }
}