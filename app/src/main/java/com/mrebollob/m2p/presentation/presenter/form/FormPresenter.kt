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

package com.mrebollob.m2p.presentation.presenter.form

import com.mrebollob.m2p.domain.datasources.DbDataSource
import com.mrebollob.m2p.domain.entities.CreditCard
import com.mrebollob.m2p.presentation.presenter.Presenter
import com.mrebollob.m2p.presentation.view.form.FormMvpView
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject

class FormPresenter @Inject constructor(val dbDataSource: DbDataSource) : Presenter<FormMvpView> {

    private val mSubscriptions = CompositeSubscription()
    private var mView: FormMvpView? = null

    override fun attachView(view: FormMvpView) {
        mView = view
    }

    fun createCreditCard(creditCard: CreditCard) {
        val subscription = dbDataSource.createCreditCard(creditCard)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ creditCard ->
                    mView?.showCreditCard(creditCard)
                }, { e ->
                    mView?.showError("No se")
                })
        mSubscriptions.add(subscription)
    }

    override fun detachView() {
        mSubscriptions.clear()
    }
}