package com.kora.android.presentation.ui.main.fragments.transactions.filter;

import android.os.Parcel;
import android.os.Parcelable;

import com.kora.android.common.utils.CommonUtils;
import com.kora.android.presentation.enums.Direction;
import com.kora.android.presentation.enums.TransactionState;
import com.kora.android.presentation.enums.TransactionType;
import com.kora.android.presentation.ui.base.adapter.filter.FilterModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TransactionFilterModel extends FilterModel implements Parcelable {

    private Direction mDirections;
    private Set<TransactionType> mTransactionTypes = new HashSet<>();
    private Set<TransactionState> mTransactionStates = new HashSet<>();

    public TransactionFilterModel() {
    }

    protected TransactionFilterModel(Parcel in) {
        final String s = in.readString();
        if (!s.equals(""))
            mDirections = Direction.valueOf(s);
        final List<String> strings = new ArrayList<>();
        in.readList(strings, null);
        for (String string : strings) {
            if (CommonUtils.isStringInEnum(string, TransactionType.class))
                mTransactionTypes.add(TransactionType.valueOf(string.toUpperCase()));
            else if (CommonUtils.isStringInEnum(string, TransactionState.class))
                mTransactionStates.add(TransactionState.valueOf(string.toUpperCase()));
        }
    }

    public static final Creator<TransactionFilterModel> CREATOR = new Creator<TransactionFilterModel>() {
        @Override
        public TransactionFilterModel createFromParcel(Parcel in) {
            return new TransactionFilterModel(in);
        }

        @Override
        public TransactionFilterModel[] newArray(int size) {
            return new TransactionFilterModel[size];
        }
    };

    public Direction getDirections() {
        return mDirections;
    }

    public void setDirections(Direction directions) {
        mDirections = directions;
    }

    public Set<TransactionType> getTransactionTypes() {
        return mTransactionTypes;
    }

    public void setTransactionTypes(Set<TransactionType> transactionTypes) {
        mTransactionTypes = transactionTypes;
    }

    public Set<TransactionState> getTransactionStates() {
        return mTransactionStates;
    }

    public void setTransactionStates(Set<TransactionState> transactionStates) {
        mTransactionStates = transactionStates;
    }

    public String getTransactionDirectionsAsStrings() {
        return mDirections == null ? "" : mDirections.name().toLowerCase();
    }

    public List<String> getTransactionTypesAsStrings() {
        List strings = new ArrayList();
        for (TransactionType type : mTransactionTypes) {
            strings.add(type.getText());
        }
        return strings;
    }

    public List<String> getTransactionStatesAsStrings() {
        List strings = new ArrayList();
        for (TransactionState state : mTransactionStates) {
            strings.add(state.name().toLowerCase());
        }
        return strings;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mDirections == null ? "" : mDirections.name());
        final List<String> strings = new ArrayList<>();
        for (TransactionType type : mTransactionTypes) {
            strings.add(type.name().toLowerCase());
        }
        for (TransactionState state : mTransactionStates) {
            strings.add(state.name().toLowerCase());
        }
        dest.writeList(strings);
    }
}
