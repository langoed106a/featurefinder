package app.util.feature;

import java.util.UUID;

public class TokenGenerator {

    public TokenGenerator() {

    }

    public static String newToken() {
        String uniqueID = UUID.randomUUID().toString();
        return uniqueID;
    }
}