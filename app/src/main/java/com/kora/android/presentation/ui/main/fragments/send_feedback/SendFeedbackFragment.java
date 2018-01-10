package com.kora.android.presentation.ui.main.fragments.send_feedback;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.kora.android.R;
import com.kora.android.di.component.FragmentComponent;
import com.kora.android.presentation.ui.base.backstack.StackFragment;
import com.kora.android.presentation.ui.base.view.BaseFragment;

import butterknife.BindView;

public class SendFeedbackFragment extends StackFragment<SendFeedbackPresenter> implements SendFeedbackView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    public static BaseFragment getNewInstance() {
        return new SendFeedbackFragment();
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_send_feedback;
    }

    @Override
    public void injectToComponent(final FragmentComponent fragmentComponent) {
        fragmentComponent.inject(this);
    }

    @Override
    protected Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    protected int getTitle() {
        return R.string.send_feedback_title;
    }

    @Override
    protected void onViewReady(final Bundle savedInstanceState) {

    }
}
