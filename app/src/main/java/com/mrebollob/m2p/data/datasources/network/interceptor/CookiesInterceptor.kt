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

package com.mrebollob.m2p.data.datasources.network.interceptor

import com.mrebollob.m2p.utils.NonPersistentCookieJar
import okhttp3.Cookie
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject


class CookiesInterceptor @Inject
constructor(private val mNonPersistentCookieJar: NonPersistentCookieJar) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val builder = chain.request().newBuilder()

        val cookies = mNonPersistentCookieJar.loadForRequest(chain.request().url())
        var cookieString = ""
        for (cookie in cookies) {
            cookieString += cookie.name() + "=" + cookie.value() + "; "
        }

        builder.header("Cookie", cookieString)

        val response = chain.proceed(builder.build())

        if (!response.headers("Set-Cookie").isEmpty()) {
            val newCookies = Cookie.parseAll(response.request().url(), response.headers())
            mNonPersistentCookieJar.saveFromResponse(response.request().url(), newCookies)
        }

        return response
    }
}