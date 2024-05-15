package com.example.roomify;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {


    public static String stripHtmlTags(String input) {
        if (input == null) {
            return null;
        }
        String htmlRegex = "<[^>]+>";
        Pattern pattern = Pattern.compile(htmlRegex);
        Matcher matcher = pattern.matcher(input);
        return matcher.replaceAll("");
    }
}
