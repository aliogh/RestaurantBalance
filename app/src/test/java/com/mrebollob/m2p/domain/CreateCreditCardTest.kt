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

package com.mrebollob.m2p.domain

import com.mrebollob.m2p.domain.datasources.DbDataSource
import com.mrebollob.m2p.domain.entities.CreditCard
import com.mrebollob.m2p.domain.executor.PostExecutionThread
import com.mrebollob.m2p.domain.executor.ThreadExecutor
import com.mrebollob.m2p.domain.interactor.CreateCreditCard
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Test
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.Mockito.verifyZeroInteractions


class CreateCreditCardTest {

    private val mockDbDataSource = mock<DbDataSource> { }
    private val mockThreadExecutor = mock<ThreadExecutor> { }
    private val mockPostExecutionThread = mock<PostExecutionThread> { }
    private var createCreditCard = CreateCreditCard(mockDbDataSource, mockThreadExecutor, mockPostExecutionThread)


    @Test
    fun shouldBuildCreditCardUseCaseObservable() {
        val creditCard = CreditCard("TEST_CARD", "4242424242424242", "10/21", "111")

        createCreditCard.buildUseCaseObservable(creditCard)

        verify(mockDbDataSource).createCreditCard(creditCard)
        verifyNoMoreInteractions(mockDbDataSource)
        verifyZeroInteractions(mockPostExecutionThread)
        verifyZeroInteractions(mockThreadExecutor)
    }
}