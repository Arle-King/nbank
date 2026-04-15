package org.example.generators;

import org.apache.commons.lang3.RandomStringUtils;

@Deprecated
public class RandomData {
    private RandomData() {};

    public static String getRandomUserName() {
        return RandomStringUtils.randomAlphanumeric(8);
    }

    public static String getRandomPassword() {
        return RandomStringUtils.randomAlphanumeric(4).toLowerCase() + RandomStringUtils.randomAlphanumeric(4).toUpperCase() + "#2";
    }
}
