package io.chengguo.ohttp;

import java.net.HttpURLConnection;

/**
 * @author FingerArt http://fingerart.me
 * @date 2017年07月26日 16:41
 */
public class PutRequest extends PostRequest {
    public PutRequest(PutRequestBuilder putRequestBuilder) {
        super(putRequestBuilder);
    }

    @Override
    protected void prepareRequest(HttpURLConnection connection) throws Exception {
        super.prepareRequest(connection);
        connection.setRequestMethod("PUT");
    }
}
