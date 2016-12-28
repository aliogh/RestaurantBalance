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

package com.mrebollob.m2p.presentation.presenter.main

import com.mrebollob.m2p.data.datasources.network.NetworkDataSourceImp
import com.mrebollob.m2p.domain.datasources.DbDataSource
import com.mrebollob.m2p.domain.entities.CreditCard
import com.mrebollob.m2p.domain.exceptions.GetBalanceException
import com.mrebollob.m2p.presentation.presenter.Presenter
import com.mrebollob.m2p.presentation.view.main.MainMvpView
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject


class MainPresenter @Inject constructor(val networkDataSource: NetworkDataSourceImp,
                                        val dbDataSource: DbDataSource) : Presenter<MainMvpView> {

    val mSubscriptions = CompositeSubscription()
    var mView: MainMvpView? = null
    var mCreditCard: CreditCard? = null

    override fun attachView(view: MainMvpView) {
        mView = view
        getCreditCard()
    }

    fun update() {
        if (mCreditCard != null) getBalance(mCreditCard as CreditCard)
    }

    fun getCreditCard() {
        val subscription = dbDataSource.getCreditCard()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ creditCard ->
                    mCreditCard = creditCard
                    mView?.showCreditCard(creditCard)
                    getBalance(creditCard)
                }, { e ->
                    mView?.showError("No se")
                })
        mSubscriptions.add(subscription)
    }

    fun getBalance(creditCard: CreditCard) {
        mView?.showLoading()
        val subscription = networkDataSource.getCreditCardBalance(creditCard)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ creditCardBalance ->
                    mView?.hideLoading()
                    mView?.showCardBalance(creditCardBalance)
                }, { e ->
                    mView?.hideLoading()
                    if (e is GetBalanceException && !e.error.isEmpty()) {
                        mView?.showError(e.error)
                    } else {
                        mView?.showError("Unknown error")
                    }
                })
        mSubscriptions.add(subscription)
    }

    override fun detachView() {
        mSubscriptions.clear()
    }
}