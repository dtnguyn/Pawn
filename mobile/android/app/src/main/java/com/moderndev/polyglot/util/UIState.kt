package com.moderndev.polyglot.util

sealed class UIState<T>(val errorMsg: String? = null, val value: T? = null){
    class Initial<T>(initialValue: T?): UIState<T>(value = initialValue)
    class Loading<T>: UIState<T>()
    class Error<T>(errorMsg: String, value: T? = null): UIState<T>(errorMsg = errorMsg, value)
    class Loaded<T>(loadedValue: T?): UIState<T>(value = loadedValue)
}
