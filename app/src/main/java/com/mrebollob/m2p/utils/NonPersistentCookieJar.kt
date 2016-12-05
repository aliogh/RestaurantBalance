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

package com.mrebollob.m2p.utils

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NonPersistentCookieJar @Inject constructor() : CookieJar {

    private var cookieStore: MutableSet<Cookie> = LinkedHashSet()

    @Synchronized override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        cookieStore.addAll(cookies)
    }

    @Synchronized override fun loadForRequest(url: HttpUrl): List<Cookie> {
        val matchingCookies = ArrayList<Cookie>()
        val it = cookieStore.iterator()
        while (it.hasNext()) {
            val cookie = it.next()
            if (cookie.expiresAt() < System.currentTimeMillis()) {
                it.remove()
            } else if (cookie.matches(url)) {
                matchingCookies.add(cookie)
            }
        }
        return matchingCookies
    }
}