package io.chengguo.ohttp;

import java.io.OutputStream;
import java.net.HttpURLConnection;

/**
 * @author FingerArt http://fingerart.me
 * @date 2017年07月31日 14:31
 */
public class DefaultContentType implements IContentType {
    @Override
    public String contentType() {
        return null;
    }

    @Override
    public void handle(HttpURLConnection connection, PostRequest request) throws Exception {
        if (request.raw == null) return;

        byte[] bytes = request.raw.getBytes("UTF-8");
        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(bytes);
        outputStream.flush();
        outputStream.close();
    }
}
