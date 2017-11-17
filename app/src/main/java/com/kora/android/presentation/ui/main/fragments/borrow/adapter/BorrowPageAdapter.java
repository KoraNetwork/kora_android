package com.kora.android.presentation.ui.main.fragments.borrow.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.kora.android.R;
import com.kora.android.presentation.enums.BorrowListType;
import com.kora.android.presentation.ui.main.fragments.borrow.fragment.BorrowFragment;

public class BorrowPageAdapter extends FragmentStatePagerAdapter {

    public static final int POSITION_REQUEST = 0;
    public static final int POSITION_LOANS = 1;
    public static final int POSITION_IN_PROGRESS = 2;

    private Context mContext;

    public BorrowPageAdapter(FragmentManager manager, Context context) {
        super(manager);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case POSITION_REQUEST:
                return BorrowFragment.getNewInstance(BorrowListType.REQUESTS);
            case POSITION_LOANS:
                return BorrowFragment.getNewInstance(BorrowListType.LOANS);
            case POSITION_IN_PROGRESS:
                return BorrowFragment.getNewInstance(BorrowListType.IN_PROGRESS);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case POSITION_REQUEST:
                return mContext.getString(R.string.borrow_request_title);
            case POSITION_LOANS:
                return mContext.getString(R.string.borrow_loans_title);
            case POSITION_IN_PROGRESS:
                return mContext.getString(R.string.borrow_in_progress_title);
        }
        return "";
    }
}

