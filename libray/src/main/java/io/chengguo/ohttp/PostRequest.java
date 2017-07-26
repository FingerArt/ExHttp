package io.chengguo.ohttp;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;

import static io.chengguo.ohttp.Utils.xWwwFormUrlencoded;

/**
 * @author FingerArt http://fingerart.me
 * @date 2017年05月18日 15:54
 */
class PostRequest extends BaseRequest {
    public PostRequest(PostRequestBuilder postRequestBuilder) {
        super(postRequestBuilder);
    }

    @Override
    protected void prepareMethod(HttpURLConnection connection) throws Exception {
        byte[] formData = xWwwFormUrlencoded(params, false).getBytes("UTF-8");
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setRequestProperty("Content-Length", Integer.toString(formData.length));
    }

    @Override
    protected void transferInputStream(HttpURLConnection connection) throws IOException {
        OutputStream connectionOutputStream = connection.getOutputStream();
        byte[] bytes = xWwwFormUrlencoded(params, false).getBytes("UTF-8");
        connectionOutputStream.write(bytes);
    }
}
