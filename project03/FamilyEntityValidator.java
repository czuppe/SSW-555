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
            results.add(new ValidationResult("Marriage date should not be after the current date.", entity, "US01"));
        }

        if (entity.Divorce != null && entity.Divorce.Date != null && entity.Divorce.Date.after(todaysDate)) {
            results.add(new ValidationResult("Divorce date should not be after the current date.", entity, "US01"));
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
                results.add(new ValidationResult("Birth should occur before marriage of an individual.", entity, "US02"));
            }
        }

        if (entity.Wife != null && entity.Wife.BirthDate != null) {
            if (entity.Marriage.Date.before(entity.Wife.BirthDate)) {
                results.add(new ValidationResult("Birth should occur before marriage of an individual.", entity, "US02"));
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
                results.add(new ValidationResult("Marriage date must be before Husband's death date.", entity, "US05"));
            }
        }
        if (entity.Wife != null && entity.Wife.DeathDate != null) {
            if (entity.Marriage.Date.after(entity.Wife.DeathDate)) {
                results.add(new ValidationResult("Marriage date must be before Wife's death date.", entity, "US05"));
            }
        }
    }

    //US06: Divorce before death
    public static void divorceBeforeDeathCheck(FamilyEntity entity, List<ValidationResult> results) {
        if (entity == null || results == null)
            return;

        if (entity.Divorce != null && entity.Divorce.Date != null) {
            //check divorce before death of either spouse (husband/wife)

            if ((entity.Husband != null && entity.Husband.DeathDate != null && entity.Husband.DeathDate.before(entity.Divorce.Date))
                    || (entity.Wife != null && entity.Wife.DeathDate != null && entity.Wife.DeathDate.before(entity.Divorce.Date))) {
                results.add(new ValidationResult("Divorce can only occur before death of both spouses.", entity, "US06"));
            }
        }
    }

    //US12: Parents not too old
    // Mother should be less than 60 years older than her children and father should be less than 80 years older than his children
    public static void parentsNotTooOldCheck(FamilyEntity entity, List<ValidationResult> results) {
        if (entity == null || results == null)
            return;

        if (entity.ChildrenId != null) {
            LocalDate husbandBirthdate = entity.Husband != null ? entity.Husband.BirthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;
            LocalDate wifeBirthdate = entity.Wife != null ? entity.Wife.BirthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;

            for (String childId : entity.ChildrenId) {
                PersonEntity child = entity.getGEDCOMData().getIndividuals().get(childId);
                LocalDate childBirthdate = child.BirthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                if ((husbandBirthdate != null && Period.between(husbandBirthdate, childBirthdate).getYears() > 80)
                        || (wifeBirthdate != null && Period.between(wifeBirthdate, childBirthdate).getYears() > 60)) {
                    results.add(new ValidationResult("Mother should be less than 60 years older than her children and father should be less than 80 years older than his children.", entity, "US12"));
                }
            }
        }
    }

    //US15: Fewer than 15 siblings
    //There should be fewer than 15 siblings in a family
    public static void fewerThan15SiblingsCheck(FamilyEntity entity, List<ValidationResult> results) {
        if (entity == null || results == null)
            return;

        if (entity.Children.size() > 15) {
            results.add(new ValidationResult("There should be fewer than 15 siblings in a family.", entity, "US15"));
        }
    }
}
