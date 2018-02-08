package com.kora.android.data.repository.mapper;

import android.webkit.MimeTypeMap;

import com.kora.android.data.network.model.response.CountryResponse;
import com.kora.android.data.network.model.response.UserResponse;
import com.kora.android.presentation.model.CountryEntity;
import com.kora.android.presentation.model.UserEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UserMapper {

    @Inject
    public UserMapper() {
    }

    public ObservableTransformer<UserResponse, UserEntity> transformResponseToEntityUser() {
        return userResponseObservable -> userResponseObservable
                .map(userResponse -> new UserEntity()
                        .addId(userResponse.getId())
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
                        .addCountryCode(userResponse.getCountryCode())
                        .addAgree(userResponse.getAgreed())
                        .addCurrencyNameFull(userResponse.getCurrencyNameFull())
                        .addAgent(userResponse.getAgent())
                        .addInterestRate(userResponse.getInterestRate())
                        .addEmailConfirmed(userResponse.getEmailVerified())
                );
    }

    public Observable<Map<String, RequestBody>> transformUserToFormData(final UserEntity userEntity) {
        return Observable.just(userEntity)
                .map(model -> {
                    final HashMap<String, RequestBody> map = new HashMap<>();
                    if (model.getPhoneNumber() != null)
                        map.put("phone", createPartFromString(model.getPhoneNumber()));
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
                    if (model.getERC20Token() != null)
                        map.put("ERC20Token", createPartFromString(model.getERC20Token()));
                    if (model.getPostalCode() != null)
                        map.put("postalCode", createPartFromString(model.getPostalCode()));
                    if (model.getAddress() != null)
                        map.put("address", createPartFromString(model.getAddress()));
                    if (model.getPassword() != null)
                        map.put("password", createPartFromString(model.getPassword()));
                    if (model.getCountryCode() != null)
                        map.put("countryCode", createPartFromString(model.getCountryCode()));
                    if (model.isAgent() != null)
                        map.put("agent", createPartFromString(Boolean.toString(model.isAgent())));
                    if (model.getInterestRate() != null)
                        map.put("interestRate", createPartFromString(Integer.toString(model.getInterestRate())));
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

    public ObservableTransformer<List<UserResponse>, List<UserEntity>> transformUserListResponseToEntityUserList() {
        return userResponseObservable -> userResponseObservable
                .flatMap(userResponses -> Observable.fromIterable(userResponses)
                        .compose(transformResponseToEntityUser())).toList().toObservable();
    }

    public Observable<List<UserEntity>> transformUserListResponseToEntityUserList(List<UserResponse> userResponses) {
        if (userResponses == null) return Observable.just(new ArrayList<>());
        return Observable.just(userResponses)
                .compose(transformUserListResponseToEntityUserList());
    }

    public Observable<UserEntity> transformResponseToEntityUser(UserResponse userResponse) {
        if (userResponse == null) return null;
        return Observable.just(userResponse).compose(transformResponseToEntityUser());
    }
}

