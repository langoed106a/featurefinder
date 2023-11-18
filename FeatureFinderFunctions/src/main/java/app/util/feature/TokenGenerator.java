package app.util.feature;

import org.apache.commons.lang3.RandomStringUtils;

public class TokenGenerator {

    public TokenGenerator() {

    }

    public static String newToken() {
        String uniqueID = RandomStringUtils.random(20).toString();
        return uniqueID;
    }
}