package org.ipgeoloc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;

public class IPAPITest {

    @Test
    public void testIP_API_dot_com() {
        Optional<IPAPIAnswer> oprequest = IPAPIRequest.remoteCall(
                "http://ip-api.com/json/" +"24.48.0.1" + "?fields=country,countryCode");
        if (oprequest.isPresent()) {
            IPAPIAnswer answer = oprequest.get();
            assertEquals("Canada", answer.getCountry());
        }

    }

    @Test
    public void testWhenURLNotSet() {
        // fallback for specific case (see IPAPIRequest code)
        assertEquals("Canada", IPAPIRequest.ip2Country("131.132.0.0"));
        // other case should not work
        assertThrows(RuntimeException.class, () -> IPAPIRequest.ip2Country("24.48.0.1"));
    }
    
    @Test
    public void testRegularUsage() {
        IPAPIRequest.setURL("http://ip-api.com/json/");
        assertEquals("Canada", IPAPIRequest.ip2Country("24.48.0.1"));
        assertEquals("United States", IPAPIRequest.ip2Country("100.0.0.0"));
    }
    
}
