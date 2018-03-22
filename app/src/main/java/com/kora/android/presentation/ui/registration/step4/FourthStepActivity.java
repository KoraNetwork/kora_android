package com.kora.android.presentation.ui.registration.step4;

import android.app.DatePickerDialog;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.kora.android.GlideApp;
import com.kora.android.R;
import com.kora.android.common.utils.DateUtils;
import com.kora.android.common.utils.ViewUtils;
import com.kora.android.di.component.ActivityComponent;
import com.kora.android.presentation.model.CountryEntity;
import com.kora.android.presentation.ui.base.view.BaseActivity;
import com.kora.android.presentation.ui.main.MainActivity;
import com.kora.android.presentation.ui.registration.countries.CountriesActivity;
import com.kora.android.presentation.ui.registration.currencies.CurrenciesActivity;
import com.miguelbcr.ui.rx_paparazzo2.RxPaparazzo;
import com.miguelbcr.ui.rx_paparazzo2.entities.Options;
import com.miguelbcr.ui.rx_paparazzo2.entities.size.CustomMaxSize;

import java.io.File;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.kora.android.common.Keys.DefaultCountry.DEFAULT_COUNTRY_CODE;
import static com.kora.android.common.Keys.DefaultCountry.DEFAULT_COUNTRY_CURRENCY;
import static com.kora.android.common.Keys.DefaultCountry.DEFAULT_COUNTRY_FLAG;
import static com.kora.android.common.Keys.DefaultCountry.DEFAULT_COUNTRY_PHONE_CODE;
import static com.kora.android.common.Keys.DefaultCountry.DEFAULT_ERC_20_TOKEN;
import static com.kora.android.common.Keys.SelectCountry.SELECT_COUNTRY_EXTRA;
import static com.kora.android.common.Keys.SelectCurrency.SELECT_CURRENCY_EXTRA;
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

    @BindView(R.id.edit_layout_full_name)
    TextInputLayout mElFullName;

    @BindView(R.id.edit_layout_email)
    TextInputLayout mElEmail;

    @BindView(R.id.edit_layout_date_of_birth)
    TextInputLayout mElDateOfBirth;
    @BindView(R.id.edit_text_date_of_birth)
    TextInputEditText mEtDateOfBirth;

    @BindView(R.id.image_view_phone_flag)
    ImageView mIvPhoneFlag;
    @BindView(R.id.edit_text_phone_code)
    TextInputEditText mEtPhoneCode;
    @BindView(R.id.edit_layout_phone_number)
    TextInputLayout mElPhoneNumber;
    @BindView(R.id.edit_text_phone_number)
    TextInputEditText mEtPhoneNumber;
    @BindView(R.id.relative_layout_phone_number)
    RelativeLayout mRlPhoneNumber;

    @BindView(R.id.image_view_currency_flag)
    ImageView mIvCurrencyFlag;
    @BindView(R.id.edit_text_currency)
    TextInputEditText mEtCurrency;

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

        initDate();
        initPhoneAndCurrency();
    }

    private void initDate() {
        final Calendar calendar = Calendar.getInstance();
        final String formattedDate = DateUtils.getFormattedDate(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        getPresenter().setDateOfBirth(formattedDate);
        final String prettyDate = DateUtils.getPrettyDate(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        mEtDateOfBirth.setText(prettyDate);
    }

    public void initPhoneAndCurrency() {
        getPresenter().setPhoneCode(DEFAULT_COUNTRY_PHONE_CODE);
        getPresenter().setCountryCode(DEFAULT_COUNTRY_CODE);
        getPresenter().setFlag(DEFAULT_COUNTRY_FLAG);

        getPresenter().setErc20Token(DEFAULT_ERC_20_TOKEN);
        getPresenter().setCurrency(DEFAULT_COUNTRY_CURRENCY);
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

    @OnTextChanged(R.id.edit_text_full_name)
    void onChangedFullName(final CharSequence fullName) {
        mElFullName.setError(null);
        getPresenter().setLegalName(fullName.toString().trim());
    }

    @Override
    public void showIncorrectFullName() {
        mElFullName.setError(getString(R.string.registration_full_name_incorrect));
        ViewUtils.scrollToView(mSvContainer, mRlContainer, mElFullName);
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

    @Override
    public void showEmptyEmail() {
        mElEmail.setError(getString(R.string.registration_email_empty));
        ViewUtils.scrollToView(mSvContainer, mRlContainer, mElEmail);
    }

    @OnClick(R.id.edit_text_date_of_birth)
    void onClickDateOfBirth() {
        mElDateOfBirth.setError(null);
        final String prettyDate = mEtDateOfBirth.getText().toString().trim();
        final Calendar calendar = DateUtils.getCalendarFromPrettyDate(prettyDate);
        new DatePickerDialog(this, R.style.AppTheme_DatePicker, (view, selectedYear, selectedMonth, selectedDay) -> {
            getPresenter().setDateOfBirth(DateUtils.getFormattedDate(selectedYear, selectedMonth, selectedDay));
            mEtDateOfBirth.setText(DateUtils.getPrettyDate(selectedYear, selectedMonth, selectedDay));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public void showIncorrectDate() {
        mElDateOfBirth.setError(getString(R.string.registration_date_of_birth_incorrect));
        ViewUtils.scrollToView(mSvContainer, mRlContainer, mElDateOfBirth);
    }

    @OnClick({R.id.edit_text_currency, R.id.image_view_currency_flag})
    void OnClickCurrency() {
        startActivityForResult(CurrenciesActivity.getLaunchIntent(this), 222);
    }

    @OnClick({R.id.edit_text_phone_code, R.id.image_view_phone_flag})
    public void onClickPhoneCode() {
        startActivityForResult(CountriesActivity.getLaunchIntent(this), 111);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (requestCode == 111 && resultCode == RESULT_OK) {

            final CountryEntity countryEntity = data.getParcelableExtra(SELECT_COUNTRY_EXTRA);
            getPresenter().setPhoneCode(countryEntity.getPhoneCode());
            getPresenter().setCountryCode(countryEntity.getCountryCode());
            getPresenter().setFlag(countryEntity.getFlag());
            GlideApp.with(this)
                    .load(API_BASE_URL + countryEntity.getFlag())
                    .into(mIvPhoneFlag);
            mEtPhoneCode.setText(countryEntity.getPhoneCode());
            mElPhoneNumber.setError(null);
            mEtPhoneNumber.setText(null);
            ViewUtils.setMaxLength(mEtPhoneNumber, countryEntity.getPhoneCode());

        } else if (requestCode == 222 && resultCode == RESULT_OK) {

            final CountryEntity countryEntity = data.getParcelableExtra(SELECT_CURRENCY_EXTRA);
            getPresenter().setCurrency(countryEntity.getCurrency());
            getPresenter().setErc20Token(countryEntity.getERC20Token());
            Glide.with(this)
                    .load(API_BASE_URL + countryEntity.getFlag())
                    .into(mIvCurrencyFlag);
            mEtCurrency.setText(getString(R.string.registration_currency_different, countryEntity.getCurrency()));
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
        ViewUtils.scrollToView(mSvContainer, mRlContainer, mRlPhoneNumber);
    }

    @Override
    public void showIncorrectPhoneNumber() {
        mElPhoneNumber.setError(getString(R.string.registration_phone_number_incorrect));
        ViewUtils.scrollToView(mSvContainer, mRlContainer, mRlPhoneNumber);
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
    public void showIncorrectPassword() {
        mElPassword.setError(getString(R.string.registration_password_incorrect));
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

    @Override
    public void showNextScreen() {
        startActivity(MainActivity.getLaunchIntent(this));
    }

    @OnClick(R.id.button_confirm)
    public void onClickConfirm() {
        getPresenter().startRegistrationTask();
    }
}
