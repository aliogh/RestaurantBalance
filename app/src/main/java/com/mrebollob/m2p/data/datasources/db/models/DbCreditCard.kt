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

package com.mrebollob.m2p.data.datasources.db.models

import io.realm.RealmModel
import io.realm.annotations.Ignore
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

internal const val LOCAL_ID = "localId"
internal const val POSITION = "position"

enum class Color {
    RED, YELLOW, GREEN, BLUE, WHITE
}

@RealmClass
open class DbCreditCard() : RealmModel {

    constructor(localId: String, number: String, expDate: String,
                colorEnum: Color = Color.WHITE, position: Long) : this() {
        this.localId = localId
        this.number = number
        this.expDate = expDate
        this.color = colorEnum.name
        this.position = position
    }

    @PrimaryKey open var localId: String = ""
    open var number: String = ""
    open var expDate: String = ""
    @Ignore private var colorEnum: Color = Color.WHITE
    open var color: String = colorEnum.name
    open var position: Long = 0

    fun getColorAsEnum(): Color = Color.valueOf(color)

    fun setColorAsEnum(color: Color) {
        this.color = color.name
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as DbCreditCard

        if (localId != other.localId) return false
        if (number != other.number) return false
        if (expDate != other.expDate) return false
        if (colorEnum != other.colorEnum) return false
        if (color != other.color) return false
        if (position != other.position) return false

        return true
    }

    override fun hashCode(): Int {
        var result = localId.hashCode()
        result = 31 * result + number.hashCode()
        result = 31 * result + expDate.hashCode()
        result = 31 * result + colorEnum.hashCode()
        result = 31 * result + color.hashCode()
        result = 31 * result + position.hashCode()
        return result
    }
}