package com.nguyen.polyglot.ui.screens.account

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.nguyen.polyglot.repo.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountViewModel
@Inject constructor(
    private val repo: UserRepository
): ViewModel() {

}