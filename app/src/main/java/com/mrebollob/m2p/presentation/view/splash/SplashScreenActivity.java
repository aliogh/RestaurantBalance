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

package com.mrebollob.m2p.presentation.view.splash;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.TypedValue;
import android.view.ViewGroup;

import com.mrebollob.m2p.R;
import com.mrebollob.m2p.databinding.ActivitySplashScreenBinding;
import com.mrebollob.m2p.domain.entities.CreditCard;
import com.mrebollob.m2p.presentation.view.splash.adapter.CreditCardListAdapter;
import com.mrebollob.m2p.utils.itemanimator.SlideInUpDelayedAnimator;

import java.util.ArrayList;
import java.util.List;


public class SplashScreenActivity extends AppCompatActivity {

    private int mContentViewHeight;

    private ActivitySplashScreenBinding mBinding;
    private CreditCardListAdapter mAdapter = new CreditCardListAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onFakeCreate();
            }
        }, getResources().getInteger(android.R.integer.config_mediumAnimTime));
    }

    private void onFakeCreate() {
        setTheme(R.style.AppTheme);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen);

        ViewCompat.animate(mBinding.textTitle)
                .alpha(1)
                .start();

        mBinding.recycler.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recycler.setItemAnimator(SlideInUpDelayedAnimator.Factory.create());
        mBinding.recycler.setAdapter(mAdapter);

        mBinding.toolbar.post(new Runnable() {
            @Override
            public void run() {
                mContentViewHeight = mBinding.toolbar.getHeight();
                startCollapseToolbarAnimation(new Runnable() {
                    @Override
                    public void run() {

                        List<CreditCard> creditCards = new ArrayList<>();
                        creditCards.add(new CreditCard(1, "424242424242424242", "12/21", "452"));
                        creditCards.add(new CreditCard(2, "424242424242424241", "12/21", "452"));

                        mAdapter.setCreditCards(creditCards);

                        // Animate fab
                        ViewCompat.animate(mBinding.fab)
                                .setStartDelay(getResources().getInteger(android.R.integer.config_mediumAnimTime))
                                .setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime))
                                .scaleY(1)
                                .scaleX(1)
                                .start();
                    }
                });
            }
        });
    }

    private void startCollapseToolbarAnimation(final Runnable onCollapseEnd) {
        final ValueAnimator valueHeightAnimator = ValueAnimator
                .ofInt(mContentViewHeight, getToolbarHeight(this));

        valueHeightAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                ViewGroup.LayoutParams lp = mBinding.toolbar.getLayoutParams();
                lp.height = (Integer) animation.getAnimatedValue();
                mBinding.toolbar.setLayoutParams(lp);
            }
        });

        valueHeightAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                onCollapseEnd.run();
            }
        });

        valueHeightAnimator.start();
    }

    private static int getToolbarHeight(Context context) {
        final TypedValue tv = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true);
        return TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
    }
}