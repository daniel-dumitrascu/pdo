package org.projects.centralpoint.Utils.String;

import java.util.ArrayList;
import java.util.List;


public class StringHelper
{
    public enum EscapeChar
    {
        SINGLE_QUOTES,
        DOUBLE_QUOTES
    }

    public static List<String> TokenizeByDelimiter(String text, String delimiter, boolean acceptEmptyStrings)
    {
        String regex = "\\";
        regex += delimiter;
        ArrayList<String> tokens = new ArrayList();
        String params[];

        if(!acceptEmptyStrings)
            regex += "+";

        params = text.split(regex);

        for (int i = 0; i < params.length; i++)
            tokens.add(params[i].trim());

        return tokens;
    }

    public static String EscapeString(String text, EscapeChar escapedChar)
    {
        if(text == null)
            return null;

        String escapedText = new String();

        if(escapedChar == EscapeChar.SINGLE_QUOTES)
            escapedText = "\'" + text + "\'";
        else if(escapedChar == EscapeChar.DOUBLE_QUOTES)
            escapedText = "\"" + text + "\"";

        return escapedText;
    }
}
