package com.kora.android.common.permission;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.SparseArray;

import com.kora.android.di.annotation.PerFragment;
import com.kora.android.presentation.ui.base.view.BaseFragment;

import javax.inject.Inject;

@PerFragment
public class PermissionCheckerFragment {

    private BaseFragment mBaseFragment;

    @Inject
    public PermissionCheckerFragment(final BaseFragment baseFragment) {
        mBaseFragment = baseFragment;
    }

    public void verifyPermissions(final String... permissions) throws PermissionException {
        final SparseArray<String> nonGrantedPermissions = new SparseArray<>(permissions.length);
        int i = 0;
        for (String permission : permissions) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
                nonGrantedPermissions.append(i++, permission);
        }
        if (nonGrantedPermissions.size() > 0) throw new PermissionException(nonGrantedPermissions);
    }

    public boolean permissionsGranted(@NonNull final String[] permissions,
                                      @NonNull final int[] grantResults) {
        for (int grantResult : grantResults)
            if (grantResult != PackageManager.PERMISSION_GRANTED)
                return false;
        return true;
    }

    public void requestPermissions(final int requestCode, final String... permissions) {
        mBaseFragment.requestPermissions(permissions, requestCode);
    }

    private int checkSelfPermission(final String permission) {
        return ContextCompat.checkSelfPermission(mBaseFragment.getBaseActivity(), permission);
    }
}
