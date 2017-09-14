package com.kora.android.common;

import android.Manifest;

public interface Keys {

    interface AuthHelperKeys {
        String AUTH_HELPER_FILE_NAME = "com_kora_android_auth_helper_file_name";
        String AUTH_HELPER_PASSWORD = "com_kora_android_auth_helper_password";
        String AUTH_HELPER_SESSION_TOKEN = "com_kora_android_auth_helper_session_token";
    }

    interface ProxyHelperKeys {
        String PROXY_HELPER_FILE_NAME = "com_kora_android_proxy_helper_file_name";
        String PROXY_HELPER_PASSWORD = "com_kora_android_proxy_helper_password";
        String PROXY_HELPER_ADDRESS = "com_kora_android_proxy_helper_address";
    }

    interface PermissionChecker {
        int PERMISSION_REQUEST_CODE = 123;
    }
}
