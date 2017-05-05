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

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.mrebollob.m2p.R
import com.mrebollob.m2p.databinding.ActivityMainBinding
import com.mrebollob.m2p.domain.entities.CreditCard
import com.mrebollob.m2p.presentation.presenter.main.MainPresenter
import com.mrebollob.m2p.presentation.view.BaseActivity
import com.mrebollob.m2p.presentation.view.form.FormActivity
import com.mrebollob.m2p.presentation.view.main.adapter.CreditCardListAdapter
import com.mrebollob.m2p.utils.analytics.AnalyticsHelper
import com.mrebollob.m2p.utils.extensions.toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject


class MainActivity : BaseActivity(), MainMvpView {

    val creditCardsAdapter = CreditCardListAdapter()
    var isNewActivity = false
    @Inject lateinit var mPresenter: MainPresenter
    @Inject lateinit var mAnalyticsHelper: AnalyticsHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initializeDependencyInjector()
        isNewActivity = (savedInstanceState == null)
        initUI(binding)

        mAnalyticsHelper.logContentView("Credit card balance view", "Output", "main-view")
    }

    private fun initializeDependencyInjector() {
        appComponent.inject(this)
    }

    private fun initUI(binding: ActivityMainBinding) {
        setSupportActionBar(toolbar)
        initRecyclerView()

        binding.presenter = mPresenter
    }

    private fun initRecyclerView() {
        cardsRecyclerView.layoutManager = LinearLayoutManager(this)
        cardsRecyclerView.adapter = creditCardsAdapter
    }

    override fun showCreditCards(creditCards: List<CreditCard>) {
        creditCardsAdapter.creditCards = creditCards
    }

    override fun showGetCreditCardsError() {
        toast("Get Credit Cards Error")
    }

    override fun showNewCardForm() {
        FormActivity.open(this)
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