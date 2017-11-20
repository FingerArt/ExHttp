package io.chengguo.ohttp;

import java.io.InputStream;
import java.net.HttpURLConnection;

import static io.chengguo.ohttp.Utils.HANDLER;
import static io.chengguo.ohttp.Utils.runOnUiThread;

/**
 * @author FingerArt http://fingerart.me
 * @date 2017年07月31日 17:20
 */
public class StringRequestCallback extends GHttpSampleRequestCallback {
    @Override
    public void onSuccess(final int responseCode, InputStream inputStream, HttpURLConnection connection) {
        final String content = Utils.outputString(inputStream);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onSuccess(responseCode, content);
            }
        });
    }

    public void onSuccess(int responseCode, String content) {
    }
}
