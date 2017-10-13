package com.kora.android.presentation.navigation.backstack;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.kora.android.presentation.ui.base.view.BaseFragment;

public class BackStackEntry implements Parcelable {

    private static final int NO_STATE = -1;
    private static final int SAVED_STATE = 0;
    private static final int PARCELABLE_STATE = 1;

    public final String mFragmentName;
    public final Fragment.SavedState mState;
    public final Bundle mArgs;

    public BackStackEntry(@NonNull String mFragmentName,
                          @Nullable Fragment.SavedState mState,
                          @Nullable Bundle mArgs) {
        this.mFragmentName = mFragmentName;
        this.mState = mState;
        this.mArgs = mArgs;
    }

    private BackStackEntry(Parcel in) {
        final ClassLoader loader = getClass().getClassLoader();
        mFragmentName = in.readString();
        mArgs = in.readBundle(loader);

        switch (in.readInt()) {
            case NO_STATE:
                mState = null;
                break;
            case SAVED_STATE:
                mState = Fragment.SavedState.CREATOR.createFromParcel(in);
                break;
            case PARCELABLE_STATE:
                mState = in.readParcelable(loader);
                break;
            default:
                throw new IllegalStateException();
        }
    }

    @NonNull
    public static BackStackEntry create(@NonNull final FragmentManager fm,
                                        final BaseFragment fragment) {
        String fname = fragment.getClass().getName();
        Fragment.SavedState state = null;
        try {
            state = fm.saveFragmentInstanceState(fragment);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bundle args = fragment.getArguments();
        return new BackStackEntry(fname, state, args);
    }

    @NonNull
    public BaseFragment toFragment(@NonNull final Context context) {
        final BaseFragment fragment = (BaseFragment) Fragment.instantiate(context, mFragmentName);
        fragment.setInitialSavedState(mState);
        fragment.setArguments(mArgs);
        return fragment;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mFragmentName);
        out.writeBundle(mArgs);

        if (mState == null) {
            out.writeInt(NO_STATE);
        } else if (mState.getClass() == Fragment.SavedState.class) {
            out.writeInt(SAVED_STATE);
            mState.writeToParcel(out, flags);
        } else {
            out.writeInt(PARCELABLE_STATE);
            out.writeParcelable(mState, flags);
        }
    }

    public static final Creator<BackStackEntry> CREATOR = new Creator<BackStackEntry>() {

        @Override
        public BackStackEntry createFromParcel(Parcel in) {
            return new BackStackEntry(in);
        }

        @Override
        public BackStackEntry[] newArray(int size) {
            return new BackStackEntry[size];
        }
    };
}
