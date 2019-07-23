package com.chen.beth.models;

import java.util.List;

public class TransactionSummaryBundleBean {

    /**
     * status : 1
     * result : {"txs":[{"hash":"0xd390563c7c08d5aa7ed860d147a8ec9aca407f9c7f366d41fbbf140439a12304","from":"0x698103f9e5e97fd3f069cad52762363e9fd80a4b","to":"0x21ab6c9fac80c59d401b37cb43f81ea9dde7fe34","fee":"0.0022000","value":"0.0000000"}]}
     * error :
     */

    public int status;
    public ResultBean result;
    public String error;

    public static class ResultBean {
        public List<TransactionSummaryBean> txs;
    }
}
