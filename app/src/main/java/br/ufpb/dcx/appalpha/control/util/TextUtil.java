package br.ufpb.dcx.appalpha.control.util;

import java.text.Normalizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

            if(caractererIndisponivel(word.charAt(i))) {
                // letra indisponivel no teclado, passar na palavra
                newS.append(String.valueOf(word.charAt(i)));
                continue;
            }

            newS.append("_");
        }

        return newS.toString();
    }

    /**
     * Caractereres indisponivel no teclado para o jogador
     * @param letra
     * @return Boolean, letra disponivel
     */
    private Boolean caractererIndisponivel(char letra)
    {
        Pattern p = Pattern.compile("[^a-z]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(String.valueOf(tratarCaracte(letra)));
        return m.find();
    }

    public static boolean isAllInteger(String text) {
        try {
            Integer.parseInt(text);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }

    /**
     * Tratar caractere removendo acentos e minusculo, para uma simples comparacao
     * @param caractere
     * @return char, letra tratada
     */
    public char tratarCaracte(char caractere)
    {
        // minuscula, sem acentos
        return Normalizer.normalize(Character.toString(caractere).toLowerCase(), Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "").charAt(0);
    }

    public static String normalize(String text) {
        return Normalizer.normalize(text, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }
}
