package com.kora.android.presentation.ui.main.fragments.deposit.filter;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.kora.android.R;
import com.kora.android.presentation.enums.DepositState;
import com.kora.android.presentation.ui.base.adapter.filter.FilterDialog;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

import static com.kora.android.common.Keys.Args.ARG_DEPOSIT_FILTER_MODEL;

public class DepositFilterDialog extends FilterDialog<DepositFilterModel> {

    @BindView(R.id.radio_group_state)
    RadioGroup mRgState;
    @BindView(R.id.radio_button_show_all)
    RadioButton mRbShowAll;
    @BindView(R.id.radio_button_in_progress)
    RadioButton mRbInProgress;
    @BindView(R.id.radio_button_rejected)
    RadioButton mRbRejected;

    private DepositFilterModel mDepositFilterModel = new DepositFilterModel();

    @Override
    protected int getLayoutRes() {
        return R.layout.dialog_deposit_filter;
    }

    public static DepositFilterDialog newInstance(final DepositFilterModel depositFilterModel) {
        final Bundle arguments = new Bundle();
        arguments.putParcelable(ARG_DEPOSIT_FILTER_MODEL, depositFilterModel);
        final DepositFilterDialog filterDialog = new DepositFilterDialog();
        filterDialog.setArguments(arguments);
        return filterDialog;
    }

    @Override
    protected void initArguments(final Bundle savedInstanceState) {
        final Bundle arguments = savedInstanceState != null && savedInstanceState.containsKey(ARG_DEPOSIT_FILTER_MODEL)
                ? savedInstanceState
                : getArguments();
        if (arguments == null) return;
        mDepositFilterModel = arguments.getParcelable(ARG_DEPOSIT_FILTER_MODEL);
        if (mDepositFilterModel == null) mDepositFilterModel = new DepositFilterModel();
    }

    @Override
    protected void onViewReady() {
        setupStates();
    }

    private void setupStates() {
        if (mDepositFilterModel.getState() != null && mDepositFilterModel.getState() == DepositState.INPROGRESS) {
            mRgState.check(mRbInProgress.getId());
        } else if (mDepositFilterModel.getState() != null && mDepositFilterModel.getState() == DepositState.REJECTED) {
            mRgState.check(mRbRejected.getId());
        } else {
            mRgState.check(mRbShowAll.getId());
        }
    }

    @OnCheckedChanged(R.id.radio_button_show_all)
    public void onShowAllCheckChanged(final boolean isChecked) {
        if (isChecked)
            mDepositFilterModel.setState(null);
    }

    @OnCheckedChanged(R.id.radio_button_in_progress)
    public void onInProgressCheckChanged(final boolean isChecked) {
        if (isChecked)
            mDepositFilterModel.setState(DepositState.INPROGRESS);
    }

    @OnCheckedChanged(R.id.radio_button_rejected)
    public void onRejectedCheckChanged(final boolean isChecked) {
        if (isChecked)
            mDepositFilterModel.setState(DepositState.REJECTED);
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
        mOnFilterListener.onFilterChanged(mDepositFilterModel);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ARG_DEPOSIT_FILTER_MODEL, mDepositFilterModel);
    }
}
