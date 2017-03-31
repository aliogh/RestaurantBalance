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

package com.mrebollob.m2p.presentation.view.splash.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mrebollob.m2p.databinding.ItemCardBinding
import com.mrebollob.m2p.domain.entities.CreditCard
import kotlin.properties.Delegates

class CreditCardListAdapter : RecyclerView.Adapter<CreditCardViewHolder>() {

    var creditCards: List<CreditCard> by Delegates.observable(emptyList())
    { prop, old, new -> notifyDataSetChange() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreditCardViewHolder {
        val binding = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CreditCardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CreditCardViewHolder, position: Int) {
        holder.bind(creditCards[position])
    }

    override fun getItemCount(): Int {
        return creditCards.count()
    }

    fun notifyDataSetChange() {
        notifyDataSetChanged()
    }
}