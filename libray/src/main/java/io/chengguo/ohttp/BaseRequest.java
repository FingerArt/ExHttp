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
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

import static io.chengguo.ohttp.Utils.HOSTNAME_VERIFIER;
import static io.chengguo.ohttp.Utils.THREAD_POOL;
import static io.chengguo.ohttp.Utils.generateTag;
import static io.chengguo.ohttp.Utils.mergeUrl;

/**
 * @author FingerArt http://fingerart.me
 * @date 2017年05月18日 15:32
 */
public abstract class BaseRequest implements IRequest {
    private static final String TAG = "BaseRequest";
    protected Map<String, String> queries;
    protected Map<String, String> params;
    protected Map<String, String> headers;
    protected String url;
    private static final int CALLBACK_START = 1;
    private static final int CALLBACK_SUCCESS = 2;

    public BaseRequest(BaseRequestBuilder requestBuilder) {
        url = requestBuilder.url;
        queries = requestBuilder.queries;
        params = requestBuilder.params;
        headers = requestBuilder.headers;
    }

    @Override
    public HttpURLConnection execute() throws Exception {
        safeCheck();
        HttpURLConnection connection = getConnection();
        connection.connect();
        transferInputStream(connection);
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
                    httpRequestListener.onStart();
                    HttpURLConnection connection = execute();
                    OHttp.addTag(tag, connection);
                    InputStream inputStream = connection.getInputStream();//start fetch
                    int responseCode = connection.getResponseCode();
                    httpRequestListener.onSuccess(responseCode, inputStream, connection);
                } catch (Exception e) {
                    try {
                        if (!filterException(e)) {
                            httpRequestListener.onError(e);
                        } else e.printStackTrace();
                    } catch (Exception ee) {
                    }
                } finally {
                    OHttp.cancel(tag);
                    try {
                        httpRequestListener.onFinish();
                    } catch (Exception ee) {
                    }
                }
            }
        });
    }

    /**
     * 传输数据
     *
     * @param connection
     */
    protected void transferInputStream(HttpURLConnection connection) throws IOException {
    }

    /**
     * Method
     *
     * @param connection
     * @throws Exception
     */
    protected abstract void prepareMethod(HttpURLConnection connection) throws Exception;

    private void safeCheck() throws Exception {
        if (TextUtils.isEmpty(url)) {
            throw new Exception("please set url");
        }
    }

    private HttpURLConnection getConnection() throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(mergeUrl(url, queries)).openConnection();

        prepareMethod(connection);
        setHeaders(connection);

        //common set
        connection.setUseCaches(false);
        connection.setReadTimeout(8 * 1000);
        connection.setConnectTimeout(8 * 1000);
        connection.setRequestProperty("User-Agent", "OHttp");

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
            https.setSSLSocketFactory(Utils.getSSL());
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