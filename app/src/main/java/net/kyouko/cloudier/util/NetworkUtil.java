package net.kyouko.cloudier.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.List;

/**
 * Util class for network.
 *
 * @author beta
 */
public class NetworkUtil {

    public static String getIpAddress() {
        try {
            List<NetworkInterface> interfaces =
                    Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface networkInterface : interfaces) {
                List<InetAddress> addresses = Collections.list(networkInterface.getInetAddresses());
                for (InetAddress address : addresses) {
                    if (!address.isLoopbackAddress()) {
                        String addressString = address.getHostAddress();
                        boolean isIpv4 = addressString.indexOf(':') < 0;

                        if (isIpv4) {
                            return addressString;
                        } else {
                            int delim = addressString.indexOf('%');
                            return (delim < 0) ?
                                    addressString.toUpperCase() :
                                    addressString.substring(0, delim).toUpperCase();
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            // Ignored.
        }

        return "";
    }

}
