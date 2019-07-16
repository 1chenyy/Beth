package com.chen.beth.Worker;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class StoreDataTask extends IntentService {
    private static final String ACTION_STORE_BLOCKS = "com.chen.beth.Worker.action.store_blocks";

    private static final String EXTRA_PARAM_BLOCKS = "com.chen.beth.Worker.extra.blocks";

    public StoreDataTask() {
        super("StoreDataTask");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionStoreBlocks(Context context, String param1) {
        Intent intent = new Intent(context, StoreDataTask.class);
        intent.setAction(ACTION_STORE_BLOCKS);
        intent.putExtra(EXTRA_PARAM_BLOCKS, param1);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_STORE_BLOCKS.equals(action)) {

            }
        }
    }

}
