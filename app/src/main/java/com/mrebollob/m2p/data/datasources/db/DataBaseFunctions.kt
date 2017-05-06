/*
 * Copyright (c) 2017. Manuel Rebollo Báez
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
import com.mrebollob.m2p.data.datasources.db.models.LOCAL_ID
import com.mrebollob.m2p.data.datasources.db.models.POSITION
import io.realm.Realm
import io.realm.RealmModel
import io.realm.RealmObject
import io.realm.RealmResults

fun Realm.queryAllCardsSortedByPosition(): RealmResults<DbCreditCard> =
        this.where(DbCreditCard::class.java).findAll().sort(POSITION)

fun Realm.queryByLocalId(id: String): DbCreditCard? =
        this.where(DbCreditCard::class.java).equalTo(LOCAL_ID, id).findFirst()

fun DbCreditCard.insertOrUpdate(db: Realm): DbCreditCard {
    val managedItem = db.insertOrUpdateInTransaction(this)
    return managedItem
}

fun <T : RealmModel> Realm.insertOrUpdateInTransaction(model: T): T =
        with(this) {
            beginTransaction()
            val managedItem = copyToRealmOrUpdate(model)
            commitTransaction()
            return managedItem
        }

fun DbCreditCard.update(db: Realm, changes: (DbCreditCard.() -> Unit)): DbCreditCard {
    executeTransaction(db) { this.changes() }
    return this
}

private fun executeTransaction(db: Realm, changes: () -> Unit) {
    db.executeTransaction { changes() }
}

fun DbCreditCard.delete(db: Realm) {
    executeTransaction(db) { RealmObject.deleteFromRealm(this) }
}