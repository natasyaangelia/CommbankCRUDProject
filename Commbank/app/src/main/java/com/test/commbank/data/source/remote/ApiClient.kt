package com.test.commbank.data.source.remote

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.test.commbank.data.source.endpoint.ApiServiceUserBearer
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

fun provideCacheInterceptor() = run {
    Interceptor { chain ->
        val response = chain.proceed(chain.request())
        val maxAge =
            60 // read from cache for 60 seconds even if there is internet connection
        response.newBuilder()
            .header("Cache-Control", "public, max-age=$maxAge")
            .removeHeader("Pragma")
            .build()
    }
}

fun provideHttpLoggingInterceptor() = run {
    HttpLoggingInterceptor().apply {
        apply { level = HttpLoggingInterceptor.Level.BODY }
    }
}

fun provideOkHttpClientJwt(clientToken: String) = run {
    val okHttpClientBuilder = OkHttpClient.Builder()
        .addInterceptor(provideHttpLoggingInterceptor())
        .addInterceptor(provideCacheInterceptor())
        .addInterceptor { chain ->
            val language = if (Locale.getDefault().language == "in") "id" else "en"
            val request = chain.request()
            val requestBuilder: Request
            requestBuilder = request.newBuilder()
                .addHeader(
                    "Authorization", "Bearer ${clientToken}"
                )
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept-Language", language)
                .build()
            val response = chain.proceed(requestBuilder)
            response
        }
        .connectTimeout(1, TimeUnit.MINUTES)
        .readTimeout(1, TimeUnit.MINUTES)
        .writeTimeout(1, TimeUnit.MINUTES)
    okHttpClientBuilder.build()
}

fun provideMoshiConverter(): Moshi = run {
    Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
}

fun provideClientUserBearer(
    okHttpClient: OkHttpClient,
    baseUrl: String,
    moshiConverter: Moshi
): ApiServiceUserBearer {
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshiConverter))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
        .create(ApiServiceUserBearer::class.java)
}