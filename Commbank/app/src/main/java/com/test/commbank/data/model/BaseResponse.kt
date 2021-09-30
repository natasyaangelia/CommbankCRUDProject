package com.test.commbank.data.model

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(

    @SerializedName("success")
    val success: Boolean,

    @SerializedName("data")
    val data: T,

    @SerializedName("message")
    val message: String,

    @SerializedName("code")
    val code: Int
)