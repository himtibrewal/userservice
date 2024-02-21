package com.safeway.userservice.utils;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Random;
import java.util.UUID;

public class Commons {
    public static final String PAGE_SIZE = "10";
    public static final String PAGE_O = "0";
    public static final String SORT_BY_ID = "id";
    public static final String TOKEN_TYPE = "Bearer";

    public static final String PAGINATED_DEFAULT = "true";
    public static final String DEFAULT_PASSWORD = "12345678";

    public static String generatePassword() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 8;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        System.out.println(generatedString);
        return generatedString;
    }

    public static String getBaseUrl() {
        return ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
    }

    public static String getUniqueString() {
        return UUID.randomUUID().toString();
    }

    public static boolean isNull(String str){
        return str == null;
    }
    public static boolean isNotNull(String str){
        return !isNull(str);
    }

    public static boolean isEmpty(String str){
        return str.trim().isEmpty();
    }

    public static boolean isNotEmpty(String str){
        return !isEmpty(str);
    }

    public static boolean isNullOrEmpty(String str) {
        return (isEmpty(str) || isEmpty(str));
    }
    public static boolean isNotNullANDEmpty(String str) {
        return (isNotNull(str) && isNotEmpty(str));
    }
}
