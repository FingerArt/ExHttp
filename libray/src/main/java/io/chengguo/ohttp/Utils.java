package io.chengguo.ohttp;

import android.os.Handler;
import android.os.Looper;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    static SSLSocketFactory getSSL() throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        return getSSL(((InputStream) null), null);
    }

    static SSLSocketFactory getSSL(InputStream inputStream, String password)
            throws NoSuchAlgorithmException, CertificateException, KeyStoreException, IOException, UnrecoverableKeyException, KeyManagementException {

        KeyManager[] keyManagers = null;
        TrustManager[] trustManagers;
        if (inputStream != null) {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");

            InputStream sslIS = null;
            Certificate certificate;
            try {
                sslIS = new BufferedInputStream(inputStream);
                certificate = certificateFactory.generateCertificate(sslIS);
            } finally {
                if (sslIS != null) {
                    sslIS.close();
                }
            }

            KeyStore keyStore = KeyStore.getInstance("PKCS12");

            keyStore.load(null, null);
            keyStore.setCertificateEntry("trust", certificate);

            KeyManagerFactory keyFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyFactory.init(keyStore, password.toCharArray());
            keyManagers = keyFactory.getKeyManagers();

            TrustManagerFactory trustFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustFactory.init(keyStore);
            trustManagers = trustFactory.getTrustManagers();
        } else {
            trustManagers = new TrustManager[]{new X509TrustManager() {
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
        }
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagers, trustManagers, new SecureRandom());
        return sslContext.getSocketFactory();
    }


    static SSLSocketFactory getSSL(String sslPath, String password)
            throws NoSuchAlgorithmException, CertificateException, KeyStoreException, IOException, UnrecoverableKeyException, KeyManagementException {
        return getSSL(new FileInputStream(sslPath), password);
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
    public static String outputString(InputStream is) {
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

    /**
     * 生成Tag
     *
     * @param text
     * @return
     */
    public static String generateTag(String text) {
        return text;
    }
}