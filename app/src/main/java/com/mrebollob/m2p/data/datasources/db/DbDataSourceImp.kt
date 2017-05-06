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

import android.app.Application
import com.mrebollob.m2p.data.datasources.db.models.DbCreditCard
import com.mrebollob.m2p.domain.datasources.DbDataSource
import com.mrebollob.m2p.domain.entities.Color
import com.mrebollob.m2p.domain.entities.CreditCard
import com.mrebollob.m2p.domain.exceptions.DbException
import com.mrebollob.m2p.presentation.di.qualifiers.DataBaseName
import io.reactivex.Observable
import io.realm.Realm
import io.realm.RealmConfiguration
import java.io.IOException
import javax.inject.Inject


class DbDataSourceImp @Inject constructor(context: Application, @DataBaseName dbName: String) : DbDataSource {

    init {
        Realm.init(context)
        val realmConfig = RealmConfiguration.Builder()
                .name(dbName)
                .deleteRealmIfMigrationNeeded()
                .build()
        Realm.setDefaultConfiguration(realmConfig)
    }

    override fun createCard(localId: String, number: String, expDate: String,
                            color: Color, position: Long): Observable<Unit> {
        return Observable.create {
            val database = Realm.getDefaultInstance()

            try {
                val dbCreditCard = DbCreditCard(
                        localId = localId,
                        number = number,
                        expDate = expDate,
                        colorEnum = color.toDataColor(),
                        position = position)


                dbCreditCard.insertOrUpdate(database)

                it.onComplete()
            } catch (exception: IOException) {
                it.onError(DbException())
            } finally {
                database.close()
            }
        }
    }

    override fun readCards(): Observable<List<CreditCard>> {
        return Observable.create {
            val database = Realm.getDefaultInstance()

            try {
                val dbCards = database.queryAllCardsSortedByPosition()

                it.onNext(dbCards.toDomainCardList())
                it.onComplete()
            } catch (exception: IOException) {
                it.onError(DbException())
            } finally {
                database.close()
            }
        }
    }

    override fun readCard(): Observable<CreditCard> {
        TODO("Sin implementar")
    }

    override fun updateCard(localId: String, number: String, expDate: String,
                            color: Color, position: Long): Observable<CreditCard> {
        TODO("Sin implementar")
    }

    override fun deleteCard(id: Long): Observable<Unit> {
        TODO("Sin implementar")
    }
}