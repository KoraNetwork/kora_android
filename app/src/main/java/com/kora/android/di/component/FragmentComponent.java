package com.kora.android.di.component;

import com.kora.android.di.annotation.PerFragment;
import com.kora.android.di.module.FragmentModule;
import com.kora.android.presentation.ui.main.fragments.borrow.BorrowFragment;
import com.kora.android.presentation.ui.main.fragments.home.HomeFragment;
import com.kora.android.presentation.ui.main.fragments.request.RequestFragment;
import com.kora.android.presentation.ui.main.fragments.send.SendFragment;
import com.kora.android.presentation.ui.main.fragments.transactions.TransactionsFragment;

import dagger.Subcomponent;

@PerFragment
@Subcomponent(modules = FragmentModule.class)
public interface FragmentComponent {

    void inject(final HomeFragment homeFragment);
    void inject(final SendFragment sendFragment);
    void inject(final BorrowFragment borrowFragment);
    void inject(final RequestFragment requestFragment);
    void inject(final TransactionsFragment transactionsFragment);
}