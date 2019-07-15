package com.chen.beth.models;

public class LatestBlockBean {


    /**
     * status : 1
     * result : {"number":8127658,"total_difficult":"10963298943648819628446"}
     * error :
     */

    public int status;
    public ResultBean result;
    public String error;

    public static class ResultBean {
        /**
         * number : 8127658
         * total_difficult : 10963298943648819628446
         */

        public int number;
        public String total_difficult;
    }
}
