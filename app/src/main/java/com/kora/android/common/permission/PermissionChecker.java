package com.kora.android.common.permission;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.SparseArray;

import com.kora.android.injection.annotation.PerActivity;
import com.kora.android.presentation.ui.base.view.BaseActivity;

import javax.inject.Inject;

@PerActivity
public class PermissionChecker {

    private BaseActivity mBaseActivity;

    @Inject
    public PermissionChecker(final BaseActivity activity) {
        mBaseActivity = activity;
    }

    public int checkSelfPermission(final String permission) {
        return ContextCompat.checkSelfPermission(mBaseActivity, permission);
    }

    public boolean shouldShowRequestPermissionRationale(final String permission) {
        return ActivityCompat.shouldShowRequestPermissionRationale(mBaseActivity, permission);
    }

    public void verifyPermissions(final String... permissions) throws SecurityException {
        final SparseArray<String> nonGrantedPermissions = new SparseArray<>(permissions.length);
        int i = 0;
        for (String permission : permissions) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
                nonGrantedPermissions.append(i++, permission);
        }
        if (nonGrantedPermissions.size() > 0) throw new SecurityException(nonGrantedPermissions);
    }

    public boolean permissionsGranted(@NonNull final String[] permissions,
                                      @NonNull final int[] grantResults) {
        for (int grantResult : grantResults)
            if (grantResult != PackageManager.PERMISSION_GRANTED)
                return false;
        return true;
    }

    public void requestPermissions(final int requestCode, final String... permissions) {
        ActivityCompat.requestPermissions(mBaseActivity, permissions, requestCode);
    }
}
