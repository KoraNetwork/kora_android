package com.kora.android.common.utils;

import android.graphics.Rect;
import android.view.View;
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
}
