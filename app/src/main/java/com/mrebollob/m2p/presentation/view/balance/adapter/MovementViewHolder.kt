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
import com.mrebollob.m2p.databinding.ItemMovementBinding
import com.mrebollob.m2p.domain.entities.CreditCardMovement

class MovementViewHolder constructor(val binding: ItemMovementBinding) : RecyclerView.ViewHolder(binding.root) {

    private val URL_PATTERN = "^(https?://)?([\\da-z\\.-]+)\\.([a-z\\.]{2,6})([/\\w \\.-]*)*/?$".toRegex()

    fun bind(movement: CreditCardMovement) {
        binding.movement = movement
    }

    private fun formatString(name: String): String {
        if (URL_PATTERN.matches(name.toLowerCase())) {
            return name.toLowerCase()
        } else {
            return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase()
        }
    }
}