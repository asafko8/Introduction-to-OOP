package image_char_matching;

import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

/**
 * Responsible for matching an ASCII character to a sub image with a given brightness.
 *
 * @author Asaf Korman
 */
public class SubImgCharMatcher {

    private static final int CHAR_RESOLUTION = 16;
    private final HashMap<Character, Double> charBrightness;

    private double minBrightness;
    private double maxBrightness;

    /**
     * Constructor. Creates the data structure that holds the set of characters and their brightness values.
     *
     * @param charset The set of ASCII characters for the use of our algorithm.
     */
    public SubImgCharMatcher(char[] charset) {
        this.charBrightness = new HashMap<>();
        for (char c : charset) {
            charBrightness.put(c, brightnessValue(c));
        }
        this.minBrightness = Collections.min(charBrightness.values());
        this.maxBrightness = Collections.max(charBrightness.values());
        normalizeBrightnessValues();
    }

    /*
     * Normalizes the brightness values of all characters in the set by linear stretching of the min/max values.
     * This is in order to prevent a situation where in an image with similar shades we will get the same
     * character for every pixel.
     */
    private void normalizeBrightnessValues() {
        for (char c : charBrightness.keySet()) {
            double newBrightness = (charBrightness.get(c) - minBrightness) / (maxBrightness - minBrightness);
            charBrightness.put(c, newBrightness);
        }
    }

    /**
     * Given a sub image brightness value, the method will return the character (from the character set) with
     * the closest absolute value to the given brightness. In case there are several characters from the set
     * with the same brightness, the character with the lowest ASCII value among them will be returned.
     *
     * @param brightness The sub image brightness value.
     * @return The character with the closest brightness value.
     */
    public char getCharByImageBrightness(double brightness) {
        char closestChar = 0; // 0 is null in the ASCII table.
        double closestBrightness = 1; // 1 is the biggest distance that could be.
        for (char c : charBrightness.keySet()) {
            double currDistance = Math.abs(brightness - charBrightness.get(c));
            if (currDistance < closestBrightness) {
                closestChar = c;
                closestBrightness = currDistance;
            } else if (currDistance == closestBrightness) {
                closestChar = (char) Math.min(c, closestChar);
            }
        }
        return closestChar;
    }

    /**
     * Adds new character to the set of characters.
     * Takes care to calculate the brightness and normalize (if necessary)  with the insert.
     *
     * @param c The character to be added.
     */
    public void addChar(char c) {
        double brightness = brightnessValue(c);
        if (brightness > maxBrightness) {
            maxBrightness = brightness;
            charBrightness.put(c, brightness);
            normalizeBrightnessValues();
        } else if (brightness < minBrightness) {
            minBrightness = brightness;
            charBrightness.put(c, brightness);
            normalizeBrightnessValues();
        } else {
            // Normalizes according to the min/max brightness values in charBrightness.
            double newBrightness = (brightness - minBrightness) / (maxBrightness - minBrightness);
            charBrightness.put(c, newBrightness);
        }
    }

    /*
     * Calculate the brightness value of a given character.
     */
    private double brightnessValue(char c) {
        double whitePixelsCounter = 0;
        boolean[][] pixels = CharConverter.convertToBoolArray(c);
        for (int row = 0 ; row < CHAR_RESOLUTION ; row++) {
            for (int col = 0 ; col < CHAR_RESOLUTION ; col++) {
                if (pixels[row][col]) {
                    whitePixelsCounter += 1;
                }
            }
        }
        // Normalize to get brightness value in [0,1].
        return whitePixelsCounter / (CHAR_RESOLUTION * CHAR_RESOLUTION);
    }

    /**
     * Removes tha given character from the set.
     *
     * @param c The character to be removed.
     */
    public void removeChar(char c) {
        if (charBrightness.get(c) == null) {
            return;
        }
        double removedCharBrightness = charBrightness.remove(c);
        if (charBrightness.size() > 1) {
            // Update min/max brightness values and normalize if necessary.
            if (minBrightness == removedCharBrightness) {
                minBrightness = Collections.min(charBrightness.values());
                normalizeBrightnessValues();
            } else if (maxBrightness == removedCharBrightness) {
                maxBrightness = Collections.max(charBrightness.values());
                normalizeBrightnessValues();
            }
        }
    }

    /**
     * Getter.
     *
     * @return The character set.
     */
    public Set<Character> getCharSet() {
        return charBrightness.keySet();
    }
}
