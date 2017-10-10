package com.kora.android.presentation.ui.registration.countries;

import com.kora.android.presentation.model.Country;
import com.kora.android.presentation.ui.base.view.BaseView;

import java.util.List;

public interface CountriesView extends BaseView<CountriesPresenter> {

    void showCountries(final List<Country> countryList);
}
