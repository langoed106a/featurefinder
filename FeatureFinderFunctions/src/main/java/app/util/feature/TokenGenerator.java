package app.util.feature;

import org.apache.commons.lang3.RandomStringUtils;

public class TokenGenerator {

    public TokenGenerator() {

    }

    public static String newToken() {
        String uniqueID = RandomStringUtils.randomAlphanumeric(20);
        return uniqueID;
    }
}