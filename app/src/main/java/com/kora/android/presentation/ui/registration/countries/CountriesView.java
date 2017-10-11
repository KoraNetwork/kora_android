package com.kora.android.presentation.ui.registration.countries;

import com.kora.android.presentation.model.CountryEntity;
import com.kora.android.presentation.ui.base.view.BaseView;

import java.util.List;

public interface CountriesView extends BaseView<CountriesPresenter> {

    void showCountries(final List<CountryEntity> countryEntityList);
}
