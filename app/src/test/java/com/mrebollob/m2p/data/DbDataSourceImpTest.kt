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

package com.mrebollob.m2p.data

import android.content.SharedPreferences
import com.mrebollob.m2p.data.datasources.db.DbDataSourceImp
import com.mrebollob.m2p.domain.entities.CreditCard
import com.mrebollob.m2p.domain.exceptions.NoCreditCardException
import com.mrebollob.m2p.utils.encryption.Encryptor
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import io.reactivex.observers.TestObserver
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.mockito.Mockito

class DbDataSourceImpTest {

    @Rule @JvmField var expectedException = ExpectedException.none()

    private val CREDIT_CARD_NUMBER = "credit_card_number"
    private val CREDIT_CARD_EXP_DATE = "credit_card_exp_date"

    @Test
    fun shouldGetCreditCardFromSharedPreferences() {
        val mockSharedPreferences = getSharedPreferences(getEditor())
        val testObserver = TestObserver<CreditCard>()
        val dbDataSource = DbDataSourceImp(mockSharedPreferences, getEncryptor())

        dbDataSource.getCreditCard().subscribe(testObserver)

        testObserver.assertNoErrors()
        Mockito.verify(mockSharedPreferences).getString(CREDIT_CARD_NUMBER, "")
        Mockito.verify(mockSharedPreferences).getString(CREDIT_CARD_EXP_DATE, "")
        Mockito.verifyNoMoreInteractions(mockSharedPreferences)
    }

    @Test
    fun shouldGetNoCreditCardException() {
        val mockSharedPreferences = getEmptySharedPreferences()
        val testObserver = TestObserver<CreditCard>()
        val dbDataSource = DbDataSourceImp(mockSharedPreferences, getEncryptor())

        dbDataSource.getCreditCard().subscribe(testObserver)

        testObserver.assertError(NoCreditCardException::class.java)
        Mockito.verify(mockSharedPreferences).getString(CREDIT_CARD_NUMBER, "")
        Mockito.verify(mockSharedPreferences).getString(CREDIT_CARD_EXP_DATE, "")
        Mockito.verifyNoMoreInteractions(mockSharedPreferences)
    }

    @Test
    fun shouldRemoveCreditCard() {
        val editor = getEditor()
        val mockSharedPreferences = getSharedPreferences(editor)
        val testObserver = TestObserver<Unit>()
        val dbDataSource = DbDataSourceImp(mockSharedPreferences, getEncryptor())

        dbDataSource.removeCreditCard().subscribe(testObserver)

        testObserver.assertNoErrors()
        Mockito.verify(editor).clear()
        Mockito.verify(editor).apply()
        Mockito.verifyNoMoreInteractions(editor)
    }

    @Test
    fun shouldSaveCreditCardInSharedPreferences() {
        val editor = getEditor()
        val mockSharedPreferences = getSharedPreferences(editor)
        val testObserver = TestObserver<Unit>()
        val dbDataSource = DbDataSourceImp(mockSharedPreferences, getEncryptor())

        dbDataSource.createCreditCard("4242424242424242", "08/21").subscribe(testObserver)

        testObserver.assertNoErrors()
        Mockito.verify(editor).putString(CREDIT_CARD_NUMBER, "4242424242424242")
        Mockito.verify(editor).putString(CREDIT_CARD_EXP_DATE, "08/21")
        Mockito.verify(editor).apply()
        Mockito.verifyNoMoreInteractions(editor)
    }

    private fun getSharedPreferences(editor: SharedPreferences.Editor): SharedPreferences {
        return mock {
            on { getString(CREDIT_CARD_NUMBER, "") } doReturn "4242424242424242"
            on { getString(CREDIT_CARD_EXP_DATE, "") } doReturn "08/21"
            on { edit() } doReturn editor
        }
    }

    private fun getEmptySharedPreferences(): SharedPreferences {
        return mock {
            on { getString(CREDIT_CARD_NUMBER, "") } doReturn ""
            on { getString(CREDIT_CARD_EXP_DATE, "") } doReturn ""
        }
    }

    private fun getEditor(): SharedPreferences.Editor {

        val editor = mock<SharedPreferences.Editor>()
        Mockito.`when`(editor.clear()).thenReturn(editor)
        Mockito.`when`(editor.putString(any(), any())).thenReturn(editor)

        return editor
    }

    private fun getEncryptor(): Encryptor {
        return mock {
            on { getAsHash("4242424242424242") } doReturn "4242424242424242"
            on { getUnhashed("4242424242424242") } doReturn "4242424242424242"
            on { getAsHash("08/21") } doReturn "08/21"
            on { getUnhashed("08/21") } doReturn "08/21"
            on { getAsHash("") } doReturn ""
            on { getUnhashed("") } doReturn ""
        }
    }
}