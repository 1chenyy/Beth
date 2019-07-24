package com.chen.beth.models;

import java.util.List;

public class AccountTransactionsBean {

    /**
     * status : 1
     * message : OK
     * result : [{"blockNumber":"8187639","timeStamp":"1563626434","hash":"0x3cf88034cd814ff97441a0ed5edcd20b69f22b280ba5685af809f34e67abcd08","nonce":"631","blockHash":"0xb9053892f3494e85f289e4ec9d1fcfe1a7b9fc5f62974464f58740ee16340333","transactionIndex":"116","from":"0x5ed8cee6b63b1c6afce3ad7c92f4fd7e1b8fad9f","to":"0xde0b295669a9fd93d5f28d9ec85e40f4cb697bae","value":"0","gas":"250000","gasPrice":"1000000000","isError":"0","txreceipt_status":"1","input":"0xb61d27f60000000000000000000000009ee457023bb3de16d51a003a247baead7fce313d00000000000000000000000000000000000000000000003635c9adc5dea0000000000000000000000000000000000000000000000000000000000000000000600000000000000000000000000000000000000000000000000000000000000000","contractAddress":"","cumulativeGasUsed":"7349917","gasUsed":"50967","confirmations":"24681"}]
     */

    public String status;
    public String message;
    public List<TransactionSummaryBean> result;


}
