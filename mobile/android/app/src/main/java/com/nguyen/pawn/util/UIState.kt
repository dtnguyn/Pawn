package com.nguyen.pawn.util

sealed class UIState<T>(val errorMsg: String? = null, val value: T? = null){
    class Initial<T>(initialValue: T?): UIState<T>(value = initialValue)
    class Loading<T>: UIState<T>()
    class Error<T>(errorMsg: String): UIState<T>(errorMsg = errorMsg)
    class Loaded<T>(loadedValue: T?): UIState<T>(value = loadedValue)
}
