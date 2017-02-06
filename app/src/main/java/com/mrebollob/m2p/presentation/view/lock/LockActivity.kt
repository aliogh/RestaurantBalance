/*
 * Copyright (c) 2017. Manuel Rebollo Báez
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.mrebollob.m2p.presentation.view.lock

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.andrognito.pinlockview.PinLockListener
import com.mrebollob.m2p.R
import com.mrebollob.m2p.presentation.view.main.MainActivity
import kotlinx.android.synthetic.main.activity_lock.*


class LockActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lock)

        pinLockView.setPinLockListener(mPinLockListener)
        pinLockView.attachIndicatorDots(indicatorDots)
    }

    private val mPinLockListener = object : PinLockListener {
        override fun onComplete(pin: String) {
            finish()
            MainActivity.open(this@LockActivity, pin)
        }

        override fun onEmpty() {
        }

        override fun onPinChange(pinLength: Int, intermediatePin: String) {
        }
    }

    companion object Navigator {
        fun open(context: Context) {
            val intent = Intent(context, LockActivity::class.java)
            context.startActivity(intent)
        }
    }
}
