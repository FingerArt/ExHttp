package io.chengguo.ohttp;

import android.text.TextUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

import static io.chengguo.ohttp.Utils.HANDLER;
import static io.chengguo.ohttp.Utils.HOSTNAME_VERIFIER;
import static io.chengguo.ohttp.Utils.THREAD_POOL;
import static io.chengguo.ohttp.Utils.generateTag;
import static io.chengguo.ohttp.Utils.getSSL;
import static io.chengguo.ohttp.Utils.mergeUrl;

/**
 * @author FingerArt http://fingerart.me
 * @date 2017年05月18日 15:32
 */
public abstract class BaseRequest implements IRequest {
    private static final String TAG = "BaseRequest";
    protected final InputStream sslInputStream;
    protected final String sslPassword;
    protected Map<String, String> queries;
    protected Map<String, String> headers;
    protected Map<String, String> cookies;
    protected String url;

    public BaseRequest(BaseRequestBuilder requestBuilder) {
        url = requestBuilder.url;
        queries = requestBuilder.queries;
        headers = requestBuilder.headers;
        cookies = requestBuilder.cookies;
        sslInputStream = requestBuilder.sslInputStream;
        sslPassword = requestBuilder.sslPassword;
    }

    @Override
    public HttpURLConnection execute() throws Exception {
        safeCheck();
        HttpURLConnection connection = getConnection();
        connection.connect();
        transferOutputStream(connection);
        return connection;
    }

    @Deprecated
    @Override
    public void execute(final IGHttpRequestCallback httpRequestListener) {
        THREAD_POOL.execute(new Runnable() {
            @Override
            public void run() {
                String tag = generateTag(url);
                try {
                    HANDLER.post(new Runnable() {
                        @Override
                        public void run() {
                            httpRequestListener.onStart();
                        }
                    });
                    HttpURLConnection connection = execute();
                    OHttp.addTag(tag, connection);
                    int responseCode = connection.getResponseCode();//start fetch
                    InputStream inputStream = responseCode == HttpURLConnection.HTTP_OK ?
                            connection.getInputStream()
                            :
                            connection.getErrorStream();
                    httpRequestListener.onSuccess(responseCode, inputStream, connection);//in thread
                } catch (final Exception e) {
                    HANDLER.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (!filterException(e)) {
                                    httpRequestListener.onError(e);
                                } else e.printStackTrace();
                            } catch (Exception ee) {
                            }
                        }
                    });
                } finally {
                    OHttp.cancel(tag);
                    HANDLER.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                httpRequestListener.onFinish();
                            } catch (Exception ee) {
                            }
                        }
                    });
                }
            }
        });
    }

    /**
     * 传输数据
     *
     * @param connection
     */
    protected void transferOutputStream(HttpURLConnection connection) throws Exception {
    }

    /**
     * Method
     *
     * @param connection
     * @throws Exception
     */
    protected abstract void prepareRequest(HttpURLConnection connection) throws Exception;

    private void safeCheck() throws Exception {
        if (TextUtils.isEmpty(url)) {
            throw new Exception("please set url");
        }
    }

    private HttpURLConnection getConnection() throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(mergeUrl(url, queries)).openConnection();

        prepareRequest(connection);
        setHeaders(connection);
        setCookies(connection);

        //common set
        connection.setUseCaches(false);
        connection.setReadTimeout(8 * 1000);
        connection.setConnectTimeout(8 * 1000);
        connection.setRequestProperty("User-Agent", "OHttp " + BuildConfig.VERSION_NAME);

        setSSL(connection);
        return connection;
    }

    /**
     * 设置Header
     *
     * @param connection
     */
    protected void setHeaders(HttpURLConnection connection) {
        if (headers == null) {
            return;
        }
        Set<Map.Entry<String, String>> entries = headers.entrySet();
        for (Map.Entry<String, String> header : entries) {
            connection.setRequestProperty(header.getKey(), header.getValue());
        }
    }

    private void setCookies(HttpURLConnection connection) {
        if (cookies == null) {
            return;
        }
        StringBuilder cookieStr = new StringBuilder();
        Iterator<Map.Entry<String, String>> iterator = cookies.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> cookie = iterator.next();
            cookieStr.append(cookie.getKey()).append("=").append(cookie.getValue());
            if (iterator.hasNext()) {
                cookieStr.append("; ");
            }
        }
        connection.setRequestProperty("Cookie", cookieStr.toString());
    }

    /**
     * 设置SSL
     * {@link URL#openConnection}会根据URL的协议自动创建{@link HttpURLConnection} 或 {@link HttpsURLConnection}
     *
     * @param connection
     * @throws CertificateException
     * @throws UnrecoverableKeyException
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     * @throws KeyManagementException
     * @throws IOException
     */
    protected void setSSL(HttpURLConnection connection) throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        if (connection instanceof HttpsURLConnection) {
            HttpsURLConnection https = (HttpsURLConnection) connection;
            https.setSSLSocketFactory(getSSL(sslInputStream, sslPassword));
            https.setHostnameVerifier(HOSTNAME_VERIFIER);
        }
    }

    /**
     * 过滤异常
     *
     * @param e
     * @return
     */
    private boolean filterException(Exception e) {
        return e instanceof SocketException;
    }
}