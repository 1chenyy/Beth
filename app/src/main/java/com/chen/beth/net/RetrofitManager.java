package com.chen.beth.net;

import com.chen.beth.Utils.Const;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {
    private static APIServices services;

    public static APIServices getAPIServices(){
        if (services == null){
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.writeTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30,TimeUnit.SECONDS)
                    .connectTimeout(15,TimeUnit.SECONDS);
            OkHttpClient client = builder.build();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Const.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(client)
                    .build();
            services = retrofit.create(APIServices.class);
        }
        return services;
    }
}
