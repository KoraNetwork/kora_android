package com.kora.android.common.permission;

import android.util.SparseArray;

public class SecurityException extends RuntimeException {

    private String[] mRequiredPermissions;

    public SecurityException() {
        mRequiredPermissions = new String[0];
    }

    public SecurityException(final String requiredPermissionName) {
        mRequiredPermissions = new String[] { requiredPermissionName };
    }

    public SecurityException(final SparseArray<String> requiredPermissionsName) {
        mRequiredPermissions = new String[requiredPermissionsName.size()];
        for (int i = 0; i < requiredPermissionsName.size(); i++)
            mRequiredPermissions[i] = requiredPermissionsName.get(i);
    }

    public String[] getRequiredPermissions() {
        return mRequiredPermissions;
    }
}
