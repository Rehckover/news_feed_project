package com.example.vjetgrouptestapp.base.remote.models

sealed class ErrorEntity (open val message: String? = null) {

    object Unauthorized : ErrorEntity()

    object Network : ErrorEntity()

    class DbException(override val message: String? = null) : ErrorEntity()

    object NoNetwork : ErrorEntity()

    object NotFound : ErrorEntity()

    object AccessDenied : ErrorEntity()

    class BadRequest(override val message: String? = null) : ErrorEntity()

    object NullPointer : ErrorEntity()

    object Unknown : ErrorEntity()
}