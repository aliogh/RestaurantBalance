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

package com.mrebollob.m2p.data

import com.mrebollob.m2p.data.scraper.M2PWebScraper
import org.assertj.core.api.Assertions
import org.junit.Test

class M2PWebScraperTest {

    @Test
    fun shouldGetFloatFromString() {

        val m2PWebScraper = M2PWebScraper()
        val testString = "saldo: 1,1 €"

        val testFloat = m2PWebScraper.getFloatFromString(testString)

        Assertions.assertThat(testFloat).isEqualTo(1.1f)
    }

    @Test
    fun shouldGetNegativeFloatFromString() {

        val m2PWebScraper = M2PWebScraper()
        val testString = "saldo: -1,1 €"

        val testFloat = m2PWebScraper.getFloatFromString(testString)

        Assertions.assertThat(testFloat).isEqualTo(-1.1f)
    }
}