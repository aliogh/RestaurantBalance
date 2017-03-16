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

package com.mrebollob.m2p.domain

import com.mrebollob.m2p.utils.mock
import com.mrebollob.m2p.domain.datasources.NetworkDataSource
import com.mrebollob.m2p.domain.entities.CreditCard
import com.mrebollob.m2p.domain.exceptions.InvalidCreditCardException
import com.mrebollob.m2p.domain.executor.PostExecutionThread
import com.mrebollob.m2p.domain.executor.ThreadExecutor
import com.mrebollob.m2p.domain.interactor.GetCreditCardBalance
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.mockito.Mockito

class GetCreditCardsBalanceTest {

    @Rule @JvmField var expectedException = ExpectedException.none()

    private lateinit var getCreditCardBalance: GetCreditCardBalance

    private val mockNetworkDataSource: NetworkDataSource = mock()
    private val mockThreadExecutor: ThreadExecutor = mock()
    private val mockPostExecutionThread: PostExecutionThread = mock()

    @Before
    fun setUp() {
        getCreditCardBalance = GetCreditCardBalance(mockNetworkDataSource,
                mockThreadExecutor, mockPostExecutionThread)
    }

    @Test
    fun shouldCreateACreditCard() {
        val testCreditCard = CreditCard("4222222222222222", "08/21", "000")

        getCreditCardBalance.buildInteractorObservable(
                GetCreditCardBalance.Params.forCreditCard(testCreditCard))

        Mockito.verify(mockNetworkDataSource).getCreditCardBalance(testCreditCard)
        Mockito.verifyNoMoreInteractions(mockNetworkDataSource)
        Mockito.verifyZeroInteractions(mockPostExecutionThread)
        Mockito.verifyZeroInteractions(mockThreadExecutor)
    }

    @Test
    fun shouldFailWithEmptyNumber() {
        val testCreditCard = CreditCard("", "08/21", "000")

        expectedException.expect(InvalidCreditCardException::class.java)

        getCreditCardBalance.buildInteractorObservable(
                GetCreditCardBalance.Params.forCreditCard(testCreditCard))
    }

    @Test
    fun shouldFailWithEmptyExpDate() {
        val testCreditCard = CreditCard("4222222222222222", "", "000")

        expectedException.expect(InvalidCreditCardException::class.java)

        getCreditCardBalance.buildInteractorObservable(
                GetCreditCardBalance.Params.forCreditCard(testCreditCard))
    }

    @Test
    fun shouldFailWithEmptyCvv() {
        val testCreditCard = CreditCard("4222222222222222", "08/21", "")

        expectedException.expect(InvalidCreditCardException::class.java)

        getCreditCardBalance.buildInteractorObservable(
                GetCreditCardBalance.Params.forCreditCard(testCreditCard))
    }
}