package io.chengguo.ohttp;

import java.net.HttpURLConnection;

/**
 * @author FingerArt http://fingerart.me
 * @date 2017年07月26日 16:44
 */
public class DeleteRequest extends PostRequest {
    public DeleteRequest(DeleteRequestBuilder deleteRequestBuilder) {
        super(deleteRequestBuilder);
    }

    @Override
    protected void prepareMethod(HttpURLConnection connection) throws Exception {
        super.prepareMethod(connection);
        connection.setRequestMethod("DELETE");
    }
}
