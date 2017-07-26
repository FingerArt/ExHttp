package io.chengguo.ohttp;

import java.util.Map;

/**
 * @author FingerArt http://fingerart.me
 * @date 2017年05月18日 15:07
 */
public class PostRequestBuilder extends BaseRequestBuilder<PostRequestBuilder> {

    /**
     * 像Body中添加参数
     *
     * @return
     */
    public PostRequestBuilder addParam(String key, String value) {
        prepareParams();
        params.put(key, value);
        return this;
    }

    /**
     * 像Body中添加参数
     *
     * @return
     */
    public PostRequestBuilder addParam(Map<String, String> params) {
        prepareParams();
        super.params.putAll(params);
        return this;
    }

    @Override
    public IRequest build() {
        return new PostRequest(this);
    }
}