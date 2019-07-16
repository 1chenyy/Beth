package com.chen.beth.net;

import com.chen.beth.models.BlockBundleBean;
import com.chen.beth.models.HistoryPriceBean;
import com.chen.beth.models.HistoryTransactionBean;
import com.chen.beth.models.LatestBlockBean;
import com.chen.beth.models.PriceAndMktcapBean;

import io.reactivex.Maybe;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Part;
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
    Maybe<BlockBundleBean> getLatest15Blocks();

    @GET("getlatestblocks/{num}")
    Maybe<BlockBundleBean> getLatestBlocks(@Path("num")int num);

    @GET("gethistoryprice")
    Maybe<HistoryPriceBean> getHistoryPrice();
}
