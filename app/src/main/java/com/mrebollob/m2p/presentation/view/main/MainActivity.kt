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


class MainActivity : BaseActivity(), MainMvpView {

    private val mPresenter: MainPresenter = MainPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initUI()
    }

    fun initUI() {
        initToolbar()
        showBalanceBtn.setOnClickListener { onShowBalanceClick() }
    }

    fun initToolbar() {
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
    }

    fun onShowBalanceClick() {
        val creditCard = CreditCard(numberEt.text.toString(), expDateEt.text.toString(), cvvEt.text.toString())

        if (creditCard.isValid()) {
            mPresenter.showBalance(creditCard)
        } else {
            Toast.makeText(this, "Card not valid", Toast.LENGTH_SHORT).show()
        }
    }

    override fun showCardBalance(creditCardBalance: CreditCardBalance) {
        Toast.makeText(this, creditCardBalance.balance, Toast.LENGTH_SHORT).show()
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