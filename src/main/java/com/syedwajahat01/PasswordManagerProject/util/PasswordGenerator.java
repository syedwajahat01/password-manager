package com.syedwajahat01.PasswordManagerProject.util;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * A utility class for generating strong, random passwords based on dynamic criteria.
 */
public class PasswordGenerator {

    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARS = "!@#$%^&*()-_=+<>?";
    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * Generates a secure random password based on specified criteria.
     *
     * @param length The desired length of the password.
     * @param useUpper true to include uppercase letters.
     * @param useLower true to include lowercase letters.
     * @param useDigits true to include digits.
     * @param useSymbols true to include special characters.
     * @return A securely generated random password.
     */
    public static String generatePassword(int length, boolean useUpper, boolean useLower, boolean useDigits, boolean useSymbols) {
        // 1. Build the set of allowed characters and a list of required characters.
        StringBuilder allowedCharsBuilder = new StringBuilder();
        List<Character> requiredChars = new ArrayList<>();

        if (useUpper) {
            allowedCharsBuilder.append(UPPERCASE);
            requiredChars.add(getRandomChar(UPPERCASE));
        }
        if (useLower) {
            allowedCharsBuilder.append(LOWERCASE);
            requiredChars.add(getRandomChar(LOWERCASE));
        }
        if (useDigits) {
            allowedCharsBuilder.append(DIGITS);
            requiredChars.add(getRandomChar(DIGITS));
        }
        if (useSymbols) {
            allowedCharsBuilder.append(SPECIAL_CHARS);
            requiredChars.add(getRandomChar(SPECIAL_CHARS));
        }

        String allowedChars = allowedCharsBuilder.toString();

        // 2. Validate the inputs to prevent impossible scenarios.
        if (allowedChars.isEmpty()) {
            throw new IllegalArgumentException("Cannot generate password: at least one character type must be selected.");
        }
        if (length < requiredChars.size()) {
            throw new IllegalArgumentException("Password length is too short to include one of each selected character type.");
        }

        // 3. Fill the rest of the length with random characters from the allowed set.
        int remainingLength = length - requiredChars.size();
        Stream<Character> remainingCharsStream = IntStream.range(0, remainingLength)
                .mapToObj(i -> getRandomChar(allowedChars));

        // 4. Combine required and remaining characters, shuffle, and build the final string.
        List<Character> passwordChars = Stream.concat(requiredChars.stream(), remainingCharsStream)
                .collect(Collectors.toList());
        Collections.shuffle(passwordChars, RANDOM);

        return passwordChars.stream()
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }

    private static Character getRandomChar(String characterSet) {
        return characterSet.charAt(RANDOM.nextInt(characterSet.length()));
    }
}