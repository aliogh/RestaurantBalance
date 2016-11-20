/*
 * Copyright (c) 2016. Manuel Rebollo Báez.
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
package com.mrebollob.m2p

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mrebollob.m2p.di.components.DaggerActivityComponent
import com.mrebollob.m2p.di.modules.ActivityModule
import com.mrebollob.m2p.extensions.inflate
import kotlinx.android.synthetic.main.fragment_card_form.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

class CardFormFragment : RxBaseFragment() {

    @Inject lateinit var m2pManager: M2PManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeDependencyInjector()
    }

    fun initializeDependencyInjector() {
        val application = activity.application as M2PApp

        DaggerActivityComponent.builder().activityModule(ActivityModule(activity))
                .appComponent(application.getAppComponent()).build().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.fragment_card_form)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        btn_show_balance.setOnClickListener { getCreditCardBalance() }
    }

    private fun getCreditCardBalance() {
        val subscription = m2pManager.getCardBalance()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { retrievedNews ->
                            Snackbar.make(et_number, retrievedNews.balance, Snackbar.LENGTH_LONG).show()
                        },
                        { e ->
                            Snackbar.make(et_number, e.message ?: "", Snackbar.LENGTH_LONG).show()
                        }
                )
        subscriptions.add(subscription)
    }
}