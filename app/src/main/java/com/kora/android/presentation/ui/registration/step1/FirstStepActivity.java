package com.kora.android.presentation.ui.registration.step1;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;

import com.kora.android.R;
import com.kora.android.common.permission.PermissionChecker;
import com.kora.android.injection.component.ActivityComponent;
import com.kora.android.presentation.ui.base.view.BaseActivity;
import com.kora.android.presentation.ui.registration.step2.SecondStepActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.kora.android.common.Keys.PermissionChecker.PERMISSION_REQUEST_CODE_READ_PHONE_STATE_AND_READ_SMS;

public class FirstStepActivity extends BaseActivity<FirstStepPresenter> implements FirstStepView {

    @Inject
    PermissionChecker mPermissionChecker;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.edit_layout_phone_number)
    TextInputLayout mElPhoneNumber;
    @BindView(R.id.edit_text_phone_number)
    TextInputEditText mEtPhoneNumber;

    public static Intent getLaunchIntent(final BaseActivity baseActivity) {
        final Intent intent = new Intent(baseActivity, FirstStepActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }

    @Override
    public void injectToComponent(final ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_regitration_step_1;
    }

    @Override
    protected void onViewReady(final Bundle savedInstanceState) {
        setToolbar(mToolbar, R.drawable.ic_back_grey);

        requestFocus();
        requestPermissions();
    }

    private void requestFocus() {
        mEtPhoneNumber.requestFocus();
    }

    private void requestPermissions() {
        mPermissionChecker.requestPermissions(
                PERMISSION_REQUEST_CODE_READ_PHONE_STATE_AND_READ_SMS,
                READ_PHONE_STATE,
                READ_SMS);
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode,
                                           @NonNull final String[] permissions,
                                           @NonNull final int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE_READ_PHONE_STATE_AND_READ_SMS:
                if (grantResults[0] == PERMISSION_GRANTED)
                    getPresenter().startGetPhoneNumberTask();
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @OnTextChanged(R.id.edit_text_phone_number)
    void onChangedPhoneNumber(final CharSequence phoneNumber) {
        mElPhoneNumber.setError(null);
        getPresenter().setPhoneNumber(phoneNumber.toString().trim());
    }

    @OnClick(R.id.card_view_send)
    public void onClickSend() {
        getPresenter().startSendPhoneNumberTask();
    }

    @Override
    public void showPhoneNumber(final String phoneNumber) {
        mEtPhoneNumber.setText(phoneNumber);
    }

    @Override
    public void showEmptyPhoneNumber() {
        mElPhoneNumber.setError(getString(R.string.registration_phone_number_empty));
    }

    @Override
    public void showIncorrectPhoneNumber() {
        mElPhoneNumber.setError(getString(R.string.registration_phone_number_incorrect));
    }

    @Override
    public void showNextScreen() {
        startActivity(SecondStepActivity.getLaunchIntent(this));
    }
}
