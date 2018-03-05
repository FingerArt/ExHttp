package io.chengguo.ohttp;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * @author FingerArt http://fingerart.me
 * @date 2017年05月18日 14:33
 */
class Utils {

    static final Handler HANDLER = new Handler(Looper.getMainLooper());

    static final ExecutorService THREAD_POOL = Executors.newCachedThreadPool();

    static final HostnameVerifier HOSTNAME_VERIFIER = new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    static SSLSocketFactory getDefaultSSLSocketFactory() {
        TrustManager[] trustManagers = new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }};
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagers, null);
            return sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 合并URL和参数
     *
     * @param url
     * @param queries
     * @return
     */
    static String mergeUrl(String url, Map<String, String> queries) {
        StringBuilder sb = new StringBuilder(url);
        if (!url.startsWith("http")) {
            sb.insert(0, "http://");
        }
        sb.append(xWwwFormUrlencoded(queries, true));
        return sb.toString();
    }

    /**
     * 拼接参数
     *
     * @param data
     * @return
     */
    static String xWwwFormUrlencoded(Map<String, String> data, boolean query) {
        StringBuilder sb = new StringBuilder();
        if (data != null) {
            if (query) sb.append("?");
            for (Map.Entry<String, String> entry : data.entrySet()) {
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            sb.delete(sb.length() - 1, sb.length());
        }
        return sb.toString();
    }

    /**
     * InputStream convert to String
     *
     * @param is
     * @return
     */
    static String outputString(InputStream is) {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String str;
        try {
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    static void inputStream2outputStream(InputStream is, OutputStream out) throws IOException {
        try {
            byte[] b = new byte[1024 * 32];
            int len;
            while ((len = is.read(b)) != -1) {
                out.write(b, 0, len);
            }
        } finally {
            closeQuietly(is);
        }
    }

    static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (RuntimeException rethrown) {
                throw rethrown;
            } catch (Exception ignored) {
            }
        }
    }

    /**
     * 生成Tag
     *
     * @param text
     * @return
     */
    static String generateTag(String text) {
        return text;
    }

    /**
     * 获取文件 Mime Type
     *
     * @param path
     * @return
     */
    static String getFileType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        return fileNameMap.getContentTypeFor(path);
    }

    /**
     * 获取Context
     *
     * @return
     */
    static Context applicationContext() {
        try {
            Application app = (Application) Class.forName("android.app.ActivityThread").getMethod("currentApplication").invoke(null, (Object[]) null);
            return app.getApplicationContext();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    static void runOnUiThread(Runnable runnable) {
        HANDLER.post(runnable);
    }

    static Future<?> runOnThread(Runnable runnable) {
        return THREAD_POOL.submit(runnable);
    }

    public static boolean isSuccessful(int code) {
        return code >= 200 && code < 300;
    }
}