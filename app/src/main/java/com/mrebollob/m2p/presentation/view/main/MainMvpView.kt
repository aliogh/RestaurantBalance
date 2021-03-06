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

import com.mrebollob.m2p.domain.entities.CreditCard
import com.mrebollob.m2p.domain.entities.CreditCardBalance
import com.mrebollob.m2p.presentation.view.MvpView


interface MainMvpView : MvpView {

    fun showCreditCard(creditCard: CreditCard)

    fun showCardBalance(creditCardBalance: CreditCardBalance)

    fun showEmptyCreditCard()

    fun showError(error: String)

    fun showCreditCardForm(number: String?, expDate: String?)

    fun showLockScreen()

    fun showLoading()

    fun hideLoading()
}