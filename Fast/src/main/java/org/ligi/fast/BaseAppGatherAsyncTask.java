package org.ligi.fast;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import org.ligi.tracedroid.logging.Log;

import java.util.List;

/**
 * Async-Task to Retrieve / Store Application Info needed by this App
 */
public class BaseAppGatherAsyncTask extends AsyncTask<Void, AppInfo, Void> {
    private Context ctx;
    protected int appCount;

    public BaseAppGatherAsyncTask(Context ctx) {
        this.ctx = ctx;

    }

    @Override
    protected Void doInBackground(Void... params) {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        try {
            List<ResolveInfo> resolveInfos = ctx.getPackageManager().queryIntentActivities(mainIntent, 0);
            appCount = resolveInfos.size();
            for (ResolveInfo info : resolveInfos) {
                AppInfo act_appinfo = new AppInfo(ctx, info);
                publishProgress(act_appinfo);
            }
        } catch (Exception e) {
            Log.d("Exception occurred when getting activities skipping...!");

        }

        return null;
    }
}
