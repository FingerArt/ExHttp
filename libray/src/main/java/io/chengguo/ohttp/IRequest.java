package io.chengguo.ohttp;

import java.net.HttpURLConnection;

/**
 * @author FingerArt http://fingerart.me
 * @date 2017年05月18日 15:29
 */
public interface IRequest {

    /**
     * 同步请求
     *
     * @return
     * @throws Exception
     */
    HttpURLConnection execute() throws Exception;

    /**
     * 异步请求
     *
     * @param httpRequestListener
     */
    void execute(IGHttpRequestCallback httpRequestListener);
}
