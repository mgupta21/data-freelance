package com.java.weddingplz.report;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringParser {

    public List<String> parse(String str, String regex) {
        Pattern pat = Pattern.compile(regex);
        Matcher matcher = pat.matcher(str);
        List<String> matches = new ArrayList<>();
        while (matcher.find()) {
            matches.add(matcher.group());
        }
        return matches;
    }
}