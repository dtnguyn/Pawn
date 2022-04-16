package com.moderndev.polyglot.ui.screens.account

import androidx.lifecycle.ViewModel
import com.moderndev.polyglot.repo.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountViewModel
@Inject constructor(
    private val repo: UserRepository
): ViewModel() {




}