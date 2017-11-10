package com.kora.android.common;

import android.support.annotation.IntDef;

public interface Keys {

    int ITEMS_PER_PAGE = 10;

    String CURRENCY_USD = "USD";
    double DEFAULT_USD_BALANCE = 25.00;

    String ADDRESS_PREFIX = "0x";
    String JSON_FILE_EXTENSION = ".json";
    String EXPORT_FOLDER_NAME = "Kora";
    String WALLET_LIST_FILE_NAME = "wallets";
    String FILE_PROVIDER = ".file_provider";

    interface RegistrationHelperKeys {
        String REGISTRATION_HELPER_FILE_NAME = "REGISTRATION_HELPER_FILE_NAME";
        String REGISTRATION_HELPER_FILE_PASSWORD = "REGISTRATION_HELPER_FILE_PASSWORD";
        String REGISTRATION_HELPER_COUNTRY = "REGISTRATION_HELPER_COUNTRY";
        String REGISTRATION_HELPER_PHONE_NUMBER = "REGISTRATION_HELPER_PHONE_NUMBER";
        String REGISTRATION_HELPER_IDENTITY_ADDRESS = "REGISTRATION_HELPER_IDENTITY_ADDRESS";
        String REGISTRATION_HELPER_CREATOR_ADDRESS = "REGISTRATION_HELPER_CREATOR_ADDRESS";
        String REGISTRATION_HELPER_RECOVERY_ADDRESS = "REGISTRATION_HELPER_RECOVERY_ADDRESS";
        String REGISTRATION_HELPER_OWNER_ADDRESS = "REGISTRATION_HELPER_OWNER_ADDRESS";
        String REGISTRATION_HELPER_PIN_CODE = "REGISTRATION_HELPER_PIN_CODE";
    }

    interface PermissionChecker {
        int PERMISSION_REQUEST_CODE_RECEIVE_READ_SMS = 111;
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

    interface SelectWalletFile {
        int SELECT_WALLET_FILE_REQUEST_CODE = 333;
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
        String EXTRA_USER = "com.kora.android.extra_user";
        String TITLE = "com.kora.android.extra_title";
        String EXCLUDED_USER_IDS = "com.kora.android.extra_excluded_user_ids";
        String EXTRA_BORROW = "com.kora.android.extra_borrow";

        String SENDER_ENTITY_EXTRA = "com.kora.android.extra_sender_entity";
        String RECEIVER_ENTITY_EXTRA = "com.kora.android.extra_receiver_entity";
        String BORROW_REQUEST_EXTRA = "com.kora.android.extra_borrow_request";
    }

    interface Args {
        String USER_ENTITY = "user_entity_args";
        String BORROW_ENTITY = "borrow_entity_args";
        String GUARANTERS_LIST = "guaranters_list_args";
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
        String ARG_BORROW_TYPE = "borrow_type_args";
        String BORROW_LIST = "borrow_list_args";
        String BORROW_TYPE = "borrow_type";
    }

    interface Users {

        int MODE_SIMPLE = 0;
        int MODE_CHOOSE = 1;

        @IntDef({MODE_SIMPLE, MODE_CHOOSE})
        @interface UserMode {
        }
    }
}
