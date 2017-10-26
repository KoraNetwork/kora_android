package com.kora.android.common;

import android.support.annotation.IntDef;

public interface Keys {

    int ITEMS_PER_PAGE = 10;

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

    interface SelectDate {
        int SELECT_DATE_DIALOG_ID = 111;
    }

    interface Shared {
        String NAME = "kora";
        String TOKEN = "token";
        String USER = "user";
    }

    interface Extras {
        String EXTRA_CURRENT_TAB = "com.kora.android.extra_current_tab";
        String EXTRA_ACTION = "com.kora.android.extra_action";
        String EXTRA_REQUEST_ENTITY = "com.kora.android.extra_request_entity";
    }

    interface Args {
        String USER_ENTITY = "user_entity_args";
        String VIEW_MODE = "view_mode_args";
        String USER_LIST = "user_list_args";
        String SENDER_AMOUNT = "sender_amount_args";
        String RECEIVER_AMOUNT = "receiver_amount_args";
        String USER_SENDER = "user_sender_args";
        String USER_RECEIVER = "user_receiver_args";
        String TRANSACTION_LIST = "transaction_list_args";
        String ARG_TRANSACTION_FILTER_MODEL = "transaction_filter_model_arg";
        String ARG_REQUEST_FILTER_MODEL = "request_filter_model_arg";
        String TRANSACTION_FILTER = "transaction_filter_arg";
        String TRANSACTION_TYPE = "transaction_type_args";
        String REQUEST_LIST = "request_entity_args";
        String REQUEST_FILTER = "request_filter_args";
        String ACTION_TYPE = "action_type_arg";
        String REQUEST_ENTITY = "request_entity_args";
        String REQUEST_ID = "request_id_arg";
    }

    interface Users {

        int MODE_SIMPLE = 0;
        int MODE_CHOOSE = 1;

        @IntDef({MODE_SIMPLE, MODE_CHOOSE})
        @interface UserMode {
        }

    }
}
