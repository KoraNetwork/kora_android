package com.kora.android.presentation.ui.registration.step2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;

import com.kora.android.R;
import com.kora.android.common.permission.PermissionChecker;
import com.kora.android.common.permission.PermissionException;
import com.kora.android.injection.component.ActivityComponent;
import com.kora.android.presentation.ui.base.view.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import static android.Manifest.permission.READ_SMS;

public class SecondStepActivity extends BaseActivity<SecondStepPresenter> implements SecondStepView {

    @Inject
    PermissionChecker mPermissionChecker;

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

        requestFocus();
    }

    private void requestFocus() {
        mEtConfirmationCode.requestFocus();
    }

    @Override
    protected void onStart() {
        super.onStart();
        startIncomingSmsService();
    }

    private void startIncomingSmsService() {
        try {
            mPermissionChecker.verifyPermissions(READ_SMS);
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
            mPermissionChecker.verifyPermissions(READ_SMS);
            getPresenter().stopIncomingSmsService();
        } catch (PermissionException ignored) {
        }
    }

    @OnTextChanged(R.id.edit_text_confirmation_code)
    void onChangedPhoneNumber(final CharSequence confirmationCode) {
        mElConfirmationCode.setError(null);
        getPresenter().setConfirmationCode(confirmationCode.toString().trim());
    }

    @OnClick(R.id.card_view_confirm)
    public void onClickConfirm() {
        getPresenter().startSendConfirmationCodeTask();
    }

    @OnClick(R.id.card_view_resend)
    public void onClickResend() {
        getPresenter().startResendPhoneNumberTask();
    }

    @Override
    public void showConfirmationCode(final String confirmationCode) {
        mEtConfirmationCode.getText().clear();
        mEtConfirmationCode.setText(confirmationCode);
    }

    @Override
    public void showEmptyConfirmationCode() {
        mElConfirmationCode.setError(getString(R.string.registration_confirmation_code_empty));
    }

    @Override
    public void showIncorrectConfirmationCode() {
        mElConfirmationCode.setError(getString(R.string.registration_confirmation_code_incorrect));
    }

    @Override
    public void showNextScreen() {
        showToastMessage("SUCCESS");
    }

    @Override
    public void showWrongConfirmationCode() {
        showDialogMessage(R.string.registration_dialog_wrong_code_title, R.string.registration_dialog_wrong_code_message);
    }
}
