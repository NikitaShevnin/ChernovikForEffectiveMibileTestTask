package com.example.bankcards.util;

/**
 * Utility class for masking sensitive card numbers.
 */
public class CardMaskUtil {
    public static String mask(String number) {
        if (number == null || number.length() < 4) {
            return number;
        }
        String last4 = number.substring(number.length() - 4);
        return "**** **** **** " + last4;
    }
}
