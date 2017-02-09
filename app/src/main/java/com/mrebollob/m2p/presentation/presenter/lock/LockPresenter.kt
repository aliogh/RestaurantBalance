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

package com.mrebollob.m2p.presentation.presenter.lock

import com.mrebollob.m2p.domain.interactor.DefaultObserver
import com.mrebollob.m2p.domain.interactor.RemoveCreditCard
import com.mrebollob.m2p.presentation.presenter.Presenter
import com.mrebollob.m2p.presentation.view.lock.LockMvpView
import javax.inject.Inject

class LockPresenter @Inject constructor(val removeCreditCard: RemoveCreditCard)
    : Presenter<LockMvpView> {

    var mView: LockMvpView? = null

    override fun attachView(view: LockMvpView, isNew: Boolean) {
        mView = view
    }

    fun onRemoveCreditCardClick() {
        removeCreditCard.execute(RemoveCreditCardObserver(), Unit)
    }

    fun onCvvComplete(cvv: String) {
        mView?.showCreditCardBalance(cvv)
    }

    override fun detachView() {
        removeCreditCard.dispose()
    }

    private inner class RemoveCreditCardObserver : DefaultObserver<Unit>() {

        override fun onNext(value: Unit) {
        }

        override fun onComplete() {
            mView?.showCreditCardRemoved()
        }

        override fun onError(e: Throwable?) {
            mView?.showRemoveCreditCardError()
        }
    }
}