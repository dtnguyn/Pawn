package com.nguyen.pawn.util

sealed class UIState{
    class Idle(val loadingOrError: UIState? = null): UIState()
    class Loading(val type: LoadingType): UIState()
    class Error(val msg: String): UIState()
}
