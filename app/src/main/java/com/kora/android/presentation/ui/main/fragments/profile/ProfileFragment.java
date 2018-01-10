package com.kora.android.presentation.ui.main.fragments.profile;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
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
import com.kora.android.BuildConfig;
import com.kora.android.R;
import com.kora.android.common.Keys;
import com.kora.android.common.permission.PermissionCheckerFragment;
import com.kora.android.common.permission.PermissionException;
import com.kora.android.common.utils.DateUtils;
import com.kora.android.common.utils.StringUtils;
import com.kora.android.common.utils.ViewUtils;
import com.kora.android.di.component.FragmentComponent;
import com.kora.android.presentation.enums.ViewMode;
import com.kora.android.presentation.model.CountryEntity;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.base.backstack.StackFragment;
import com.kora.android.presentation.ui.base.view.BaseFragment;
import com.kora.android.presentation.ui.main.MainActivity;
import com.kora.android.presentation.ui.registration.currencies.CurrenciesActivity;
import com.miguelbcr.ui.rx_paparazzo2.RxPaparazzo;
import com.miguelbcr.ui.rx_paparazzo2.entities.Options;
import com.miguelbcr.ui.rx_paparazzo2.entities.size.CustomMaxSize;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;
import static com.kora.android.common.Keys.DEFAULT_AGENT_RATE;
import static com.kora.android.common.Keys.FILE_PROVIDER;
import static com.kora.android.common.Keys.PermissionChecker.PERMISSION_REQUEST_CODE_WRITE_EXTERNAL_STORAGE;
import static com.kora.android.common.Keys.SelectCurrency.SELECT_CURRENCY_EXTRA;
import static com.kora.android.common.Keys.SelectCurrency.SELECT_CURRENCY_REQUEST_CODE;
import static com.kora.android.common.Keys.SelectWalletFile.SELECT_WALLET_FILE_REQUEST_CODE;
import static com.kora.android.data.network.Constants.API_BASE_URL;
import static com.kora.android.presentation.enums.ViewMode.EDIT_MODE;
import static com.kora.android.presentation.enums.ViewMode.VIEW_MODE;

public class ProfileFragment extends StackFragment<ProfilePresenter> implements ProfileView,
        DatePickerDialog.OnDateSetListener, DialogInterface.OnClickListener {

    @Inject
    PermissionCheckerFragment mPermissionCheckerFragment;

    @BindView(R.id.toolbar) Toolbar mToolbar;

    @BindView(R.id.image_view_avatar) ImageView mIvAvatar;
    @BindView(R.id.text_view_upload_photo) TextView mTvUploadPhoto;

    @BindView(R.id.edit_layout_user_name) TextInputLayout mElUserName;
    @BindView(R.id.edit_text_user_name) TextInputEditText mEtUserName;
    @BindView(R.id.edit_layout_phone_number) TextInputLayout mElPhoneNumber;
    @BindView(R.id.edit_text_phone_number) TextInputEditText mEtPhoneNumber;

    @BindView(R.id.edit_layout_email) TextInputLayout mElEmail;
    @BindView(R.id.edit_text_currency) TextInputEditText mEtCurrency;
    @BindView(R.id.edit_text_postal_code) TextInputEditText mEtPostalCode;
    @BindView(R.id.edit_text_date_of_birth) TextInputEditText mEtDateOfBirth;
    @BindView(R.id.edit_text_address) TextInputEditText mEtAddress;

    @BindView(R.id.edit_layout_full_name) TextInputLayout mElFullName;
    @BindView(R.id.edit_text_full_name) TextInputEditText mEtFullName;
    @BindView(R.id.edit_text_email) TextInputEditText mEtEmail;

    @BindView(R.id.edit_layout_date_of_birth) TextInputLayout mElDateOfBirth;

    @BindView(R.id.scroll_view_container) ScrollView mSvContainer;
    @BindView(R.id.relative_layout_container) RelativeLayout mRlContainer;
    @BindView(R.id.returned_container) LinearLayout mLlContainer;

    @BindView(R.id.button_export_wallet) TextView mTvExportWallet;
    @BindView(R.id.button_import_wallet) TextView mTvImportWallet;

    @BindView(R.id.text_agent_on_off) TextView mTvAgentOnOff;
    @BindView(R.id.switch_agent) SwitchCompat mScAgentSwitch;
    @BindView(R.id.edit_layout_interest_rate) TextInputLayout mElInterestRate;
    @BindView(R.id.edit_text_interest_rate) TextInputEditText mEtInterestRate;

    private ViewMode mViewMode = VIEW_MODE;

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

//        setRequirements(false);

        if (savedInstanceState == null) {
            getPresenter().loadUserData();
        }
    }

    private void setRequirements(boolean required) {
        ViewUtils.setRequired(mElEmail, getContext().getString(R.string.registration_email), required);
        ViewUtils.setRequired(mElUserName, getContext().getString(R.string.registration_user_name), required);
        ViewUtils.setRequired(mElPhoneNumber, getContext().getString(R.string.registration_phone_number), required);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.profile_menu, menu);

        MenuItem edit = menu.findItem(R.id.action_edit);
        MenuItem save = menu.findItem(R.id.action_save);

        if (mViewMode == VIEW_MODE) {
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

        if (mViewMode == VIEW_MODE) {
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
                changeMode(EDIT_MODE);
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

        if (mViewMode == EDIT_MODE) {
            mLlContainer.setVisibility(View.INVISIBLE);
            mTvUploadPhoto.setVisibility(View.VISIBLE);
            setTitle(R.string.title_edit_profile);
            setDrawerListener(true);
//            setRequirements(true);
//            mEtUserName.setEnabled(true);
//            mEtEmail.setEnabled(true);
//            mEtPhoneNumber.setEnabled(true);
            mEtFullName.setEnabled(true);
//            mEtCurrency.setEnabled(true);
            mEtPostalCode.setEnabled(true);
            mEtDateOfBirth.setEnabled(true);
            mEtAddress.setEnabled(true);
            mEtInterestRate.setEnabled(true);
            mTvExportWallet.setVisibility(View.GONE);
            mTvImportWallet.setVisibility(View.GONE);
            mTvAgentOnOff.setVisibility(View.GONE);
            mScAgentSwitch.setVisibility(View.VISIBLE);
        } else {
            mLlContainer.setVisibility(View.VISIBLE);
            mTvUploadPhoto.setVisibility(View.INVISIBLE);
            setTitle(R.string.title_profile);
//            setRequirements(false);
            setDrawerListener(false);
            mEtUserName.setEnabled(false);
            mEtEmail.setEnabled(false);
            mEtPhoneNumber.setEnabled(false);
            mEtFullName.setEnabled(false);
            mEtCurrency.setEnabled(false);
            mEtPostalCode.setEnabled(false);
            mEtDateOfBirth.setEnabled(false);
            mEtAddress.setEnabled(false);
            mEtInterestRate.setEnabled(false);
            ViewUtils.clearFocus(mEtFullName, mEtPostalCode, mEtDateOfBirth, mEtAddress, mEtInterestRate);
            ViewUtils.deleteErrors(mElDateOfBirth, mElInterestRate);
            mTvExportWallet.setVisibility(View.VISIBLE);
            mTvImportWallet.setVisibility(View.VISIBLE);
            mTvAgentOnOff.setVisibility(View.VISIBLE);
            mScAgentSwitch.setVisibility(View.GONE);
        }
    }

    @Override
    public void retrieveUserData(final UserEntity userEntity) {
        if (getBaseActivity() != null) {
            ((MainActivity) getBaseActivity()).showUserData(userEntity);
        }
        Glide.with(this)
                .load(API_BASE_URL + userEntity.getAvatar())
                .apply(RequestOptions.circleCropTransform())
                .thumbnail(Glide.with(this).load(R.drawable.ic_user_default))
                .into(mIvAvatar);

        retrieveCurrency(userEntity.getFlag(), userEntity.getCurrency());
        mEtUserName.setText(userEntity.getUserName());
        mEtEmail.setText(userEntity.getEmail());
        mEtPhoneNumber.setText(StringUtils.getFormattedPhoneNumber(userEntity.getPhoneNumber(), userEntity.getCountryCode()));
        mEtFullName.setText(userEntity.getLegalName());
        mEtPostalCode.setText(userEntity.getPostalCode());
        mEtDateOfBirth.setText(DateUtils.getPrettyDateFromFormatted(userEntity.getDateOfBirth()));
        mEtAddress.setText(userEntity.getAddress());

        mTvAgentOnOff.setText(userEntity.isAgent() ? R.string.profile_agent_on : R.string.profile_agent_off);
        mScAgentSwitch.setChecked(userEntity.isAgent());
        mElInterestRate.setVisibility(userEntity.isAgent() ? View.VISIBLE : View.GONE);
        if (userEntity.getInterestRate() != null)
            mEtInterestRate.setText(String.valueOf(userEntity.getInterestRate()));
        else
            mEtInterestRate.setText(String.valueOf(DEFAULT_AGENT_RATE));
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

    @OnTextChanged(R.id.edit_text_full_name)
    void onFullNameChanged(final CharSequence legalName) {
        mElFullName.setError(null);
        getPresenter().setLegalName(legalName.toString().trim());
    }

    @Override
    public void showIncorrectFullName() {
        mElFullName.setError(getString(R.string.registration_full_name_incorrect));
        ViewUtils.scrollToView(mSvContainer, mRlContainer, mElFullName);
    }

    @OnTextChanged(R.id.edit_text_email)
    void onEmailChanged(final CharSequence email) {
        mElEmail.setError(null);
        getPresenter().setEmail(email.toString().trim());
    }

    @Override
    public void showEmptyEmail() {
        mElEmail.setError(getString(R.string.registration_email_empty));
        ViewUtils.scrollToView(mSvContainer, mRlContainer, mElEmail);
    }

    @Override
    public void showIncorrectEmail() {
        mElEmail.setError(getString(R.string.registration_email_incorrect));
        ViewUtils.scrollToView(mSvContainer, mRlContainer, mElEmail);
    }

    @Override
    public void onUserUpdated() {
        changeMode(VIEW_MODE);
        Toast.makeText(getContext(), R.string.update_profile_success_message, Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.edit_text_date_of_birth)
    void onDateOfBirthClicked() {
        mElDateOfBirth.setError(null);

        Calendar cal = DateUtils.getCalendarFromPrettyDate(mEtDateOfBirth.getText().toString().trim());
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

    @Override
    public void showIncorrectDate() {
        mElDateOfBirth.setError(getString(R.string.registration_date_of_birth_incorrect));
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

    @OnTextChanged(R.id.edit_text_phone_number)
    void onPhoneNumberChanged(final CharSequence phoneNumber) {
        getPresenter().setPhoneNumber(phoneNumber.toString().trim());
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
    public void onDateSet(DatePickerDialog view, int year, int month, int day) {
        final String prettyDate = DateUtils.getPrettyDate(year, month, day);
        mEtDateOfBirth.setText(prettyDate);
        final String formattedDate = DateUtils.getFormattedDate(year, month, day);
        getPresenter().setDateOfBirth(formattedDate);
    }

    @OnCheckedChanged(R.id.switch_agent)
    public void OnCheckedChangedSwitchAgent(boolean isChecked) {
        if (mViewMode == EDIT_MODE) {
            if (isChecked) {
                getPresenter().setAgent(true);
                mElInterestRate.setVisibility(View.VISIBLE);
                ViewUtils.scrollToView(mSvContainer, mLlContainer, mElInterestRate);
            } else {
                getPresenter().setAgent(false);
                mElInterestRate.setVisibility(View.GONE);
            }
        }
    }

    @OnTextChanged(R.id.edit_text_interest_rate)
    void onInterestRateChanged(final CharSequence interestRate) {
        getPresenter().setInterestRate(interestRate.toString().trim());
    }

    @Override
    public void showIncorrectInterestRate() {
        mElInterestRate.setError(getString(R.string.profile_incorrect_interest_rate));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Keys.Args.VIEW_MODE, mViewMode);
        outState.putParcelable(Keys.Args.USER_ENTITY, getPresenter().getUserEntity());
    }

    @Override
    protected void onBackPressed() {
        if (mViewMode == EDIT_MODE) {
            changeMode(VIEW_MODE);
            getPresenter().onChangeMode(VIEW_MODE);
        } else {
            super.onBackPressed();
        }
    }

    @OnClick({R.id.image_view_avatar, R.id.text_view_upload_photo})
    public void onClickUploadPhoto() {
        if (mViewMode == VIEW_MODE) return;
        final Options options = new Options();
        options.setToolbarColor(ContextCompat.getColor(getContext(), R.color.color_cropping_background));
        options.setStatusBarColor(ContextCompat.getColor(getContext(), R.color.color_cropping_background));
        options.setAspectRatio(1, 1);

        RxPaparazzo.single(getActivity())
                .crop(options)
                .size(new CustomMaxSize())
                .usingGallery()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.resultCode() == RESULT_OK) {
                        getPresenter().updateAvatar(response.data().getFile().getAbsolutePath());
                    }
                }, Throwable::printStackTrace);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            UserEntity user = savedInstanceState.getParcelable(Keys.Args.USER_ENTITY);
            ViewMode mode = (ViewMode) savedInstanceState.getSerializable(Keys.Args.VIEW_MODE);
            changeMode(mode);
            getPresenter().setUserEntity(user);
        }
    }

    @OnClick(R.id.button_export_wallet)
    public void onClickExportWallet() {
        getPresenter().getWalletFileFromInternalStorage();
    }

    @Override
    public void showExportWalletDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getBaseActivity());
        builder.setItems(R.array.export_wallet_dialog_items, this);
        builder.create().show();
    }

    @Override
    public void onClick(final DialogInterface dialog, final int which) {
        switch (which) {
            case 0:
                shareWallet();
                break;
            case 1:
                exportWallet();
                break;
        }
    }

    public void shareWallet() {
        final Uri uri = FileProvider.getUriForFile(
                getBaseActivity(),
                BuildConfig.APPLICATION_ID + FILE_PROVIDER,
                getPresenter().getWalletFile());
        final Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("application/json"); // change type if needed
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        final PackageManager packageManager = getActivity().getPackageManager();
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(Intent.createChooser(intent, getString(R.string.export_wallet_share)));
        }
    }

    public void exportWallet() {
        try {
            mPermissionCheckerFragment.verifyPermissions(WRITE_EXTERNAL_STORAGE);
            getPresenter().exportWalletFileOnExternalStorage();
        } catch (final PermissionException permissionException) {
            mPermissionCheckerFragment.requestPermissions(
                    PERMISSION_REQUEST_CODE_WRITE_EXTERNAL_STORAGE,
                    permissionException.getRequiredPermissions());
        }
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode,
                                           @NonNull final String[] permissions,
                                           @NonNull final int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE_WRITE_EXTERNAL_STORAGE:
                if (mPermissionCheckerFragment.permissionsGranted(permissions, grantResults)) {
                    exportWallet();
                } else {
                    showDialogMessage(
                            R.string.export_wallet_pemission_dialog_title,
                            R.string.export_wallet_permission_dialog_message);
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void showExportedWalletMessage() {
        Toast.makeText(getBaseActivity(), R.string.export_wallet_exported, Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.button_import_wallet)
    public void onClickImportWallet() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*"); // change type if needed
        startActivityForResult(
                Intent.createChooser(intent, getString(R.string.import_wallet_select)),
                SELECT_WALLET_FILE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_CURRENCY_REQUEST_CODE && resultCode == RESULT_OK) {
            CountryEntity currency = data.getParcelableExtra(SELECT_CURRENCY_EXTRA);
            retrieveCurrency(currency.getFlag(), currency.getCurrency());
            getPresenter().setCurrency(currency);

        } else if (requestCode == SELECT_WALLET_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            final Uri walletFileUri = data.getData();
            getPresenter().importWalletFileOnInternalStorage(walletFileUri);
        }
    }

    @Override
    public void showImportedWalletMessage() {
        Toast.makeText(getBaseActivity(), R.string.import_wallet_imported, Toast.LENGTH_LONG).show();
    }
}
