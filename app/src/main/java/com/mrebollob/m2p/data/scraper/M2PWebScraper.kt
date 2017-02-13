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

package com.mrebollob.m2p.data.scraper

import com.mrebollob.m2p.domain.entities.CreditCardBalance
import com.mrebollob.m2p.domain.entities.CreditCardMovement
import org.jsoup.Jsoup
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * https://jsoup.org/cookbook/extracting-data/selector-syntax
 */
@Singleton
class M2PWebScraper @Inject constructor() {

    val DATE_FORMAT = SimpleDateFormat("dd/MM/yyyy", Locale.US)

    fun getFormUrl(html: String): String {
        val document = Jsoup.parse(html)

        val linkElements = document.select("div.row.registro > div.col-md-6 > div.box > a[href]")

        linkElements.map { it.attr("abs:href") }
                .filter { it.contains("movements") }
                .forEach { return it }

        throw  RuntimeException("Link not found")
    }

    fun getPostFormUrl(html: String): String {
        val document = Jsoup.parse(html)

        val form = document.select("form#activation-form").first()

        return form.attr("action")
    }

    fun getCardBalance(html: String): CreditCardBalance {
        val document = Jsoup.parse(html)

        val infoElements = document.select("div.col-xs-12.usuario-acciones-bloque > div.col-xs-12.col-sm-3")
        val movements = getMovements(html)

        infoElements.filter { it.select("p.usuario-acciones-bloque-title").text().contains("Saldo") }
                .map { it.text() }
                .forEach { return CreditCardBalance(getFloatFromString(it), movements) }

        throw  RuntimeException("Data not found")
    }

    fun getMovements(html: String): List<CreditCardMovement> {
        val document = Jsoup.parse(html)
        val infoElements = document.select("div.col-xs-12.usuario-acciones-bloque.tabla")
        val nameElements = infoElements.select("div.col-xs-5")[0].children()
        val dateElements = infoElements.select("div.col-xs-3")[0].children()
        val valueElements = infoElements.select("div.col-xs-3")[1].children()

        val creditCardMovement: MutableList<CreditCardMovement> = ArrayList()

        nameElements.forEachIndexed { i, element ->
            if (i > 0) {
                creditCardMovement.add(CreditCardMovement(
                        element.text(),
                        getDateFromString(dateElements[i].text()),
                        getFloatFromString(valueElements[i].text())))
            }
        }

        return creditCardMovement
    }

    fun getError(html: String): String {
        val document = Jsoup.parse(html)

        val infoElements = document.select("div.error-div")

        if (!infoElements.isEmpty()) {
            infoElements.map { it.text() }
                    .forEach { return it }
        }

        return ""
    }

    fun getDateFromString(text: String): Date {
        try {
            return DATE_FORMAT.parse(text)
        } catch (e: Exception) {
            return Date(0)
        }
    }

    fun getFloatFromString(text: String): Float {
        return text.replace(",", ".").replace("[^\\d.-]+|\\.(?!\\d)".toRegex(), "").toFloat()
    }
}