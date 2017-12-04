package com.kora.android.presentation.ui.main.fragments.deposit_withdraw.filter;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.kora.android.R;
import com.kora.android.presentation.enums.DepositWithdrawState;
import com.kora.android.presentation.ui.base.adapter.filter.FilterDialog;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

import static com.kora.android.common.Keys.Args.ARG_DEPOSIT_FILTER_MODEL;

public class DepositWithdrawFilterDialog extends FilterDialog<DepositWithdrawFilterModel> {

    @BindView(R.id.radio_group_state)
    RadioGroup mRgState;
    @BindView(R.id.radio_button_show_all)
    RadioButton mRbShowAll;
    @BindView(R.id.radio_button_in_progress)
    RadioButton mRbInProgress;
    @BindView(R.id.radio_button_rejected)
    RadioButton mRbRejected;

    private DepositWithdrawFilterModel mDepositWithdrawFilterModel = new DepositWithdrawFilterModel();

    @Override
    protected int getLayoutRes() {
        return R.layout.dialog_deposit_filter;
    }

    public static DepositWithdrawFilterDialog newInstance(final DepositWithdrawFilterModel depositWithdrawFilterModel) {
        final Bundle arguments = new Bundle();
        arguments.putParcelable(ARG_DEPOSIT_FILTER_MODEL, depositWithdrawFilterModel);
        final DepositWithdrawFilterDialog filterDialog = new DepositWithdrawFilterDialog();
        filterDialog.setArguments(arguments);
        return filterDialog;
    }

    @Override
    protected void initArguments(final Bundle savedInstanceState) {
        final Bundle arguments = savedInstanceState != null && savedInstanceState.containsKey(ARG_DEPOSIT_FILTER_MODEL)
                ? savedInstanceState
                : getArguments();
        if (arguments == null) return;
        mDepositWithdrawFilterModel = arguments.getParcelable(ARG_DEPOSIT_FILTER_MODEL);
        if (mDepositWithdrawFilterModel == null) mDepositWithdrawFilterModel = new DepositWithdrawFilterModel();
    }

    @Override
    protected void onViewReady() {
        setupStates();
    }

    private void setupStates() {
        if (mDepositWithdrawFilterModel.getState() != null && mDepositWithdrawFilterModel.getState() == DepositWithdrawState.INPROGRESS) {
            mRgState.check(mRbInProgress.getId());
        } else if (mDepositWithdrawFilterModel.getState() != null && mDepositWithdrawFilterModel.getState() == DepositWithdrawState.REJECTED) {
            mRgState.check(mRbRejected.getId());
        } else {
            mRgState.check(mRbShowAll.getId());
        }
    }

    @OnCheckedChanged(R.id.radio_button_show_all)
    public void onShowAllCheckChanged(final boolean isChecked) {
        if (isChecked)
            mDepositWithdrawFilterModel.setState(null);
    }

    @OnCheckedChanged(R.id.radio_button_in_progress)
    public void onInProgressCheckChanged(final boolean isChecked) {
        if (isChecked)
            mDepositWithdrawFilterModel.setState(DepositWithdrawState.INPROGRESS);
    }

    @OnCheckedChanged(R.id.radio_button_rejected)
    public void onRejectedCheckChanged(final boolean isChecked) {
        if (isChecked)
            mDepositWithdrawFilterModel.setState(DepositWithdrawState.REJECTED);
    }

    @OnClick(R.id.text_view_cancel)
    public void onCancelClicked() {
        this.dismiss();
        if (mOnFilterListener == null)
            return;
        mOnFilterListener.onCancelFilter();
    }

    @OnClick(R.id.text_view_done)
    public void onDoneClicked() {
        this.dismiss();
        if (mOnFilterListener == null)
            return;
        mOnFilterListener.onFilterChanged(mDepositWithdrawFilterModel);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ARG_DEPOSIT_FILTER_MODEL, mDepositWithdrawFilterModel);
    }
}
