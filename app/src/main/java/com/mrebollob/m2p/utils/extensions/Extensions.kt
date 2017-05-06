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

package com.mrebollob.m2p.utils.extensions

import android.content.Context
import android.support.v4.content.ContextCompat.getColor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.mrebollob.m2p.R
import com.mrebollob.m2p.domain.entities.Color
import com.mrebollob.m2p.domain.entities.CreditCard

fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}

fun Context.toast(text: String) = Toast.makeText(this, text, Toast.LENGTH_SHORT).show()

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.changeBackgroundColor(colorId: Int) =
        setBackgroundColor(getColor(context, colorId))

fun View.changeBackgroundColor(color: Color) =
        setBackgroundColor(getColor(context, color.toColorResource()))

fun Color.toColorResource(): Int =
        //We don't use Presentation models, we enrich the Store models using EF
        when (this) {
            Color.RED -> R.color.red
            Color.YELLOW -> R.color.yellow
            Color.GREEN -> R.color.green
            Color.BLUE -> R.color.blue
            Color.WHITE -> R.color.white
        }

fun CreditCard.getStableId(): Long = localId.hashCode().toLong()

fun EditText.updateText(newText: String?) =
        newText?.let {
            if (isNotBlank() && isDifferentThan(it)) {
                setText(it)
            }
        }

fun EditText.isDifferentThan(newText: String): Boolean =
        text.toString() != newText

fun EditText.isNotBlank(): Boolean =
        text.isNotBlank()