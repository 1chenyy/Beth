package com.chen.beth.models;

public class PriceAndMktcapBean {

    /**
     * status : 1
     * result : {"price":"309.25","mktcap":"33039290489.00"}
     * error :
     */

    public int status;
    public ResultBean result;
    public String error;

    public static class ResultBean {
        /**
         * price : 309.25
         * mktcap : 33039290489.00
         */

        public String price;
        public String mktcap;


    }
}
