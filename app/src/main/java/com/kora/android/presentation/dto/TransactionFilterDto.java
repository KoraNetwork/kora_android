package com.kora.android.presentation.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.kora.android.presentation.enums.TransactionDirection;
import com.kora.android.presentation.enums.TransactionType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TransactionFilterDto implements Parcelable {

    private TransactionDirection mTransactionDirections;
    private Set<TransactionType> mTransactionTypes = new HashSet<>();

    public TransactionFilterDto() {
    }

    protected TransactionFilterDto(Parcel in) {
        String s = in.readString();
        TransactionDirection.valueOf(s);
        List<String> strings = new ArrayList<>();
        in.readList(strings, null);
        for (String str : strings) {
            mTransactionTypes.add(TransactionType.valueOf(str));
        }
    }

    public static final Creator<TransactionFilterDto> CREATOR = new Creator<TransactionFilterDto>() {
        @Override
        public TransactionFilterDto createFromParcel(Parcel in) {
            return new TransactionFilterDto(in);
        }

        @Override
        public TransactionFilterDto[] newArray(int size) {
            return new TransactionFilterDto[size];
        }
    };

    public TransactionDirection getTransactionDirections() {
        return mTransactionDirections;
    }

    public void setTransactionDirections(TransactionDirection transactionDirections) {
        mTransactionDirections = transactionDirections;
    }

    public Set<TransactionType> getTransactionTypes() {
        return mTransactionTypes;
    }

    public void setTransactionTypes(Set<TransactionType> transactionTypes) {
        mTransactionTypes = transactionTypes;
    }

    public String getTransactionDirectionsAsStrings() {
        return mTransactionDirections == null ? "" : mTransactionDirections.name().toLowerCase();
    }

    public List<String> getTransactionTypesAsStrings() {
        List strings = new ArrayList();
        for (TransactionType type : mTransactionTypes) {
            strings.add(type.name().toLowerCase());
        }
        return strings;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTransactionDirections.name());
        List<String> strings = new ArrayList<String>();
        for (TransactionType type : mTransactionTypes) {
            strings.add(type.name());
        }
        dest.writeList(strings);
    }
}
