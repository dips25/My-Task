package com.assgn.mytask.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public static boolean isBlank(String s) {

        if (s != null && !s.equals("")) {

            Pattern p = Pattern.compile("\\s+");
            Matcher m = p.matcher(s);

            if (m.matches()) {

                return true;
            }

            return false;


        } else {

            return true;
        }

    }
}
