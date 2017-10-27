package com.kora.android.presentation.ui.main.fragments.borrow.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.kora.android.R;
import com.kora.android.presentation.enums.BorrowType;
import com.kora.android.presentation.ui.main.fragments.borrow.fragment.BorrowFragment;

public class BorrowPageAdapter extends FragmentStatePagerAdapter {

    private Context mContext;

    public BorrowPageAdapter(FragmentManager manager, Context context) {
        super(manager);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return BorrowFragment.getNewInstance(BorrowType.REQUEST);
            case 1:
                return BorrowFragment.getNewInstance(BorrowType.LOANS);
            case 2:
                return BorrowFragment.getNewInstance(BorrowType.IN_PROGRESS);
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
            case 0:
                return mContext.getString(R.string.borrow_request_title);
            case 1:
                return mContext.getString(R.string.borrow_loans_title);
            case 2:
                return mContext.getString(R.string.borrow_in_progress_title);
        }
        return "";
    }
}

