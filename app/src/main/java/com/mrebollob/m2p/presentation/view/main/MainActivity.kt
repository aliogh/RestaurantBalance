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
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import com.mrebollob.m2p.BuildConfig
import com.mrebollob.m2p.R
import com.mrebollob.m2p.domain.entities.CreditCard
import com.mrebollob.m2p.domain.entities.CreditCardBalance
import com.mrebollob.m2p.presentation.presenter.main.MainPresenter
import com.mrebollob.m2p.presentation.view.BaseActivity
import com.mrebollob.m2p.presentation.view.form.FormActivity
import com.mrebollob.m2p.presentation.view.lock.LockActivity
import com.mrebollob.m2p.presentation.view.main.adapter.MovementsAdapter
import com.mrebollob.m2p.utils.analytics.AnalyticsHelper
import com.mrebollob.m2p.utils.extensions.gone
import com.mrebollob.m2p.utils.extensions.visible
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject


class MainActivity : BaseActivity(), MainMvpView, SwipeRefreshLayout.OnRefreshListener {

    val GET_CVV = 0x61
    val movementsAdapter = MovementsAdapter()
    var isNewActivity = false
    var shouldResetCvv = false
    @Inject lateinit var mPresenter: MainPresenter
    @Inject lateinit var mAnalyticsHelper: AnalyticsHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeDependencyInjector()
        isNewActivity = (savedInstanceState == null)
        initUI()

        shouldResetCvv = false

        mAnalyticsHelper.logContentView("Credit card balance view", "Output", "main-view")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_share, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_share) {
            shareData()
            return true
        } else {
            return super.onOptionsItemSelected(item)
        }
    }

    private fun shareData() {
        mAnalyticsHelper.logInvite()
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_text, BuildConfig.APP_URL))
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share)))
    }

    private fun initializeDependencyInjector() {
        appComponent.inject(this)
    }

    private fun initUI() {
        setSupportActionBar(toolbar)
        initRecyclerView()
        initRefreshLayout()

        fab.setOnClickListener { view ->
            mPresenter.onEditCreditCardClick()
        }
        retryBtn.setOnClickListener { view ->
            mPresenter.update()
        }
    }

    private fun initRecyclerView() {
        movementList.layoutManager = LinearLayoutManager(this)
        movementList.adapter = movementsAdapter
    }

    private fun initRefreshLayout() {
        dataView.setOnRefreshListener(this)
        movementList.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {}

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                val topRowVerticalPosition = if (recyclerView == null || recyclerView.childCount == 0)
                    0 else recyclerView.getChildAt(0).top
                dataView.isEnabled = topRowVerticalPosition >= 0
            }
        })
    }

    override fun showCreditCard(creditCard: CreditCard) {
        creditCardView.cardHolderName = "M2P"
        creditCardView.cardNumber = creditCard.number
        creditCardView.setCardExpiry(creditCard.expDate)

        fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_edit))
    }

    override fun showCardBalance(creditCardBalance: CreditCardBalance) {
        errorView.gone()
        dataView.visible()

        cardBalanceTv.text = getString(R.string.money_format, creditCardBalance.balance)
        movementsAdapter.movements = creditCardBalance.movements
    }

    override fun showEmptyCreditCard() {
        creditCardView.cardHolderName = "M2P"
        creditCardView.cardNumber = ""
        creditCardView.setCardExpiry("")
        fab.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_add))

        errorView.gone()
        dataView.visible()
        cardBalanceTv.text = getString(R.string.add_credit_card)
    }

    override fun showError(error: String) {
        dataView.gone()
        errorView.visible()

        errorTv.text = error
    }

    override fun showCreditCardForm(number: String?, expDate: String?) {
        FormActivity.openForResult(this@MainActivity, number, expDate)
    }

    override fun showLockScreen() {
        finish()
        LockActivity.open(this@MainActivity)
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
        if (resultCode == Activity.RESULT_OK && data != null) {
            when (requestCode) {
                GET_CVV -> {
                    mPresenter.mCvv = data.getStringExtra(EXTRA_CARD_CVV)
                }
                FormActivity.CREDIT_CARD_FORM -> {
                    isNewActivity = true
                    shouldResetCvv = true
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mPresenter.mCvv = getCvv()
        mPresenter.attachView(this, isNewActivity)
    }

    override fun onStop() {
        super.onStop()
        mPresenter.detachView()
    }

    private fun getCvv(): String? {
        return if (shouldResetCvv) "" else intent.getStringExtra(EXTRA_CARD_CVV)
    }

    companion object Navigator {

        val EXTRA_CARD_CVV = "extra_card_cvv"

        fun open(context: Context, pin: String) {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(EXTRA_CARD_CVV, pin)
            context.startActivity(intent)
        }
    }
}