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

package com.mrebollob.m2p.utils

import android.text.Editable
import android.widget.EditText


class CardNumberTextWatcher constructor(val numberEditText: EditText,
                                        actionListener: CardActionListener)
    : CreditCardTextWatcher(actionListener) {

    val MAX_LENGTH_CARD_NUMBER_WITH_SPACES = 19
    val MAX_LENGTH_CARD_NUMBER = 16
    val SPACE_SEPERATOR = " "

    override fun afterTextChanged(s: Editable?) {
        val cursorPosition = numberEditText.selectionEnd
        val previousLength = numberEditText.text.length

        val cardNumber = handleCardNumber(s.toString())
        val modifiedLength = cardNumber.length

        numberEditText.removeTextChangedListener(this)
        numberEditText.setText(cardNumber)
        numberEditText.setSelection(if (cardNumber.length > MAX_LENGTH_CARD_NUMBER_WITH_SPACES)
            MAX_LENGTH_CARD_NUMBER_WITH_SPACES else cardNumber.length)
        numberEditText.addTextChangedListener(this)

        if (modifiedLength <= previousLength && cursorPosition < modifiedLength) {
            numberEditText.setSelection(cursorPosition)
        }

        if (cardNumber.replace(SPACE_SEPERATOR, "").length == MAX_LENGTH_CARD_NUMBER) {
            onComplete()
        }
    }

    fun handleCardNumber(inputCardNumber: String): String {
        return handleCardNumber(inputCardNumber, " ")
    }

    fun handleCardNumber(inputCardNumber: String, seperator: String): String {
        val formattingText = inputCardNumber.replace(seperator, "")
        var text: String
        if (formattingText.length >= 4) {
            text = formattingText.substring(0, 4)
            if (formattingText.length >= 8) {
                text = text + seperator + formattingText.substring(4, 8)
            } else if (formattingText.length > 4) {
                text = text + seperator + formattingText.substring(4)
            }

            if (formattingText.length >= 12) {
                text = text + seperator + formattingText.substring(8, 12)
            } else if (formattingText.length > 8) {
                text = text + seperator + formattingText.substring(8)
            }

            if (formattingText.length >= 16) {
                text = text + seperator + formattingText.substring(12)
            } else if (formattingText.length > 12) {
                text = text + seperator + formattingText.substring(12)
            }

            return text
        } else {
            text = formattingText.trim { it <= ' ' }
            return text
        }
    }

    override fun getIndex(): Int {
        return 0
    }

    override fun focus() {
        numberEditText.requestFocus()
        numberEditText.selectAll()
    }
}