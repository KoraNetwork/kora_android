package com.kora.android.presentation.ui.registration.currencies;

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
import com.kora.android.injection.component.ActivityComponent;
import com.kora.android.presentation.model.Country;
import com.kora.android.presentation.ui.base.view.BaseActivity;
import com.kora.android.presentation.ui.registration.currencies.adapter.CurrencyAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

import static com.kora.android.common.Keys.SelectCurrency.SELECT_CURRENCY_EXTRA;

public class CurrenciesActivity extends BaseActivity<CurrenciesPresenter> implements CurrenciesView, CurrencyAdapter.OnItemClickListener, TextWatcher {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.edit_text_search)
    EditText mEtSearch;
    @BindView(R.id.recycler_view_currencies)
    RecyclerView mRvCurrencies;

    @Inject
    public CurrencyAdapter mCurrencyAdapter;


    public static Intent getLaunchIntent(final BaseActivity baseActivity) {
        return new Intent(baseActivity, CurrenciesActivity.class);
    }

    @Override
    public void injectToComponent(final ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_registration_currencies;
    }

    @Override
    protected void onViewReady(final Bundle savedInstanceState) {
        setToolbar(mToolbar, R.drawable.ic_back_grey);

        initUI();
        getPresenter().startGetCountriesTask();
    }

    private void initUI() {
        mEtSearch.addTextChangedListener(this);

        mRvCurrencies.setHasFixedSize(true);
        mRvCurrencies.setLayoutManager(new LinearLayoutManager(this));
        mRvCurrencies.setAdapter(mCurrencyAdapter);

        mCurrencyAdapter.setOnItemClickListener(this);
    }

    @Override
    public void showCurrencies(final List<Country> countryList) {
        mCurrencyAdapter.addAll(countryList);
    }

    @Override
    public void beforeTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {

    }

    @Override
    public void onTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {
        mCurrencyAdapter.getFilter().filter(charSequence.toString());
    }

    @Override
    public void afterTextChanged(final Editable editable) {

    }

    @Override
    public void onClickSelectCurrency(final Country country, final int position) {
        ViewUtils.hideKeyboard(this);
        final Intent intent = new Intent();
        intent.putExtra(SELECT_CURRENCY_EXTRA, country);
        setResult(RESULT_OK, intent);
        finish();
    }
}
