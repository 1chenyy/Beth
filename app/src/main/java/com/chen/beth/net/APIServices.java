package com.chen.beth.net;

import com.chen.beth.models.MainFragmentBlockBundleBean;
import com.chen.beth.models.HistoryPriceBean;
import com.chen.beth.models.HistoryTransactionBean;
import com.chen.beth.models.LatestBlockBean;
import com.chen.beth.models.PriceAndMktcapBean;
import com.chen.beth.models.TransactionBundleBean;

import io.reactivex.Maybe;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/*
	ns := beego.NewNamespace("/v1",
		beego.NSRouter("/current",
			currentBlockController),

		beego.NSRouter("/getblockbynum/:num",
			blockController,"get:GetBlockByNumber"),

		beego.NSRouter("/getlatest15blocks",
			blockController,"get:GetLatest15Blocks"),

		beego.NSRouter("/getlatestblocks/:num",
			blockController,"get:GetLatestBlocks"),

		beego.NSRouter("/gettransactionsbynum/:num",
			blockController,"get:GetTransactionsByNumber"),

		beego.NSRouter("/gettransactiondetails/:hashnum",
			blockController,"get:GetTransactionDetails"),

		beego.NSRouter("/getonepageblocks/:num",
			blockController,"get:GetOnePageBlocks"),

		beego.NSRouter("/gettransactionsstatistic",
			statisticController,"get:GetTransactionsStatistic"),

		beego.NSRouter("/getpriceandmakcap",
			statisticController,"get:GetPriceAndMakcap"),

		beego.NSRouter("/gethistoryprice",
			statisticController,"get:GetHistoryPrice"),
 */

public interface APIServices {
    @GET("getpriceandmakcap")
    Maybe<PriceAndMktcapBean> getPriceAndMketcap();

    @GET("current")
    Maybe<LatestBlockBean> getLatestBlock();

    @GET("gettransactionsstatistic")
    Maybe<HistoryTransactionBean> getHistoryTransaction();

    @GET("getlatest15blocks")
    Maybe<MainFragmentBlockBundleBean> getLatest15Blocks();

    @GET("getlatestblocks/{num}")
    Maybe<MainFragmentBlockBundleBean> getLatestBlocks(@Path("num")int num);

    @GET("gethistoryprice")
    Maybe<HistoryPriceBean> getHistoryPrice();

    @GET("getonepageblocks/{num}")
    Call<MainFragmentBlockBundleBean> getOnePageBlocks(@Path("num")int num);

    @GET("gettransactionsbynum/{num}")
    Call<TransactionBundleBean> getTransactionsByNumber(@Path("num")int num);
}
