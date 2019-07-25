package com.chen.beth.models;

import java.util.List;

public class AccountBlocksBean {

    /**
     * status : 1
     * message : OK
     * result : [{"blockNumber":"3462296","timeStamp":"1491118514","blockReward":"5194770940000000000"}]
     */

    public String status;
    public String message;
    public List<ResultBean> result;

    public static class ResultBean {
        /**
         * blockNumber : 3462296
         * timeStamp : 1491118514
         * blockReward : 5194770940000000000
         */

        public String blockNumber;
        public String timeStamp;
        public String blockReward;
    }
}
