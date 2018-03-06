package io.chengguo.exhttp;

import java.net.HttpURLConnection;

/**
 * @author FingerArt http://fingerart.me
 * @date 2017年07月28日 16:21
 */
interface IContentType {

    String contentType();

    void handle(HttpURLConnection connection, PostRequest request) throws Exception;
}
