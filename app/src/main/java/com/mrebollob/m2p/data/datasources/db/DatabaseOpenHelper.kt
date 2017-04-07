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

import android.app.Application
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseOpenHelper @Inject constructor(application: Application)
    : ManagedSQLiteOpenHelper(application, "Restaurants", null, 1) {

    val TABLE_DB_CREDIT_CARD = "DB_CREDIT_CARD"
    val ID = "_id"
    val NUMBER = "number"
    val EXP_DATE = "expDate"

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(TABLE_DB_CREDIT_CARD, true,
                ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                NUMBER to TEXT,
                EXP_DATE to TEXT)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.dropTable(TABLE_DB_CREDIT_CARD, true)
    }
}