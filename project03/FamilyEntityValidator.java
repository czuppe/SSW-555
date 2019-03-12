/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project03;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author nraj39 bella458
 */
public class FamilyEntityValidator {

    private FamilyEntityValidator() {

    }

    //US01: Dates before current date
    //Dates (birth, marriage, divorce, death) should not be after the current date
    public static void datesBeforeCurrentDateCheck(FamilyEntity entity, List<ValidationResult> results) {
        if (entity == null || results == null) {
            return;
        }

        Date todaysDate = Utility.getTodaysDate();
        if (entity.Marriage != null && entity.Marriage.Date != null && entity.Marriage.Date.after(todaysDate)) {
            results.add(new ValidationResult("Marriage " + Utility.DateToString(entity.Marriage.Date) + " occurs in the future.", entity, "US01"));
        }

        if (entity.Divorce != null && entity.Divorce.Date != null && entity.Divorce.Date.after(todaysDate)) {
            results.add(new ValidationResult("Divorce " + Utility.DateToString(entity.Divorce.Date) + " occurs in the future.", entity, "US01"));
        }
    }

    //US02: Birth before marriage
    //Birth should occur before marriage of an individual
    public static void birthBeforeMarriageCheck(FamilyEntity entity, List<ValidationResult> results) {
        if (entity == null || results == null)
            return;

        if (entity.Marriage == null || entity.Marriage.Date == null) {
            return;
        }

        if (entity.Husband != null && entity.Husband.BirthDate != null) {
            if (entity.Marriage.Date.before(entity.Husband.BirthDate)) {
                results.add(new ValidationResult("Birthday " + Utility.DateToString(entity.Husband.BirthDate) + " should occur before marriage " + Utility.DateToString(entity.Marriage.Date) + ".", entity, "US02"));
            }
        }

        if (entity.Wife != null && entity.Wife.BirthDate != null) {
            if (entity.Marriage.Date.before(entity.Wife.BirthDate)) {
                results.add(new ValidationResult("Birthday " + Utility.DateToString(entity.Wife.BirthDate) + " should occur before marriage " + Utility.DateToString(entity.Marriage.Date) + ".", entity, "US02"));
            }
        }
    }
   
    //US04:	Marriage before divorce 
    public static void marriageBeforeDivorceCheck(FamilyEntity entity, List<ValidationResult> results) {
        if (entity == null || results == null) {
            return;
        }
        if (entity.Marriage == null || entity.Marriage.Date == null) {
            return;
        }
        if (entity.Divorce == null || entity.Divorce.Date == null) {
            return;
        }
        if (entity.Husband != null && entity.Divorce.Date != null) {
            if (entity.Marriage.Date.after(entity.Divorce.Date)) {
                results.add(new ValidationResult("Marriage date " + Utility.DateToString(entity.Marriage.Date) + " must be before Divorce date " + Utility.DateToString(entity.Divorce.Date) + " .", entity, "US04"));
            }
        }
        if (entity.Wife != null && entity.Divorce.Date != null) {
            if (entity.Marriage.Date.after(entity.Divorce.Date)) {
                results.add(new ValidationResult("Marriage date " + Utility.DateToString(entity.Marriage.Date) + " must be before Divorce date " + Utility.DateToString(entity.Divorce.Date) + " .", entity, "US04"));
            }
        }
    }

    //US05: Marriage before death : Marriage should occur before death of either spouse
    public static void marriageBeforeDeathCheck(FamilyEntity entity, List<ValidationResult> results) {
        if (entity == null || results == null)
            return;

        if (entity.Marriage == null || entity.Marriage.Date == null) {
            return;
        }
        
        if (entity.Husband != null && entity.Husband.DeathDate != null) {
        	if (entity.Marriage.Date.after(entity.Husband.DeathDate)) {
        		results.add(new ValidationResult("Marriage date " + Utility.DateToString(entity.Marriage.Date) + " must be before Husband's death date " + Utility.DateToString(entity.Husband.DeathDate) + ".", entity, "US05"));
        	}
        }
        if (entity.Wife != null && entity.Wife.DeathDate != null) {
        	if (entity.Marriage.Date.after(entity.Wife.DeathDate)) {
        		results.add(new ValidationResult("Marriage date " + Utility.DateToString(entity.Marriage.Date) + " must be before Wife's death date " + Utility.DateToString(entity.Wife.DeathDate) + ".", entity, "US05"));
        	}
        } 
    }

    //US06: Divorce before death
    public static void divorceBeforeDeathCheck(FamilyEntity entity, List<ValidationResult> results) {
        if (entity == null || results == null)
            return;

        if (entity.Divorce != null && entity.Divorce.Date != null) {
            //check divorce before death of either spouse (husband/wife)            
            if (entity.Husband != null && entity.Husband.DeathDate != null && entity.Husband.DeathDate.before(entity.Divorce.Date)) {
                results.add(new ValidationResult("Divorce date " 
                        + Utility.DateToString(entity.Divorce.Date)
                        + " occurs after "
                        + Utility.DateToString(entity.Husband.DeathDate) 
                        + " (death of spouse " 
                        + entity.Husband.getId() 
                        + ")." , entity, "US06"));
            }
            if (entity.Wife != null && entity.Wife.DeathDate != null && entity.Wife.DeathDate.before(entity.Divorce.Date)) {
                results.add(new ValidationResult("Divorce date " 
                        + Utility.DateToString(entity.Divorce.Date) 
                        + " occurs after "
                        + Utility.DateToString(entity.Wife.DeathDate) 
                        + " (death of spouse " 
                        + entity.Wife.getId() 
                        + ")." , entity, "US06"));
            }            
        }
    }
    
    //US10: Marriage after fourteen
    public static void marriageAfterFourteen(FamilyEntity family, List<ValidationResult> results) {
        if (family == null || results == null)
            return;
        
        LocalDate marriageDate = family.MarriageDate != null ? Utility.ToLocalDate(family.MarriageDate) : null; 
        
        if(marriageDate == null)
            return;
        
        LocalDate husbandBirthdate = family.Husband != null ? Utility.ToLocalDate(family.Husband.BirthDate) : null;            
        LocalDate wifeBirthdate = family.Wife != null ? Utility.ToLocalDate(family.Wife.BirthDate) : null;
        
        //check wife was at least 14 at time of marriage
        if (wifeBirthdate != null && Utility.YearsBetween(wifeBirthdate, marriageDate) < 14) {
                    results.add(new ValidationResult("Wife " + family.WifeId + " " + family.Wife.FullName + " was only " + Utility.YearsBetween(wifeBirthdate, marriageDate)
                            + " years old at time of marriage.", family, "US10"));
        }
        
        //check husband was at least 14 at time of marriage
        if (husbandBirthdate != null && Utility.YearsBetween(husbandBirthdate, marriageDate) < 14) {
            results.add(new ValidationResult("Husband " + family.HusbandId + " " + family.Husband.FullName + " was only " + Utility.YearsBetween(husbandBirthdate, marriageDate)
                    + " years old at time of marriage.", family, "US10"));
        }
    }
    

    //US12: Parents not too old
    // Mother should be less than 60 years older than her children and father should be less than 80 years older than his children
    public static void parentsNotTooOldCheck(FamilyEntity entity, List<ValidationResult> results) {
        if (entity == null || results == null)
            return;

        if (entity.ChildrenId != null) {
            //LocalDate husbandBirthdate = entity.Husband != null ? entity.Husband.BirthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;
            LocalDate husbandBirthdate = entity.Husband != null ? Utility.ToLocalDate(entity.Husband.BirthDate) : null;            
            LocalDate wifeBirthdate = entity.Wife != null ?Utility.ToLocalDate(entity.Wife.BirthDate) : null;

            for (String childId : entity.ChildrenId) {
                PersonEntity child = entity.getGEDCOMData().getIndividuals().get(childId);
                LocalDate childBirthdate = null;
                
                if(child != null)
                    childBirthdate = child.BirthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                
                if (husbandBirthdate != null && Utility.YearsBetween(husbandBirthdate, childBirthdate) > 80) {
                    results.add(new ValidationResult("Father " + entity.HusbandId + " is " + Utility.YearsBetween(husbandBirthdate, childBirthdate) 
                            + " years older than his child " + child.getId(), entity, "US12"));
                }
                
                if (wifeBirthdate != null && Utility.YearsBetween(wifeBirthdate, childBirthdate) > 60) {
                    results.add(new ValidationResult("Mother " + entity.WifeId + " is " + Utility.YearsBetween(wifeBirthdate, childBirthdate) 
                            + " years older than his child " + child.getId(), entity, "US12"));
                }                
            }
        }
    }
    
    //US14: No more than five siblings should be born at the same time  
    public static void multipleBirthsLessThanOrEqualToFive(FamilyEntity family, List<ValidationResult> results) {
        if (family == null || results == null)
            return;
        
        Map<Date, ArrayList<String>> birthdatesMap = new HashMap<>();
        
        
        //loop over children id list in family entity
        family.ChildrenId.forEach((String childId) -> {
            Date childBirthdate = null;
            if(family.getGEDCOMData().getIndividuals().get(childId) != null){
                childBirthdate = family.getGEDCOMData().getIndividuals().get(childId).BirthDate;
            }                            
            
            //if an entry does exist, create an entry in birthday hashmap with Date as key and childId as value
            if(birthdatesMap.get(childBirthdate) == null){
                ArrayList<String> ids = new ArrayList<>();
                ids.add(childId);
                birthdatesMap.put(childBirthdate, ids);
            }
            else{ //an entry already exists for this child's birthdate, so add child id to the list
                ArrayList<String> ids = birthdatesMap.get(childBirthdate);
                ids.add(childId);
                birthdatesMap.put(childBirthdate, ids);
            }
        });
        
        birthdatesMap.keySet().stream().filter((date) -> (birthdatesMap.get(date).size() > 5)).forEachOrdered((date) -> {
            results.add(new ValidationResult("There are more than five siblings born at the same time: " + String.join(",", birthdatesMap.get(date)), family, "US14"));
        });
        
    }

    //US15: There should be fewer than 15 siblings in a family
    public static void fewerThan15SiblingsCheck(FamilyEntity entity, List<ValidationResult> results) {
        if (entity == null || results == null)
            return;
        if (entity.Children.size() > 15) {
            results.add(new ValidationResult("There should be fewer than 15 siblings in a family.", entity, "US15"));
        }
    }
}
   

    

