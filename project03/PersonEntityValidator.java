/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project03;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 *
 * @author nraj39 bella458
 */
public class PersonEntityValidator {
    private PersonEntityValidator() {
        
    }
    
    //US01: Dates before current date
    //Dates (birth, marriage, divorce, death) should not be after the current date
    public static void datesBeforeCurrentDateCheck(PersonEntity entity, List<ValidationResult> results) {
        if (entity == null || results == null) {
            return;
        }

        Date todaysDate = Utility.getTodaysDate();
        if (entity.BirthDate == null) {
            results.add(new ValidationResult("Missing Birth date.", entity, "US01"));
        }
        if (entity.BirthDate != null && entity.BirthDate.after(todaysDate)) {
            results.add(new ValidationResult("Birth date should not be after the current date.", entity, "US01"));
        }
        if (entity.DeathDate != null && entity.DeathDate.after(todaysDate)) {
            results.add(new ValidationResult("Death date should not be after the current date.", entity, "US01"));
        }
    }

    //US03: Birth before death- Birth should occur before death of an individual
    public static void birthBeforeDeathDateCheck(PersonEntity entity, List<ValidationResult> results) {
        if (entity == null || results == null) {
            return;
        }

        if (entity.DeathDate == null) {
            return;
        }
        if (entity.DeathDate != null && entity.BirthDate != null) {
            if (entity.DeathDate.before(entity.BirthDate)) {
                results.add(new ValidationResult("Birth date " + Utility.DateToString(entity.BirthDate) + " should occur before death date " + Utility.DateToString(entity.DeathDate) + ".", entity, "US03"));
            }
        }
    }
  
    //US07:	@BELLA Less then 150 years old 
    public static void lessthan150YearsOldCheck(PersonEntity entity, List<ValidationResult> results) {
        if (entity == null || results == null)
        	return;
        
        Date todaysDate = Utility.getTodaysDate();
        
        LocalDate birthdate = entity.BirthDate != null ? entity.BirthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;
        LocalDate deathdate = entity.DeathDate != null ? entity.DeathDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;                          
        
        LocalDate todaysDateLocal = Utility.getTodaysDate() != null ? Utility.getTodaysDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null; 
        
        //if birthdate and deathdate exist, must not be over 150 years
        if (entity.BirthDate != null && entity.DeathDate != null) {
        	if (entity.BirthDate.before(todaysDate)) {
        		if (Period.between(birthdate, deathdate).getYears() > 150){
        			results.add(new ValidationResult("Age cannot be over 150.",entity, "US07"));
        			}
        	}
        }
        //if birthday exists and deathdate doesn't exist, use today's date to calculate
        if (entity.BirthDate != null && entity.DeathDate == null) {
        	if (entity.BirthDate.before(todaysDate)) {
        		if (Period.between(birthdate, todaysDateLocal).getYears() > 150){
        			results.add(new ValidationResult("Age cannot be over 150.",entity, "US07"));
        			}
        	}
        }
     }
    // 
    
    //US12: Parents not too old
    // Mother should be less than 60 years older than her children and father should be less than 80 years older than his children
    public static void parentsNotTooOldCheck(FamilyEntity entity, List<ValidationResult> results) {
    	if (entity.ChildrenId != null) {            
    		LocalDate husbandBirthdate = entity.Husband != null ? entity.Husband.BirthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;
    		LocalDate wifeBirthdate = entity.Wife != null ? entity.Wife.BirthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;                          
        
        for(String childId : entity.ChildrenId){                
            PersonEntity child = entity.getGEDCOMData().getIndividuals().get(childId);                       
            LocalDate childBirthdate = child.BirthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            if((husbandBirthdate != null && Period.between(husbandBirthdate, childBirthdate).getYears() > 80) || 
                  (wifeBirthdate != null && Period.between(wifeBirthdate, childBirthdate).getYears() > 60) ){                    
                results.add(new ValidationResult("Mother should be less than 60 years older than her children and father should be less than 80 years older than his children.", entity, "US12"));                    
            }                
        }
    }
}
}