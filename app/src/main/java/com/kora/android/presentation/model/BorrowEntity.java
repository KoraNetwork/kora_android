package com.kora.android.presentation.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.kora.android.presentation.enums.Direction;
import com.kora.android.presentation.enums.RequestState;

import java.util.Date;
import java.util.List;

public class BorrowEntity implements Parcelable {

    private String id;
    private Direction direction;
    private RequestState state;
    private double fromAmount;
    private double toAmount;
    private int rate;
    private String additionalNote;
    private Date startDate;
    private Date maturityDate;
    private Date createdAt;
    private UserEntity sender;
    private UserEntity receiver;
    private List<UserEntity> guarantors;

    public BorrowEntity(String id, Direction direction, RequestState state,
                        double fromAmount, double toAmount,
                        int rate, String additionalNote,
                        Date startDate, Date maturityDate,
                        Date createdAt, UserEntity sender,
                        UserEntity receiver, List<UserEntity> guarantors) {
        this.id = id;
        this.direction = direction;
        this.state = state;
        this.fromAmount = fromAmount;
        this.toAmount = toAmount;
        this.rate = rate;
        this.additionalNote = additionalNote;
        this.startDate = startDate;
        this.maturityDate = maturityDate;
        this.createdAt = createdAt;
        this.sender = sender;
        this.receiver = receiver;
        this.guarantors = guarantors;
    }

    protected BorrowEntity(Parcel in) {
        id = in.readString();
        fromAmount = in.readDouble();
        toAmount = in.readDouble();
        rate = in.readInt();
        state = RequestState.valueOf(in.readString());
        direction = Direction.valueOf(in.readString());
        additionalNote = in.readString();
        sender = in.readParcelable(UserEntity.class.getClassLoader());
        receiver = in.readParcelable(UserEntity.class.getClassLoader());
        guarantors = in.createTypedArrayList(UserEntity.CREATOR);
        long startDateLong = in.readLong();
        startDate = startDateLong == -1 ? null : new Date(startDateLong);
        long maturityDateLong = in.readLong();
        maturityDate = maturityDateLong == -1 ? null : new Date(maturityDateLong);
        long createdAtLong = in.readLong();
        createdAt = createdAtLong == -1 ? null : new Date(createdAtLong);
    }

    public static final Creator<BorrowEntity> CREATOR = new Creator<BorrowEntity>() {
        @Override
        public BorrowEntity createFromParcel(Parcel in) {
            return new BorrowEntity(in);
        }

        @Override
        public BorrowEntity[] newArray(int size) {
            return new BorrowEntity[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public RequestState getState() {
        return state;
    }

    public void setState(RequestState state) {
        this.state = state;
    }

    public double getFromAmount() {
        return fromAmount;
    }

    public void setFromAmount(double fromAmount) {
        this.fromAmount = fromAmount;
    }

    public double getToAmount() {
        return toAmount;
    }

    public void setToAmount(double toAmount) {
        this.toAmount = toAmount;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getAdditionalNote() {
        return additionalNote;
    }

    public void setAdditionalNote(String additionalNote) {
        this.additionalNote = additionalNote;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(Date maturityDate) {
        this.maturityDate = maturityDate;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public UserEntity getSender() {
        return sender;
    }

    public void setSender(UserEntity sender) {
        this.sender = sender;
    }

    public UserEntity getReceiver() {
        return receiver;
    }

    public void setReceiver(UserEntity receiver) {
        this.receiver = receiver;
    }

    public List<UserEntity> getGuarantors() {
        return guarantors;
    }

    public void setGuarantors(List<UserEntity> guarantors) {
        this.guarantors = guarantors;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeDouble(fromAmount);
        dest.writeDouble(toAmount);
        dest.writeInt(rate);
        dest.writeString(state.name());
        dest.writeString(direction.name());
        dest.writeString(additionalNote);
        dest.writeParcelable(sender, flags);
        dest.writeParcelable(receiver, flags);
        dest.writeTypedList(guarantors);
        dest.writeLong(startDate == null ? -1 : startDate.getTime());
        dest.writeLong(maturityDate == null ? -1 : maturityDate.getTime());
        dest.writeLong(createdAt == null ? -1 : createdAt.getTime());
    }
}
