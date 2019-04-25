package am.iunetworks.ecr.client.V04.pojo;

import sun.misc.BASE64Decoder;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: vmomjyan
 * Date: 12/19/13
 * Time: 1:37 AM
 */
public class OperatorLoginResponse extends CommonResponse {
    String key;

    public byte[] getKeyBytes() {
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            return decoder.decodeBuffer(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
