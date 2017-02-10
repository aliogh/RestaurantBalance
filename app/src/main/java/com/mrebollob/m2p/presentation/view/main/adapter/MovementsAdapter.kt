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

package com.mrebollob.m2p.presentation.view.main.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.mrebollob.m2p.R
import com.mrebollob.m2p.domain.entities.CreditCardMovement
import com.mrebollob.m2p.utils.extensions.inflate
import kotlin.properties.Delegates

class MovementsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var movements: List<CreditCardMovement> by Delegates.observable(emptyList())
    { prop, old, new -> notifyDataSetChange() }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
        val view = parent.inflate(R.layout.movement_item_view)
        return MovementViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val movementViewHolder = holder as MovementViewHolder
        val movement = movements[position]
        movementViewHolder.render(movement)
    }

    override fun getItemCount(): Int {
        return movements.count()
    }

    fun notifyDataSetChange() {
        notifyDataSetChanged()
    }
}