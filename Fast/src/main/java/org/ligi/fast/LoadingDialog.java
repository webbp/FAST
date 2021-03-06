package org.ligi.fast;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import org.ligi.androidhelper.AndroidHelper;

/**
 * Dialog to make the waiting time for the initial index building nicer for the user
 * inform him which app we are processing and how far we are progressed with that
 */
public class LoadingDialog extends Activity {

    private ImageView icon_iv; // we will show the icon of the act app which is processed - makes the time appear shorter
    private TextView label_tv;
    private ProgressBar progressBar;
    private String newIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_dialog);
        icon_iv = (ImageView) findViewById(R.id.imageView);
        label_tv = (TextView) findViewById(R.id.textView);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        setTitle(getString(R.string.loadingDialogTitle));

        newIndex = "";

        new BaseAppGatherAsyncTask(this) {

            private int actAppIndex = 0;

            @Override
            protected void onProgressUpdate(AppInfo... values) {
                super.onProgressUpdate(values);

                actAppIndex++;
                getProgessBar().setMax(appCount);
                getProgessBar().setProgress(actAppIndex);

                setIcon(values[0].getIcon());
                setText(values[0].getLabel());

                newIndex += values[0].toCacheString() + "\n";
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                Intent resultIntent = new Intent();
                resultIntent.putExtra("newIndex", newIndex);
                setResult(RESULT_OK, resultIntent);

                finish();
            }

        }.execute();

        setWindowWidth();
        AndroidHelper.at(this).disableRotation();
    }

    @SuppressWarnings("deprecation") // we cannot use the new getSize function - or we would get a NoSuchMethod error on newer devices
    private void setWindowWidth() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = 3 * getWindowManager().getDefaultDisplay().getWidth() / 4;
        getWindow().setAttributes(params);
    }

    public void setIcon(Drawable icon) {
        icon_iv.setImageDrawable(icon);
    }

    public void setText(String text) {
        label_tv.setText(text);
    }

    public ProgressBar getProgessBar() {
        return progressBar;
    }

}
