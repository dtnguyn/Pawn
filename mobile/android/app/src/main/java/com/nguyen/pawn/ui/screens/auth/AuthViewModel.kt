package com.nguyen.pawn.ui.screens.auth

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nguyen.pawn.model.Token
import com.nguyen.pawn.repo.AuthRepository
import com.nguyen.pawn.util.LoadingType
import com.nguyen.pawn.util.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/** This viewModel contains states
 *  for Auth screen */

@HiltViewModel
class AuthViewModel
@Inject constructor(
    private val authRepo: AuthRepository
) : ViewModel() {

    /** ---STATES--- */

    /** This state is used for displaying loading animation or error dialog */
    private val _uiState = mutableStateOf<UIState>(UIState.Idle())
    val uiState: State<UIState> = _uiState

    /** This state is used for pushing the user to home screen after login */
    private val _token = mutableStateOf(Token(null, null))
    val token: State<Token> = _token


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
                emitError("Please enter all the required information!")
                return@launch
            }
            if (password != passwordVerify) {
                emitError("Enter password again not match registered password!")
                return@launch
            }
            turnOnLoading(LoadingType.AUTH_LOADING)
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

    /** Check required inputs, emit error if necessary,
     * if not then login to account, if the token is null
     * then emit error */
    fun login(emailOrUsername: String, password: String) {
        viewModelScope.launch {
            if (emailOrUsername.isBlank() || password.isBlank()) {
                emitError("Please enter all the required information!")
                return@launch
            }
            turnOnLoading(LoadingType.AUTH_LOADING)
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

    /** Update the current state of auth token */
    fun initializeToken(accessToken: String?, refreshToken: String?){
        _token.value = Token(accessToken, refreshToken)
    }


    /** Set ui state to loading */
    suspend fun turnOnLoading(type: LoadingType) {
        withContext(Main) {
            if (_uiState.value !is UIState.Loading) _uiState.value = UIState.Loading(type)
        }
    }

    /** Set ui state to Idle  */
    suspend fun goToIdle(from: UIState) {
        withContext(Main) {
            _uiState.value = UIState.Idle(from)
        }
    }

    /** Set ui state to error  */
    suspend fun emitError(errMsg: String) {
        withContext(Main) {
            _uiState.value = UIState.Error(errMsg)
        }
    }


}