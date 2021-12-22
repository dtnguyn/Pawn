package com.nguyen.polyglot.repo

import com.nguyen.polyglot.db.PolyglotDatabase
import io.ktor.client.*
import javax.inject.Inject

class UserRepository
@Inject constructor(
    private val apiClient: HttpClient,
    private val database: PolyglotDatabase
) {

}