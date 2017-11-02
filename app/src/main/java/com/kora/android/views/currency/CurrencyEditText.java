package com.kora.android.views.currency;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.TextInputEditText;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;

import com.kora.android.R;

import java.util.Currency;
import java.util.Locale;

public class CurrencyEditText extends TextInputEditText {

    private Locale defaultLocale = Locale.US;
    private Locale currentLocale;
    private String currency;
    private boolean allowNegativeValues = false;
    private long rawValue = 0L;
    private CurrencyTextWatcher textWatcher;
    private String hintCache = null;
    private int decimalDigits = 0;

    public CurrencyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        processAttributes(context, attrs);
    }

    private void init() {
        this.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);

        Currency currentCurrency = getCurrencyForLocale(currentLocale);
        decimalDigits = currentCurrency.getDefaultFractionDigits();
        initCurrencyTextWatcher();
    }

    private void processAttributes(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CurrencyEditText);
        this.hintCache = getHintString();
        updateHint();

        this.setAllowNegativeValues(array.getBoolean(R.styleable.CurrencyEditText_allow_negative_values, false));
        this.setDecimalDigits(array.getInteger(R.styleable.CurrencyEditText_decimal_digits, decimalDigits));
        this.setCurrency(array.getString(R.styleable.CurrencyEditText_currency));

        array.recycle();
    }

    private void updateHint() {
        if (hintCache == null) {
            setHint(getDefaultHintValue());
        }
    }

    private String getDefaultHintValue() {
        try {
            return Currency.getInstance(defaultLocale).getCurrencyCode();
        } catch (Exception e) {
            Log.w("CurrencyEditText", String.format("An error occurred while getting currency symbol for hint using locale '%s', falling back to defaultLocale", defaultLocale));
            try {
                return Currency.getInstance(defaultLocale).getCurrencyCode();
            } catch (Exception e1) {
                Log.w("CurrencyEditText", String.format("An error occurred while getting currency symbol for hint using default locale '%s', falling back to USD", defaultLocale));
                return Currency.getInstance(Locale.US).getCurrencyCode();
            }

        }
    }

    public Locale getLocale() {
        return currentLocale;
    }

    public void setLocale(Locale locale) {
        currentLocale = locale;
        refreshView();
    }

    public void configureViewForLocale(Locale locale) {
        this.currentLocale = locale;
        Currency currentCurrency = getCurrencyForLocale(locale);
        decimalDigits = currentCurrency.getDefaultFractionDigits();
        refreshView();
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
        refreshView();
    }

    public void setDefaultLocale(Locale locale) {
        this.defaultLocale = locale;
    }

    public Locale getDefaultLocale() {
        return defaultLocale;
    }

    protected void setRawValue(long value) {
        rawValue = value;
    }

    public long getRawValue() {
        return rawValue;
    }

    public double getAmount() {
        return parse(getRawValue());
    }

    public void setValue(long value) {
        String formattedText = format(value);
        setText(formattedText);
    }

    public void setValue(String value) {
        String formattedText = formatCurrency(value);
        setText(formattedText);
    }

    public String getHintString() {
        CharSequence result = super.getHint();
        if (result == null) return null;
        return super.getHint().toString();
    }

    public int getDecimalDigits() {
        return decimalDigits;
    }

    public void setDecimalDigits(int digits) {
        if (digits < 0 || digits > 340) {
            throw new IllegalArgumentException("Decimal Digit value must be between 0 and 340");
        }
        decimalDigits = digits;

        refreshView();
    }

    public String formatCurrency(String val) {
        return format(val);
    }

    public String formatCurrency(long rawVal) {
        return format(rawVal);
    }

    public void setAllowNegativeValues(boolean negativeValuesAllowed) {
        allowNegativeValues = negativeValuesAllowed;
    }

    public boolean areNegativeValuesAllowed() {
        return allowNegativeValues;
    }

    private void refreshView() {
        setText(format(getRawValue()));
        updateHint();
    }

    private String format(long val) {
        return CurrencyTextFormatter.formatText(String.valueOf(val), currency, decimalDigits);
    }

    private String format(String val) {
        return CurrencyTextFormatter.formatText(val, currency, decimalDigits);
    }

    private double parse(long val) {
        return CurrencyTextFormatter.parseText(String.valueOf(val), currency, decimalDigits);
    }

    private void initCurrencyTextWatcher() {
        if (textWatcher != null) {
            this.removeTextChangedListener(textWatcher);
        }
        textWatcher = new CurrencyTextWatcher(this);
        this.addTextChangedListener(textWatcher);
    }

    private Locale retrieveLocale() {
        Locale locale;
        try {
            locale = getResources().getConfiguration().locale;
        } catch (Exception e) {
            Log.w("CurrencyEditText", String.format("An error occurred while retrieving users device locale, using fallback locale '%s'", defaultLocale), e);
            locale = defaultLocale;
        }
        return locale;
    }

    private Currency getCurrencyForLocale(Locale locale) {
        Currency currency;
        try {
            currency = Currency.getInstance(locale);
        } catch (Exception e) {
            try {
                Log.w("CurrencyEditText", String.format("Error occurred while retrieving currency information for locale '%s'. Trying default locale '%s'...", currentLocale, defaultLocale));
                currency = Currency.getInstance(defaultLocale);
            } catch (Exception e1) {
                Log.e("CurrencyEditText", "Both device and configured default locales failed to report currentCurrency data. Defaulting to USD.");
                currency = Currency.getInstance(Locale.US);
            }
        }
        return currency;
    }


}
