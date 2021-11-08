package br.ufpb.dcx.appalpha.control.util;

import java.text.Normalizer;

public class TextUtil {
    private static TextUtil instance;

    private TextUtil(){}

    public static TextUtil getInstance(){
        if(instance == null){
            instance = new TextUtil();
        }

        return instance;
    }

    public String getUnderlineOfThis(String word) {
        StringBuilder s = new StringBuilder(word);
        StringBuilder newS = new StringBuilder("");

        for (int i = 0; i < s.length(); i++) {
            newS.append("_");
        }

        return newS.toString();
    }

    public static boolean isAllInteger(String text) {
        try {
            Integer.parseInt(text);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }

    public static String normalize(String text) {
        return Normalizer.normalize(text, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }
}
