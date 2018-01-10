package com.kora.android.presentation.ui.registration.step4;

import com.kora.android.presentation.model.CountryEntity;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.base.view.BaseView;

import java.io.File;

public interface FourthStepView extends BaseView<FourthStepPresenter> {

    void showCurrency(final CountryEntity countryEntity);
    void showDateOfBirth(final String dateOfBirth);

    void showAvatar(final File file);

    void showEmptyUserName();
    void showIncorrectUserName();

    void showIncorrectFullName();

    void showEmptyEmail();
    void showIncorrectEmail();

    void  showIncorrectDate();

    void showEmptyPassword();
    void showIncorrectPassword();

    void showEmptyConfirmPassword();
    void showIncorrectConfirmPassword();

    void showNextScreen();
}
