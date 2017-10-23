package com.kora.android.presentation.ui.main.fragments.transactions.filter;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.kora.android.R;
import com.kora.android.presentation.dto.TransactionFilterDto;
import com.kora.android.presentation.enums.TransactionDirection;
import com.kora.android.presentation.enums.TransactionType;

import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

import static com.kora.android.common.Keys.Args.ARG_TRANSACTION_FILTER_MODEL;

public class FilterDialog extends BottomSheetDialogFragment {

    @BindView(R.id.show_group) RadioGroup mShowGroup;
    @BindView(R.id.show_all_radio) RadioButton mShowAllRadio;
    @BindView(R.id.show_incoming_radio) RadioButton mShowIncomingRadio;
    @BindView(R.id.show_upcoming_radio) RadioButton mShowUpcomingRadio;
    @BindView(R.id.type_sent) CheckBox mTypeSentCheckbox;
    @BindView(R.id.type_requesed) CheckBox mTypeRequestedCheckbox;
    @BindView(R.id.type_borrowed) CheckBox mTypeBorrowedCheckbox;
    @BindView(R.id.type_deposit) CheckBox mTypeDepositCheckbox;

    private TransactionFilterDto mTransactionFilterDto = new TransactionFilterDto();

    @Nullable
    private OnClickListener mOnClickListener;

    public static FilterDialog newInstance(TransactionFilterDto transactionFilterDto) {
        final Bundle args = new Bundle();
        args.putParcelable(ARG_TRANSACTION_FILTER_MODEL, transactionFilterDto);
        final FilterDialog filterDialog = new FilterDialog();
        filterDialog.setArguments(args);
        return filterDialog;
    }

    public void show(FragmentManager manager) {
        show(manager, this.getClass().getName());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCanceledOnTouchOutside(false);
        if (dialog.getWindow() != null) {
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        initArguments(savedInstanceState);
        return dialog;
    }

    private void initArguments(Bundle savedInstanceState) {
        final Bundle arguments = savedInstanceState != null && savedInstanceState.containsKey(ARG_TRANSACTION_FILTER_MODEL)
                ? savedInstanceState
                : getArguments();
        if (arguments == null) return;
        mTransactionFilterDto = arguments.getParcelable(ARG_TRANSACTION_FILTER_MODEL);
        if (mTransactionFilterDto == null) mTransactionFilterDto = new TransactionFilterDto();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Window window = getDialog().getWindow();
        if (window != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_filter, container, false);
        ButterKnife.bind(this, rootView);

        setupViews();
        setupTypes();
        return rootView;
    }

    private void setupViews() {
        if (mTransactionFilterDto.getTransactionDirections() != null && mTransactionFilterDto.getTransactionDirections() == TransactionDirection.TO) {
            mShowGroup.check(mShowUpcomingRadio.getId());
        } else if (mTransactionFilterDto.getTransactionDirections() != null && mTransactionFilterDto.getTransactionDirections() == TransactionDirection.FROM) {
            mShowGroup.check(mShowIncomingRadio.getId());
        } else {
            mShowGroup.check(mShowAllRadio.getId());
        }
    }

    private void setupTypes() {
        if (mTransactionFilterDto.getTransactionTypes().size() == 0) {
            mTypeSentCheckbox.setChecked(true);
            mTypeRequestedCheckbox.setChecked(true);
            mTypeBorrowedCheckbox.setChecked(true);
            mTypeDepositCheckbox.setChecked(true);
            return;
        }
        Set<TransactionType> transactionTypes = mTransactionFilterDto.getTransactionTypes();

        if (transactionTypes.contains(TransactionType.SEND)) {
            mTypeSentCheckbox.setChecked(true);
        }
        if (transactionTypes.contains(TransactionType.REQUEST)) {
            mTypeRequestedCheckbox.setChecked(true);
        }
        if (transactionTypes.contains(TransactionType.BORROW)) {
            mTypeBorrowedCheckbox.setChecked(true);
        }
        if (transactionTypes.contains(TransactionType.DEPOSIT)) {
            mTypeDepositCheckbox.setChecked(true);
        }

    }

    @OnCheckedChanged(R.id.type_sent)
    public void onTypeSentCheckChanged(boolean isChecked) {
        if (isChecked) mTransactionFilterDto.getTransactionTypes().add(TransactionType.SEND);
        else mTransactionFilterDto.getTransactionTypes().remove(TransactionType.SEND);

    }

    @OnCheckedChanged(R.id.type_requesed)
    public void onTypeRequestedCheckChanged(boolean isChecked) {
        if (isChecked) mTransactionFilterDto.getTransactionTypes().add(TransactionType.REQUEST);
        else mTransactionFilterDto.getTransactionTypes().remove(TransactionType.REQUEST);

    }

    @OnCheckedChanged(R.id.type_borrowed)
    public void onTypeBorrowedCheckChanged(boolean isChecked) {
        if (isChecked) mTransactionFilterDto.getTransactionTypes().add(TransactionType.BORROW);
        else mTransactionFilterDto.getTransactionTypes().remove(TransactionType.BORROW);

    }

    @OnCheckedChanged(R.id.type_deposit)
    public void onTypeDepositCheckChanged(boolean isChecked) {
        if (isChecked) mTransactionFilterDto.getTransactionTypes().add(TransactionType.DEPOSIT);
        else mTransactionFilterDto.getTransactionTypes().remove(TransactionType.DEPOSIT);

    }

    @OnCheckedChanged(R.id.show_all_radio)
    public void onShowAllCheckChanged(boolean isChecked) {
        if (isChecked) mTransactionFilterDto.setTransactionDirections(null);
    }

    @OnCheckedChanged(R.id.show_incoming_radio)
    public void onShowIncomingCheckChanged(boolean isChecked) {
        if (isChecked) {
            mTransactionFilterDto.setTransactionDirections(TransactionDirection.TO);
        }
    }

    @OnCheckedChanged(R.id.show_upcoming_radio)
    public void onShowUpcomingCheckChanged(boolean isChecked) {
        if (isChecked) {
            mTransactionFilterDto.setTransactionDirections(TransactionDirection.FROM);
        }
    }

    public void setOnClickListener(@Nullable OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public boolean isShowing() {
        return getDialog() != null && getDialog().isShowing();
    }

    @OnClick(R.id.button_cancel)
    public void onCancelClicked() {
        this.dismiss();
        if (mOnClickListener == null) return;
        mOnClickListener.onCancelFilter();
    }

    @OnClick(R.id.button_done)
    public void onDoneClicked() {
        this.dismiss();
        if (mOnClickListener == null) return;
        mOnClickListener.onFilterChanged(mTransactionFilterDto);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ARG_TRANSACTION_FILTER_MODEL, mTransactionFilterDto);
    }

    public interface OnClickListener {
        void onCancelFilter();

        void onFilterChanged(final TransactionFilterDto transactionFilterDto);
    }
}
