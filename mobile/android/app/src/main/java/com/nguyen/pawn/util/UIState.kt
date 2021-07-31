package com.nguyen.pawn.util

sealed class UIState<T>(val initialValue: T? = null, val errorMsg: String? = null, val loadedValue: T? = null){
    class Initial<T>(initialValue: T?): UIState<T>(initialValue = initialValue)
    class Loading<T>: UIState<T>()
    class Error<T>(errorMsg: String): UIState<T>(errorMsg = errorMsg)
    class Idle<T>(loadedValue: T?): UIState<T>(loadedValue = loadedValue)
}
