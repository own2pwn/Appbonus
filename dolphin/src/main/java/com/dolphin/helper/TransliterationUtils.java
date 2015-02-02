package com.dolphin.helper;

import java.util.HashMap;
import java.util.Map;


public class TransliterationUtils {
    private static final Map<Character, String> charMap = new HashMap<Character, String>();
    private static final Map<Character, String> vowelsMap = new HashMap<Character, String>();
    private static final Map<Character, String> consonantsMap = new HashMap<Character, String>();

    static {
        vowelsMap.put('А', "A");
        vowelsMap.put('Е', "E");
        vowelsMap.put('Ё', "E");
        vowelsMap.put('И', "I");
        vowelsMap.put('О', "O");
        vowelsMap.put('У', "U");
        vowelsMap.put('Ы', "Y");
        vowelsMap.put('Э', "E");
        vowelsMap.put('Ю', "Yu");
        vowelsMap.put('Я', "Ya");

        vowelsMap.put('а', "a");
        vowelsMap.put('е', "e");
        vowelsMap.put('ё', "e");
        vowelsMap.put('и', "i");
        vowelsMap.put('о', "o");
        vowelsMap.put('у', "u");
        vowelsMap.put('ы', "y");
        vowelsMap.put('э', "e");
        vowelsMap.put('ю', "yu");
        vowelsMap.put('я', "ya");

        consonantsMap.put('Б', "B");
        consonantsMap.put('В', "V");
        consonantsMap.put('Г', "G");
        consonantsMap.put('Д', "D");
        consonantsMap.put('Ж', "Zh");
        consonantsMap.put('З', "Z");
        consonantsMap.put('Й', "Y");
        consonantsMap.put('К', "K");
        consonantsMap.put('Л', "L");
        consonantsMap.put('М', "M");
        consonantsMap.put('Н', "N");
        consonantsMap.put('П', "P");
        consonantsMap.put('Р', "R");
        consonantsMap.put('С', "S");
        consonantsMap.put('Т', "T");
        consonantsMap.put('Ф', "F");
        consonantsMap.put('Х', "Kh");
        consonantsMap.put('Ц', "Ts");
        consonantsMap.put('Ч', "Ch");
        consonantsMap.put('Ш', "Sh");
        consonantsMap.put('Щ', "Shch");
        consonantsMap.put('Ъ', "");
        consonantsMap.put('Ь', "");

        consonantsMap.put('б', "b");
        consonantsMap.put('в', "v");
        consonantsMap.put('г', "g");
        consonantsMap.put('д', "d");
        consonantsMap.put('ж', "zh");
        consonantsMap.put('з', "z");
        consonantsMap.put('й', "y");
        consonantsMap.put('к', "k");
        consonantsMap.put('л', "l");
        consonantsMap.put('м', "m");
        consonantsMap.put('н', "n");
        consonantsMap.put('п', "p");
        consonantsMap.put('р', "r");
        consonantsMap.put('с', "s");
        consonantsMap.put('т', "t");
        consonantsMap.put('ф', "f");
        consonantsMap.put('х', "kh");
        consonantsMap.put('ц', "ts");
        consonantsMap.put('ч', "ch");
        consonantsMap.put('ш', "sh");
        consonantsMap.put('щ', "shch");
        consonantsMap.put('ъ', "");
        consonantsMap.put('ь', "");

        charMap.putAll(vowelsMap);
        charMap.putAll(consonantsMap);
    }

    public static String transliterate(String string) {
        StringBuilder transliteratedString = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            Character ch = string.charAt(i);
            Character nextCh = null;
            if (i != string.length() - 1) {
                nextCh = string.charAt(i + 1);
            }
            String charFromMap = charMap.get(ch);
            if (charFromMap == null) {
                transliteratedString.append(ch);
            } else if (ch == 'ь' && vowelsMap.containsKey(nextCh) &&
                    (!vowelsMap.get(nextCh).startsWith("y") && !vowelsMap.get(nextCh).startsWith("Y"))) {
                transliteratedString.append('y');
            } else if (ch == 'Ь' && vowelsMap.containsKey(nextCh) &&
                    (!vowelsMap.get(nextCh).startsWith("y") && !vowelsMap.get(nextCh).startsWith("Y"))) {
                transliteratedString.append('Y');
            } else {
                transliteratedString.append(charFromMap);
            }
        }
        return transliteratedString.toString();
    }
}
