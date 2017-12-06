package com.kora.android.presentation.ui.registration.step2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.kora.android.R;
import com.kora.android.common.permission.PermissionCheckerActivity;
import com.kora.android.common.permission.PermissionException;
import com.kora.android.di.component.ActivityComponent;
import com.kora.android.presentation.ui.base.view.BaseActivity;
import com.kora.android.presentation.ui.registration.step3.ThirdStepActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import static android.Manifest.permission.READ_SMS;

public class SecondStepActivity extends BaseActivity<SecondStepPresenter> implements SecondStepView {

    @Inject
    PermissionCheckerActivity mPermissionCheckerActivity;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.edit_layout_confirmation_code)
    TextInputLayout mElConfirmationCode;
    @BindView(R.id.edit_text_confirmation_code)
    TextInputEditText mEtConfirmationCode;

    public static Intent getLaunchIntent(final BaseActivity baseActivity) {
        return new Intent(baseActivity, SecondStepActivity.class);
    }

    @Override
    public void injectToComponent(final ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_registration_step_2;
    }

    @Override
    protected void onViewReady(final Bundle savedInstanceState) {
        setToolbar(mToolbar, R.drawable.ic_back_grey);
    }

    @Override
    protected void onStart() {
        super.onStart();
        startIncomingSmsService();
    }

    private void startIncomingSmsService() {
        try {
            mPermissionCheckerActivity.verifyPermissions(READ_SMS);
            getPresenter().startIncomingSmsService();
        } catch (PermissionException ignored) {
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        stopIncomingSmsService();
    }

    private void stopIncomingSmsService() {
        try {
            mPermissionCheckerActivity.verifyPermissions(READ_SMS);
            getPresenter().stopIncomingSmsService();
        } catch (PermissionException ignored) {
        }
    }

    @Override
    public void showConfirmationCode(final String confirmationCode) {
        mEtConfirmationCode.getText().clear();
        mEtConfirmationCode.setText(confirmationCode);
    }

    @OnTextChanged(R.id.edit_text_confirmation_code)
    void onChangedPhoneNumber(final CharSequence confirmationCode) {
        mElConfirmationCode.setError(null);
        getPresenter().setConfirmationCode(confirmationCode.toString().trim());
    }

    @Override
    public void showEmptyConfirmationCode() {
        mElConfirmationCode.setError(getString(R.string.registration_confirmation_code_empty));
    }

    @Override
    public void showIncorrectConfirmationCode() {
        mElConfirmationCode.setError(getString(R.string.registration_confirmation_code_incorrect));
    }

    @OnClick(R.id.button_confirm)
    public void onClickConfirm() {
        getPresenter().startSendConfirmationCodeTask();
    }

    @OnClick(R.id.button_resend)
    public void onClickResend() {
        getPresenter().startResendPhoneNumberTask();
    }

    @Override
    public void showNextScreen() {
        startActivity(ThirdStepActivity.getLaunchIntent(this));
    }

    @Override
    public void showSendCodeMessage() {
        Toast.makeText(this, R.string.registration_dialog_message_verification_dode_was_sent, Toast.LENGTH_SHORT).show();
    }
}
