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

import com.mrebollob.m2p.domain.executor.PostExecutionThread
import com.mrebollob.m2p.domain.executor.ThreadExecutor
import com.mrebollob.m2p.domain.interactor.UseCase
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import io.reactivex.Observable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.TestScheduler
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test


class UseCaseTest {

    private var useCase: UseCaseTestClass? = null
    private val testObserver = TestDisposableObserver<Any>()

    @Before
    fun setUp() {
        val mockThreadExecutor = mock<ThreadExecutor> { }
        val mockPostExecutionThread = mock<PostExecutionThread> {
            on { getScheduler() } doReturn TestScheduler()
        }

        useCase = UseCaseTestClass(mockThreadExecutor, mockPostExecutionThread)
    }

    @Test
    fun shouldReturnCorrectResult() {
        useCase!!.execute(testObserver, Any())

        assertThat(testObserver.valuesCount).isZero()
    }

    @Test
    fun shouldDisposeObserver() {
        useCase!!.execute(testObserver, Any())
        useCase!!.dispose()

        assertThat(testObserver.isDisposed).isTrue()
    }

    private class UseCaseTestClass internal constructor(threadExecutor: ThreadExecutor,
                                                        postExecutionThread: PostExecutionThread)
        : UseCase<Any, Any>(threadExecutor, postExecutionThread) {

        override fun buildUseCaseObservable(params: Any): Observable<Any> {
            return Observable.empty()
        }

        override fun execute(observer: DisposableObserver<Any>, params: Any) {
            super.execute(observer, params)
        }
    }

    private class TestDisposableObserver<T> : DisposableObserver<T>() {
        var valuesCount = 0

        override fun onNext(value: T) {
            valuesCount++
        }

        override fun onError(e: Throwable) {
            // no-op by default.
        }

        override fun onComplete() {
            // no-op by default.
        }
    }
}

