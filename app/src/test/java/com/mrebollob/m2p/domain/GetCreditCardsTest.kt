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
import com.mrebollob.m2p.domain.datasources.DbDataSource
import com.mrebollob.m2p.domain.executor.PostExecutionThread
import com.mrebollob.m2p.domain.executor.ThreadExecutor
import com.mrebollob.m2p.domain.interactor.GetCreditCards
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.mockito.Mockito.*


class GetCreditCardsTest {

    @Rule @JvmField var expectedException = ExpectedException.none()

    private lateinit var mGetCreditCards: GetCreditCards

    private val mockDbDataSource: DbDataSource = mock()
    private val mockThreadExecutor: ThreadExecutor = mock()
    private val mockPostExecutionThread: PostExecutionThread = mock()

    @Before
    fun setUp() {
        mGetCreditCards = GetCreditCards(mockDbDataSource, mockThreadExecutor, mockPostExecutionThread)
    }

    @Test
    fun shouldGetACreditCard() {
        mGetCreditCards.buildInteractorObservable(Unit)

        verify(mockDbDataSource).getCreditCard()
        verifyNoMoreInteractions(mockDbDataSource)
        verifyZeroInteractions(mockPostExecutionThread)
        verifyZeroInteractions(mockThreadExecutor)
    }
}