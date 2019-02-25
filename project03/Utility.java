/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project03;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author nraj39
 */
public class Utility {

    private Utility() {

    }

    public static Date getTodaysDate() {
        Calendar today = Calendar.getInstance();
        today.clear(Calendar.HOUR);
        today.clear(Calendar.MINUTE);
        today.clear(Calendar.SECOND);
        return today.getTime();
    }

    public static boolean isNullOrBlank(String param) {
        if (param == null || param.trim().length() == 0) {
            return true;
        }
        return false;
    }

    public static String readFileAsString(String fileName) throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        StringBuilder sb = new StringBuilder();

        String line = br.readLine();
        while (line != null) {
            sb.append(line).append("\n");
            line = br.readLine();
        }

        return sb.toString();
    }

    public static String DateToString(Date date) {
        if (date == null)
            return "";
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public static String DateToString(Date date, String format) {
        if (date == null)
            return "";
        if (format == null)
            return date.toString();
        
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }
}
