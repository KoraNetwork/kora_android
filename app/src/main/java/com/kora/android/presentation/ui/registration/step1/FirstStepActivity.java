package com.kora.android.presentation.ui.registration.step1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.Toast;

import com.kora.android.GlideApp;
import com.kora.android.R;
import com.kora.android.common.permission.PermissionCheckerActivity;
import com.kora.android.common.utils.ViewUtils;
import com.kora.android.di.component.ActivityComponent;
import com.kora.android.presentation.model.CountryEntity;
import com.kora.android.presentation.ui.base.view.BaseActivity;
import com.kora.android.presentation.ui.registration.countries.CountriesActivity;
import com.kora.android.presentation.ui.registration.step2.SecondStepActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.RECEIVE_SMS;
import static com.kora.android.common.Keys.PermissionChecker.PERMISSION_REQUEST_CODE_RECEIVE_READ_SMS;
import static com.kora.android.common.Keys.SelectCountry.SELECT_COUNTRY_EXTRA;
import static com.kora.android.common.Keys.SelectCountry.SELECT_COUNTRY_REQUEST_CODE;
import static com.kora.android.data.network.Constants.API_BASE_URL;

public class FirstStepActivity extends BaseActivity<FirstStepPresenter> implements FirstStepView {

    @Inject
    PermissionCheckerActivity mPermissionCheckerActivity;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.image_view_country_flag)
    ImageView mIvCountryFlag;
    @BindView(R.id.edit_text_phone_code)
    TextInputEditText mEtPhoneCode;
    @BindView(R.id.edit_layout_phone_number)
    TextInputLayout mElPhoneNumber;
    @BindView(R.id.edit_text_phone_number)
    TextInputEditText mEtPhoneNumber;

    public static Intent getLaunchIntent(final BaseActivity baseActivity) {
        return new Intent(baseActivity, FirstStepActivity.class);
    }

    @Override
    public void injectToComponent(final ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_registration_step_1;
    }

    @Override
    protected void onViewReady(final Bundle savedInstanceState) {
        setToolbar(mToolbar, R.drawable.ic_back_grey);

        requestPermission();
    }

    private void requestPermission() {
        mPermissionCheckerActivity.requestPermissions(PERMISSION_REQUEST_CODE_RECEIVE_READ_SMS, RECEIVE_SMS, READ_SMS);
    }

    @OnClick({R.id.edit_text_phone_code, R.id.image_view_country_flag})
    public void onClickPhoneCode() {
        startActivityForResult(CountriesActivity.getLaunchIntent(this), SELECT_COUNTRY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (requestCode == SELECT_COUNTRY_REQUEST_CODE && resultCode == RESULT_OK) {
            final CountryEntity countryEntity = data.getParcelableExtra(SELECT_COUNTRY_EXTRA);
            getPresenter().setCountry(countryEntity);
            GlideApp.with(this)
                    .load(API_BASE_URL + countryEntity.getFlag())
                    .into(mIvCountryFlag);
            mEtPhoneCode.setText(countryEntity.getPhoneCode());
            mElPhoneNumber.setError(null);
            mEtPhoneNumber.setText(null);
            ViewUtils.setMaxLength(mEtPhoneNumber, countryEntity.getPhoneCode());
        }
    }

    @OnTextChanged(R.id.edit_text_phone_number)
    void onChangedPhoneNumber(final CharSequence phoneNumber) {
        mElPhoneNumber.setError(null);
        getPresenter().setPhoneNumber(phoneNumber.toString().trim());
    }

    @Override
    public void showEmptyPhoneNumber() {
        mElPhoneNumber.setError(getString(R.string.registration_phone_number_empty));
    }

    @Override
    public void showIncorrectPhoneNumber() {
        mElPhoneNumber.setError(getString(R.string.registration_phone_number_incorrect));
    }

    @OnClick(R.id.card_view_send)
    public void onClickSend() {
        getPresenter().startSendPhoneNumberTask();
    }

    @Override
    public void showNextScreen() {
        Toast.makeText(this, R.string.registration_dialog_message_verification_dode_was_sent, Toast.LENGTH_SHORT).show();
        startActivity(SecondStepActivity.getLaunchIntent(this));
    }
}
