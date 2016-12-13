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

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.widget.Toast
import com.mrebollob.m2p.R
import com.mrebollob.m2p.domain.entities.CreditCard
import com.mrebollob.m2p.domain.entities.CreditCardBalance
import com.mrebollob.m2p.presentation.presenter.main.MainPresenter
import com.mrebollob.m2p.presentation.view.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : BaseActivity(), MainMvpView {

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
        initToolbar()
        showBalanceBtn.setOnClickListener { onShowBalanceClick() }

        numberEt.setText("4047000019143012")
        expDateEt.setText("08/21")
        cvvEt.setText("582")
    }

    fun initToolbar() {
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
    }

    fun onShowBalanceClick() {
        numberIl.error = null
        expDateIl.error = null
        cvvIl.error = null

        val expDate = expDateEt.text.split("/")
        val creditCard = CreditCard(numberEt.text.toString(), expDate[0], expDate[1], cvvEt.text.toString())

        if (creditCard.isValid()) {
            mPresenter.showBalance(creditCard)
        } else {
            numberIl.error = "Card not valid"
            expDateIl.error = "MM/YY"
        }
    }

    override fun showCardBalance(creditCardBalance: CreditCardBalance) {
        Toast.makeText(this, creditCardBalance.balance, Toast.LENGTH_LONG).show()
    }

    override fun showError(error: String) {
        Toast.makeText(this, "ERROR: " + error, Toast.LENGTH_SHORT).show()
    }

    override fun onStart() {
        super.onStart()
        mPresenter.attachView(this)
    }

    override fun onStop() {
        super.onStop()
        mPresenter.detachView()
    }
}