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

import com.mrebollob.m2p.data.mapper.CreditCardBalanceMapper
import com.mrebollob.m2p.domain.datasources.NetworkDataSource
import com.mrebollob.m2p.domain.entities.CreditCard
import com.mrebollob.m2p.domain.entities.CreditCardBalance
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import rx.Observable
import java.io.IOException


class NetworkDataSourceImp : NetworkDataSource {

    val creditCardBalanceMapper = CreditCardBalanceMapper()
    var client = OkHttpClient.Builder()
            .addInterceptor(provideLoggingInterceptor())
            .build()

    override fun getCreditCardBalance(creditCard: CreditCard): Observable<CreditCardBalance> {
        return Observable.create {
            subscriber ->
            try {
                val request = Request.Builder()
                        .url(getUrl())
                        .post(getFormBody())
                        .build()

                val response = client.newCall(request).execute()



                subscriber.onNext(creditCardBalanceMapper.transform(creditCard.number))
                subscriber.onCompleted()

                if (!response.isSuccessful) {
                    subscriber.onError(Exception("error"))
                }
            } catch (exception: IOException) {
                subscriber.onError(exception)
            }
        }
    }

    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    fun getFormBody(): FormBody {
        return FormBody.Builder()
                .add("card1", "4047")
                .add("card2", "0000")
                .add("card3", "1914")
                .add("card4", "3012")
                .add("cardMonth", "08")
                .add("cardYear", "21")
                .add("ccv2", "582")
                .build()
    }

    fun getUrl(): String {
        return "https://www.moneytopay.com/es/gestion-tarjeta-regalo"
    }
}