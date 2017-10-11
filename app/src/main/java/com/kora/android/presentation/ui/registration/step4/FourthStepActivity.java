package com.kora.android.presentation.ui.registration.step4;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kora.android.R;
import com.kora.android.common.utils.ViewUtils;
import com.kora.android.injection.component.ActivityComponent;
import com.kora.android.presentation.model.CountryEntity;
import com.kora.android.presentation.ui.base.view.BaseActivity;
import com.kora.android.presentation.ui.registration.currencies.CurrenciesActivity;
import com.miguelbcr.ui.rx_paparazzo2.RxPaparazzo;
import com.miguelbcr.ui.rx_paparazzo2.entities.Options;
import com.miguelbcr.ui.rx_paparazzo2.entities.size.CustomMaxSize;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.kora.android.common.Keys.SelectCurrency.SELECT_CURRENCY_EXTRA;
import static com.kora.android.common.Keys.SelectCurrency.SELECT_CURRENCY_REQUEST_CODE;
import static com.kora.android.data.network.Constants.API_BASE_URL;

public class FourthStepActivity extends BaseActivity<FourthStepPresenter> implements FourthStepView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.image_view_avatar)
    ImageView mIvAvatar;
    @BindView(R.id.text_view_upload_photo)
    TextView mTvUploadPhoto;

    @BindView(R.id.edit_layout_user_name)
    TextInputLayout mElUserName;
    @BindView(R.id.edit_text_user_name)
    TextInputEditText mEtPhoneNumber;

    @BindView(R.id.edit_layout_email)
    TextInputLayout mElEmail;

    @BindView(R.id.image_view_country_flag)
    ImageView mIvCountryFlag;
    @BindView(R.id.text_view_currency)
    TextView mTvCurrency;

    @BindView(R.id.edit_layout_password)
    TextInputLayout mElPassword;
    @BindView(R.id.edit_layout_confirm_password)
    TextInputLayout mElConfirmPassword;

    @BindView(R.id.scroll_view_container)
    ScrollView mSvContainer;
    @BindView(R.id.relative_layout_container)
    RelativeLayout mRlContainer;



    public static Intent getLaunchIntent(final BaseActivity baseActivity) {
        return new Intent(baseActivity, FourthStepActivity.class);
    }

    @Override
    public void injectToComponent(final ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_registration_step_4;
    }

    @Override
    protected void onViewReady(final Bundle savedInstanceState) {
        setToolbar(mToolbar, R.drawable.ic_back_grey);

        getPresenter().startGetCountryTask();
    }

    @Override
    public void onBackPressed() {
        showDialogMessage(
                R.string.registration_dialog_title_back_pressed,
                R.string.registration_dialog_message_back_pressed,
                R.string.registration_dialog_positive_back_pressed,
                (dialogInterface, i) -> super.onBackPressed(),
                R.string.registration_dialog_negative_back_pressed,
                (dialogInterface, i) -> dialogInterface.dismiss());
    }

    @Override
    public void showCurrency(final CountryEntity countryEntity) {
        Glide.with(this)
                .load(API_BASE_URL + countryEntity.getFlag())
                .into(mIvCountryFlag);
        mTvCurrency.setText(countryEntity.getCurrency());
        getPresenter().setCurrency(countryEntity.getCurrency());
    }

    @OnClick({R.id.image_view_avatar, R.id.text_view_upload_photo})
    public void onClickUploadPhoto() {

        final Options options = new Options();
        options.setToolbarColor(ContextCompat.getColor(this, R.color.color_cropping_background));
        options.setStatusBarColor(ContextCompat.getColor(this, R.color.color_cropping_background));
        options.setAspectRatio(1, 1);

        RxPaparazzo.single(this)
                .crop(options)
                .size(new CustomMaxSize())
                .usingGallery()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.resultCode() == RESULT_OK) {
                        response.targetUI().showAvatar(response.data().getFile());
                        getPresenter().setAvatar(response.data().getFile().getAbsolutePath());
                    }
                }, throwable -> {
                    Log.e("_____", throwable.toString());
                    throwable.printStackTrace();
                });
    }

    @Override
    public void showAvatar(final File file) {
        Glide.with(this)
                .load(file)
                .apply(RequestOptions.circleCropTransform())
                .into(mIvAvatar);
    }

    @OnTextChanged(R.id.edit_text_user_name)
    void onChangedUserName(final CharSequence userName) {
        mElUserName.setError(null);
        getPresenter().setUserName(userName.toString().trim());
    }

    @Override
    public void showEmptyUserName() {
        mElUserName.setError(getString(R.string.registration_user_name_empty));
        ViewUtils.scrollToView(mSvContainer, mRlContainer, mElUserName);
    }

    @Override
    public void showIncorrectUserName() {
        mElUserName.setError(getString(R.string.registration_user_name_incorrect));
        ViewUtils.scrollToView(mSvContainer, mRlContainer, mElUserName);
    }

    @Override
    public void showTooShortUserName() {
        mElUserName.setError(getString(R.string.registration_user_name_too_short));
        ViewUtils.scrollToView(mSvContainer, mRlContainer, mElUserName);
    }

    @OnTextChanged(R.id.edit_text_legal_name)
    void onChangedLegalName(final CharSequence legalName) {
        getPresenter().setLegalName(legalName.toString().trim());
    }

    @OnTextChanged(R.id.edit_text_email)
    void onChangedEmail(final CharSequence email) {
        mElEmail.setError(null);
        getPresenter().setEmail(email.toString().trim());
    }

    @Override
    public void showIncorrectEmail() {
        mElEmail.setError(getString(R.string.registration_email_incorrect));
        ViewUtils.scrollToView(mSvContainer, mRlContainer, mElEmail);
    }

    @OnTextChanged(R.id.edit_text_date_of_birth)
    void onChangedDateOfBirth(final CharSequence dateOfBirth) {
        getPresenter().setDateOfBirth(dateOfBirth.toString().trim());
    }

    @OnClick(R.id.card_view_select_currency)
    void OnClickSelectCurrency() {
        startActivityForResult(CurrenciesActivity.getLaunchIntent(this), SELECT_CURRENCY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (requestCode == SELECT_CURRENCY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                final CountryEntity countryEntity = data.getParcelableExtra(SELECT_CURRENCY_EXTRA);
                getPresenter().setCurrency(countryEntity.getCurrency());
                Glide.with(this)
                        .load(API_BASE_URL + countryEntity.getFlag())
                        .into(mIvCountryFlag);
                mTvCurrency.setText(countryEntity.getCurrency());
            }
        }
    }

    @OnTextChanged(R.id.edit_text_postal_code)
    void onChangedPostalCode(final CharSequence postalCode) {
        getPresenter().setPostalCode(postalCode.toString().trim());
    }

    @OnTextChanged(R.id.edit_text_address)
    void onChangedAddress(final CharSequence address) {
        getPresenter().setAddress(address.toString().trim());
    }

    @OnTextChanged(R.id.edit_text_password)
    void onChangedPassword(final CharSequence password) {
        mElPassword.setError(null);
        getPresenter().setPassword(password.toString().trim());
    }

    @Override
    public void showEmptyPassword() {
        mElPassword.setError(getString(R.string.registration_password_empty));
    }

    @Override
    public void showTooShortPassword() {
        mElPassword.setError(getString(R.string.registration_password_too_short));
    }

    @OnTextChanged(R.id.edit_text_confirm_password)
    void onChangedConfirmPassword(final CharSequence confirmPassword) {
        mElConfirmPassword.setError(null);
        getPresenter().setConfirmPassword(confirmPassword.toString().trim());
    }

    @Override
    public void showEmptyConfirmPassword() {
        mElConfirmPassword.setError(getString(R.string.registration_confirm_password_empty));
    }

    @Override
    public void showIncorrectConfirmPassword() {
        mElConfirmPassword.setError(getString(R.string.registration_confirm_password_incorrect));
    }

    @OnClick(R.id.card_view_confirm)
    public void onClickConfirm() {
        getPresenter().startRegistrationTask();
    }

    @Override
    public void showNextScreen() {
        Toast.makeText(this, "YEAH", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showServerError(final String message) {
        showDialogMessage(R.string.registration_dialog_title_server_error_validation, message);
    }
}
