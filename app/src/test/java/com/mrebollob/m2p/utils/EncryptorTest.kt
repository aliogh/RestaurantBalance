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

package com.mrebollob.m2p.utils

import com.mrebollob.m2p.utils.encryption.Encryptor
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class EncryptorTest {

    @Test
    fun shouldEncryptACreditCard() {
        val number = "4222222222222222"
        val encryptor = Encryptor()

        val hash = encryptor.getAsHash(number)
        val numberUnhashed = encryptor.getUnhashed(hash)

        assertThat(numberUnhashed).isEqualTo(number)
    }
}