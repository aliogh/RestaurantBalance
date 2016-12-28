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

import android.util.Log
import com.mrebollob.m2p.data.scraper.M2PWebScraper
import com.mrebollob.m2p.domain.datasources.NetworkDataSource
import com.mrebollob.m2p.domain.entities.CreditCard
import com.mrebollob.m2p.domain.entities.CreditCardBalance
import com.mrebollob.m2p.domain.exceptions.GetBalanceException
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import rx.Observable
import java.io.IOException
import javax.inject.Inject


class NetworkDataSourceImp @Inject constructor(val httpClient: OkHttpClient, val m2PWebScraper: M2PWebScraper)
    : NetworkDataSource {

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
                        .post(getFormBody(creditCard))
                        .build()
                val response = httpClient.newCall(requestCard).execute()
                val stringBody = response.body().string()
                val error = m2PWebScraper.getError(stringBody)

                if (error.isEmpty()) {
                    subscriber.onNext(m2PWebScraper.getCardBalance(stringBody))
                    subscriber.onCompleted()
                } else {
                    subscriber.onError(GetBalanceException(error))
                }

            } catch (exception: IOException) {
                Log.e("NetworkDataSourceImp", "getCreditCardBalance", exception)
                subscriber.onError(GetBalanceException(""))
            }
        }
    }

    fun getFormBody(creditCard: CreditCard): FormBody {
        return FormBody.Builder()
                .add("card1", creditCard.number.substring(0, 4))
                .add("card2", creditCard.number.substring(4, 8))
                .add("card3", creditCard.number.substring(8, 12))
                .add("card4", creditCard.number.substring(12, 16))
                .add("cardMonth", creditCard.getExpMonth())
                .add("cardYear", creditCard.getExpYear())
                .add("ccv2", creditCard.cvv)
                .build()
    }

    fun getUrl(): String {
        return "https://www.moneytopay.com/es/gestion-tarjeta-regalo"
    }
}