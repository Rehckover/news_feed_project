package com.example.vjetgrouptestapp.base.arch

import android.util.Log
import com.example.vjetgrouptestapp.base.remote.interceptors.NoNetworkException
import com.example.vjetgrouptestapp.base.remote.models.ErrorEntity
import retrofit2.HttpException
import java.io.IOException
import java.net.HttpURLConnection

const val GENERAL_ERROR_TAG = "General error"
class GeneralErrorHandle {

    fun getError(throwable: Throwable): ErrorEntity {
        Log.e(GENERAL_ERROR_TAG,"Error - ", throwable)
        return when(throwable) {
            is NoNetworkException -> ErrorEntity.NoNetwork
            is IOException -> ErrorEntity.Network
            is NullPointerException -> ErrorEntity.NullPointer
            is HttpException -> {
                when(throwable.code()) {
                    // Unauthorized
                    HttpURLConnection.HTTP_UNAUTHORIZED -> ErrorEntity.Unauthorized

                    // not found
                    HttpURLConnection.HTTP_NOT_FOUND -> ErrorEntity.NotFound

                    // access denied
                    HttpURLConnection.HTTP_FORBIDDEN -> ErrorEntity.AccessDenied
                    

                    HttpURLConnection.HTTP_BAD_REQUEST -> ErrorEntity.BadRequest(throwable.message())

                    // all the others will be treated as unknown error
                    else -> ErrorEntity.Unknown
                }
            }
            else -> ErrorEntity.Unknown
        }
    }
}