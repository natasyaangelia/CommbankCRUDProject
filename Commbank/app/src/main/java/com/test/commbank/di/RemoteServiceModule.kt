package com.test.commbank.di

import com.test.commbank.data.source.remote.*
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val BEARER_AUTH_TYPE = "bearer_auth"

val remoteModule = module {
    single { provideCacheInterceptor() }
    single { provideHttpLoggingInterceptor() }
    single { provideMoshiConverter() }
    single(named(BEARER_AUTH_TYPE)) { provideOkHttpClientJwt(get(named(TOKEN))) }
    single {
        provideClientUserBearer(
            get(named(BEARER_AUTH_TYPE)),
            get(named(BASE_URL)),
            get()
        )
    }
}