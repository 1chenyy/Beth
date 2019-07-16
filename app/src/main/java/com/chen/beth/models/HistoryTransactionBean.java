package com.chen.beth.models;

import java.util.List;

public class HistoryTransactionBean {

    /**
     * status : 1
     * result : {"number":[756002,760258,777241,591529,833861,783265,748528,880775,841201,872344,690428,931138,833984,845601,787495]}
     * error :
     */

    public int status;
    public ResultBean result;
    public String error;

    public static class ResultBean {
        public List<Integer> number;
    }
}
