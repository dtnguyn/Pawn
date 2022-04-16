package com.moderndev.polyglot.repo

import com.moderndev.polyglot.db.PolyglotDatabase
import io.ktor.client.*
import javax.inject.Inject

class UserRepository
@Inject constructor(
    private val apiClient: HttpClient,
    private val database: PolyglotDatabase
) {

}