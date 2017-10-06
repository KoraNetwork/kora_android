package com.kora.android.data.repository.mapper;

import com.kora.android.data.network.model.response.CountryResponse;
import com.kora.android.presentation.model.Country;
import com.kora.android.presentation.model.User;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.ObservableTransformer;
import io.reactivex.Single;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class RegistrationMapper {

    @Inject
    public RegistrationMapper() {

    }

    public Single<Map<String, RequestBody>> transformUserToFormData(final User user) {
        return Single.just(user)
                .map(model -> {
                    final HashMap<String, RequestBody> map = new HashMap<>();
                    if (model.getPhoneNumber() != null)
                        map.put("phone", createPartFromString(model.getPhoneNumber()));
                    if (model.getIdentity() != null)
                        map.put("identity", createPartFromString(model.getIdentity()));
                    if (model.getCreator() != null)
                        map.put("creator", createPartFromString(model.getCreator()));
                    if (model.getRecoveryKey() != null)
                        map.put("recoveryKey", createPartFromString(model.getRecoveryKey()));
                    if (model.getOwner() != null)
                        map.put("owner", createPartFromString(model.getOwner()));
                    if (model.getUserName() != null)
                        map.put("userName", createPartFromString(model.getUserName()));
                    if (model.getLegalName() != null)
                        map.put("legalName", createPartFromString(model.getLegalName()));
                    if (model.getEmail() != null)
                        map.put("email", createPartFromString(model.getEmail()));
                    if (model.getDateOfBirth() != null)
                        map.put("dateOfBirth", createPartFromString(model.getDateOfBirth()));
                    if (model.getCurrency() != null)
                        map.put("currency", createPartFromString(model.getCurrency()));
                    if (model.getPostalCode() != null)
                        map.put("postalCode", createPartFromString(model.getPostalCode()));
                    if (model.getAddress() != null)
                        map.put("address", createPartFromString(model.getAddress()));
                    if (model.getPassword() != null)
                        map.put("password", createPartFromString(model.getPassword()));
                    return map;
                });
    }

    private RequestBody createPartFromString(final String string) {
        return string != null ? RequestBody.create(MediaType.parse("multipart/form-data"), string) : null;
    }

    public ObservableTransformer<CountryResponse, Country> transformResponseToEntityCountry() {
        return countryResponseObservable -> countryResponseObservable
                .map(countryResponse -> new Country()
                        .addName(countryResponse.getName())
                        .addCurrency(countryResponse.getCurrency())
                        .addPhoneCode(countryResponse.getPhoneCode())
                        .addFlag(countryResponse.getFlag())
                );
    }
}
