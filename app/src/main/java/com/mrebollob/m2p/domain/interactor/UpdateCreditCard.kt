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

package com.mrebollob.m2p.domain.interactor

import com.mrebollob.m2p.domain.datasources.DbDataSource
import com.mrebollob.m2p.domain.entities.Color
import com.mrebollob.m2p.domain.entities.CreditCard
import com.mrebollob.m2p.domain.executor.PostExecutionThread
import com.mrebollob.m2p.domain.executor.ThreadExecutor
import io.reactivex.Observable
import javax.inject.Inject

class UpdateCreditCard @Inject constructor(val dbDataSource: DbDataSource,
                                           threadExecutor: ThreadExecutor,
                                           postExecutionThread: PostExecutionThread)
    : AbstractInteractor<CreditCard, UpdateCreditCard.Params>(threadExecutor, postExecutionThread) {

    override fun buildInteractorObservable(params: Params): Observable<CreditCard> {

        return dbDataSource.updateCard(
                localId = params.localId,
                number = params.number,
                expDate = params.expDate,
                color = params.color,
                position = params.position)
    }

    class Params private constructor(val localId: String, val number: String, val expDate: String,
                                     val color: Color, val position: Long) {
        companion object {
            fun updated(localId: String, number: String, expDate: String, color: Color, position: Long): Params {
                return Params(localId, number, expDate, color, position)
            }
        }
    }
}