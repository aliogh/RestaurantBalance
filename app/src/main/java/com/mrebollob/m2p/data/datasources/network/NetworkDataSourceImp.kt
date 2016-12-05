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

package com.mrebollob.m2p.data.datasources.network

import com.mrebollob.m2p.data.scraper.M2PWebScraper
import com.mrebollob.m2p.domain.datasources.NetworkDataSource
import com.mrebollob.m2p.domain.entities.CreditCard
import com.mrebollob.m2p.domain.entities.CreditCardBalance
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import rx.Observable
import java.io.IOException
import javax.inject.Inject


class NetworkDataSourceImp @Inject constructor(val httpClient: OkHttpClient,
                                               val m2PWebScraper: M2PWebScraper) : NetworkDataSource {

    override fun getCreditCardBalance(creditCard: CreditCard): Observable<CreditCardBalance> {
        return Observable.create {
            subscriber ->
            try {

                val requestCookie = Request.Builder()
                        .url(getUrl())
                        .get()
                        .build()
                val baseResponse = httpClient.newCall(requestCookie).execute()

                val formUrl = m2PWebScraper.getFormUrl(baseResponse.body().string())
                val requestForm = Request.Builder()
                        .url(formUrl)
                        .get()
                        .build()
                val formResponse = httpClient.newCall(requestForm).execute()

                val postFormUrl = m2PWebScraper.getPostFormUrl(formResponse.body().string())
                val requestCard = Request.Builder()
                        .url(postFormUrl)
                        .post(getFormBody())
                        .build()
                val response = httpClient.newCall(requestCard).execute()

                subscriber.onNext(m2PWebScraper.getCardBalance(response.body().string()))
                subscriber.onCompleted()

                if (!response.isSuccessful) {
                    subscriber.onError(Exception("error"))
                }
            } catch (exception: IOException) {
                subscriber.onError(exception)
            }
        }
    }

    fun getFormBody(): FormBody {
        return FormBody.Builder()
                .add("card1", "0000")
                .add("card2", "0000")
                .add("card3", "0000")
                .add("card4", "0000")
                .add("cardMonth", "00")
                .add("cardYear", "00")
                .add("ccv2", "000")
                .build()
    }

    fun getUrl(): String {
        return "https://www.moneytopay.com/es/gestion-tarjeta-regalo"
    }
}