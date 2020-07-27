package com.example.vjetgrouptestapp.base.remote.models

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Result<out R> {

    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val error: ErrorEntity) : Result<Nothing>()

    object Loading : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$error]"
            Loading -> "Loading"
        }
    }
}

/**
 * `true` if [Result] is of type [Success]
 */
val Result<*>.succeeded
    get() = this is Result.Success
