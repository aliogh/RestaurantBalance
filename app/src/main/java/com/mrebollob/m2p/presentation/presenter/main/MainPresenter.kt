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
import com.mrebollob.m2p.domain.entities.CreditCard
import com.mrebollob.m2p.presentation.presenter.Presenter
import com.mrebollob.m2p.presentation.view.main.MainMvpView
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject


class MainPresenter @Inject constructor(private val networkDataSource: NetworkDataSourceImp) : Presenter<MainMvpView> {

    private val mSubscriptions = CompositeSubscription()
    private var mView: MainMvpView? = null

    override fun attachView(view: MainMvpView) {
        mView = view
    }

    fun showBalance(creditCard: CreditCard) {
        val subscription = networkDataSource.getCreditCardBalance(creditCard)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ creditCardBalance ->
                    mView?.showCardBalance(creditCardBalance)
                }, { e ->
                    mView?.showError("No se")
                })
        mSubscriptions.add(subscription)
    }

    override fun detachView() {
        mSubscriptions.clear()
    }
}