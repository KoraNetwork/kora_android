package com.kora.android.presentation.ui.borrow;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextPaint;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.kora.android.R;
import com.kora.android.common.utils.StringUtils;
import com.kora.android.common.utils.ViewUtils;
import com.kora.android.di.component.ActivityComponent;
import com.kora.android.presentation.model.UserEntity;
import com.kora.android.presentation.ui.base.custom.MultiDialog;
import com.kora.android.presentation.ui.base.view.BaseActivity;
import com.kora.android.presentation.ui.base.view.ToolbarActivity;
import com.kora.android.presentation.ui.borrow.adapter.GuarantorsAdapter;
import com.kora.android.presentation.ui.common.add_contact.GetContactActivity;
import com.kora.android.views.currency.CurrencyEditText;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import static com.kora.android.common.Keys.Args.GUARANTERS_LIST;
import static com.kora.android.common.Keys.Args.USER_ENTITY;
import static com.kora.android.common.Keys.Extras.EXTRA_USER;
import static com.kora.android.data.network.Constants.API_BASE_URL;
import static com.kora.android.presentation.ui.borrow.adapter.GuarantorsAdapter.MAX_SIZE;

public class BorrowMoneyActivity extends ToolbarActivity<BorrowMoneyPresenter>
        implements BorrowMoneyView, GuarantorsAdapter.OnItemClickListener {

    public static final int REQUEST_ADD_GUARANTOR = 513;

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.user_image) ImageView mLenderAvatar;
    @BindView(R.id.lender_name) TextView mLenderName;
    @BindView(R.id.lender_phone) TextView mLenderPhone;
    @BindView(R.id.guarantors_list) RecyclerView mGuarantorList;
    @BindView(R.id.add_guarantor_button) ImageButton mAddGuarantorBtn;
    @BindView(R.id.his_suffix) TextView mHisSuffixText;
    @BindView(R.id.my_suffix) TextView mMySuffixText;
    @BindView(R.id.edit_text_sender_amount) CurrencyEditText mSenderAmount;
    @BindView(R.id.edit_layout_amount) TextInputLayout mSenderAmountContainer;
    @BindView(R.id.edit_text_receiver_amount) CurrencyEditText mReceiverAmount;
    @BindView(R.id.edit_layout_converted_amount) TextInputLayout mReceiverAmountContainer;
    @BindView(R.id.amount_container) LinearLayout mAmountContainer;

    private GuarantorsAdapter mUserAdapter;
    private int mAmountEditTextWidth = 0;

    public static Intent getLaunchIntent(final BaseActivity baseActivity,
                                         final UserEntity userEntity) {
        final Intent intent = new Intent(baseActivity, BorrowMoneyActivity.class);
        intent.putExtra(USER_ENTITY, userEntity);
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

        if (savedInstanceState == null) {
            getPresenter().getCurrentUser();
        }

        initArguments(savedInstanceState);
    }

    private void initArguments(final Bundle bundle) {
        if (bundle != null) {
            if (bundle.containsKey(USER_ENTITY))
                getPresenter().setLender(bundle.getParcelable(USER_ENTITY));
            if (bundle.containsKey(GUARANTERS_LIST)) {
                mUserAdapter.addItems(bundle.getParcelableArrayList(GUARANTERS_LIST));
                changeAddGuarantorButtonState();
            }
        }
        if (getIntent() != null) {
            getPresenter().setLender(getIntent().getParcelableExtra(USER_ENTITY));
        }
    }

    private void initUI() {
        mUserAdapter = new GuarantorsAdapter();
        mUserAdapter.setOnClickListener(this);

        mGuarantorList.setLayoutManager(new LinearLayoutManager(this));
        mGuarantorList.setAdapter(mUserAdapter);
        mGuarantorList.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(USER_ENTITY, getPresenter().getLender());
        outState.putParcelableArrayList(GUARANTERS_LIST, (ArrayList<UserEntity>) mUserAdapter.getItems());
    }

    @Override
    public void showLender(UserEntity lender) {
        setTitle(getString(R.string.borrow_borrow_from, lender.getFullName()));
        Glide.with(this)
                .load(API_BASE_URL + lender.getAvatar())
                .apply(RequestOptions.circleCropTransform())
                .thumbnail(Glide.with(this).load(R.drawable.ic_user_default))
                .into(mLenderAvatar);

        Glide.with(this)
                .asBitmap()
                .load(API_BASE_URL + lender.getFlag())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        mReceiverAmount.setCompoundDrawablesRelativeWithIntrinsicBounds(new BitmapDrawable(getResources(), resource), null, null, null);
                        mReceiverAmount.setCompoundDrawablePadding(ViewUtils.convertDpToPixel(12));
                    }
                });

//        mHisSuffixText.setText(lender.getCurrency());
        mReceiverAmount.setCurrency(lender.getCurrency());
        mLenderName.setText(lender.getFullName());
        mLenderPhone.setText(StringUtils.getFormattedPhoneNumber(lender.getPhoneNumber()));
    }

    @Override
    public void showConvertedCurrency(double amount, String currency) {
        mReceiverAmount.setText(String.format(Locale.ENGLISH, "%1$.2f", amount));
        changeAmountContainerOrientation();
    }

    private void changeAmountContainerOrientation() {
        if (isTextLonger(mSenderAmount) || isTextLonger(mReceiverAmount)) {
            if (mAmountContainer.getOrientation() != LinearLayout.VERTICAL)
                mAmountContainer.setOrientation(LinearLayout.VERTICAL);
        } else {
            if (mAmountContainer.getOrientation() != LinearLayout.HORIZONTAL)
                mAmountContainer.setOrientation(LinearLayout.HORIZONTAL);
        }
    }

    @Override
    public void retrieveSenderCurrency(UserEntity userEntity) {
//        mMySuffixText.setText(userEntity.getCurrency());
        mSenderAmount.setCurrency(userEntity.getCurrency());
        mSenderAmount.setCurrency(userEntity.getCurrency());
        Glide.with(this)
                .asBitmap()
                .load(API_BASE_URL + userEntity.getFlag())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        mSenderAmount.setCompoundDrawablesRelativeWithIntrinsicBounds(new BitmapDrawable(getResources(), resource), null, null, null);
                        mSenderAmount.setCompoundDrawablePadding(ViewUtils.convertDpToPixel(12));
                    }
                });
    }

    @OnClick(R.id.add_guarantor_button)
    public void onAddGuarantorClicked() {
        startActivityForResult(GetContactActivity.getLaunchIntent(this, getString(R.string.borrow_add_guarantor_title)), REQUEST_ADD_GUARANTOR);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_ADD_GUARANTOR) {
            UserEntity user = data.getParcelableExtra(EXTRA_USER);
            mUserAdapter.addItem(user);
            changeAddGuarantorButtonState();
        }
    }

    private void changeAddGuarantorButtonState() {
        boolean visible = mUserAdapter.getItemCount() >= MAX_SIZE;
        mAddGuarantorBtn.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onItemClicked(int position) {

    }

    @Override
    public void onDeleteItemClicked(int position) {
        new MultiDialog.DialogBuilder()
                .setTitle(R.string.dialog_remove_guarantor_title)
                .setMessage(R.string.dialog_remove_guarantor_message)
                .setPositiveButton(R.string.dialog_remove_guarantor_positive_btn, (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    mUserAdapter.removeItem(position);
                    changeAddGuarantorButtonState();
                })
                .setNegativeButton(R.string.dialog_remove_guarantor_negative_btn, (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                })
                .build(this).show();
    }

    Handler timer = new Handler();

    @OnTextChanged(R.id.edit_text_sender_amount)
    public void onAmountChanged() {
        mSenderAmountContainer.setError(null);
        mReceiverAmountContainer.setError(null);
        timer.removeCallbacks(converter);
        timer.postDelayed(converter, 500);

        changeAmountContainerOrientation();
    }

    private boolean isTextLonger(TextInputEditText editText) {
        Editable text = editText.getText();
        TextPaint paint = editText.getPaint();
        float textSize = paint.measureText(text.toString());
        int width = editText.getMeasuredWidth();
        width -= editText.getPaddingRight();
        width -= editText.getPaddingLeft();
//        width -= editText.getCompoundDrawables().length > 0 ? editText.getCompoundDrawables()[0].getMinimumWidth() : 0;
        width -= editText.getCompoundDrawablePadding();
        if (mAmountEditTextWidth > 0 && textSize >= mAmountEditTextWidth)
            return true;
        if (textSize >= width) {
            mAmountEditTextWidth = width;
            return true;
        }
        return false;
    }

    Runnable converter = new Runnable() {
        @Override
        public void run() {
            long val = mSenderAmount.getRawValue();
            getPresenter().convertIfNeed(val);
        }
    };
}
