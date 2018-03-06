package io.chengguo.exhttp;

import static io.chengguo.exhttp.Utils.runOnUiThread;

/**
 * @author FingerArt http://fingerart.me
 * @date 2017年07月31日 17:20
 */
public abstract class StringRequestCallback extends GHttpSampleRequestCallback {

    @Override
    public void onSuccess(final Response response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onSuccess(response.code(), response.string());
            }
        });
    }

    public abstract void onSuccess(int responseCode, String content);
}
