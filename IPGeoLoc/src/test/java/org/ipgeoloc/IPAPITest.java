package org.ipgeoloc;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

public class IPAPITest {
    public static void main(String[] args) throws IOException {
        Optional<IPAPIAnswer> oprequest = IPAPIRequest.remoteCall(
                "http://ip-api.com/json/" +"24.48.0.1" + "?fields=country,countryCode");
        if (oprequest.isPresent()) {
            IPAPIAnswer answer = oprequest.get();
            System.out.println("Your IP is: " + answer.getIp());
            System.out.println("Your country is: " + answer.getCountry());
            System.out.println("Your country code is: " + answer.getCountryCode());
            System.out.println("Your region is: " + answer.getRegion());
            System.out.println("Your region name is: " + answer.getRegionName());
            System.out.println("Your city is: " + answer.getCity());
            System.out.println("Your zip code is: " + answer.getZip());
            System.out.println("Your latitude is: " + answer.getLat());
            System.out.println("Your longitude is: " + answer.getLon());
            System.out.println("Your timezone is: " + answer.getTimezone());
            System.out.println("Your ISP is: " + answer.getIsp());
            System.out.println("Your organization is: " + answer.getOrg());
            System.out.println("Your autonomous system is: " + answer.getAs());
            System.out.println("Your query is: " + answer.getQuery());
        }

        try {
            IPAPIRequest.ip2Country("24.48.0.1");
        } catch (RuntimeException e) {
            e.printStackTrace();
        }

        IPAPIRequest.setURL("http://ip-api.com/json/");
        System.out.println(IPAPIRequest.ip2Country("24.48.0.1"));
        System.out.println(IPAPIRequest.ip2LatLong("24.48.0.1"));
    }
    
}
