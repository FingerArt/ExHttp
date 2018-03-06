package io.chengguo.exhttp;

/**
 * @author FingerArt http://fingerart.me
 * @date 2017年07月26日 16:57
 */
public abstract class GHttpSampleRequestCallback implements IHttpRequestCallback {
    @Override
    public void onStart() {
    }

    @Override
    public void onError(Exception e) {
    }

    @Override
    public void onSuccess(Response response) {
    }

    @Override
    public void onFinish() {
    }
}
