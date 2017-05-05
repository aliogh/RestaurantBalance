/*
 * Copyright (c) 2017. Manuel Rebollo Báez
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.mrebollob.m2p.presentation.view.form

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.mrebollob.m2p.R
import com.mrebollob.m2p.presentation.presenter.form.FormPresenter
import com.mrebollob.m2p.presentation.view.BaseActivity
import com.mrebollob.m2p.presentation.view.balance.BalanceActivity
import com.mrebollob.m2p.utils.analytics.AnalyticsHelper
import com.mrebollob.m2p.utils.creditcard.CardNumberTextWatcher
import com.mrebollob.m2p.utils.creditcard.CreditCardTextWatcher
import com.mrebollob.m2p.utils.creditcard.ExpDateTextWatcher
import kotlinx.android.synthetic.main.activity_form.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject


class FormActivity : BaseActivity(), FormMvpView, CreditCardTextWatcher.CardActionListener {

    @Inject lateinit var mPresenter: FormPresenter
    @Inject lateinit var mAnalyticsHelper: AnalyticsHelper
    var isNewActivity = false
    var expDateTextWatcher: ExpDateTextWatcher? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)
        initializeDependencyInjector()
        isNewActivity = (savedInstanceState == null)

        initUI()
        mAnalyticsHelper.logContentView("Credit card input view", "Input", "form-view", "Is new", "todo")
    }

    private fun initUI() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener({ onBackPressed() })

        showInputMethod(numberEt)

        val cardNumberTextWatcher = CardNumberTextWatcher(numberEt, this)
        numberEt.addTextChangedListener(cardNumberTextWatcher)
        expDateTextWatcher = ExpDateTextWatcher(this, expDateEt, this)
        expDateEt.addTextChangedListener(expDateTextWatcher)
    }

    fun showInputMethod(view: View) {
        Handler().postDelayed({
            view.isFocusableInTouchMode = true
            view.requestFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(numberEt, InputMethodManager.SHOW_IMPLICIT)
        }, 200)
    }

    private fun initializeDependencyInjector() {
        appComponent.inject(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_done, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_done) {
            updateCreditCard()
            return true
        } else {
            return super.onOptionsItemSelected(item)
        }
    }

    override fun onComplete(index: Int) {
        if (index == 0) {
            expDateTextWatcher?.focus()
        }
    }

    private fun updateCreditCard() {
        mPresenter.updateCreditCard(numberEt.text.toString().replace(" ", ""),
                expDateEt.text.toString())
    }

    override fun showCreditCard(number: String?, expDate: String?) {
        numberEt.setText(number)
        expDateEt.setText(expDate)

        if (number.isNullOrBlank()) {
            title = getString(R.string.new_card)
        } else {
            title = getString(R.string.edit_card)
        }
    }

    override fun showCardBalanceView() {
        val returnIntent = Intent()
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

    override fun showInvalidCreditCardError() {
        Snackbar.make(numberEt, R.string.invalid_card, Snackbar.LENGTH_LONG).show()
    }

    override fun showError() {
        Snackbar.make(numberEt, R.string.error, Snackbar.LENGTH_LONG).show()
    }

    override fun onStart() {
        super.onStart()
        mPresenter.mCardId = getCardId()
        mPresenter.attachView(this, isNewActivity)
    }

    override fun onStop() {
        super.onStop()
        mPresenter.detachView()
    }

    private fun getCardId(): String {
        return intent.getStringExtra(BalanceActivity.EXTRA_CARD_ID)
    }

    companion object Navigator {

        val EXTRA_CARD_ID = "extra_card_id"

        fun open(context: Context) {
            val intent = Intent(context, FormActivity::class.java)
            intent.putExtra(EXTRA_CARD_ID, "")
            context.startActivity(intent)
        }

        fun open(context: Context, cardId: String) {
            val intent = Intent(context, FormActivity::class.java)
            intent.putExtra(EXTRA_CARD_ID, cardId)
            context.startActivity(intent)
        }
    }
}