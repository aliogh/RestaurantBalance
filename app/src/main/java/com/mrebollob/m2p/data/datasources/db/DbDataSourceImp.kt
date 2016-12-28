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

package com.mrebollob.m2p.data.datasources.db

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.mrebollob.m2p.domain.datasources.DbDataSource
import com.mrebollob.m2p.domain.entities.CreditCard
import rx.Observable
import javax.inject.Inject

class DbDataSourceImp @Inject constructor(val sharedPreferences: SharedPreferences, val gson: Gson) : DbDataSource {

    init {
        System.loadLibrary("encryptor-lib")
    }

    val CREDIT_CARD_KEY = "credit_card_key"

    override fun getCreditCard(): Observable<CreditCard> {
        return Observable.create {
            subscriber ->

            Log.d("DbDataSourceImp", "Key: " + getKey())

            val creditCardJson = sharedPreferences.getString(CREDIT_CARD_KEY, "")

            val creditCard = gson.fromJson(creditCardJson, CreditCard::class.java)

            subscriber.onNext(creditCard)
            subscriber.onCompleted()
        }
    }

    override fun createCreditCard(creditCard: CreditCard): Observable<CreditCard> {
        return Observable.create {
            subscriber ->

            val creditCardJson = gson.toJson(creditCard)

            sharedPreferences.edit()
                    .putString(CREDIT_CARD_KEY, creditCardJson)
                    .apply()

            subscriber.onNext(creditCard)
            subscriber.onCompleted()
        }
    }

    external fun getKey(): String
}