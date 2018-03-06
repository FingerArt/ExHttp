package io.chengguo.exhttp;

/**
 * @author FingerArt http://fingerart.me
 * @date 2017年05月18日 15:06
 */
public class GetRequestBuilder extends BaseRequestBuilder<GetRequestBuilder> {

    @Override
    public IRequest build() {
        return new GetRequest(this);
    }
}
