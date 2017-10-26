package com.kora.android.di.component;

import com.kora.android.di.annotation.PerActivity;
import com.kora.android.di.module.ActivityModule;
import com.kora.android.presentation.ui.borrow.BorrowMoneyActivity;
import com.kora.android.presentation.ui.login.LoginActivity;
import com.kora.android.presentation.ui.main.MainActivity;
import com.kora.android.presentation.ui.common.recent.RecentActivity;
import com.kora.android.presentation.ui.common.add_contact.AddContactActivity;
import com.kora.android.presentation.ui.common.enter_pin.EnterPinActivity;
import com.kora.android.presentation.ui.test.TestActivity;
import com.kora.android.presentation.ui.registration.countries.CountriesActivity;
import com.kora.android.presentation.ui.registration.currencies.CurrenciesActivity;
import com.kora.android.presentation.ui.registration.step1.FirstStepActivity;
import com.kora.android.presentation.ui.registration.step2.SecondStepActivity;
import com.kora.android.presentation.ui.registration.step3.ThirdStepActivity;
import com.kora.android.presentation.ui.registration.step4.FourthStepActivity;
import com.kora.android.presentation.ui.common.send_to.SendMoneyActivity;
import com.kora.android.presentation.ui.splash.SplashActivity;

import dagger.Subcomponent;

@PerActivity
@Subcomponent(modules = {ActivityModule.class})
public interface ActivityComponent {

    void inject(SplashActivity splashActivity);
    void inject(TestActivity testActivity);

    void inject(FirstStepActivity firstStepActivity);
    void inject(SecondStepActivity secondStepActivity);
    void inject(ThirdStepActivity thirdStepActivity);
    void inject(FourthStepActivity fourthStepActivity);

    void inject(CountriesActivity countriesActivity);
    void inject(CurrenciesActivity currenciesActivity);

    void inject(LoginActivity loginActivity);

    void inject(MainActivity mainActivity);

    void inject(AddContactActivity addContactActivity);
    void inject(EnterPinActivity enterPinActivity);
    void inject(SendMoneyActivity sendMoneyActivity);
    void inject(RecentActivity recentActivity);

    void inject(BorrowMoneyActivity borrowMoneyActivity);
}
