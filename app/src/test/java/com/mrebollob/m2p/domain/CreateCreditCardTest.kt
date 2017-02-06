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

import com.mrebollob.m2p.domain.datasources.DbDataSource
import com.mrebollob.m2p.domain.exceptions.InvalidCreditCardException
import com.mrebollob.m2p.domain.executor.PostExecutionThread
import com.mrebollob.m2p.domain.executor.ThreadExecutor
import com.mrebollob.m2p.domain.interactor.CreateCreditCard
import com.mrebollob.m2p.utils.mock
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.mockito.Mockito

class CreateCreditCardTest {

    @Rule @JvmField var expectedException = ExpectedException.none()

    private lateinit var createCreditCard: CreateCreditCard

    private val mockDbDataSource: DbDataSource = mock()
    private val mockThreadExecutor: ThreadExecutor = mock()
    private val mockPostExecutionThread: PostExecutionThread = mock()

    @Before
    fun setUp() {
        createCreditCard = CreateCreditCard(mockDbDataSource, mockThreadExecutor, mockPostExecutionThread)
    }

    @Test
    fun shouldCreateACreditCard() {
        val number = "4222222222222222"
        val expDate = "08/21"

        createCreditCard.buildInteractorObservable(
                CreateCreditCard.Params.newCreditCard(number, expDate))

        Mockito.verify(mockDbDataSource).createCreditCard(number, expDate)
        Mockito.verifyNoMoreInteractions(mockDbDataSource)
        Mockito.verifyZeroInteractions(mockPostExecutionThread)
        Mockito.verifyZeroInteractions(mockThreadExecutor)
    }

    @Test
    fun shouldFailWithEmptyNumber() {
        val number = ""
        val expDate = "08/21"

        expectedException.expect(InvalidCreditCardException::class.java)

        createCreditCard.buildInteractorObservable(
                CreateCreditCard.Params.newCreditCard(number, expDate))
    }

    @Test
    fun shouldFailWithEmptyExpDate() {
        val number = "4222222222222222"
        val expDate = ""

        expectedException.expect(InvalidCreditCardException::class.java)

        createCreditCard.buildInteractorObservable(
                CreateCreditCard.Params.newCreditCard(number, expDate))
    }
}