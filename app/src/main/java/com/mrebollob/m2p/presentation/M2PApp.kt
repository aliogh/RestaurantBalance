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

package com.mrebollob.m2p.presentation

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.mrebollob.m2p.presentation.di.components.AppComponent
import com.mrebollob.m2p.presentation.di.components.DaggerAppComponent
import com.mrebollob.m2p.presentation.di.modules.AppModule
import io.fabric.sdk.android.Fabric


class M2PApp : Application() {

    companion object {
        lateinit var mAppComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        initializeInjector()
        initializeCrashlytics()
    }

    private fun initializeInjector() {
        mAppComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }

    private fun initializeCrashlytics() {
        Fabric.with(this, Crashlytics())
    }

    fun getAppComponent(): AppComponent {
        return mAppComponent
    }
}