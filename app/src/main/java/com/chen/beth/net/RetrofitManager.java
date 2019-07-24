package com.chen.beth.net;


import com.chen.beth.Utils.Const;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {
    private static BethAPIServices bethAPIServices;
    private static EtherscanProxyApiServices etherscanProxyApiServices;

    public static BethAPIServices getBethAPIServices() {
        if (bethAPIServices == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.writeTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .connectTimeout(15, TimeUnit.SECONDS);
            OkHttpClient client = builder.build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Const.BETH_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(client)
                    .build();
            bethAPIServices = retrofit.create(BethAPIServices.class);
        }
        return bethAPIServices;
    }

    public static EtherscanProxyApiServices getEtherscanProxyAPIServices(){
        if (etherscanProxyApiServices == null){
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.writeTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .connectTimeout(15, TimeUnit.SECONDS);
            OkHttpClient client = builder.build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Const.ETHERSCAN_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(client)
                    .build();
            etherscanProxyApiServices = retrofit.create(EtherscanProxyApiServices.class);
        }
        return etherscanProxyApiServices;
    }
}
