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

import com.mrebollob.m2p.domain.entities.CreditCard
import com.mrebollob.m2p.domain.entities.CreditCardBalance
import com.mrebollob.m2p.domain.exceptions.GetBalanceException
import com.mrebollob.m2p.domain.exceptions.NoCreditCardException
import com.mrebollob.m2p.domain.interactor.CreateCreditCard
import com.mrebollob.m2p.domain.interactor.DefaultObserver
import com.mrebollob.m2p.domain.interactor.GetCreditCard
import com.mrebollob.m2p.domain.interactor.GetCreditCardBalance
import com.mrebollob.m2p.presentation.presenter.Presenter
import com.mrebollob.m2p.presentation.view.main.MainMvpView
import javax.inject.Inject

class MainPresenter @Inject constructor(val getCreditCardBalance: GetCreditCardBalance,
                                        val createCreditCard: CreateCreditCard,
                                        val getCreditCard: GetCreditCard) : Presenter<MainMvpView> {

    var mView: MainMvpView? = null
    var mCreditCard: CreditCard? = null
    var mCvv: String? = null

    override fun attachView(view: MainMvpView, isNew: Boolean) {
        mView = view
        if (mCreditCard == null || isNew) {
            getCreditCard()
        }
    }

    fun onEditCreditCardClick() {
        mView?.showCreditCardForm(mCreditCard?.number, mCreditCard?.expDate)
    }

    fun update() {
        if (mCreditCard != null) getBalance(mCreditCard as CreditCard)
    }

    private fun getCreditCard() {
        getCreditCard.execute(CreditCardObserver(), Unit)
    }

    private fun getBalance(creditCard: CreditCard) {
        if (mCvv.isNullOrBlank()) {
            mView?.showLockScreen()
        } else {
            mView?.showLoading()
            getCreditCardBalance.execute(BalanceObserver(), GetCreditCardBalance.Params
                    .forCreditCard(creditCard.copy(cvv = mCvv as String)))
        }
    }

    override fun detachView() {
        getCreditCardBalance.dispose()
        createCreditCard.dispose()
        getCreditCard.dispose()
    }

    private inner class CreditCardObserver : DefaultObserver<CreditCard>() {

        override fun onNext(value: CreditCard) {
            mCreditCard = value
            mView?.showCreditCard(value)
            getBalance(value)
        }

        override fun onComplete() {
        }

        override fun onError(e: Throwable?) {
            if (e is NoCreditCardException) {
                mView?.showEmptyCreditCard()
            } else {
                mView?.showError("Unknown error")
            }
        }
    }

    private inner class BalanceObserver : DefaultObserver<CreditCardBalance>() {

        override fun onNext(value: CreditCardBalance) {
            mView?.showCardBalance(value)
        }

        override fun onComplete() {
            mView?.hideLoading()
        }

        override fun onError(e: Throwable?) {
            mView?.hideLoading()
            if (e is GetBalanceException && !e.error.isEmpty()) {
                mView?.showError(e.error)
            } else {
                mView?.showError("Unknown error")
            }
        }
    }
}