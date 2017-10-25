package com.kora.android.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import com.kora.android.R;

public class CustomButton extends AppCompatButton {

    public CustomButton(Context context) {
        super(context);
        applyCustomFont(context, null);
    }

    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context, attrs);
    }

    public CustomButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context, attrs);
    }

    private void applyCustomFont(final Context context, final AttributeSet attrs) {
        final TypedArray attributeArray = context.obtainStyledAttributes(
                attrs,
                R.styleable.CustomButton);
        final String fontName = attributeArray.getString(R.styleable.CustomButton_fontFamily);
        final Typeface customFont = selectTypeface(context, fontName);
        setTypeface(customFont);
        attributeArray.recycle();
    }

    private Typeface selectTypeface(final Context context, final String fontName) {
        if (fontName.contentEquals(context.getString(R.string.font_name_muli))) {
            return FontCache.getTypeface("Muli.ttf", context);
        } else if (fontName.contentEquals(context.getString(R.string.font_name_muli_bold))) {
            return FontCache.getTypeface("MuliBold.ttf", context);
        } else if (fontName.contentEquals(context.getString(R.string.font_name_muli_light))) {
            return FontCache.getTypeface("MuliLight.ttf", context);
        } else if (fontName.contentEquals(context.getString(R.string.font_name_muli_regular))) {
            return FontCache.getTypeface("MuliRegular.ttf", context);
        } else if (fontName.contentEquals(context.getString(R.string.font_name_muli_semi_bold))) {
            return FontCache.getTypeface("MuliSemiBold.ttf", context);
        } else
            return null;
    }
}

