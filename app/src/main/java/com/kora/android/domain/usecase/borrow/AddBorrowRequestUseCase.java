package com.kora.android.domain.usecase.borrow;

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
    private List<UserEntity> mGuaranters;
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

    public void setData(UserEntity lender, List<UserEntity> guaranters,
                        double amount, double convertedAmount, int rate,
                        String startDate, String maturityDate, String note) {
        mLender = lender;
        mGuaranters = guaranters;
        mAmount = amount;
        mConvertedAmount = convertedAmount;
        mRate = rate;
        mNote = note;

        Calendar calendarByDatePattern = DateUtils.getCalendarByDatePattern(startDate, DateUtils.PRETTY_DATE_PATTERN);
        mStartDate = calendarByDatePattern == null ? null : calendarByDatePattern.getTime();

        calendarByDatePattern = DateUtils.getCalendarByDatePattern(maturityDate, DateUtils.PRETTY_DATE_PATTERN);
        mMaturityDate = calendarByDatePattern == null ? null : calendarByDatePattern.getTime();
    }

    @Override
    protected Observable buildObservableTask() {
        return mBorrowRepository.addBorrowRequest(mLender, mGuaranters, mAmount, mConvertedAmount, mRate, mNote, mStartDate, mMaturityDate);
    }
}
