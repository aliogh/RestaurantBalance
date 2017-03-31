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

package com.mrebollob.m2p.utils.itemanimator

import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPropertyAnimatorCompat
import android.support.v7.widget.RecyclerView
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator


class SlideInUpDelayedAnimator constructor(val mInterpolator: Interpolator) : BaseItemAnimator() {

    private val offsetDelay = 200

    override fun preAnimateAdd(holder: RecyclerView.ViewHolder) {
        ViewCompat.setTranslationY(holder.itemView, holder.itemView.height.toFloat())
        ViewCompat.setAlpha(holder.itemView, 0f)
    }

    override fun onAnimatedAdd(holder: RecyclerView.ViewHolder): ViewPropertyAnimatorCompat {
        return ViewCompat.animate(holder.itemView)
                .translationY(0f)
                .setInterpolator(mInterpolator)
                .setStartDelay((offsetDelay * holder.layoutPosition).toLong())
    }

    companion object Factory {

        fun create(): RecyclerView.ItemAnimator {
            val animator = SlideInUpDelayedAnimator(DecelerateInterpolator(1.2f))
            animator.addDuration = 600
            return animator
        }
    }
}