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

package com.mrebollob.m2p.presentation.view.main

import android.content.Intent
import android.os.Bundle
import com.mrebollob.m2p.R
import com.mrebollob.m2p.domain.entities.CreditCard
import com.mrebollob.m2p.domain.entities.CreditCardBalance
import com.mrebollob.m2p.presentation.presenter.main.MainPresenter
import com.mrebollob.m2p.presentation.view.BaseActivity
import com.mrebollob.m2p.presentation.view.form.FormActivity
import com.mrebollob.m2p.utils.extensions.gone
import com.mrebollob.m2p.utils.extensions.toast
import com.mrebollob.m2p.utils.extensions.visible
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : BaseActivity(), MainMvpView {

    init {
        System.loadLibrary("encryptor-lib")
    }

    @Inject lateinit var mPresenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeDependencyInjector()

        initUI()
    }

    private fun initializeDependencyInjector() {
        this.getApplicationComponent().inject(this)
    }

    fun initUI() {
        initFab()

    }

    fun initFab() {
        fab.setOnClickListener { view ->
            val intent = Intent(this, FormActivity::class.java)
            startActivityForResult(intent, 0x62)
        }
    }

    override fun showCreditCard(creditCard: CreditCard) {
        val cardNumber = "****" + creditCard.number.substring(creditCard.number.length - 4)

        creditCardView.cardNumber = creditCard.number
        creditCardView.setCardExpiry(creditCard.expMonth + "/" + creditCard.expYear)
        creditCardView.cardHolderName = "Money to Pay"

        toast(getKey())
    }

    override fun showCardBalance(creditCardBalance: CreditCardBalance) {
        cardBalanceTv.text = "Balance: " + creditCardBalance.balance
    }

    override fun showError(error: String) {
        toast("ERROR: " + error)
    }

    override fun showLoading() {
        loading.visible()
        cardBalanceTv.gone()
        loading.start()
    }

    override fun hideLoading() {
        loading.gone()
        cardBalanceTv.visible()
        loading.stop()
    }

    override fun onStart() {
        super.onStart()
        mPresenter.attachView(this)
    }

    override fun onStop() {
        super.onStop()
        mPresenter.detachView()
    }

    external fun getKey(): String
}