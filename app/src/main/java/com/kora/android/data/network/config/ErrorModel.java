package com.kora.android.data.network.config;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.List;

@JsonObject
public class ErrorModel {

    @JsonField(name = {"message", "error"})
    private String error;

    @JsonField(name = "invalidAttributes")
    private Attributes mAttributes;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Attributes getAttributes() {
        return mAttributes;
    }

    public void setAttributes(Attributes attributes) {
        mAttributes = attributes;
    }


    @JsonObject
    public static class Attributes {

        @JsonField(name = {"userName", "email", "phone"})
        private List<AttributeName> mAttributeName;

        public List<AttributeName> getAttributeName() {
            return mAttributeName;
        }

        public void setAttributeName(List<AttributeName> attributeName) {
            mAttributeName = attributeName;
        }
    }

    @JsonObject
    public static class AttributeName {

        @JsonField(name = {"message", "error"})
        private String error;

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }
    }
}
