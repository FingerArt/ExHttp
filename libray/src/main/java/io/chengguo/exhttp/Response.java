package io.chengguo.exhttp;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;

/**
 * @author FingerArt http://fingerart.me
 * @date 2017年05月18日 16:01
 */
public final class Response implements Closeable {
    private static HttpURLConnection mCurrentConnection;
    private final int code;
    private final String message;
    private final InputStream stream;

    public Response(Builder builder) {
        code = builder.code;
        message = builder.message;
        stream = builder.stream;
    }

    static Response getResponseWithConnection(HttpURLConnection connection) throws IOException {
        try {
            mCurrentConnection = connection;
            connection.connect();
        } catch (SocketTimeoutException timeout) {
            throw timeout;
        } catch (Exception ignored) {
        }
        int code = connection.getResponseCode();
        return new Builder()
                .code(code)
                .message(connection.getResponseMessage())
                .content(Utils.isSuccessful(code) ? connection.getInputStream() : connection.getErrorStream())
                .build();
    }

    public int code() {
        return code;
    }

    public String message() {
        return message;
    }

    public InputStream stream() {
        return stream;
    }

    public String string() {
        return Utils.outputString(stream);
    }

    public boolean isSuccessful() {
        return Utils.isSuccessful(code);
    }

    @Override
    public String toString() {
        return "Response{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", content=" + string() +
                '}';
    }

    @Override
    public void close() throws IOException {
        mCurrentConnection.disconnect();
    }

    static class Builder {
        int code = -1;
        String message;
        InputStream stream;

        public Builder code(int code) {
            this.code = code;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder content(InputStream stream) {
            this.stream = stream;
            return this;
        }

        Response build() {
            return new Response(this);
        }
    }
}