package com.test.commbank.utils

import com.google.gson.JsonParser
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.HttpsURLConnection

sealed class Errors
    (msg: String) : Exception(msg) {
    class OfflineException(msg: String = "Not Connected to Internet") : Errors(msg)
    class FetchException(msg: String) : Errors(msg)
}

object ErrorUtils {

    fun getMsgFromError(error: Throwable): String {
        if (error is HttpException) {
            try {
                val errorJsonString = error.response()?.errorBody()?.string()
                return JsonParser().parse(errorJsonString)
                    .asJsonObject["data"]
                    .asJsonObject["message"]
                    .asString
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return "Please try again later"
    }

    fun getMsgFromError(body: retrofit2.Response<Unit>): String {
        try {
            val errorJsonString = body.errorBody()?.string()
            return JsonParser().parse(errorJsonString)
                .asJsonObject["data"]
                .asJsonObject["message"]
                .asString
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return "Please try again later"
    }

    fun getMsgFromErrorAddUser(error: Throwable): String {
        if (error is HttpException) {
            try {
                val errorJsonString = error.response()?.errorBody()?.string()
                val data = JsonParser().parse(errorJsonString)
                    .asJsonObject["data"]
                    .asJsonArray

                val message =
                        ""+
                        data[0]
                            .asJsonObject["field"]
                            .asString +
                        " "+
                        data[0]
                            .asJsonObject["message"]
                            .asString

                return message
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return "Please try again later"
    }
}