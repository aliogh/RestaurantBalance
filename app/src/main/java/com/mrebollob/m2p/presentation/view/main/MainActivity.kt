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

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import com.cooltechworks.creditcarddesign.CardEditActivity
import com.cooltechworks.creditcarddesign.CreditCardUtils
import com.mrebollob.m2p.R
import com.mrebollob.m2p.domain.entities.CreditCard
import com.mrebollob.m2p.domain.entities.CreditCardBalance
import com.mrebollob.m2p.presentation.presenter.main.MainPresenter
import com.mrebollob.m2p.presentation.view.BaseActivity
import com.mrebollob.m2p.utils.extensions.gone
import com.mrebollob.m2p.utils.extensions.visible
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : BaseActivity(), MainMvpView, SwipeRefreshLayout.OnRefreshListener {

    val GET_NEW_CARD = 0x62
    var isNewActivity = false
    @Inject lateinit var mPresenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeDependencyInjector()
        isNewActivity = (savedInstanceState == null)
        initUI()
    }

    private fun initializeDependencyInjector() {
        getApplicationComponent().inject(this)
    }

    private fun initUI() {
        dataView.setOnRefreshListener(this)

        fab.setOnClickListener { view ->
            mPresenter.addNewCreditCard()
        }

        retryBtn.setOnClickListener { view ->
            mPresenter.update()
        }
    }

    override fun showCreditCard(creditCard: CreditCard) {
        creditCardView.cardHolderName = creditCard.holderName
        creditCardView.cardNumber = creditCard.number
        creditCardView.setCardExpiry(creditCard.expDate)
    }

    override fun showCardBalance(creditCardBalance: CreditCardBalance) {
        errorView.gone()
        dataView.visible()

        cardBalanceTv.text = getString(R.string.balance_format, creditCardBalance.balance)
    }

    override fun showError(error: String) {
        dataView.gone()
        errorView.visible()

        errorTv.text = error
    }

    override fun showCreditCardForm() {
        val intent = Intent(this, CardEditActivity::class.java)
        startActivityForResult(intent, GET_NEW_CARD)
    }

    override fun showLoading() {
        loadingView.visible()
        mainView.gone()
        loadingView.start()
    }

    override fun hideLoading() {
        loadingView.stop()
        loadingView.gone()
        mainView.visible()
    }

    override fun onRefresh() {
        mPresenter.update()
        dataView.isRefreshing = false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == GET_NEW_CARD && resultCode == Activity.RESULT_OK && data != null) {
            val cardHolderName = data.getStringExtra(CreditCardUtils.EXTRA_CARD_HOLDER_NAME)
            val cardNumber = data.getStringExtra(CreditCardUtils.EXTRA_CARD_NUMBER)
            val expiry = data.getStringExtra(CreditCardUtils.EXTRA_CARD_EXPIRY)
            val cvv = data.getStringExtra(CreditCardUtils.EXTRA_CARD_CVV)

            mPresenter.createCreditCard(CreditCard(cardHolderName, cardNumber, expiry, cvv))
            isNewActivity = false
        }
    }

    override fun onStart() {
        super.onStart()
        mPresenter.attachView(this, isNewActivity)
    }

    override fun onStop() {
        super.onStop()
        mPresenter.detachView()
    }
}