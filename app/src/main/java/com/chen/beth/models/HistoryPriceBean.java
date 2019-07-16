package com.chen.beth.models;

import java.util.List;

public class HistoryPriceBean {

    /**
     * status : 1
     * result : {"price":["294.17","291.61","302.17","283.10","287.90","287.98","306.43","313.34","307.89","288.64","268.56","275.41","268.94","226.16","228.14","227.72"]}
     * error :
     */

    public int status;
    public ResultBean result;
    public String error;

    public static class ResultBean {
        public List<String> price;
    }
}
