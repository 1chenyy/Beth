package com.chen.beth.net;

import com.chen.beth.models.AccountBalanceBean;
import com.chen.beth.models.AccountBlocksBean;
import com.chen.beth.models.AccountTransactionsBean;
import com.chen.beth.models.TransactionDetailBean;

import io.reactivex.Maybe;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface EtherscanProxyApiServices {
    @GET("api")
    Maybe<TransactionDetailBean> getTransactionByHash(@Query("module")String module,
                                       @Query("action")String action,
                                       @Query("txhash")String txhash);

    @GET("api")
    Maybe<AccountBalanceBean> getAccountBalance(@Query("module")String module,
                                                @Query("action")String action,
                                                @Query("address")String address,
                                                @Query("tag")String tag);

    @GET("api")
    Maybe<AccountTransactionsBean> getAccountTransaction(@Query("module")String module,
                                                        @Query("action")String action,
                                                        @Query("address")String address,
                                                        @Query("startblock")String startblock,
                                                        @Query("endblock")String endblock,
                                                        @Query("page")int page,
                                                        @Query("offset")int offset,
                                                        @Query("sort")String sort);

    @GET("api")
    Maybe<AccountBlocksBean> getAccountBlocks(@Query("module")String module,
                                              @Query("action")String action,
                                              @Query("address")String address,
                                              @Query("blocktype")String blocktype,
                                              @Query("page")int page,
                                              @Query("offset")int offset);
}
