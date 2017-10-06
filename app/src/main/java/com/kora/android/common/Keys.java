package com.kora.android.common;

import android.Manifest;

public interface Keys {

    interface SessionHelperKeys {
        String SESSION_HELPER_FILE_NAME = "com_kora_android_session_helper_file_name";
        String SESSION_HELPER_PASSWORD = "com_kora_android_session_helper_password";
        String SESSION_HELPER_SESSION_TOKEN = "com_kora_android_session_helper_session_token";
    }

    interface ProxyHelperKeys {
        String PROXY_HELPER_FILE_NAME = "com_kora_android_proxy_helper_file_name";
        String PROXY_HELPER_PASSWORD = "com_kora_android_proxy_helper_password";
        String PROXY_HELPER_ADDRESS = "com_kora_android_proxy_helper_address";
    }

    interface RegistrationHelperKeys {
        String REGISTRATION_HELPER_FILE_NAME = "REGISTRATION_HELPER_FILE_NAME";
        String REGISTRATION_HELPER_FILE_PASSWORD = "REGISTRATION_HELPER_FILE_PASSWORD";
        String REGISTRATION_HELPER_PHONE_NUMBER = "REGISTRATION_HELPER_PHONE_NUMBER";
        String REGISTRATION_HELPER_IDENTITY_ADDRESS = "REGISTRATION_HELPER_IDENTITY_ADDRESS";
        String REGISTRATION_HELPER_CREATOR_ADDRESS = "REGISTRATION_HELPER_CREATOR_ADDRESS";
        String REGISTRATION_HELPER_RECOVERY_ADDRESS = "REGISTRATION_HELPER_RECOVERY_ADDRESS";
        String REGISTRATION_HELPER_OWNER_ADDRESS = "REGISTRATION_HELPER_OWNER_ADDRESS";
    }

    interface PermissionChecker {
        int PERMISSION_REQUEST_CODE_READ_PHONE_STATE_AND_READ_SMS = 111;
        int PERMISSION_REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 222;
    }

    interface IncomingSms {
        String INCOMING_SMS_SENDER_1 = "AutoInform";
        String INCOMING_SMS_SENDER_2 = "InfoSms";
        String INCOMING_SMS_ACTION_RECEIVED = "INCOMING_SMS_ACTION_RECEIVED";
        String INCOMING_SMS_EXTRA_MESSAGE = "INCOMING_SMS_EXTRA_MESSAGE";
    }
}
