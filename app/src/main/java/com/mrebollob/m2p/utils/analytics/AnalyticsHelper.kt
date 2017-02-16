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

package com.mrebollob.m2p.utils.analytics

import com.crashlytics.android.answers.Answers
import com.crashlytics.android.answers.ContentViewEvent
import com.crashlytics.android.answers.CustomEvent
import com.crashlytics.android.answers.InviteEvent


class AnalyticsHelper constructor(private val answers: Answers) {

    fun logEvent(customEvent: CustomEvent) {
        answers.logCustom(customEvent)
    }

    fun logInvite() {
        answers.logInvite(InviteEvent())
    }

    fun logContentView(name: String, type: String, id: String) {
        answers.logContentView(ContentViewEvent()
                .putContentName(name)
                .putContentType(type)
                .putContentId(id))
    }

    fun logContentView(name: String, type: String, id: String, key: String, value: String) {
        answers.logContentView(ContentViewEvent()
                .putContentName(name)
                .putContentType(type)
                .putContentId(id)
                .putCustomAttribute(key, value))
    }
}