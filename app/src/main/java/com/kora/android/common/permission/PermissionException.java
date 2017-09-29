package com.kora.android.common.permission;

import android.util.SparseArray;

public class PermissionException extends RuntimeException {

    private String[] mRequiredPermissions;

    public PermissionException() {
        mRequiredPermissions = new String[0];
    }

    public PermissionException(final String requiredPermissionName) {
        mRequiredPermissions = new String[] { requiredPermissionName };
    }

    public PermissionException(final SparseArray<String> requiredPermissionsName) {
        mRequiredPermissions = new String[requiredPermissionsName.size()];
        for (int i = 0; i < requiredPermissionsName.size(); i++)
            mRequiredPermissions[i] = requiredPermissionsName.get(i);
    }

    public String[] getRequiredPermissions() {
        return mRequiredPermissions;
    }
}
