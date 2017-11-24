package com.kora.android.domain.usecase.borrow;

import android.util.Log;

import com.kora.android.common.utils.DateUtils;
import com.kora.android.data.repository.BorrowRepository;
import com.kora.android.di.annotation.ConfigPersistent;
import com.kora.android.domain.base.AsyncUseCase;
import com.kora.android.presentation.model.UserEntity;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

@ConfigPersistent
public class AddBorrowRequestUseCase extends AsyncUseCase {

    private final BorrowRepository mBorrowRepository;

    private UserEntity mLender;
    private List<UserEntity> mGuarantors;
    private double mAmount;
    private double mConvertedAmount;
    private int mRate;
    private Date mStartDate;
    private Date mMaturityDate;
    private String mNote;

    @Inject
    public AddBorrowRequestUseCase(final BorrowRepository borrowRepository) {
        mBorrowRepository = borrowRepository;
    }

    public void setData(UserEntity lender, List<UserEntity> guarantors,
                        double amount, double convertedAmount, int rate,
                        String startDate, String maturityDate, String note) {
        mLender = lender;
        mGuarantors = guarantors;
        mAmount = amount;
        mConvertedAmount = convertedAmount;
        mRate = rate;
        mNote = note;

        final Calendar startDateCalendar = DateUtils.getCalendarByDatePattern(startDate, DateUtils.PRETTY_DATE_PATTERN);
        mStartDate = startDateCalendar == null ? null : startDateCalendar.getTime();
        mStartDate = DateUtils.setPlusFiveMinutes(mStartDate);

        final Calendar maturityDateCalendar = DateUtils.getCalendarByDatePattern(maturityDate, DateUtils.PRETTY_DATE_PATTERN);
        mMaturityDate = maturityDateCalendar == null ? null : maturityDateCalendar.getTime();
        mMaturityDate = DateUtils.setAlmostMidnight(mMaturityDate);
    }

    @Override
    protected Observable buildObservableTask() {
        return mBorrowRepository.addBorrowRequest(mLender, mGuarantors, mAmount, mConvertedAmount, mRate, mNote, mStartDate, mMaturityDate);
    }
}
