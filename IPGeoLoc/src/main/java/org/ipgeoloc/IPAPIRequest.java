package org.ipgeoloc;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.rules.engine.annotations.PureFunction;
import ilog.rules.bom.annotations.CustomProperty;
import ilog.rules.bom.annotations.NotBusiness;
import ilog.rules.bom.annotations.NotVerbalized;
import org.ipgeoloc.IPAPIAnswer;

public class IPAPIRequest {

    private IPAPIRequest() {} // utility class

    private static final AtomicReference<String> url = new AtomicReference<>();

    @NotVerbalized
    public static void setURL(String url) {
        IPAPIRequest.url.set(url);
    }

    @PureFunction
    public static LatLong ip2LatLong(String ipAddress) {
        String url = IPAPIRequest.url.get();
        if (url != null) {
             Optional<IPAPIAnswer> answer = remoteCall(url + ipAddress + "?fields=lat,lon");
            return answer.map(ipapiAnswer -> new LatLong(ipapiAnswer.getLat(), ipapiAnswer.getLon())).orElse(null);
        } else
            throw new RuntimeException("url not initialized");
    }

    @PureFunction
    public static String ip2Country(String ipAddress) {
        String url = IPAPIRequest.url.get();
        if (url != null)
            return remoteCall(url+ ipAddress +  "?fields=country").map(IPAPIAnswer::getCountry).orElse(null);
        else {
            // for testing only
            // 131.132.0.0	131.141.255.255 is in Canada
            try {
                InetAddress inetAddress = InetAddress.getByName(ipAddress);
                byte[] bytes = inetAddress.getAddress();
                int i1 = bytes[0] & 0xFF;
                int i2 = bytes[1] & 0xFF;
                if (i1 == 131 && i2 >= 132 && i2 <=141 )
                    return "Canada";
            } catch (UnknownHostException ignored) {
            }
        }
        throw new RuntimeException("url not initialized");
    }


    @NotBusiness
    public static Optional<IPAPIAnswer> remoteCall(String apiUrl)  {
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            ObjectMapper mapper = new ObjectMapper();
            IPAPIAnswer response = mapper.readValue(reader, IPAPIAnswer.class);
            return Optional.of(response);
        } catch (IOException ignored) {
            return Optional.empty();
        }

    }
}