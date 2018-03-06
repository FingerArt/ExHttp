package io.chengguo.exhttp;

import java.net.HttpURLConnection;
import java.net.ProtocolException;

/**
 * @author FingerArt http://fingerart.me
 * @date 2017年05月18日 15:49
 */
class GetRequest extends BaseRequest {

    public GetRequest(GetRequestBuilder requestBuilder) {
        super(requestBuilder);
    }

    @Override
    protected void prepareRequest(HttpURLConnection connection) throws ProtocolException {
        connection.setRequestMethod("GET");
        connection.setDoInput(true);
    }

}
