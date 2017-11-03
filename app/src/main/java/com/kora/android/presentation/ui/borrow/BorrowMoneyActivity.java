package com.kora.android.presentation.ui.borrow;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.kora.android.R;
import com.kora.android.common.utils.DateUtils;
import com.kora.android.common.utils.StringUtils;
import com.kora.android.common.utils.ViewUtils;
import com.kora.android.di.component.ActivityComponent;
import com.kora.android.presentation.enums.ViewMode;
import com.kora.android.presentation.model.BorrowEntity;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.base.view.BaseActivity;
import com.kora.android.presentation.ui.base.view.ToolbarActivity;
import com.kora.android.presentation.ui.borrow.adapter.GuarantorsAdapter;
import com.kora.android.views.currency.CurrencyEditText;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Date;

import butterknife.BindView;

import static com.kora.android.common.Keys.Args.BORROW_ENTITY;
import static com.kora.android.common.Keys.Args.GUARANTERS_LIST;
import static com.kora.android.common.Keys.Args.USER_ENTITY;
import static com.kora.android.common.Keys.Args.VIEW_MODE;
import static com.kora.android.common.Keys.Extras.BORROW_REQUEST_EXTRA;
import static com.kora.android.common.Keys.Extras.RECEIVER_ENTITY_EXTRA;
import static com.kora.android.common.Keys.Extras.SENDER_ENTITY_EXTRA;
import static com.kora.android.data.network.Constants.API_BASE_URL;
import static com.kora.android.presentation.ui.borrow.adapter.GuarantorsAdapter.MAX_SIZE;

public class BorrowMoneyActivity extends ToolbarActivity<BorrowMoneyPresenter>
        implements BorrowMoneyView, GuarantorsAdapter.OnItemClickListener,
        DatePickerDialog.OnDateSetListener {

    public static final int REQUEST_ADD_GUARANTOR = 513;
    private static final String START_DATE_PICKER = "start_date_picker";
    private static final String MATURITI_DATE_PICKER = "maturity_date_picker";

    @BindView(R.id.toolbar) Toolbar mToolbar;

    @BindView(R.id.scroll_view) ScrollView mScrollView;
    @BindView(R.id.main_container) LinearLayout mMainContainer;

    @BindView(R.id.lender_container) LinearLayout mLenderContainer;
    @BindView(R.id.lender_avatar) ImageView mLenderAvatar;
    @BindView(R.id.lender_name) TextView mLenderName;
    @BindView(R.id.lender_phone) TextView mLenderPhone;

    @BindView(R.id.borrower_container) LinearLayout mBorrowerContainer;
    @BindView(R.id.borower_avatar) ImageView mBorrowerAvatar;
    @BindView(R.id.borower_name) TextView mBorrowerName;
    @BindView(R.id.borower_phone) TextView mBorrowerPhone;

    @BindView(R.id.borrow_status) TextView mBorrowRequestStatusLender;
    @BindView(R.id.borrow_status2) TextView mBorrowRequestStatusBorower;

    @BindView(R.id.guarantors_list) RecyclerView mGuarantorList;
    @BindView(R.id.add_guarantor_button) ImageButton mAddGuarantorBtn;

    @BindView(R.id.amount_container) LinearLayout mAmountContainer;
    @BindView(R.id.text_total_interest) TextView mTotalInterest;
    @BindView(R.id.text_total_amount) TextView mTotalAmount;
    @BindView(R.id.guarantors_title) TextView mGuarantorsTitle;

    @BindView(R.id.no_guaranter_error) TextView mNoGuaranterError;
    @BindView(R.id.edit_text_sender_amount) CurrencyEditText mSenderAmount;
    @BindView(R.id.edit_layout_amount) TextInputLayout mSenderAmountContainer;
    @BindView(R.id.edit_text_receiver_amount) CurrencyEditText mReceiverAmount;
    @BindView(R.id.edit_layout_converted_amount) TextInputLayout mReceiverAmountContainer;
    @BindView(R.id.edit_text_interesr_rate) EditText mRateEditText;
    @BindView(R.id.edit_layout_interest_rate) TextInputLayout mElRate;
    @BindView(R.id.edit_layout_start_date) TextInputLayout mElStartDate;
    @BindView(R.id.edit_text_start_date) EditText mEtStartDate;
    @BindView(R.id.edit_layout_maturity_date) TextInputLayout mElMaturityDate;
    @BindView(R.id.edit_text_maturity_date) EditText mEtMaturityDate;
    @BindView(R.id.edit_text_additional_note) EditText mEtAdditionalNote;

    @BindView(R.id.action_button) Button mActionButton;

    private GuarantorsAdapter mUserAdapter;
    private int mAmountEditTextWidth = 0;

    private ViewMode mViewMode;

    public static Intent getLaunchIntent(final BaseActivity baseActivity, final UserEntity receiver) {
        final Intent intent = new Intent(baseActivity, BorrowMoneyActivity.class);
        intent.putExtra(RECEIVER_ENTITY_EXTRA, receiver);
        intent.putExtra(VIEW_MODE, ViewMode.EDIT_MODE);
        return intent;
    }

    public static Intent getLaunchIntent(final BaseActivity baseActivity, final BorrowEntity borrowEntity) {
        final Intent intent = new Intent(baseActivity, BorrowMoneyActivity.class);
        intent.putExtra(BORROW_REQUEST_EXTRA, borrowEntity);
        intent.putExtra(VIEW_MODE, ViewMode.VIEW_MODE);
        return intent;
    }

    @Override
    public void injectToComponent(final ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_borrow_money;
    }

    @Override
    protected Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    protected int getTitleRes() {
        return 0;
    }

    @Override
    protected void onViewReady(final Bundle savedInstanceState) {
        super.onViewReady(savedInstanceState);

        initUI();

        initArguments(savedInstanceState);
        setupMode();
    }


    // ================= BASIC

    private void initUI() {
        mEtStartDate.setKeyListener(null);
        mEtMaturityDate.setKeyListener(null);

        mUserAdapter = new GuarantorsAdapter();
        mUserAdapter.setOnClickListener(this);

        mGuarantorList.setLayoutManager(new LinearLayoutManager(this));
        mGuarantorList.setAdapter(mUserAdapter);
        mGuarantorList.setItemAnimator(new DefaultItemAnimator());
        mGuarantorList.setNestedScrollingEnabled(false);
    }

    private void initArguments(final Bundle bundle) {
        if (bundle != null) {
            if (bundle.containsKey(VIEW_MODE))
                mViewMode = (ViewMode) bundle.getSerializable(VIEW_MODE);
            if (bundle.containsKey(USER_ENTITY))
                getPresenter().setSender(bundle.getParcelable(USER_ENTITY));
            if (bundle.containsKey(BORROW_ENTITY))
                getPresenter().setBorrow(bundle.getParcelable(BORROW_ENTITY));
            if (bundle.containsKey(GUARANTERS_LIST)) {
                mUserAdapter.addItems(bundle.getParcelableArrayList(GUARANTERS_LIST));
                changeAddGuarantorButtonState();
            }
        }
        if (getIntent() != null) {
            getPresenter().setSender(getIntent().getParcelableExtra(SENDER_ENTITY_EXTRA));
            getPresenter().setReceiver(getIntent().getParcelableExtra(RECEIVER_ENTITY_EXTRA));
            getPresenter().setBorrow(getIntent().getParcelableExtra(BORROW_REQUEST_EXTRA));
            mViewMode = (ViewMode) getIntent().getSerializableExtra(VIEW_MODE);
        }
    }

    private void setupMode() {
        switch (mViewMode) {
            case EDIT_MODE:
                setupEditMode();
                break;
            case VIEW_MODE:
                setupViewMode();
                break;
        }
    }

    private void setupEditMode() {
        setTitle(getString(R.string.borrow_borrow_from, getPresenter().getReceiver().getFullName()));
        mUserAdapter.setViewMode(ViewMode.EDIT_MODE);
        setEditableViews(false);
        mLenderContainer.setVisibility(View.VISIBLE);
        mBorrowerContainer.setVisibility(View.GONE);
        getPresenter().loadCurrentUser();
        UserEntity receiver = getPresenter().getReceiver();
        setupReceiver(receiver, true);
    }

    private void setupViewMode() {
        mUserAdapter.setViewMode(ViewMode.VIEW_MODE);
        setEditableViews(true);

        mEtStartDate.setText(DateUtils.getFormattedDate(DateUtils.PRETTY_DATE_PATTERN, new Date()));
    }

    // ================= COLLBACKS

    @Override
    public void showCurrentUser(UserEntity user) {
        switch (mViewMode) {
            case EDIT_MODE:
                setupSender(user, false);
                break;
            case VIEW_MODE:

                break;
        }
    }


    // ================= HELPERS

    private void changeAddGuarantorButtonState() {
        boolean visible = mUserAdapter.getItemCount() < MAX_SIZE;
        mAddGuarantorBtn.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    private void setEditableViews(boolean editable) {
        mSenderAmount.setEnabled(editable);
        mRateEditText.setEnabled(editable);
        mEtStartDate.setEnabled(editable);
        mEtMaturityDate.setEnabled(editable);
        mEtAdditionalNote.setEnabled(editable);
        mAddGuarantorBtn.setVisibility(editable ? View.VISIBLE : View.GONE);
    }

    private void setupSender(UserEntity user, boolean showMain) {
        mSenderAmount.setCurrency(user.getCurrency());
        Glide.with(this)
                .asBitmap()
                .load(API_BASE_URL + user.getFlag())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        mSenderAmount.setCompoundDrawablesRelativeWithIntrinsicBounds(new BitmapDrawable(getResources(), resource), null, null, null);
                        mSenderAmount.setCompoundDrawablePadding(ViewUtils.convertDpToPixel(12));
                    }
                });

        if (showMain) {
            Glide.with(this)
                    .load(API_BASE_URL + user.getAvatar())
                    .apply(RequestOptions.circleCropTransform())
                    .thumbnail(Glide.with(this).load(R.drawable.ic_user_default))
                    .into(mLenderAvatar);

            mLenderName.setText(user.getFullName());
            mLenderPhone.setText(StringUtils.getFormattedPhoneNumber(user.getPhoneNumber(), user.getCountryCode()));
        }
    }

    private void setupReceiver(UserEntity user, boolean showMain) {
        mReceiverAmount.setCurrency(user.getCurrency());
        Glide.with(this)
                .asBitmap()
                .load(API_BASE_URL + user.getFlag())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        mReceiverAmount.setCompoundDrawablesRelativeWithIntrinsicBounds(new BitmapDrawable(getResources(), resource), null, null, null);
                        mReceiverAmount.setCompoundDrawablePadding(ViewUtils.convertDpToPixel(12));
                    }
                });

        if (showMain) {
            Glide.with(this)
                    .load(API_BASE_URL + user.getAvatar())
                    .apply(RequestOptions.circleCropTransform())
                    .thumbnail(Glide.with(this).load(R.drawable.ic_user_default))
                    .into(mBorrowerAvatar);

            mBorrowerName.setText(user.getFullName());
            mBorrowerPhone.setText(StringUtils.getFormattedPhoneNumber(user.getPhoneNumber(), user.getCountryCode()));
        }
    }


    // ================= LISTENERS


    //
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
////        outState.putParcelable(USER_ENTITY, getPresenter().getLender());
////        outState.putSerializable(VIEW_MODE, mViewMode);
////        outState.putParcelableArrayList(GUARANTERS_LIST, (ArrayList<UserEntity>) mUserAdapter.getItems());
//    }
//
//    @Override
//    public void showBorrowRequest(BorrowEntity borrowEntity) {
//        switch (borrowEntity.getDirection()) {
//            case FROM:
//                mBorrowTypeHeader.setText(R.string.borrow_borrower_label);
//                showReceiver(borrowEntity.getReceiver());
//                break;
//            case TO:
//                mBorrowTypeHeader.setText(R.string.borrow_lender_label);
//                showSender(borrowEntity.getSender());
//                break;
//            case GUARANTOR:
//                mBorrowTypeHeader.setText(R.string.borrow_lender_label);
//                showReceiver(borrowEntity.getSender()); // TODO: should be lender and receiver
//                break;
//        }
//
//        mBorrowStatus.setText(borrowEntity.getState().text());
//        mUserAdapter.addItems(borrowEntity.getGuarantors());
//        mSenderAmount.setValue(String.format(Locale.ENGLISH, "%1$.2f", borrowEntity.getFromAmount()));
//        mSenderAmount.setCurrency(borrowEntity.getSender().getCurrency());
//        showConvertedCurrency(borrowEntity.getToAmount());
//        mEtStartDate.setText(DateUtils.getFormattedDate(DateUtils.PRETTY_DATE_PATTERN, borrowEntity.getStartDate()));
//        mEtMaturityDate.setText(DateUtils.getFormattedDate(DateUtils.PRETTY_DATE_PATTERN, borrowEntity.getMaturityDate()));
//        mRateEditText.setText(String.valueOf(borrowEntity.getRate()));
//        mEtAdditionalNote.setText(borrowEntity.getAdditionalNote());
//        changeAmountContainerOrientation();
//    }
//
//    @Override
//    public void showConvertedCurrency(double amount) {
//        mReceiverAmount.setValue(String.format(Locale.ENGLISH, "%1$.2f", amount));
//        changeAmountContainerOrientation();
//        calculateTotals();
//    }
//
//    private void changeAmountContainerOrientation() {
//        if (isTextLonger(mSenderAmount) || isTextLonger(mReceiverAmount)) {
//            if (mAmountContainer.getOrientation() != LinearLayout.VERTICAL)
//                mAmountContainer.setOrientation(LinearLayout.VERTICAL);
//        } else {
//            if (mAmountContainer.getOrientation() != LinearLayout.HORIZONTAL)
//                mAmountContainer.setOrientation(LinearLayout.HORIZONTAL);
//        }
//    }
//
//    @Override
//    public void showNoGuarantersError() {
//        mNoGuaranterError.setVisibility(View.VISIBLE);
//        ViewUtils.scrollToView(mScrollView, mMainContainer, mGuarantorsTitle);
//    }
//
//    @Override
//    public void showInvalidAmountError() {
//        mSenderAmountContainer.setError(getString(R.string.borrow_empty_amount_error_message));
//        ViewUtils.scrollToView(mScrollView, mMainContainer, mSenderAmountContainer);
//    }
//
//    @Override
//    public void showInvalidConvertedAmountError() {
//        mReceiverAmountContainer.setError(getString(R.string.borrow_empty_amount_error_message));
//        ViewUtils.scrollToView(mScrollView, mMainContainer, mReceiverAmountContainer);
//    }
//
//    @Override
//    public void showEmptyRateError() {
//        mElRate.setError(getString(R.string.borrow_empty_rate_error_message));
//        ViewUtils.scrollToView(mScrollView, mMainContainer, mElRate);
//    }
//
//    @Override
//    public void showEmptyStartDateError() {
//        mElStartDate.setError(getString(R.string.borrow_empty_start_date_error_message));
//        ViewUtils.scrollToView(mScrollView, mMainContainer, mElStartDate);
//    }
//
//    @Override
//    public void showPastStartDateError() {
//        mElStartDate.setError(getString(R.string.borrow_past_start_date_error_message));
//        ViewUtils.scrollToView(mScrollView, mMainContainer, mElStartDate);
//    }
//
//    @Override
//    public void showEmptyMaturityDateError() {
//        mElMaturityDate.setError(getString(R.string.borrow_empty_maturity_date_error_message));
//        ViewUtils.scrollToView(mScrollView, mMainContainer, mElMaturityDate);
//    }
//
//    @Override
//    public void showPastMaturityDateError() {
//        mElMaturityDate.setError(getString(R.string.borrow_past_maturity_date_error_message));
//        ViewUtils.scrollToView(mScrollView, mMainContainer, mElMaturityDate);
//    }
//
//    @Override
//    public void onBorrowRequestAdded(BorrowEntity borrowEntity) {
//        Toast.makeText(this, R.string.borrow_request_added_message, Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent();
//        intent.putExtra(Keys.Extras.EXTRA_BORROW, borrowEntity);
//        setResult(RESULT_OK, intent);
//        finishActivity();
//    }
//
//    @OnClick(R.id.add_guarantor_button)
//    public void onAddGuarantorClicked() {
//        startActivityForResult(GetContactActivity.getLaunchIntent(this,
//                getString(R.string.borrow_add_guarantor_title),
//                getExistedIds()), REQUEST_ADD_GUARANTOR);
//    }
//
//    private List<String> getExistedIds() {
//        List<String> userIds = new ArrayList<>();
////        userIds.add(getPresenter().getLender().getId());
//        List<UserEntity> items = mUserAdapter.getItems();
//        for (UserEntity entity : items) {
//            userIds.add(entity.getId());
//        }
//
//        return userIds;
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK && requestCode == REQUEST_ADD_GUARANTOR) {
//            UserEntity user = data.getParcelableExtra(EXTRA_USER);
//            mUserAdapter.addItem(user);
//            changeAddGuarantorButtonState();
//            mNoGuaranterError.setVisibility(View.GONE);
//        }
//    }
//

    //
    @Override
    public void onItemClicked(int position) {

    }

    //
    @Override
    public void onDeleteItemClicked(int position) {
//        new MultiDialog.DialogBuilder()
//                .setTitle(R.string.dialog_remove_guarantor_title)
//                .setMessage(R.string.dialog_remove_guarantor_message)
//                .setPositiveButton(R.string.dialog_remove_guarantor_positive_btn, (dialogInterface, i) -> {
//                    dialogInterface.dismiss();
//                    mUserAdapter.removeItem(position);
//                    changeAddGuarantorButtonState();
//                })
//                .setNegativeButton(R.string.dialog_remove_guarantor_negative_btn, (dialogInterface, i) -> {
//                    dialogInterface.dismiss();
//                })
//                .build(this).show();
    }

    //
//    Handler timer = new Handler();
//
//    @OnTextChanged(R.id.edit_text_sender_amount)
//    public void onAmountChanged() {
//        mSenderAmountContainer.setError(null);
//        mReceiverAmountContainer.setError(null);
//        timer.removeCallbacks(converter);
//        timer.postDelayed(converter, 500);
//
//        changeAmountContainerOrientation();
//    }
//
//    @OnTextChanged(R.id.edit_text_interesr_rate)
//    public void onRateChanged() {
//        calculateTotals();
//    }
//
//    private boolean isTextLonger(TextInputEditText editText) {
//        Editable text = editText.getText();
//        TextPaint paint = editText.getPaint();
//        float textSize = paint.measureText(text.toString());
//        int width = editText.getMeasuredWidth();
//        width -= editText.getPaddingRight();
//        width -= editText.getPaddingLeft();
////        width -= editText.getCompoundDrawables().length > 0 ? editText.getCompoundDrawables()[0].getMinimumWidth() : 0;
//        width -= editText.getCompoundDrawablePadding();
//        if (mAmountEditTextWidth > 0 && textSize >= mAmountEditTextWidth)
//            return true;
//        if (textSize >= width && width > 0) {
//            mAmountEditTextWidth = width;
//            return true;
//        }
//        return false;
//    }
//
//    Runnable converter = new Runnable() {
//        @Override
//        public void run() {
//            double val = mSenderAmount.getAmount();
////            getPresenter().convertIfNeed(val);
//        }
//    };
//
//    private void calculateTotals() {
//        double amount = mReceiverAmount.getAmount();
//        String rateString = mRateEditText.getText().toString();
//
//        if (amount == 0 || rateString.isEmpty()) {
//            mTotalInterest.setText("");
//            mTotalAmount.setText("");
//            return;
//        }
//        try {
//            double rate = Double.valueOf(rateString);
//
//            double totalInterest = amount * rate / 100;
//            double totalAmount = amount + totalInterest;
//
//            mTotalInterest.setText(getString(R.string.borrow_amount, totalInterest, mReceiverAmount.getCurrency()));
//            mTotalAmount.setText(getString(R.string.borrow_amount, totalAmount, mReceiverAmount.getCurrency()));
//
//        } catch (Exception e) {
//        }
//
//    }
//
//    @OnClick(R.id.edit_text_start_date)
//    void onStartDateClicked() {
//        mElStartDate.setError(null);
//
//        Calendar cal = DateUtils.getCalendarFromPrettyDate(mEtStartDate.getText().toString().trim());
//        if (cal == null) cal = Calendar.getInstance();
//        DatePickerDialog dpd = DatePickerDialog.newInstance(this, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
//        dpd.setAccentColor("#77cda4");
//        dpd.show(getFragmentManager(), START_DATE_PICKER);
//    }
//
//    @OnFocusChange(R.id.edit_text_start_date)
//    void onStartDateFocusChanged(View view, boolean isFocused) {
//        if (isFocused) {
//            onStartDateClicked();
//        }
//    }
//
//    @OnClick(R.id.edit_text_maturity_date)
//    void onMaturityDateClicked() {
//        mElMaturityDate.setError(null);
//
//        Calendar cal = DateUtils.getCalendarFromPrettyDate(mEtMaturityDate.getText().toString().trim());
//        if (cal == null) cal = Calendar.getInstance();
//        DatePickerDialog dpd = DatePickerDialog.newInstance(this, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
//        dpd.setAccentColor("#77cda4");
//        dpd.show(getFragmentManager(), MATURITI_DATE_PICKER);
//    }
//
//    @OnFocusChange(R.id.edit_text_maturity_date)
//    void onMaturityDateFocusChanged(View view, boolean isFocused) {
//        if (isFocused) {
//            onMaturityDateClicked();
//        }
//    }
//
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
//        final String prettyDate = DateUtils.getPrettyDate(year, monthOfYear, dayOfMonth);
//        switch (view.getTag()) {
//            case START_DATE_PICKER:
//                mEtStartDate.setText(prettyDate);
//                break;
//            case MATURITI_DATE_PICKER:
//                mEtMaturityDate.setText(prettyDate);
//                break;
//        }
    }
//
//    @OnClick(R.id.action_button)
//    public void onBorrowClicked() {
//        mSenderAmountContainer.setError(null);
//        mReceiverAmountContainer.setError(null);
//        mElRate.setError(null);
//
//        getPresenter().onBorrowClicked(mUserAdapter.getItems(),
//                mSenderAmount.getAmount(),
//                mReceiverAmount.getAmount(),
//                mRateEditText.getText().toString().trim(),
//                mEtStartDate.getText().toString().trim(),
//                mEtMaturityDate.getText().toString().trim(),
//                mEtAdditionalNote.getText().toString().trim());
//    }


}
