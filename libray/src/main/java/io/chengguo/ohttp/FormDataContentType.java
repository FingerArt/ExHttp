package io.chengguo.ohttp;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Map;

import static io.chengguo.ohttp.Utils.closeQuietly;
import static io.chengguo.ohttp.Utils.getFileType;
import static io.chengguo.ohttp.Utils.inputStream2outputStream;

/**
 * @author FingerArt http://fingerart.me
 * @date 2017年07月28日 16:30
 */
class FormDataContentType extends DefaultContentType {
    private static final String BOUNDARY = "------WebKitFormBoundarygiFNTzqCQ01yutx4";

    @Override
    public String contentType() {
        return "multipart/form-data; boundary=" + BOUNDARY;
    }

    @Override
    public void handle(HttpURLConnection connection, PostRequest request) throws Exception {
        OutputStream outputStream = connection.getOutputStream();

        if (hasParams(request)) {//Key - Value
            for (Map.Entry<String, String> param : request.params.entrySet()) {
                StringBuffer data = new StringBuffer();
                data.append(BOUNDARY).append("\r\n");
                data.append(String.format("Content-Disposition: form-data; name=\"%s\"", param.getKey())).append("\r\n");
                data.append("\r\n");
                data.append(param.getValue()).append("\r\n");
                System.out.print(data.toString());
                outputStream.write(data.toString().getBytes("UTF-8"));
            }
        }

        if (hasFiles(request)) {
            for (Map.Entry<String, File> file : request.files.entrySet()) {
                StringBuffer data = new StringBuffer();
                data.append(BOUNDARY).append("\r\n");
                data.append(String.format("Content-Disposition: form-data; name=\"%s\"; filename=\"%s\"", file.getKey(), file.getValue().getName())).append("\r\n");
                data.append(String.format("Content-Type: %s", getFileType(file.getValue().getPath()))).append("\r\n");
                data.append("\r\n");
                System.out.print(data.toString());
                outputStream.write(data.toString().getBytes("UTF-8"));

                try {
                    inputStream2outputStream(new DataInputStream(new FileInputStream(file.getValue())), outputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                    outputStream.write("\r\n".getBytes("UTF-8"));
                }
            }
        }
        if (hasParams(request) || hasFiles(request)) {
            StringBuilder data = new StringBuilder(BOUNDARY);
            data.append("--").append("\r\n");
            data.append("\r\n");
            System.out.print(data.toString());
            outputStream.write(data.toString().getBytes("UTF-8"));
        }
        outputStream.flush();
        closeQuietly(outputStream);
    }

    private boolean hasFiles(PostRequest request) {
        return request.files != null && !request.files.isEmpty();
    }

    private boolean hasParams(PostRequest request) {
        return request.params != null && !request.params.isEmpty();
    }
}
