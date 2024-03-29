package com.chen.beth.Utils;

import java.text.SimpleDateFormat;

public class Const {
    public static final String BETH_BASE_URL = "http://www.beth.gq:8080/v1/";

    public static final String ETHERSCAN_BASE_URL = "https://api.etherscan.io/";

    public static final String ETHERSCAN_PROXY_MODULE = "proxy";
    public static final String ETHERSCAN_ACCOUNT_MODULE = "account";

    public static final String ETHERSCAN_PROXY_ACTION_GETTXS = "eth_getTransactionByHash";
    public static final String ETHERSCAN_ACCOUNT_ACTION_GETBALANCE = "balance";
    public static final String ETHERSCAN_ACCOUNT_ACTION_GETTXS = "txlist";
    public static final String ETHERSCAN_ACCOUNT_ACTION_GETBLOCKS = "getminedblocks";

    public static final String ETHERSCAN_ACCOUNT_ARG_TAG = "latest";
    public static final String ETHERSCAN_ACCOUNT_ARG_STRAT = "0";
    public static final String ETHERSCAN_ACCOUNT_ARG_END = "99999999";
    public static final int ETHERSCAN_ACCOUNT_ARG_OFFSET = 20;
    public static final String ETHERSCAN_ACCOUNT_ARG_SORT = "desc";
    public static final String ETHERSCAN_ACCOUNT_ARG_BLOCK_TYPE = "blocks";

    public static final int RESULT_SUCCESS = 1;
    public static final int RESULT_NO_DATA = 0;
    public static final int RESULT_NO_NET = -1;
    public static final String KEY_HISTORY_TRANSACTION_DATE = "key_history_transaction_date";
    public static final String KEY_HISTORY_TRANSACTION_VALUE = "key_history_transaction_value";
    public static final String KEY_HISTORY_PRICE_DATE = "key_history_price_date";
    public static final String KEY_HISTORY_PRICE_VALUE = "key_history_price_value";
    public static final String KEY_MKTCAP_DATE = "key_mktcap_date";
    public static final String KEY_MKTCAP_VALUE = "key_mktcap_value";
    public static final SimpleDateFormat SDF_DAY = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat SDF_DETAIL = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat CHART_CHART_DATE = new SimpleDateFormat("M.dd");
    public static final String ARG_POSITION = "position";
    public static final int TYPE_TX = 0;
    public static final int TYPE_ACCOUNT = 1;
    public static final int TYPE_BLOCK = 2;
    public static final String ARG_TYPE = "type";
    public static final String ARG_TRANSITION_NAME = "name";
    public static final String ARG_SRC = "src";
    public static final String ARG_ARG = "arg";
    public static final String ARG_USER = "user";
    public static final String IS_SHOW_NOTIFY = "is_show_notify";
    public static final String FROM_BROADCAST = "from_broadcast";
}
