package com.kora.android.presentation.ui.base.adapter.filter;

public interface OnFilterListener<T extends FilterModel> {

    void onCancelFilter();

    void onFilterChanged(final T t);
}
