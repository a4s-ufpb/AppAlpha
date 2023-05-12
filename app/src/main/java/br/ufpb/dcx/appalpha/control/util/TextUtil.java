package br.ufpb.dcx.appalpha.control.util;

import java.text.Normalizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class to manage text
 */
public class TextUtil {
    private static TextUtil instance;

    private TextUtil() {
    }

    /**
     * Get shared instance
     *
     * @return
     */
    public static TextUtil getInstance() {
        if (instance == null) {
            instance = new TextUtil();
        }

        return instance;
    }

    /**
     * Generate underline format from an word
     *
     * @param word
     * @return
     */
    public String getUnderlineOfThis(String word) {
        StringBuilder s = new StringBuilder(word);
        StringBuilder newS = new StringBuilder("");

        for (int i = 0; i < s.length(); i++) {

            if (isCharacterAvailable(word.charAt(i))) {
                // letter available in the keyboard, we can use underline
                newS.append(String.valueOf(word.charAt(i)));
                continue;
            }

            newS.append("_");
        }

        return newS.toString();
    }

    /**
     * Character available in the Keyboard of player
     *
     * @param character
     * @return Boolean, character disponivel
     */
    private Boolean isCharacterAvailable(char character) {
        Pattern p = Pattern.compile("[^a-z]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(String.valueOf(treatCharacter(character)));
        return m.find();
    }

    /**
     * Check if an word is only number
     *
     * @param text
     * @return
     */
    public static boolean isAllInteger(String text) {
        try {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Treat letter, removes ascents and lowercase
     *
     * @param character
     * @return char, letter to treat
     */
    public char treatCharacter(char character) {
        // lowercase without accents
        return Normalizer.normalize(Character.toString(character).toLowerCase(), Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "").charAt(0);
    }

    /**
     * Return an word without ascents
     *
     * @param text
     * @return
     */
    public static String normalize(String text) {
        return Normalizer.normalize(text, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }
}
