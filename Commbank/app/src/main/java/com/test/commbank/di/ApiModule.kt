package com.test.commbank.di

import org.koin.core.qualifier.named
import org.koin.dsl.module

const val BASE_URL: String = "BASE_URL"
const val TOKEN: String = "TOKEN"

val apiModule = module {

    object {
        init {
            System.loadLibrary("native-lib")
        }
    }

    single(named(BASE_URL)) { getBaseUrl() }
    single(named(TOKEN)) { getToken() }
}

external fun getCommbankBaseUrl(): String
external fun getClientToken(): String

private fun getBaseUrl(): String = getCommbankBaseUrl()
private fun getToken(): String = getClientToken()