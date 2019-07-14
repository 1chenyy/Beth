package com.chen.beth.Utils;

import java.text.SimpleDateFormat;

public class Const {
    public static final String BASE_URL = "http://www.beth.gq:8080/v1/";
    public static final int RESULT_NORMAL = 1;
    public static final int RESULT_NO_DATA = 0;
    public static final int RESULT_NO_NET = -1;
    public static final String KEY_HISTORY_DATE = "key_history_date";
    public static final String KEY_HISTORY_VALUE = "key_history_value";
    public static final SimpleDateFormat SDF_DAY = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat CHART_CHART_DATE = new SimpleDateFormat("M.dd");
}
