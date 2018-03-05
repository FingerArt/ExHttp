package io.chengguo.ohttp;

import java.io.OutputStream;
import java.net.HttpURLConnection;

import static io.chengguo.ohttp.Utils.xWwwFormUrlencoded;

/**
 * ContentType XWwwFormUrlencoded 处理者
 *
 * @author FingerArt http://fingerart.me
 * @date 2017年07月28日 16:22
 */
class XWwwFormUrlencodedContentType extends DefaultContentType {
    @Override
    public String contentType() {
        return "application/x-www-form-urlencoded";
    }

    @Override
    public void handle(HttpURLConnection connection, PostRequest request) throws Exception {
        byte[] bytes = xWwwFormUrlencoded(request.params, false).getBytes("UTF-8");
        OutputStream connectionOutputStream = connection.getOutputStream();
        connectionOutputStream.write(bytes);
    }
}