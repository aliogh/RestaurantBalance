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

package com.mrebollob.m2p.presentation.presenter.main

import com.mrebollob.m2p.presentation.presenter.Presenter
import com.mrebollob.m2p.presentation.view.main.MainMvpView


class MainPresenter : Presenter<MainMvpView> {
    override fun attachView(view: MainMvpView) {
        throw UnsupportedOperationException("not implemented")
    }

    override fun detachView() {
        throw UnsupportedOperationException("not implemented")
    }
}