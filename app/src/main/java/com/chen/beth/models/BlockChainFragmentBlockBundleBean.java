package com.chen.beth.models;

import java.util.List;

public class BlockChainFragmentBlockBundleBean {

    /**
     * status : 1
     * error :
     */

    public int status;
    public ResultBean result;
    public String error;

    public static class ResultBean {
        public List<BlockSummaryBean> blocks;
    }
}
