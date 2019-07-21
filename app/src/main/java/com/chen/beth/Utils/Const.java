package com.chen.beth.Utils;

import java.text.SimpleDateFormat;

public class Const {
    public static final String BETH_BASE_URL = "http://www.beth.gq:8080/v1/";
    public static final String ETHERSCAN_PROXY_BASE_URL = "http://api.etherscan.io/api";
    public static final String ETHERSCAN_PROXY_MODULE = "proxy";
    public static final String ETHERSCAN_PROXY_ACTION_GETTXS = "eth_getTransactionByHash";
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
}
