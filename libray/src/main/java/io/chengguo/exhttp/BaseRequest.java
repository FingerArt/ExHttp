package io.chengguo.exhttp;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

import static io.chengguo.exhttp.Utils.HOSTNAME_VERIFIER;
import static io.chengguo.exhttp.Utils.getDefaultSSLSocketFactory;
import static io.chengguo.exhttp.Utils.mergeUrl;
import static io.chengguo.exhttp.Utils.runOnThread;
import static io.chengguo.exhttp.Utils.runOnUiThread;

/**
 * @author FingerArt http://fingerart.me
 * @date 2017年05月18日 15:32
 */
public abstract class BaseRequest implements IRequest {
    private static final String TAG = "BaseRequest";
    protected final SSLSocketFactory sslSocketFactory;
    protected final HostnameVerifier hostnameVerifier;
    protected Map<String, String> queries;
    protected Map<String, String> headers;
    protected Map<String, String> cookies;
    protected String url;
    private HttpURLConnection connection;

    public BaseRequest(BaseRequestBuilder requestBuilder) {
        url = requestBuilder.url;
        queries = requestBuilder.queries;
        headers = requestBuilder.headers;
        cookies = requestBuilder.cookies;
        sslSocketFactory = requestBuilder.sslSocketFactory;
        hostnameVerifier = requestBuilder.hostnameVerifier;
    }

    public Response execute() throws Exception {
        connection = transferOutputStream(getConnection());
        return Response.getResponseWithConnection(connection);
    }

    @Override
    public void execute(final IHttpRequestCallback httpRequestListener) {
        runOnThread(new Runnable() {
            @Override
            public void run() {
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            httpRequestListener.onStart();
                        }
                    });
                    final Response response = execute();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            httpRequestListener.onSuccess(response);
                        }
                    });
                } catch (final Exception e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            httpRequestListener.onError(e);
                        }
                    });
                } finally {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            httpRequestListener.onFinish();
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
    protected HttpURLConnection transferOutputStream(HttpURLConnection connection) throws Exception {
        return connection;
    }

    /**
     * Method
     *
     * @param connection
     * @throws Exception
     */
    protected abstract void prepareRequest(HttpURLConnection connection) throws Exception;

    private HttpURLConnection getConnection() throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(mergeUrl(url, queries)).openConnection();

        prepareRequest(connection);
        setHeaders(connection);
        setCookies(connection);

        //common set
        connection.setUseCaches(false);
        connection.setReadTimeout(8 * 1000);
        connection.setConnectTimeout(8 * 1000);
        connection.setRequestProperty("User-Agent", "ExHttp " + BuildConfig.VERSION_NAME);

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
    protected void setSSL(HttpURLConnection connection) throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException, NoSuchProviderException {
        if (connection instanceof HttpsURLConnection) {
            HttpsURLConnection https = (HttpsURLConnection) connection;
            https.setSSLSocketFactory(sslSocketFactory == null ? getDefaultSSLSocketFactory() : sslSocketFactory);
            https.setHostnameVerifier(hostnameVerifier == null ? HOSTNAME_VERIFIER : hostnameVerifier);
        }
    }

    @Override
    public void cancel() {
        if (connection != null) {
            connection.disconnect();
        }
    }
}