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

import com.mrebollob.m2p.domain.datasources.DbDataSource
import com.mrebollob.m2p.domain.entities.CreditCard
import com.mrebollob.m2p.domain.exceptions.DbException
import com.mrebollob.m2p.utils.encryption.Encryptor
import io.reactivex.Observable
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.parseList
import org.jetbrains.anko.db.rowParser
import org.jetbrains.anko.db.select
import java.io.IOException
import javax.inject.Inject


class DbDataSourceImp @Inject constructor(val database: DatabaseOpenHelper, val encryptor: Encryptor)
    : DbDataSource {

    override fun getCreditCards(): Observable<List<CreditCard>> {
        return Observable.create {
            try {

                val creditCards = database.use {
                    select(database.TABLE_DB_CREDIT_CARD).exec {
                        parseList(rowParser { id: Int, number: String, expDate: String ->
                            CreditCard(id, encryptor.getUnhashed(number), encryptor.getUnhashed(expDate), "")
                        })
                    }
                }

                it.onNext(creditCards)
                it.onComplete()
            } catch (exception: IOException) {
                it.onError(DbException())
            }
        }
    }

    override fun removeCreditCard(id: Long): Observable<Unit> {
        return Observable.create {
            try {


                it.onComplete()
            } catch (exception: IOException) {
                it.onError(DbException())
            }
        }
    }

    override fun createCreditCard(number: String, expDate: String): Observable<Unit> {
        return Observable.create {
            try {
                database.use {
                    insert(database.TABLE_DB_CREDIT_CARD,
                            database.NUMBER to encryptor.getAsHash(number),
                            database.EXP_DATE to encryptor.getAsHash(expDate)
                    )
                }

                it.onComplete()
            } catch (exception: IOException) {
                it.onError(DbException())
            }
        }
    }
}