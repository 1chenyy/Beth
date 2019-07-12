package com.chen.beth.Worker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.RxWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.chen.beth.models.HistoryTransactionBean;
import com.chen.beth.net.RetrofitManager;

import io.reactivex.Single;
import io.reactivex.functions.Function;

public class QueryHistoryTransactionWorker extends RxWorker {

    /**
     * @param appContext   The application {@link Context}
     * @param workerParams Parameters to setup the internal state of this worker
     */
    public QueryHistoryTransactionWorker(@NonNull Context appContext, @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
    }

    @Override
    public Single<Result> createWork() {
        return null;
    }
}
