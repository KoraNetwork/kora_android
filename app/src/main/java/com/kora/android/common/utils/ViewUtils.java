package com.kora.android.common.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.text.InputFilter;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ScrollView;

public class ViewUtils {

    public static void scrollToView(final ScrollView scrollView,
                                    final View parentView,
                                    final View childView) {
        final long delay = 250;
        scrollView.postDelayed(() -> {
            final Rect rect = new Rect();
            childView.getHitRect(rect);
            scrollView.requestChildRectangleOnScreen(parentView, rect, false);
        }, delay);
    }

    public static void setMaxLength(final EditText editText,
                                    final String countryPhoneCode) {
        int maxLength = 13 - countryPhoneCode.length();
        final InputFilter[] inputFilters = new InputFilter[1];
        inputFilters[0] = new InputFilter.LengthFilter(maxLength);
        editText.setFilters(inputFilters);
    }

    public static void hideKeyboard(final Activity activity) {
        final View view = activity.getCurrentFocus();
        if (view != null) {
            final InputMethodManager inputMethodManager =
                    (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
