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
import android.util.Base64
import android.util.Log
import com.google.gson.Gson
import com.mrebollob.m2p.domain.datasources.DbDataSource
import com.mrebollob.m2p.domain.entities.CreditCard
import com.mrebollob.m2p.domain.exceptions.NoCreditCardException
import com.mrebollob.m2p.presentation.di.qualifiers.EncryptorKey
import io.reactivex.Observable
import java.io.IOException
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject

class DbDataSourceImp @Inject constructor(val sharedPreferences: SharedPreferences, val gson: Gson,
                                          @EncryptorKey val key: String) : DbDataSource {

    val initVector = "RandomInitVector"
    val CREDIT_CARD_KEY = "credit_card_key"

    override fun getCreditCard(): Observable<CreditCard> {
        return Observable.create {
            try {
                val creditCardJson = sharedPreferences.getString(CREDIT_CARD_KEY, "")
                val creditCard = gson.fromJson(decrypt(creditCardJson), CreditCard::class.java)

                if (creditCard != null) {
                    it.onNext(creditCard)
                    it.onComplete()
                } else {
                    it.onError(NoCreditCardException())
                }
            } catch (exception: IOException) {
                Log.e("DbDataSourceImp", "getCreditCard", exception)
                //TODO change exception
                it.onError(exception)
            }
        }
    }

    override fun createCreditCard(creditCard: CreditCard): Observable<CreditCard> {
        return Observable.create {
            try {
                val creditCardJson = gson.toJson(creditCard)

                sharedPreferences.edit()
                        .putString(CREDIT_CARD_KEY, encrypt(creditCardJson))
                        .apply()

                it.onNext(creditCard)
                it.onComplete()
            } catch (exception: IOException) {
                Log.e("DbDataSourceImp", "createCreditCard", exception)
                //TODO change exception
                it.onError(exception)
            }
        }
    }

    fun encrypt(value: String): String {
        val iv = IvParameterSpec(initVector.toByteArray(charset("UTF-8")))
        val skeySpec = SecretKeySpec(key.toByteArray(charset("UTF-8")), "AES")

        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv)

        val encrypted = cipher.doFinal(value.toByteArray())

        return Base64.encodeToString(encrypted, Base64.DEFAULT)
    }

    fun decrypt(encrypted: String): String {
        val iv = IvParameterSpec(initVector.toByteArray(charset("UTF-8")))
        val skeySpec = SecretKeySpec(key.toByteArray(charset("UTF-8")), "AES")

        val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv)

        val original = cipher.doFinal(Base64.decode(encrypted, Base64.DEFAULT))

        return String(original)
    }
}