package com.kora.android.presentation.navigation.backstack;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Stack;

public class BackStack implements Parcelable {
    public final int mHostId;
    private final Stack<BackStackEntry> mEntriesStack = new Stack<>();

    public BackStack(int hostId) {
        this.mHostId = hostId;
    }

    private BackStack(final Parcel in) {
        mHostId = in.readInt();
        int size = in.readInt();
        for (int i = 0; i < size; i++) {
            mEntriesStack.push(BackStackEntry.CREATOR.createFromParcel(in));
        }

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mHostId);
        int size = mEntriesStack.size();
        dest.writeInt(size);
        for (int i = 0; i < size; i++) {
            mEntriesStack.get(i).writeToParcel(dest, flags);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void push(@NonNull final BackStackEntry entry) {
        mEntriesStack.push(entry);
    }

    @Nullable
    public BackStackEntry pop() {
        return empty() ? null : mEntriesStack.pop();
    }

    public boolean empty() {
        return mEntriesStack.empty();
    }

    public static final Creator<BackStack> CREATOR = new Creator<BackStack>() {
        @Override
        public BackStack createFromParcel(Parcel in) {
            return new BackStack(in);
        }

        @Override
        public BackStack[] newArray(int size) {
            return new BackStack[size];
        }
    };

}
