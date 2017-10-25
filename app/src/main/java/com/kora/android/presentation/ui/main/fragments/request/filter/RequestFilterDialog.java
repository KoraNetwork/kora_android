package com.kora.android.presentation.ui.main.fragments.request.filter;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.kora.android.R;
import com.kora.android.presentation.enums.Direction;
import com.kora.android.presentation.enums.RequestState;
import com.kora.android.presentation.ui.base.adapter.filter.FilterDialog;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

import static com.kora.android.common.Keys.Args.ARG_REQUEST_FILTER_MODEL;

public class RequestFilterDialog extends FilterDialog<RequestFilterModel> {

    @BindView(R.id.show_group) RadioGroup mShowGroup;
    @BindView(R.id.show_all_radio) RadioButton mShowAllRadio;
    @BindView(R.id.show_incoming_radio) RadioButton mShowIncomingRadio;
    @BindView(R.id.show_upcoming_radio) RadioButton mShowUpcomingRadio;

    @BindView(R.id.type_group) RadioGroup mTypeGroup;
    @BindView(R.id.type_all_radio) RadioButton mTypeAllRadio;
    @BindView(R.id.type_in_progress_radio) RadioButton mTypeInProgressRadio;
    @BindView(R.id.type_rejected_radio) RadioButton mTypeRejectedRadio;

    private RequestFilterModel mRequestFilterModel = new RequestFilterModel();

    @Override
    protected int getLayoutRes() {
        return R.layout.dialog_request_filter;
    }

    public static RequestFilterDialog newInstance(RequestFilterModel requestFilterModel) {
        final Bundle args = new Bundle();
        args.putParcelable(ARG_REQUEST_FILTER_MODEL, requestFilterModel);
        final RequestFilterDialog filterDialog = new RequestFilterDialog();
        filterDialog.setArguments(args);
        return filterDialog;
    }

    @Override
    protected void initArguments(Bundle savedInstanceState) {
        final Bundle arguments = savedInstanceState != null && savedInstanceState.containsKey(ARG_REQUEST_FILTER_MODEL)
                ? savedInstanceState
                : getArguments();
        if (arguments == null) return;
        mRequestFilterModel = arguments.getParcelable(ARG_REQUEST_FILTER_MODEL);
        if (mRequestFilterModel == null) mRequestFilterModel = new RequestFilterModel();
    }

    @Override
    protected void onViewReady() {
        setupViews();
        setupTypes();
    }

    private void setupViews() {
        if (mRequestFilterModel.getDirection() != null && mRequestFilterModel.getDirection() == Direction.TO) {
            mShowGroup.check(mShowUpcomingRadio.getId());
        } else if (mRequestFilterModel.getDirection() != null && mRequestFilterModel.getDirection() == Direction.FROM) {
            mShowGroup.check(mShowIncomingRadio.getId());
        } else {
            mShowGroup.check(mShowAllRadio.getId());
        }
    }

    private void setupTypes() {
        if (mRequestFilterModel.getRequestStyte() != null && mRequestFilterModel.getRequestStyte() == RequestState.INPROGRESS) {
            mTypeGroup.check(mTypeInProgressRadio.getId());
        } else if (mRequestFilterModel.getRequestStyte() != null && mRequestFilterModel.getRequestStyte() == RequestState.REJECTED) {
            mTypeGroup.check(mTypeRejectedRadio.getId());
        } else {
            mTypeGroup.check(mTypeAllRadio.getId());
        }

    }

    @OnCheckedChanged(R.id.show_all_radio)
    public void onShowAllCheckChanged(boolean isChecked) {
        if (isChecked) mRequestFilterModel.setDirection(null);
    }

    @OnCheckedChanged(R.id.type_all_radio)
    public void onTypeAllCheckChanged(boolean isChecked) {
        if (isChecked) mRequestFilterModel.setRequestStyte(null);
    }

    @OnCheckedChanged(R.id.show_incoming_radio)
    public void onShowIncomingCheckChanged(boolean isChecked) {
        if (isChecked) {
            mRequestFilterModel.setDirection(Direction.TO);
        }
    }

    @OnCheckedChanged(R.id.type_in_progress_radio)
    public void onTypeInProgressCheckChanged(boolean isChecked) {
        if (isChecked) {
            mRequestFilterModel.setRequestStyte(RequestState.INPROGRESS);
        }
    }

    @OnCheckedChanged(R.id.show_upcoming_radio)
    public void onShowUpcomingCheckChanged(boolean isChecked) {
        if (isChecked) {
            mRequestFilterModel.setDirection(Direction.FROM);
        }
    }

    @OnCheckedChanged(R.id.type_rejected_radio)
    public void onTypeRejectedCheckChanged(boolean isChecked) {
        if (isChecked) {
            mRequestFilterModel.setRequestStyte(RequestState.REJECTED);
        }
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
        mOnFilterListener.onFilterChanged(mRequestFilterModel);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(ARG_REQUEST_FILTER_MODEL, mRequestFilterModel);
    }
}
