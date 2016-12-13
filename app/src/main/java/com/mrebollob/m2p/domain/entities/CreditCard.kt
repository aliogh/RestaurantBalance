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

class CreditCard(val number: String, val expMonth: String, val expYear: String, val cvv: String) {

    fun isValid(): Boolean {
        return checkSum(number) != 0
    }

    fun checkSum(numberString: String): Int {
        return checkSum(numberString, false)
    }

    fun checkSum(numberString: String, noCheckDigit: Boolean): Int {
        var numberString = numberString
        var sum = 0
        var checkDigit = 0

        if (!noCheckDigit)
            numberString = numberString.substring(0, numberString.length - 1)

        var isDouble = true
        for (i in numberString.length - 1 downTo 0) {
            val k = Integer.parseInt(numberString[i].toString())
            sum += sumToSingleDigit(k * if (isDouble) 2 else 1)
            isDouble = !isDouble
        }

        if (sum % 10 > 0)
            checkDigit = 10 - sum % 10

        return checkDigit
    }

    private fun sumToSingleDigit(k: Int): Int {
        if (k < 10) {
            return k
        } else {
            return sumToSingleDigit(k / 10) + k % 10
        }
    }
}