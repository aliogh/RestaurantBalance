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
import com.mrebollob.m2p.domain.interactor.DefaultObserver
import com.mrebollob.m2p.domain.interactor.GetCreditCards
import com.mrebollob.m2p.presentation.presenter.Presenter
import com.mrebollob.m2p.presentation.view.main.MainMvpView
import javax.inject.Inject

class MainPresenter @Inject constructor(val getCreditCards: GetCreditCards) : Presenter<MainMvpView> {

    var mView: MainMvpView? = null

    override fun attachView(view: MainMvpView, isNew: Boolean) {
        mView = view
        getCreditCards()
    }

    fun onNewCardClick() {
        mView?.showNewCardForm()
    }

    private fun getCreditCards() {
        getCreditCards.execute(CreditCardObserver(), Unit)
    }

    override fun detachView() {
        getCreditCards.dispose()
    }

    private inner class CreditCardObserver : DefaultObserver<List<CreditCard>>() {

        override fun onNext(value: List<CreditCard>) {
            mView?.showCreditCards(value)
        }

        override fun onComplete() {
            mView?.hideLoading()
        }

        override fun onError(e: Throwable?) {
            mView?.hideLoading()
            mView?.showGetCreditCardsError()
        }
    }
}