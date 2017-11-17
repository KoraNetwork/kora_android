package com.kora.android.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.design.widget.TextInputLayout;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ScrollView;

import com.kora.android.R;

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
        int maxLength = 14 - countryPhoneCode.length();
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

    public static int convertDpToPixel(float dp){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }

    public static void setRequired(TextInputLayout view, String hint, boolean isRequired) {
        Context context = view.getContext();
        view.setHint(isRequired ?
                TextUtils.concat(
                        hint, " ",
                        context.getString(R.string.required_asterisk))
                : hint
        );
    }

    public static void clearFocus(final View ... viewArray) {
        for (View view : viewArray) {
            view.clearFocus();
        }
    }

    public static void deleteErrors(final TextInputLayout ... textInputLayoutArray) {
        for (TextInputLayout textInputLayout : textInputLayoutArray) {
            textInputLayout.setError(null);
        }
    }
}
