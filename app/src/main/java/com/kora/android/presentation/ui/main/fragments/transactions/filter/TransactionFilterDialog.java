package com.kora.android.presentation.ui.main.fragments.transactions.filter;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.kora.android.R;
import com.kora.android.presentation.enums.Direction;
import com.kora.android.presentation.enums.TransactionState;
import com.kora.android.presentation.enums.TransactionType;
import com.kora.android.presentation.ui.base.adapter.filter.FilterDialog;

import java.util.Set;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

import static com.kora.android.common.Keys.Args.ARG_TRANSACTION_FILTER_MODEL;

public class TransactionFilterDialog extends FilterDialog<TransactionFilterModel> {

    @BindView(R.id.show_group) RadioGroup mShowGroup;
    @BindView(R.id.show_all_radio) RadioButton mShowAllRadio;
    @BindView(R.id.show_incoming_radio) RadioButton mShowIncomingRadio;
    @BindView(R.id.show_upcoming_radio) RadioButton mShowUpcomingRadio;
    @BindView(R.id.type_sent) CheckBox mTypeSentCheckbox;
    @BindView(R.id.type_requesed) CheckBox mTypeRequestedCheckbox;
    @BindView(R.id.type_borrowed_funded) CheckBox mTypeBorrowedFundedCheckbox;
    @BindView(R.id.type_borrowed_paid_back) CheckBox mTypeBorrowedPaidBackCheckbox;
    @BindView(R.id.state_success) CheckBox mSateSuccessCheckbox;
    @BindView(R.id.state_pending) CheckBox mStatePendingCheckbox;
    @BindView(R.id.state_error) CheckBox mStateErrorCheckbox;

    private TransactionFilterModel mTransactionFilterModel = new TransactionFilterModel();

    @Override
    protected int getLayoutRes() {
        return R.layout.dialog_transaction_filter;
    }

    public static TransactionFilterDialog newInstance(TransactionFilterModel transactionFilterModel) {
        final Bundle args = new Bundle();
        args.putParcelable(ARG_TRANSACTION_FILTER_MODEL, transactionFilterModel);
        final TransactionFilterDialog transactionFilterDialog = new TransactionFilterDialog();
        transactionFilterDialog.setArguments(args);
        return transactionFilterDialog;
    }

    @Override
    protected void initArguments(Bundle savedInstanceState) {
        final Bundle arguments = savedInstanceState != null && savedInstanceState.containsKey(ARG_TRANSACTION_FILTER_MODEL)
                ? savedInstanceState
                : getArguments();
        if (arguments == null) return;
        mTransactionFilterModel = arguments.getParcelable(ARG_TRANSACTION_FILTER_MODEL);
        if (mTransactionFilterModel == null) mTransactionFilterModel = new TransactionFilterModel();
    }

    @Override
    protected void onViewReady() {
        setupViews();
        setupTypes();
        setupStates();
    }

    private void setupViews() {
        if (mTransactionFilterModel.getDirections() != null && mTransactionFilterModel.getDirections() == Direction.TO) {
            mShowGroup.check(mShowIncomingRadio.getId());
        } else if (mTransactionFilterModel.getDirections() != null && mTransactionFilterModel.getDirections() == Direction.FROM) {
            mShowGroup.check(mShowUpcomingRadio.getId());
        } else {
            mShowGroup.check(mShowAllRadio.getId());
        }
    }

    private void setupTypes() {
        if (mTransactionFilterModel.getTransactionTypes().size() == 0) {
            mTypeSentCheckbox.setChecked(true);
            mTypeRequestedCheckbox.setChecked(true);
            mTypeBorrowedFundedCheckbox.setChecked(true);
            mTypeBorrowedPaidBackCheckbox.setChecked(true);
            return;
        }
        final Set<TransactionType> transactionTypes = mTransactionFilterModel.getTransactionTypes();

        if (transactionTypes.contains(TransactionType.SEND)) {
            mTypeSentCheckbox.setChecked(true);
        }
        if (transactionTypes.contains(TransactionType.REQUEST)) {
            mTypeRequestedCheckbox.setChecked(true);
        }
        if (transactionTypes.contains(TransactionType.BORROWFUND)) {
            mTypeBorrowedFundedCheckbox.setChecked(true);
        }
        if (transactionTypes.contains(TransactionType.BORROWFUND)) {
            mTypeBorrowedPaidBackCheckbox.setChecked(true);
        }
    }

    private void setupStates() {
        if (mTransactionFilterModel.getTransactionStates().size() == 0) {
            mSateSuccessCheckbox.setChecked(true);
            mStatePendingCheckbox.setChecked(true);
            mStateErrorCheckbox.setChecked(true);
            return;
        }
        final Set<TransactionState> transactionStates = mTransactionFilterModel.getTransactionStates();

        if (transactionStates.contains(TransactionState.SUCCESS)) {
            mSateSuccessCheckbox.setChecked(true);
        }
        if (transactionStates.contains(TransactionState.PENDING)) {
            mStatePendingCheckbox.setChecked(true);
        }
        if (transactionStates.contains(TransactionState.ERROR)) {
            mStateErrorCheckbox.setChecked(true);
        }
    }

    @OnCheckedChanged(R.id.show_all_radio)
    public void onShowAllCheckChanged(boolean isChecked) {
        if (isChecked) mTransactionFilterModel.setDirections(null);
    }

    @OnCheckedChanged(R.id.show_incoming_radio)
    public void onShowIncomingCheckChanged(boolean isChecked) {
        if (isChecked) {
            mTransactionFilterModel.setDirections(Direction.TO);
        }
    }

    @OnCheckedChanged(R.id.show_upcoming_radio)
    public void onShowUpcomingCheckChanged(boolean isChecked) {
        if (isChecked) {
            mTransactionFilterModel.setDirections(Direction.FROM);
        }
    }

    @OnCheckedChanged(R.id.type_sent)
    public void onTypeSentCheckChanged(boolean isChecked) {
        if (isChecked) mTransactionFilterModel.getTransactionTypes().add(TransactionType.SEND);
        else mTransactionFilterModel.getTransactionTypes().remove(TransactionType.SEND);
    }

    @OnCheckedChanged(R.id.type_requesed)
    public void onTypeRequestedCheckChanged(boolean isChecked) {
        if (isChecked) mTransactionFilterModel.getTransactionTypes().add(TransactionType.REQUEST);
        else mTransactionFilterModel.getTransactionTypes().remove(TransactionType.REQUEST);
    }

    @OnCheckedChanged(R.id.type_borrowed_funded)
    public void onTypeBorrowedFundCheckChanged(boolean isChecked) {
        if (isChecked) mTransactionFilterModel.getTransactionTypes().add(TransactionType.BORROWFUND);
        else mTransactionFilterModel.getTransactionTypes().remove(TransactionType.BORROWFUND);
    }

    @OnCheckedChanged(R.id.type_borrowed_paid_back)
    public void onTypeBorrowedPaidBackCheckChanged(boolean isChecked) {
        if (isChecked) mTransactionFilterModel.getTransactionTypes().add(TransactionType.BORROWPAYBACK);
        else mTransactionFilterModel.getTransactionTypes().remove(TransactionType.BORROWPAYBACK);
    }

    @OnCheckedChanged(R.id.state_success)
    public void onStateSuccessCheckChanged(boolean isChecked) {
        if (isChecked) mTransactionFilterModel.getTransactionStates().add(TransactionState.SUCCESS);
        else mTransactionFilterModel.getTransactionStates().remove(TransactionState.SUCCESS);
    }

    @OnCheckedChanged(R.id.state_pending)
    public void onStatePendingCheckChanged(boolean isChecked) {
        if (isChecked) mTransactionFilterModel.getTransactionStates().add(TransactionState.PENDING);
        else mTransactionFilterModel.getTransactionStates().remove(TransactionState.PENDING);
    }

    @OnCheckedChanged(R.id.state_error)
    public void onStateErrorCheckChanged(boolean isChecked) {
        if (isChecked) mTransactionFilterModel.getTransactionStates().add(TransactionState.ERROR);
        else mTransactionFilterModel.getTransactionStates().remove(TransactionState.ERROR);
    }

    @OnClick(R.id.button_cancel)
    public void onCancelClicked() {
        this.dismiss();
        if (mOnFilterListener == null) return;
        mOnFilterListener.onCancelFilter();
    }

    @OnClick(R.id.button_done)
    public void onDoneClicked() {
        this.dismiss();
        if (mOnFilterListener == null) return;
        mOnFilterListener.onFilterChanged(mTransactionFilterModel);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ARG_TRANSACTION_FILTER_MODEL, mTransactionFilterModel);
    }
}
