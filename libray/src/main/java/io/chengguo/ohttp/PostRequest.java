package io.chengguo.ohttp;

import java.io.File;
import java.net.HttpURLConnection;
import java.util.Map;

/**
 * @author FingerArt http://fingerart.me
 * @date 2017年05月18日 15:54
 */
class PostRequest extends BaseRequest {
    protected final IContentType contentTypeHandler;
    protected Map<String, String> params;
    protected Map<String, File> files;

    public PostRequest(PostRequestBuilder postRequestBuilder) {
        super(postRequestBuilder);
        params = postRequestBuilder.params;
        files = postRequestBuilder.files;
        contentTypeHandler = postRequestBuilder.contentTypeHandler;
    }

    @Override
    protected void prepareRequest(HttpURLConnection connection) throws Exception {
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", contentTypeHandler.contentType());
    }

    @Override
    protected void transferOutputStream(HttpURLConnection connection) throws Exception {
        contentTypeHandler.handle(connection, this);
    }
}
