package io.chengguo.exhttp;

/**
 * 请求回调
 *
 * @author FingerArt http://fingerart.me
 * @date 2017年05月18日 16:05
 */
public interface IHttpRequestCallback {

    /**
     * 开始请求
     */
    void onStart();

    /**
     * 请求成功
     *
     * @param response
     */
    void onSuccess(Response response);

    /**
     * 请求发生错误
     *
     * @param e
     */
    void onError(Exception e);

    /**
     * 请求完成
     */
    void onFinish();
}
