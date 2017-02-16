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

package com.mrebollob.m2p.presentation.di.components

import android.app.Application
import com.mrebollob.m2p.domain.datasources.NetworkDataSource
import com.mrebollob.m2p.presentation.di.modules.AppModule
import com.mrebollob.m2p.presentation.di.modules.DbModule
import com.mrebollob.m2p.presentation.di.modules.NetworkModule
import com.mrebollob.m2p.presentation.view.form.FormActivity
import com.mrebollob.m2p.presentation.view.lock.LockActivity
import com.mrebollob.m2p.presentation.view.main.MainActivity
import dagger.Component
import okhttp3.OkHttpClient
import javax.inject.Singleton
import com.mrebollob.m2p.utils.analytics.AnalyticsHelper



@Singleton
@Component(modules = arrayOf(AppModule::class, NetworkModule::class, DbModule::class))
interface AppComponent {

    fun inject(activity: LockActivity)

    fun inject(activity: MainActivity)

    fun inject(activity: FormActivity)

    fun app(): Application

    fun networkDataSource(): NetworkDataSource

    fun okHttpClient(): OkHttpClient

    fun analyticsHelper(): AnalyticsHelper
}