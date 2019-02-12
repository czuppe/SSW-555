/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project03;

import java.util.Date;
import java.util.List;

/**
 *
 * @author nraj39
 */
public class PersonEntityValidator {
    private PersonEntityValidator() {
        
    }
    
    //US01: Dates before current date
    public static void datesBeforeCurrentDateCheck(PersonEntity entity, List<ValidationResult> results) {
        if (entity == null || results == null)
            return;
        
        Date todaysDate = Utility.getTodaysDate();
        if (entity.BirthDate != null && entity.BirthDate.after(todaysDate)) {
            results.add(new ValidationResult("Birth date must be before today's date.", entity));
        }
        if (entity.DeathDate != null && entity.DeathDate.after(todaysDate)) {
            results.add(new ValidationResult("Death date must be before today's date.", entity));
        }
    }    
    
    //US03: Birth before death
    public static void birthBeforeDeathDateCheck(PersonEntity entity, List<ValidationResult> results) {
        if (entity == null || results == null)
            return;
    }    
}
