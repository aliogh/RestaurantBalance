/*
 * Copyright (c) 2016. Manuel Rebollo Báez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mrebollob.m2p.presentation.di.modules


import com.mrebollob.m2p.data.datasources.network.NetworkDataSourceImp
import com.mrebollob.m2p.data.datasources.network.interceptor.CookiesInterceptor
import com.mrebollob.m2p.domain.datasources.NetworkDataSource
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
class NetworkModule {

    companion object {
        private val TIMEOUT_IN_MS = 30000L
    }

    @Provides
    @Singleton
    fun provideNetworkDataSource(networkDataSource: NetworkDataSourceImp): NetworkDataSource {

        return networkDataSource
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor,
                            cookiesInterceptor: CookiesInterceptor): OkHttpClient {

        val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(TIMEOUT_IN_MS, TimeUnit.MILLISECONDS)
                .readTimeout(TIMEOUT_IN_MS, TimeUnit.MILLISECONDS)
                .addInterceptor(cookiesInterceptor)
                .addInterceptor(httpLoggingInterceptor)
                .build()

        return okHttpClient
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }
}