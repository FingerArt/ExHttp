package io.chengguo.ohttp;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author FingerArt http://fingerart.me
 * @date 2017年05月18日 15:07
 */
public abstract class BaseRequestBuilder<T> {
    protected String url;
    protected Map<String, String> queries;
    protected Map<String, String> headers;
    protected Map<String, String> cookies;
    protected InputStream sslInputStream;
    protected String sslPassword;

    /**
     * 设置Url
     *
     * @return
     */
    public T url(String url) {
        this.url = url;
        return (T) this;
    }

    /**
     * 添加Query参数
     *
     * @return
     */
    public T addQuery(String key, String value) {
        prepareQueries();
        queries.put(key, value);
        return (T) this;
    }

    /**
     * 添加Query参数
     *
     * @return
     */
    public T addQuery(Map<String, String> querys) {
        prepareQueries();
        this.queries.putAll(querys);
        return (T) this;
    }

    /**
     * 添加请求头
     *
     * @param key
     * @param value
     * @return
     */
    public T addHeader(String key, String value) {
        prepareHeaders();
        headers.put(key, value);
        return (T) this;
    }

    /**
     * 添加请求头
     *
     * @param headers
     * @return
     */
    public T addHeader(Map<String, String> headers) {
        prepareHeaders();
        headers.putAll(headers);
        return (T) this;
    }

    /**
     * 添加cookie
     *
     * @param key
     * @param value
     * @return
     */
    public T addCookie(String key, String value) {
        prepareCookies();
        cookies.put(key, value);
        return ((T) this);
    }

    /**
     * 添加cookie
     *
     * @param cookies
     * @return
     */
    public T addCookie(Map<String, String> cookies) {
        prepareCookies();
        cookies.putAll(cookies);
        return ((T) this);
    }

    public T ssl(InputStream inputStream, String password) {
        sslInputStream = inputStream;
        sslPassword = password;
        return ((T) this);
    }

    /**
     * 构建请求
     *
     * @return
     */
    public abstract IRequest build();

    protected synchronized void prepareQueries() {
        if (queries == null) {
            queries = new LinkedHashMap<>();
        }
    }

    private synchronized void prepareHeaders() {
        if (headers == null) {
            headers = new LinkedHashMap<>();
        }
    }

    private void prepareCookies() {
        if (cookies == null) {
            cookies = new LinkedHashMap<>();
        }
    }
}
