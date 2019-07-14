package com.chen.beth.net;

import com.chen.beth.models.BlockBundleBean;
import com.chen.beth.models.HistoryTransactionBean;
import com.chen.beth.models.LatestBlockBean;
import com.chen.beth.models.PriceAndMktcapBean;

import io.reactivex.Maybe;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;

public interface APIServices {
    @GET("getpriceandmakcap")
    Maybe<PriceAndMktcapBean> getPriceAndMketcap();

    @GET("current")
    Maybe<LatestBlockBean> getLatestBlock();

    @GET("gettransactionsstatistic")
    Maybe<HistoryTransactionBean> getHistoryTransaction();

    @GET("getlatest15blocks")
    Maybe<BlockBundleBean> getLatest15Blocks();
}
