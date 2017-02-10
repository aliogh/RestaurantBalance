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
import com.andrognito.pinlockview.PinLockListener
import com.mrebollob.m2p.R
import com.mrebollob.m2p.presentation.presenter.lock.LockPresenter
import com.mrebollob.m2p.presentation.view.BaseActivity
import com.mrebollob.m2p.presentation.view.main.MainActivity
import com.mrebollob.m2p.utils.extensions.toast
import kotlinx.android.synthetic.main.activity_lock.*
import javax.inject.Inject


class LockActivity : BaseActivity(), LockMvpView {

    var isNewActivity = false
    @Inject lateinit var mPresenter: LockPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lock)
        initializeDependencyInjector()
        isNewActivity = (savedInstanceState == null)
        initUI()
    }

    private fun initializeDependencyInjector() {
        appComponent.inject(this)
    }

    private fun initUI() {
        pinLockView.setPinLockListener(mPinLockListener)
        pinLockView.attachIndicatorDots(indicatorDots)

        removeCardButton.setOnClickListener { mPresenter.onRemoveCreditCardClick() }
    }

    private val mPinLockListener = object : PinLockListener {
        override fun onComplete(pin: String) {
            mPresenter.onCvvComplete(pin)
        }

        override fun onEmpty() {
        }

        override fun onPinChange(pinLength: Int, intermediatePin: String) {
        }
    }

    override fun showCreditCardBalance(cvv: String) {
        finish()
        MainActivity.open(this@LockActivity, cvv)
    }

    override fun showCreditCardRemoved() {
        finish()
        MainActivity.open(this@LockActivity, "")
    }

    override fun showRemoveCreditCardError() {
        toast(getString(R.string.remove_credit_card_error))
    }

    override fun onStart() {
        super.onStart()
        mPresenter.attachView(this, isNewActivity)
    }

    override fun onStop() {
        super.onStop()
        mPresenter.detachView()
    }

    companion object Navigator {
        fun open(context: Context) {
            val intent = Intent(context, LockActivity::class.java)
            context.startActivity(intent)
        }
    }
}
