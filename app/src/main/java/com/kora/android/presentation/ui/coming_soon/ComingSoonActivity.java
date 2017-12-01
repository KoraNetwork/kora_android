package com.kora.android.presentation.ui.coming_soon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.kora.android.R;
import com.kora.android.di.component.ActivityComponent;
import com.kora.android.presentation.enums.ComingSoonType;
import com.kora.android.presentation.ui.base.view.BaseActivity;
import com.kora.android.presentation.ui.base.view.ToolbarActivity;

import butterknife.BindView;

import static com.kora.android.common.Keys.Extras.COMING_SOON_TYPE;

public class ComingSoonActivity extends ToolbarActivity<ComingSoonPresenter> implements ComingSoonView {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    public static Intent getLaunchIntent(final BaseActivity baseActivity,
                                         final ComingSoonType comingSoonType) {
        final Intent intent = new Intent(baseActivity, ComingSoonActivity.class);
        intent.putExtra(COMING_SOON_TYPE, comingSoonType);
        return intent;
    }

    @Override
    public void injectToComponent(final ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_coming_soon;
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
    protected void onViewReady(Bundle savedInstanceState) {
        super.onViewReady(savedInstanceState);

        initArguments(savedInstanceState);
        initUI();
    }

    private void initArguments(final Bundle bundle) {
        if (bundle != null) {
            if (bundle.containsKey(COMING_SOON_TYPE))
                getPresenter().setComingSoonType((ComingSoonType) bundle.getSerializable(COMING_SOON_TYPE));
        }
        if (getIntent() != null) {
            if (getIntent().hasExtra(COMING_SOON_TYPE))
                getPresenter().setComingSoonType((ComingSoonType) getIntent().getSerializableExtra(COMING_SOON_TYPE));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(COMING_SOON_TYPE, getPresenter().getComingSoonType());
    }

    private void initUI() {
        switch (getPresenter().getComingSoonType()) {
            case ADD_NEW:
                setTitle(R.string.coming_soon_add_new);
                break;
            case AGENT:
                setTitle(R.string.coming_soon_agent);
                break;
            case MERCHANT:
                setTitle(R.string.coming_soon_merchant);
                break;
            case LENDING:
                setTitle(R.string.coming_soon_lending);
                break;
            case COOPERATIVE:
                setTitle(R.string.coming_soon_cooperative);
                break;
        }
    }
}
