package com.kora.android.common;

public interface Keys {

    interface ProxyHelperKeys {
        String PROXY_HELPER_FILE_NAME = "com_kora_android_proxy_helper_file_name";
        String PROXY_HELPER_PASSWORD = "com_kora_android_proxy_helper_password";
        String PROXY_HELPER_ADDRESS = "com_kora_android_proxy_helper_address";
    }

    interface SessionHelperKeys {
        String SESSION_HELPER_FILE_NAME = "SESSION_HELPER_FILE_NAME";
        String SESSION_HELPER_PASSWORD = "SESSION_HELPER_PASSWORD";
        String SESSION_HELPER_SESSION_TOKEN = "SESSION_HELPER_SESSION_TOKEN";
        String SESSION_HELPER_USER = "SESSION_HELPER_USER";
    }

    interface RegistrationHelperKeys {
        String REGISTRATION_HELPER_FILE_NAME = "REGISTRATION_HELPER_FILE_NAME";
        String REGISTRATION_HELPER_FILE_PASSWORD = "REGISTRATION_HELPER_FILE_PASSWORD";
        String REGISTRATION_HELPER_COUNTRY = "REGISTRATION_HELPER_COUNTRY";
        String REGISTRATION_HELPER_PHONE_NUMBER = "REGISTRATION_HELPER_PHONE_NUMBER";
        String REGISTRATION_HELPER_IDENTITY_ADDRESS = "REGISTRATION_HELPER_IDENTITY_ADDRESS";
        String REGISTRATION_HELPER_CREATOR_ADDRESS = "REGISTRATION_HELPER_CREATOR_ADDRESS";
        String REGISTRATION_HELPER_RECOVERY_ADDRESS = "REGISTRATION_HELPER_RECOVERY_ADDRESS";
        String REGISTRATION_HELPER_OWNER_ADDRESS = "REGISTRATION_HELPER_OWNER_ADDRESS";
    }

    interface PermissionChecker {
        int PERMISSION_REQUEST_CODE_READ_SMS = 111;
        int PERMISSION_REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 222;
    }

    interface IncomingSms {
        String INCOMING_SMS_SENDER_1 = "AutoInform";
        String INCOMING_SMS_SENDER_2 = "InfoSMS";
        String INCOMING_SMS_ACTION_RECEIVED = "INCOMING_SMS_ACTION_RECEIVED";
        String INCOMING_SMS_EXTRA_MESSAGE = "INCOMING_SMS_EXTRA_MESSAGE";
    }

    interface DefaultCountry {
        String DEFAULT_COUNTRY_CODE = "US";
        String DEFAULT_COUNTRY_NAME = "United States";
        String DEFAULT_COUNTRY_CURRENCY = "USD";
        String DEFAULT_CURRENCY_NAME_FULL = "US Dollar";
        String DEFAULT_ERC_20_TOKEN = "0x545053968018bff704408dacf69bb8f9cacf161f";
        String DEFAULT_COUNTRY_PHONE_CODE = "+1";
        String DEFAULT_COUNTRY_FLAG = "/images/flags/US.png";
    }

    interface SelectCountry {
        int SELECT_COUNTRY_REQUEST_CODE = 111;
        String SELECT_COUNTRY_EXTRA = "SELECT_COUNTRY_EXTRA";
    }

    interface SelectCurrency {
        int SELECT_CURRENCY_REQUEST_CODE = 222;
        String SELECT_CURRENCY_EXTRA = "SELECT_CURRENCY_EXTRA";
    }

    interface Shared {
        String NAME = "kora";
        String TOKEN = "token";
        String USER = "user";
    }

    interface Extras {
        String EXTRA_CURRENT_TAB = "com.kora.android.extra_current_tab";
    }
}
