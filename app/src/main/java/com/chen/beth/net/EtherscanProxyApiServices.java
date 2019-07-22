package com.chen.beth.net;

import com.chen.beth.models.TransactionDetailBean;

import io.reactivex.Maybe;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface EtherscanProxyApiServices {
    @GET("api")
    Maybe<TransactionDetailBean> getTransactionByHash(@Query("module")String module,
                                       @Query("action")String action,
                                       @Query("txhash")String txhash);
}
