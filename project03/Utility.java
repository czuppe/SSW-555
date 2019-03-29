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
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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

    public static int YearsBetween(LocalDate beginDate, LocalDate endDate){
        return Period.between(beginDate, endDate).getYears();
    }
    
    public static LocalDate ToLocalDate(Date date){
        return date != null ? date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;
    }
    
    //sort by oldest first
    public static void SortChildrenByBirthdate(FamilyEntity entity){        
        
        if(entity.ChildrenId == null || entity.ChildrenId.size() == 1){
            return;
        }                   
        
        //childrenId list larger than 1
        List<String> childrenIds = entity.ChildrenId;
        
        for(int i=1; i<childrenIds.size(); i++){//start at 1
            //get person's birthdate
            LocalDate temp;
            int j = i-1;
                        
            LocalDate birthdate1 = Utility.ToLocalDate(entity.getGEDCOMData().getIndividuals().get(childrenIds.get(i)).BirthDate);          
            LocalDate birthdate2 = Utility.ToLocalDate(entity.getGEDCOMData().getIndividuals().get(childrenIds.get(j)).BirthDate);
            
            while(j >= 0 && birthdate1.isBefore(birthdate2)){
                temp = birthdate2;
                birthdate2 = birthdate1;
                birthdate1 = temp;
                //swap i and i-1
                String tempId = entity.ChildrenId.get(i);
                entity.ChildrenId.set(i, entity.ChildrenId.get(j));
                entity.ChildrenId.set(j, tempId);
                j--;                
            }
        }
    }
}

