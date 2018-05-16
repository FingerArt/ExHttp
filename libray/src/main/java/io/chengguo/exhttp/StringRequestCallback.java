package io.chengguo.exhttp;

import static io.chengguo.exhttp.Utils.runOnThread;
import static io.chengguo.exhttp.Utils.runOnUiThread;

/**
 * @author FingerArt http://fingerart.me
 * @date 2017年07月31日 17:20
 */
public abstract class StringRequestCallback extends HttpSampleRequestCallback {

    @Override
    public void onSuccess(final Response response) {
        runOnThread(new Runnable() {
            @Override
            public void run() {
                final String string = response.string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        onSuccess(response.code(), string);
                    }
                });
            }
        });
    }

    public abstract void onSuccess(int responseCode, String content);
}
