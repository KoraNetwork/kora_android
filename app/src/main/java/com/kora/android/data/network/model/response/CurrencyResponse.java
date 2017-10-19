package com.kora.android.data.network.model.response;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.util.Map;

@JsonObject
public class CurrencyResponse {

    @JsonField(name = "results")
    private Map<String, Currency> currencyMap;

    public Map<String, Currency> getCurrencyMap() {
        return currencyMap;
    }

    public void setCurrencyMap(Map<String, Currency> currencyMap) {
        this.currencyMap = currencyMap;
    }

    @JsonObject
    public static class Currency {

        @JsonField(name = "val")
        private Double value;

        public Double getValue() {
            return value;
        }

        public void setValue(Double value) {
            this.value = value;
        }
    }
}
