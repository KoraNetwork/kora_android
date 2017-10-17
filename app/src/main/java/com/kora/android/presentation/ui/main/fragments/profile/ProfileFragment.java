package com.kora.android.presentation.ui.main.fragments.profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.kora.android.R;
import com.kora.android.common.Keys;
import com.kora.android.common.utils.DateUtils;
import com.kora.android.common.utils.ViewUtils;
import com.kora.android.di.component.FragmentComponent;
import com.kora.android.presentation.enums.ViewMode;
import com.kora.android.presentation.model.CountryEntity;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.base.backstack.StackFragment;
import com.kora.android.presentation.ui.base.view.BaseFragment;
import com.kora.android.presentation.ui.registration.currencies.CurrenciesActivity;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;

import static android.app.Activity.RESULT_OK;
import static com.kora.android.common.Keys.SelectCurrency.SELECT_CURRENCY_EXTRA;
import static com.kora.android.common.Keys.SelectCurrency.SELECT_CURRENCY_REQUEST_CODE;
import static com.kora.android.data.network.Constants.API_BASE_URL;

public class ProfileFragment extends StackFragment<ProfilePresenter> implements ProfileView, DatePickerDialog.OnDateSetListener {

    @BindView(R.id.toolbar) Toolbar mToolbar;

    @BindView(R.id.image_view_avatar) ImageView mIvAvatar;
    @BindView(R.id.text_view_upload_photo) TextView mTvUploadPhoto;

    @BindView(R.id.edit_layout_user_name) TextInputLayout mElUserName;
    @BindView(R.id.edit_text_user_name) TextInputEditText mEtUserName;
    @BindView(R.id.edit_text_phone_number) TextInputEditText mEtPhoneNumber;

    @BindView(R.id.edit_layout_email) TextInputLayout mElEmail;
    @BindView(R.id.edit_text_currency) TextInputEditText mEtCurrency;
    @BindView(R.id.edit_text_postal_code) TextInputEditText mEtPostalCode;
    @BindView(R.id.edit_text_date_of_birth) TextInputEditText mEtDateOfBirth;
    @BindView(R.id.edit_text_address) TextInputEditText mEtAddress;

    @BindView(R.id.edit_text_legal_name) TextInputEditText mEtLegalName;
    @BindView(R.id.edit_text_email) TextInputEditText mEtEmail;

    @BindView(R.id.scroll_view_container) ScrollView mSvContainer;
    @BindView(R.id.relative_layout_container) RelativeLayout mRlContainer;
    @BindView(R.id.returned_container) LinearLayout mLlContainer;

    private ViewMode mViewMode = ViewMode.VIEW_MODE;

    public static BaseFragment getNewInstance() {
        return new ProfileFragment();
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_profile;
    }

    @Override
    public void injectToComponent(FragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    public Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    protected int getTitle() {
        return R.string.title_profile;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    protected void onViewReady(Bundle savedInstanceState) {
        mEtCurrency.setKeyListener(null);
        mEtDateOfBirth.setKeyListener(null);

//        mEtEmail.setHint(
//                TextUtils.concat(
//                        mEtEmail.getHint(),
//                        Html.fromHtml(getContext().getString(R.string.required_asterisk)))
//        );
//
//        mEtUserName.setHint(
//                TextUtils.concat(
//                        mEtUserName.getHint(),
//                        Html.fromHtml(getContext().getString(R.string.required_asterisk)))
//        );
//
//        mEtPhoneNumber.setHint(
//                TextUtils.concat(
//                        mEtPhoneNumber.getHint(),
//                        Html.fromHtml(getContext().getString(R.string.required_asterisk)))
//        );

        if (savedInstanceState == null) {
            getPresenter().loadUserData();
        } else {
            UserEntity user = savedInstanceState.getParcelable(Keys.Args.USER_ENTITY);
            ViewMode mode = (ViewMode) savedInstanceState.getSerializable(Keys.Args.VIEW_MODE);
            changeMode(mode);
            getPresenter().setUserEntity(user);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.profile_menu, menu);

        MenuItem edit = menu.findItem(R.id.action_edit);
        MenuItem save = menu.findItem(R.id.action_save);

        if (mViewMode == ViewMode.VIEW_MODE) {
            save.setVisible(false);
            edit.setVisible(true);
        } else {
            save.setVisible(true);
            edit.setVisible(false);
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem edit = menu.findItem(R.id.action_edit);
        MenuItem save = menu.findItem(R.id.action_save);

        if (mViewMode == ViewMode.VIEW_MODE) {
            save.setVisible(false);
            edit.setVisible(true);
        } else {
            save.setVisible(true);
            edit.setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                changeMode(ViewMode.EDIT_MODE);
                return true;

            case R.id.action_save:
                getPresenter().onSaveClicked();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void changeMode(ViewMode viewMode) {
        mViewMode = viewMode;
        getActivity().invalidateOptionsMenu();

        if (mViewMode == ViewMode.EDIT_MODE) {
            mEtUserName.setEnabled(true);
            mEtEmail.setEnabled(true);
            mEtPhoneNumber.setEnabled(true);
            mEtLegalName.setEnabled(true);
            mEtCurrency.setEnabled(true);
            mEtPostalCode.setEnabled(true);
            mEtDateOfBirth.setEnabled(true);
            mEtAddress.setEnabled(true);
            mLlContainer.setVisibility(View.INVISIBLE);
            mTvUploadPhoto.setVisibility(View.VISIBLE);
            setTitle(R.string.title_edit_profile);
        } else {
            mEtUserName.setEnabled(false);
            mEtEmail.setEnabled(false);
            mEtPhoneNumber.setEnabled(false);
            mEtLegalName.setEnabled(false);
            mEtCurrency.setEnabled(false);
            mEtPostalCode.setEnabled(false);
            mEtDateOfBirth.setEnabled(false);
            mEtAddress.setEnabled(false);
            mLlContainer.setVisibility(View.VISIBLE);
            mTvUploadPhoto.setVisibility(View.INVISIBLE);
            setTitle(R.string.title_profile);
        }
    }

    @Override
    public void retrieveUserData(final UserEntity userEntity) {
        Glide.with(this)
                .load(API_BASE_URL + userEntity.getAvatar())
                .apply(RequestOptions.circleCropTransform())
                .into(mIvAvatar);

        retrieveCurrency(userEntity.getFlag(), userEntity.getCurrency());
        mEtUserName.setText(userEntity.getUserName());
        mEtEmail.setText(userEntity.getEmail());
        mEtPhoneNumber.setText(userEntity.getPhoneNumber());
        mEtLegalName.setText(userEntity.getLegalName());
        mEtPostalCode.setText(userEntity.getPostalCode());
        mEtDateOfBirth.setText(DateUtils.getPrettyDate(userEntity.getDateOfBirth()));
        mEtAddress.setText(userEntity.getAddress());

    }

    private void retrieveCurrency(String flag, String currency) {
        Glide.with(this)
                .asBitmap()
                .load(API_BASE_URL + flag)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        mEtCurrency.setCompoundDrawablesRelativeWithIntrinsicBounds(new BitmapDrawable(getResources(), resource), null, null, null);
                        mEtCurrency.setCompoundDrawablePadding(ViewUtils.convertDpToPixel(12));
                    }
                });
        mEtCurrency.setText(currency);
    }

    @OnTextChanged(R.id.edit_text_user_name)
    void onUserNameChanged(final CharSequence userName) {
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
    void onLegalNameChanged(final CharSequence legalName) {
        getPresenter().setLegalName(legalName.toString().trim());
    }

    @OnTextChanged(R.id.edit_text_email)
    void onEmailChanged(final CharSequence email) {
        mElEmail.setError(null);
        getPresenter().setEmail(email.toString().trim());
    }

    @Override
    public void showIncorrectEmail() {
        mElEmail.setError(getString(R.string.registration_email_incorrect));
        ViewUtils.scrollToView(mSvContainer, mRlContainer, mElEmail);
    }

    @Override
    public void onUserUpdated() {
        changeMode(ViewMode.VIEW_MODE);
        Toast.makeText(getContext(), R.string.update_profile_success_message, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.edit_text_date_of_birth)
    void onDateOfBirthClicked() {
        Calendar cal = DateUtils.getCalendarByDatePattern(mEtDateOfBirth.getText().toString().trim(), "dd MMM YYYY");
        if (cal == null) return;
        DatePickerDialog dpd = DatePickerDialog.newInstance(this, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        dpd.setAccentColor("#77cda4");
        dpd.show(getActivity().getFragmentManager(), "DatePickerDialog");
    }

    @OnFocusChange(R.id.edit_text_date_of_birth)
    void onDateOfBirthFocusChanged(View view, boolean isFocused) {
        if (isFocused) {
            onDateOfBirthClicked();
        }
    }

    @OnClick(R.id.edit_text_currency)
    void onCurrencyClicked() {
        startActivityForResult(CurrenciesActivity.getLaunchIntent(getBaseActivity()), SELECT_CURRENCY_REQUEST_CODE);
    }

    @OnFocusChange(R.id.edit_text_currency)
    void onCurrencyFocusChanged(View view, boolean isFocused) {
        if (isFocused) {
            onCurrencyClicked();
        }
    }

    @OnTextChanged(R.id.edit_text_postal_code)
    void onPostalCodeChanged(final CharSequence postalCode) {
        getPresenter().setPostalCode(postalCode.toString().trim());
    }

    @OnTextChanged(R.id.edit_text_address)
    void onAddressChanged(final CharSequence address) {
        getPresenter().setAddress(address.toString().trim());
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String prettyDate = DateUtils.getPrettyDate(year, monthOfYear, dayOfMonth);
        mEtDateOfBirth.setText(prettyDate);

        getPresenter().setDateOfBirth(prettyDate);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_CURRENCY_REQUEST_CODE && resultCode == RESULT_OK) {
            CountryEntity currency = data.getParcelableExtra(SELECT_CURRENCY_EXTRA);
            retrieveCurrency(currency.getFlag(), currency.getCurrency());
            getPresenter().setCurrency(currency);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Keys.Args.VIEW_MODE, mViewMode);
        outState.putParcelable(Keys.Args.USER_ENTITY, getPresenter().getUserEntity());
    }
}
