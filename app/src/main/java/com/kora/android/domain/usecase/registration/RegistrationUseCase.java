package com.kora.android.domain.usecase.registration;

import com.kora.android.data.repository.RegistrationRepository;
import com.kora.android.domain.base.AsyncUseCase;
import com.kora.android.injection.annotation.ConfigPersistent;
import com.kora.android.presentation.model.User;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.observers.DisposableSingleObserver;

@ConfigPersistent
public class RegistrationUseCase extends AsyncUseCase<DisposableSingleObserver, Single> {

    private final RegistrationRepository mRegistrationRepository;

    private User mUser;

    @Inject
    public RegistrationUseCase(final RegistrationRepository registrationRepository) {
        this.mRegistrationRepository = registrationRepository;
    }

    public void setData(final User user) {
       mUser = user;
    }

    @Override
    protected Single buildTask() {
        return mRegistrationRepository.register(mUser);
    }
}
