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

package com.mrebollob.m2p.domain.entities

import com.mrebollob.m2p.utils.PositionsFactory
import java.util.*

const val LOCAL_ID = "localId"

fun generateLocalId(): String = LOCAL_ID + "_" + UUID.randomUUID().toString().replace("-".toRegex(), "")

enum class Color {
    RED, YELLOW, GREEN, BLUE, WHITE
}

data class CreditCard(val localId: String = generateLocalId(),
                      val number: String? = null,
                      val expDate: String? = null,
                      val color: Color = Color.WHITE,
                      val position: Long = object : PositionsFactory {}.newPosition()) {

    fun getExpMonth(): String? {
        return expDate?.split("/")?.get(0)
    }

    fun getExpYear(): String? {
        return expDate?.split("/")?.get(1)
    }

    fun isEmpty(): Boolean = number == null || expDate == null

    fun isNotEmpty(): Boolean = !isEmpty()
}