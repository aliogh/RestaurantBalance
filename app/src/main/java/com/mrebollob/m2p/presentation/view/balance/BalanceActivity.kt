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

package com.mrebollob.m2p.presentation.view.balance

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.mrebollob.m2p.R
import com.mrebollob.m2p.domain.entities.CreditCard
import com.mrebollob.m2p.domain.entities.CreditCardBalance
import com.mrebollob.m2p.presentation.presenter.balance.BalancePresenter
import com.mrebollob.m2p.presentation.view.BaseActivity
import com.mrebollob.m2p.presentation.view.balance.adapter.MovementsAdapter
import com.mrebollob.m2p.utils.analytics.AnalyticsHelper
import com.mrebollob.m2p.utils.extensions.gone
import com.mrebollob.m2p.utils.extensions.visible
import kotlinx.android.synthetic.main.activity_balance.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class BalanceActivity : BaseActivity(), BalanceMvpView {

    val movementsAdapter = MovementsAdapter()
    var isNewActivity = false
    @Inject lateinit var mPresenter: BalancePresenter
    @Inject lateinit var mAnalyticsHelper: AnalyticsHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_balance)
        initializeDependencyInjector()
        isNewActivity = (savedInstanceState == null)
        initUI()

        mAnalyticsHelper.logContentView("Credit card balance view", "Output", "main-view")
    }

    private fun initializeDependencyInjector() {
        appComponent.inject(this)
    }

    private fun initUI() {
        setSupportActionBar(toolbar)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        movementList.layoutManager = LinearLayoutManager(this)
        movementList.adapter = movementsAdapter
    }

    override fun showCreditCard(creditCard: CreditCard) {
        creditCardView.cardHolderName = "M2P"
        creditCardView.cardNumber = creditCard.number
        creditCardView.setCardExpiry(creditCard.expDate)

//        fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_edit))
    }

    override fun showCardBalance(creditCardBalance: CreditCardBalance) {
        errorView.gone()
        dataView.visible()

        cardBalanceTv.text = getString(R.string.money_format, creditCardBalance.balance)
        movementsAdapter.movements = creditCardBalance.movements
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

    override fun onStart() {
        super.onStart()
        mPresenter.attachView(this, isNewActivity)
    }

    override fun onStop() {
        super.onStop()
        mPresenter.detachView()
    }

    private fun getCardId(): String {
        return intent.getStringExtra(EXTRA_CARD_ID)
    }

    companion object Navigator {

        val EXTRA_CARD_ID = "extra_card_id"

        fun open(context: Context, cardId: String) {
            val intent = Intent(context, BalanceActivity::class.java)
            intent.putExtra(EXTRA_CARD_ID, cardId)
            context.startActivity(intent)
        }
    }
}