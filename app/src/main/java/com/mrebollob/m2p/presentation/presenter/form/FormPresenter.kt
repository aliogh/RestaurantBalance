/*
 * Copyright (c) 2017. Manuel Rebollo Báez
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.mrebollob.m2p.presentation.presenter.form

import com.mrebollob.m2p.domain.exceptions.InvalidCreditCardException
import com.mrebollob.m2p.domain.interactor.CreateCreditCard
import com.mrebollob.m2p.domain.interactor.DefaultObserver
import com.mrebollob.m2p.presentation.presenter.Presenter
import com.mrebollob.m2p.presentation.view.form.FormMvpView
import javax.inject.Inject

class FormPresenter @Inject constructor(private val createCreditCard: CreateCreditCard)
    : Presenter<FormMvpView> {

    private var mView: FormMvpView? = null
    var mCardNumber: String? = null
    var mCardExpDate: String? = null

    override fun attachView(view: FormMvpView, isNew: Boolean) {
        mView = view
        mView?.showCreditCard(mCardNumber, mCardExpDate)
    }

    fun updateCreditCard(number: String, expDate: String) {
        createCreditCard.execute(CreateCreditCardObserver(),
                CreateCreditCard.Params.newCreditCard(number, expDate))
    }

    override fun detachView() {
        createCreditCard.dispose()
    }

    private inner class CreateCreditCardObserver : DefaultObserver<Unit>() {

        override fun onNext(value: Unit) {
        }

        override fun onComplete() {
            mView?.showCardBalanceView()
        }

        override fun onError(e: Throwable?) {
            if (e is InvalidCreditCardException) {
                mView?.showInvalidCreditCardError()
            } else {
                mView?.showError()
            }
        }
    }
}