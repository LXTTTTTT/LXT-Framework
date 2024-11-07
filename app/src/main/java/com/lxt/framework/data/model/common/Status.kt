package com.lxt.framework.data.model.common

// Activity data loading status
sealed class Status<T>(
        val data: T? = null,
        val errorCode: Int? = null
) {
    class Success<T>(data: T) : Status<T>(data)
    class Loading<T>(data: T? = null) : Status<T>(data)
    class Error<T>(errorCode: Int) : Status<T>(null, errorCode)

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Loading<T> -> "Loading"
            is Error -> "Error[exception=$errorCode]"
        }
    }
}
