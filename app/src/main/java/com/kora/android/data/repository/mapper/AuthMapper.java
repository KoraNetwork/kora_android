package com.kora.android.data.repository.mapper;

import com.kora.android.data.network.model.response.CountryResponse;
import com.kora.android.data.network.model.response.UserResponse;
import com.kora.android.presentation.model.CountryEntity;
import com.kora.android.presentation.model.UserEntity;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class AuthMapper {

    @Inject
    public AuthMapper() {
    }

    public ObservableTransformer<UserResponse, UserEntity> transformResponseToEntityUser() {
        return userResponseObservable -> userResponseObservable
                .map(userResponse -> new UserEntity()
                        .addAvatar(userResponse.getAvatar())
                        .addPhoneNumber(userResponse.getPhoneNumber())
                        .addIdentity(userResponse.getIdentity())
                        .addCreator(userResponse.getCreator())
                        .addRecoveryKey(userResponse.getRecoveryKey())
                        .addOwner(userResponse.getOwner())
                        .addUserName(userResponse.getUserName())
                        .addLegalName(userResponse.getLegalName())
                        .addEmail(userResponse.getEmail())
                        .addDateOfBirth(userResponse.getDateOfBirth())
                        .addCurrency(userResponse.getCurrency())
                        .addPostalCode(userResponse.getPostalCode())
                        .addAddress(userResponse.getAddress())
                        .addERC20Token(userResponse.getERC20Token())
                        .addFlag(userResponse.getFlag())
                        .addCurrencyNameFull(userResponse.getCurrencyNameFull())
                );
    }

    public Observable<Map<String, RequestBody>> transformUserToFormData(final UserEntity userEntity) {
        return Observable.just(userEntity)
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
                    if (model.getCountryCode() != null)
                        map.put("countryCode", createPartFromString(model.getCountryCode()));
                    return map;
                });
    }

    private RequestBody createPartFromString(final String string) {
        return string != null ? RequestBody.create(MediaType.parse("multipart/form-data"), string) : null;
    }

    public ObservableTransformer<CountryResponse, CountryEntity> transformResponseToEntityCountry() {
        return countryResponseObservable -> countryResponseObservable
                .map(countryResponse -> new CountryEntity()
                        .addCountryCode(countryResponse.getCountryCode())
                        .addName(countryResponse.getName())
                        .addCurrency(countryResponse.getCurrency())
                        .addCurrencyNameFull(countryResponse.getCurrencyNameFull())
                        .addERC20Token(countryResponse.getERC20Token())
                        .addPhoneCode(countryResponse.getPhoneCode())
                        .addFlag(countryResponse.getFlag())
                );
    }
}
