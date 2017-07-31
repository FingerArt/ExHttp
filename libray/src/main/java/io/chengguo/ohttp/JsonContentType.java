package io.chengguo.ohttp;

/**
 * @author FingerArt http://fingerart.me
 * @date 2017年07月31日 9:56
 */
public class JsonContentType extends DefaultContentType {

    @Override
    public String contentType() {
        return "application/json";
    }

}
