package com.kora.android.presentation.ui.registration.countries;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.kora.android.R;
import com.kora.android.common.utils.ViewUtils;
import com.kora.android.di.component.ActivityComponent;
import com.kora.android.presentation.model.Country;
import com.kora.android.presentation.ui.base.view.BaseActivity;
import com.kora.android.presentation.ui.registration.countries.adapter.CountryAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

import static com.kora.android.common.Keys.SelectCountry.SELECT_COUNTRY_EXTRA;

public class CountriesActivity extends BaseActivity<CountriesPresenter> implements CountriesView, CountryAdapter.OnItemClickListener, TextWatcher {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.edit_text_search)
    EditText mEtSearch;
    @BindView(R.id.recycler_view_countries)
    RecyclerView mRvCountries;

    @Inject
    public CountryAdapter mCountryAdapter;


    public static Intent getLaunchIntent(final BaseActivity baseActivity) {
        return new Intent(baseActivity, CountriesActivity.class);
    }

    @Override
    public void injectToComponent(final ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_registration_countries;
    }

    @Override
    protected void onViewReady(final Bundle savedInstanceState) {
        setToolbar(mToolbar, R.drawable.ic_back_grey);

        initUI();
        getPresenter().startGetCountriesTask();
    }

    private void initUI() {
        mEtSearch.addTextChangedListener(this);

        mRvCountries.setHasFixedSize(true);
        mRvCountries.setLayoutManager(new LinearLayoutManager(this));
        mRvCountries.setAdapter(mCountryAdapter);

        mCountryAdapter.setOnItemClickListener(this);
    }

    @Override
    public void showCountries(final List<Country> countryList) {
        mCountryAdapter.addAll(countryList);
    }

    @Override
    public void beforeTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {

    }

    @Override
    public void onTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {
        mCountryAdapter.getFilter().filter(charSequence.toString());
    }

    @Override
    public void afterTextChanged(final Editable editable) {

    }

    @Override
    public void onClickSelectCounrty(final Country country, final int position) {
        ViewUtils.hideKeyboard(this);
        final Intent intent = new Intent();
        intent.putExtra(SELECT_COUNTRY_EXTRA, country);
        setResult(RESULT_OK, intent);
        finish();
    }
}
