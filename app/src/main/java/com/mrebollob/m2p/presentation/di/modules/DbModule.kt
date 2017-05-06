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

package com.mrebollob.m2p.presentation.di.modules

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.mrebollob.m2p.BuildConfig
import com.mrebollob.m2p.data.datasources.db.DbDataSourceImp
import com.mrebollob.m2p.domain.datasources.DbDataSource
import com.mrebollob.m2p.presentation.di.qualifiers.DataBaseName
import com.mrebollob.m2p.presentation.di.qualifiers.SharedPreferencesName
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class DbModule {

    @Provides
    @Singleton
    fun provideDbDataSource(dbDataSource: DbDataSourceImp): DbDataSource {
        return dbDataSource
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(application: Application, @SharedPreferencesName sharedPreferencesName: String)
            : SharedPreferences {
        return application.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    @SharedPreferencesName
    fun provideSharedPreferencesName(): String {
        return "M2P" + if (BuildConfig.DEBUG) "-dev" else ""
    }

    @Provides
    @Singleton
    @DataBaseName
    fun provideDataBaseName(): String {
        return "M2P" + if (BuildConfig.DEBUG) "-dev" else ""
    }
}