package com.kora.android.presentation.ui.registration.step1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kora.android.R;
import com.kora.android.common.permission.PermissionChecker;
import com.kora.android.common.utils.ViewUtils;
import com.kora.android.injection.component.ActivityComponent;
import com.kora.android.presentation.model.CountryEntity;
import com.kora.android.presentation.ui.base.view.BaseActivity;
import com.kora.android.presentation.ui.registration.countries.CountriesActivity;
import com.kora.android.presentation.ui.registration.step2.SecondStepActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import static android.Manifest.permission.READ_SMS;
import static com.kora.android.common.Keys.PermissionChecker.PERMISSION_REQUEST_CODE_READ_SMS;
import static com.kora.android.common.Keys.SelectCountry.SELECT_COUNTRY_EXTRA;
import static com.kora.android.common.Keys.SelectCountry.SELECT_COUNTRY_REQUEST_CODE;
import static com.kora.android.data.network.Constants.API_BASE_URL;

public class FirstStepActivity extends BaseActivity<FirstStepPresenter> implements FirstStepView {

    @Inject
    PermissionChecker mPermissionChecker;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.image_view_country_flag)
    ImageView mIvCountryFlag;
    @BindView(R.id.text_view_phone_code)
    TextView mTvPhoneCode;
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

        getPresenter().startDeleteWalletsTask();
    }

    @Override
    public void showNextViews() {
        requestPermission();
    }

    private void requestPermission() {
        mPermissionChecker.requestPermissions(PERMISSION_REQUEST_CODE_READ_SMS, READ_SMS);
    }

    @OnClick(R.id.card_view_select_country)
    public void onClickSelectCountry() {
        startActivityForResult(CountriesActivity.getLaunchIntent(this), SELECT_COUNTRY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (requestCode == SELECT_COUNTRY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                final CountryEntity countryEntity = data.getParcelableExtra(SELECT_COUNTRY_EXTRA);
                getPresenter().setCountry(countryEntity);
                Glide.with(this)
                        .load(API_BASE_URL + countryEntity.getFlag())
                        .into(mIvCountryFlag);
                mTvPhoneCode.setText(countryEntity.getPhoneCode());
                mElPhoneNumber.setError(null);
                mEtPhoneNumber.setText(null);
                ViewUtils.setMaxLength(mEtPhoneNumber, countryEntity.getPhoneCode());
            }
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
        startActivity(SecondStepActivity.getLaunchIntent(this));
    }

    @Override
    public void showServerError() {
        showDialogMessage(
                R.string.registration_dialog_title_server_error_phone,
                R.string.registration_dialog_message_server_error_phone);
    }

    @Override
    public void showTwilioError(final String message) {
        showDialogMessage(
                R.string.registration_dialog_title_twilio_error_phone,
                message);
    }
}
