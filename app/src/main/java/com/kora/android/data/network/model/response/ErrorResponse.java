package com.kora.android.data.network.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ErrorResponse {

    @SerializedName("error")
    private String mError;
    @SerializedName("status")
    private int mStatus;
    @SerializedName("summary")
    private String mSummary;
    @SerializedName("invalidAttributes")
    private Invalidattributes mInvalidattributes;

    public String getError() {
        return mError;
    }

    public void setError(String mError) {
        this.mError = mError;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int mStatus) {
        this.mStatus = mStatus;
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String mSummary) {
        this.mSummary = mSummary;
    }

    public Invalidattributes getInvalidattributes() {
        return mInvalidattributes;
    }

    public void setInvalidattributes(Invalidattributes mInvalidattributes) {
        this.mInvalidattributes = mInvalidattributes;
    }

    @Override
    public String toString() {
        return "ErrorResponse{" + "\n" +
                "mError='" + mError + "\n" +
                "mStatus=" + mStatus + "\n" +
                "mSummary='" + mSummary + "\n" +
                "mInvalidattributes=" + mInvalidattributes + "\n" +
                '}';
    }

    public static class Phone {
        @SerializedName("rule")
        private String mRule;
        @SerializedName("value")
        private String mValue;
        @SerializedName("message")
        private String mMessage;

        public String getRule() {
            return mRule;
        }

        public void setRule(String mRule) {
            this.mRule = mRule;
        }

        public String getValue() {
            return mValue;
        }

        public void setValue(String mValue) {
            this.mValue = mValue;
        }

        public String getMessage() {
            return mMessage;
        }

        public void setMessage(String mMessage) {
            this.mMessage = mMessage;
        }

        @Override
        public String toString() {
            return "Phone{" + "\n" +
                    "       mRule=" + mRule + "\n" +
                    "       mValue=" + mValue + "\n" +
                    "       mMessage='" + mMessage + "\n" +
                    "       }";
        }
    }

    public static class UserNameUnique {
        @SerializedName("rule")
        private String mRule;
        @SerializedName("value")
        private String mValue;
        @SerializedName("message")
        private String mMessage;

        public String getRule() {
            return mRule;
        }

        public void setRule(String mRule) {
            this.mRule = mRule;
        }

        public String getValue() {
            return mValue;
        }

        public void setValue(String mValue) {
            this.mValue = mValue;
        }

        public String getMessage() {
            return mMessage;
        }

        public void setMessage(String mMessage) {
            this.mMessage = mMessage;
        }

        @Override
        public String toString() {
            return "UserNameUnique{" + "\n" +
                    "       mRule=" + mRule + "\n" +
                    "       mValue=" + mValue + "\n" +
                    "       mMessage='" + mMessage + "\n" +
                    "       }";
        }
    }

    public static class Email {
        @SerializedName("rule")
        private String mRule;
        @SerializedName("value")
        private String mValue;
        @SerializedName("message")
        private String mMessage;

        public String getRule() {
            return mRule;
        }

        public void setRule(String mRule) {
            this.mRule = mRule;
        }

        public String getValue() {
            return mValue;
        }

        public void setValue(String mValue) {
            this.mValue = mValue;
        }

        public String getMessage() {
            return mMessage;
        }

        public void setMessage(String mMessage) {
            this.mMessage = mMessage;
        }

        @Override
        public String toString() {
            return "Email{" + "\n" +
                    "       mRule=" + mRule + "\n" +
                    "       mValue=" + mValue + "\n" +
                    "       mMessage='" + mMessage + "\n" +
                    "       }";
        }
    }

    public static class Invalidattributes {
        @SerializedName("phone")
        private List<Phone> mPhone;

        @SerializedName("userNameUnique")
        private List<UserNameUnique> mUserNameUnique;

        @SerializedName("email")
        private List<Email> mEmail;

        public List<Email> getEmail() {
            return mEmail;
        }

        public void setEmail(List<Email> mEmail) {
            this.mEmail = mEmail;
        }

        public List<UserNameUnique> getUserNameUnique() {
            return mUserNameUnique;
        }

        public void setUserNameUnique(List<UserNameUnique> mUserNameUnique) {
            this.mUserNameUnique = mUserNameUnique;
        }

        public List<Phone> getPhone() {
            return mPhone;
        }

        public void setPhone(List<Phone> mPhone) {
            this.mPhone = mPhone;
        }

        @Override
        public String toString() {
            return "Invalidattributes{" + "\n" +
                    "   mPhone=" + mPhone + "\n" +
                    "   mUserNameUnique=" + mUserNameUnique + "\n" +
                    "   mEmail=" + mEmail + "\n" +
                    "   }";
        }
    }
}
