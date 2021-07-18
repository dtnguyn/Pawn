package com.nguyen.pawn.util

sealed class UIState{
    object Idle: UIState()
    object Loading: UIState()
    class Error(val msg: String): UIState()
}
