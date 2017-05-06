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
import com.mrebollob.m2p.domain.entities.CreditCard
import com.mrebollob.m2p.data.datasources.db.models.Color as DataColor
import com.mrebollob.m2p.domain.entities.Color as DomainColor


fun CreditCard.toDataCard(): DbCreditCard =
        with(this) {
            DbCreditCard(localId, number, expDate, color.toDataColor(), position)
        }

fun DomainColor.toDataColor(): DataColor =
        when (this) {
            DomainColor.BLUE -> DataColor.BLUE
            DomainColor.GREEN -> DataColor.GREEN
            DomainColor.RED -> DataColor.RED
            DomainColor.WHITE -> DataColor.WHITE
            DomainColor.YELLOW -> DataColor.YELLOW
        }

fun DataColor.toDomainColor(): DomainColor =
        when (this) {
            DataColor.BLUE -> DomainColor.BLUE
            DataColor.GREEN -> DomainColor.GREEN
            DataColor.RED -> DomainColor.RED
            DataColor.WHITE -> DomainColor.WHITE
            DataColor.YELLOW -> DomainColor.YELLOW
        }

fun DbCreditCard.toDomainCard(): CreditCard =
        with(this) {
            CreditCard(localId, number, expDate, getColorAsEnum().toDomainColor(), position)
        }

fun List<DbCreditCard>.toDomainCardList(): List<CreditCard> =
        this.map(DbCreditCard::toDomainCard)