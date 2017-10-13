package com.kora.android.presentation.navigation.backstack;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BackStackManager {

    private static final int FIRST_INDEX = 0;
    private static final int UNDEFINED_INDEX = -1;

    protected final LinkedList<BackStack> mBackStacks = new LinkedList<>();

    public void push(int hostId, @NonNull BackStackEntry entry) {
        BackStack backStack = peekBackStack(hostId);
        if (backStack == null) {
            backStack = new BackStack(hostId);
            mBackStacks.push(backStack);
        }
        backStack.push(entry);
    }

    @NonNull
    protected BackStackEntry pop(@NonNull BackStack backStack) {
        BackStackEntry entry = backStack.pop();
        assert entry != null;
        if (backStack.empty()) {
            mBackStacks.remove(backStack);
        }
        return entry;
    }

    @Nullable
    public BackStackEntry pop(int hostId) {
        BackStack backStack = peekBackStack(hostId);
        if (backStack == null) {
            return null;
        }
        return pop(backStack);
    }

    @Nullable
    public Pair<Integer, BackStackEntry> pop() {
        BackStack backStack = peekBackStack();
        if (backStack == null) {
            return null;
        }
        return Pair.create(backStack.mHostId, pop(backStack));
    }

    public boolean clear(int hostId) {
        BackStack backStack = getBackStack(hostId);
        if (backStack == null) {
            return false;
        }
        mBackStacks.remove(backStack);
        return true;
    }

    public boolean resetToRoot(int hostId) {
        BackStack backStack = getBackStack(hostId);
        if (backStack == null) {
            return false;
        }
        resetToRoot(backStack);
        return true;
    }

    protected void resetToRoot(@NonNull BackStack backStack) {
        while (true) {
            BackStackEntry entry = backStack.pop();
            assert entry != null;
            if (backStack.empty()) {
                backStack.push(entry);
                return;
            }
        }
    }

    @Nullable
    protected BackStack peekBackStack(int hostId) {
        int index = indexOfBackStack(hostId);
        if (index == UNDEFINED_INDEX) {
            return null;
        }
        BackStack backStack = mBackStacks.get(index);
        if (index != FIRST_INDEX) {
            mBackStacks.remove(index);
            mBackStacks.push(backStack);
        }
        return backStack;
    }

    @Nullable
    protected BackStack peekBackStack() {
        return mBackStacks.peek();
    }

    @Nullable
    protected BackStack getBackStack(int hostId) {
        int index = indexOfBackStack(hostId);
        if (index == UNDEFINED_INDEX) {
            return null;
        }
        return mBackStacks.get(index);
    }

    protected int indexOfBackStack(int hostId) {
        int size = mBackStacks.size();
        for (int i = 0; i < size; i++) {
            if (mBackStacks.get(i).mHostId == hostId) {
                return i;
            }
        }
        return UNDEFINED_INDEX;
    }

    @NonNull
    public Parcelable saveState() {
        return new SavedState(mBackStacks);
    }

    public void restoreState(@Nullable Parcelable state) {
        if (state != null) {
            SavedState savedState = (SavedState) state;
            mBackStacks.addAll(savedState.backStacks);
        }
    }

    static class SavedState implements Parcelable {
        final List<BackStack> backStacks;

        public SavedState(List<BackStack> backStacks) {
            this.backStacks = backStacks;
        }

        private SavedState(Parcel in) {
            int size = in.readInt();
            backStacks = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                backStacks.add(BackStack.CREATOR.createFromParcel(in));
            }
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            int size = backStacks.size();
            out.writeInt(size);
            for (int i = 0; i < size; i++) {
                backStacks.get(i).writeToParcel(out, flags);
            }
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {

            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
