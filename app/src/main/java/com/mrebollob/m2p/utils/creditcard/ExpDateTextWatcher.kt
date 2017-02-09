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

package com.mrebollob.m2p.utils.creditcard

import android.content.Context
import android.widget.EditText
import com.cooltechworks.creditcarddesign.CreditCardUtils
import com.mrebollob.m2p.R
import java.util.*


class ExpDateTextWatcher constructor(val context: Context, val dateEditText: EditText,
                                     actionListener: CardActionListener)
    : CreditCardTextWatcher(actionListener) {

    private val mValidateCard = true

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        var text = s.toString().replace(CreditCardUtils.SLASH_SEPERATOR, "")

        val month: String
        var year = ""
        if (text.length >= 2) {
            month = text.substring(0, 2)

            if (text.length > 2) {
                year = text.substring(2)
            }

            if (mValidateCard) {
                val mm = Integer.parseInt(month)

                if (mm <= 0 || mm >= 13) {
                    dateEditText.error = context.getString(R.string.error_invalid_month)
                    return
                }

                if (text.length >= 4) {

                    val yy = Integer.parseInt(year)

                    val calendar = Calendar.getInstance()
                    val currentYear = calendar.get(Calendar.YEAR)
                    val currentMonth = calendar.get(Calendar.MONTH) + 1

                    val millenium = currentYear / 1000 * 1000


                    if (yy + millenium < currentYear) {
                        dateEditText.error = context.getString(R.string.error_card_expired)
                        return
                    } else if (yy + millenium == currentYear && mm < currentMonth) {
                        dateEditText.error = context.getString(R.string.error_card_expired)
                        return
                    }
                }
            }
        } else {
            month = text
        }

        val previousLength = dateEditText.text.length
        val cursorPosition = dateEditText.selectionEnd

        text = CreditCardUtils.handleExpiration(month, year)

        dateEditText.removeTextChangedListener(this)
        dateEditText.setText(text)
        dateEditText.setSelection(text.length)
        dateEditText.addTextChangedListener(this)

        val modifiedLength = text.length

        if (modifiedLength <= previousLength && cursorPosition < modifiedLength) {
            dateEditText.setSelection(cursorPosition)
        }

        if (text.length == 5) {
            onComplete()
        }
    }

    override fun getIndex(): Int {
        return 1
    }

    override fun focus() {
        dateEditText.requestFocus()
        dateEditText.selectAll()
    }
}