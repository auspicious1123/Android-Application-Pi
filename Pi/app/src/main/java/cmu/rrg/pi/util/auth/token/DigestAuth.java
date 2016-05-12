package cmu.rrg.pi.util.auth.token;

/**
 * Created by Yang on 5/3/16.
 */

import cmu.rrg.pi.util.Config;
import cmu.rrg.pi.exception.AuthException;

public class DigestAuth {
    public static String signWithData(Mac mac, byte[] data) throws AuthException {
        if (mac == null) {
            mac = new Mac(Config.ACCESS_KEY, Config.SECRET_KEY);
        }
        return mac.signWithData(data);
    }
}
