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

import com.mrebollob.m2p.data.datasources.db.models.DbCreditCard
import com.mrebollob.m2p.domain.datasources.DbDataSource
import com.mrebollob.m2p.domain.entities.CreditCard
import com.mrebollob.m2p.domain.exceptions.DbException
import com.mrebollob.m2p.utils.encryption.Encryptor
import com.orm.SugarRecord
import io.reactivex.Observable
import java.io.IOException
import javax.inject.Inject


class DbDataSourceImp @Inject constructor(val encryptor: Encryptor) : DbDataSource {

    override fun getCreditCards(): Observable<List<CreditCard>> {
        return Observable.create {
            try {
                val dbCreditCards = SugarRecord.listAll(DbCreditCard::class.java)

                val creditCards = dbCreditCards.map {
                    CreditCard(it.id, encryptor.getUnhashed(it.number),
                            encryptor.getUnhashed(it.expDate), "")
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
                val dbCreditCard = SugarRecord.findById(DbCreditCard::class.java, id)
                SugarRecord.delete(dbCreditCard)

                it.onComplete()
            } catch (exception: IOException) {
                it.onError(DbException())
            }
        }
    }

    override fun createCreditCard(number: String, expDate: String): Observable<Unit> {
        return Observable.create {
            try {
                val dbCreditCard = DbCreditCard()
                dbCreditCard.number = encryptor.getAsHash(number)
                dbCreditCard.expDate = encryptor.getAsHash(expDate)

                dbCreditCard.save()

//                SugarRecord.save(dbCreditCard)
                it.onComplete()
            } catch (exception: IOException) {
                it.onError(DbException())
            }
        }
    }
}