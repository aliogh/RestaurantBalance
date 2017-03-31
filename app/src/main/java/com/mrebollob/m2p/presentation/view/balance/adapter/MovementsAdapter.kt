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

package com.mrebollob.m2p.presentation.view.balance.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mrebollob.m2p.databinding.ItemMovementBinding
import com.mrebollob.m2p.domain.entities.CreditCardMovement
import kotlin.properties.Delegates

class MovementsAdapter : RecyclerView.Adapter<MovementViewHolder>() {

    var movements: List<CreditCardMovement> by Delegates.observable(emptyList())
    { prop, old, new -> notifyDataSetChange() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovementViewHolder {
        val binding = ItemMovementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovementViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovementViewHolder, position: Int) {
        holder.bind(movements[position])
    }

    override fun getItemCount(): Int {
        return movements.count()
    }

    fun notifyDataSetChange() {
        notifyDataSetChanged()
    }
}