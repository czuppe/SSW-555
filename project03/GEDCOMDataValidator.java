/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project03;

import java.util.*;

/**
 *
 * @author nraj39
 */
public class GEDCOMDataValidator {

    private GEDCOMDataValidator() {

    }

    //US01: Dates before current date
    //Dates (birth, marriage, divorce, death) should not be after the current date
    public static void datesBeforeCurrentDateCheck(GEDCOMData entity, List<ValidationResult> results) {
        if (entity == null || results == null) {
            return;
        }
        entity.getIndividuals().forEach((k, v) -> {
            PersonEntityValidator.datesBeforeCurrentDateCheck(v, results);
        });
        entity.getFamilies().forEach((k, v) -> {
            FamilyEntityValidator.datesBeforeCurrentDateCheck(v, results);
        });
    }

    //US02: Birth before marriage
    //Birth should occur before marriage of an individual
    public static void birthBeforeMarriageCheck(GEDCOMData entity, List<ValidationResult> results) {
        if (entity == null || results == null) {
            return;
        }
        entity.getFamilies().forEach((k, v) -> {
            FamilyEntityValidator.birthBeforeMarriageCheck(v, results);
        });
    }

    //US15: There should be fewer than 15 siblings in a family
    public static void fewerThan15SiblingsCheck(GEDCOMData entity, List<ValidationResult> results) {
        if (entity == null || results == null) {
            return;
        }
        entity.getFamilies().forEach((k, v) -> {
            FamilyEntityValidator.fewerThan15SiblingsCheck(v, results);
        });
    }

    //US16: Male last names
    //All male members of a family should have the same last name
    public static void maleLastNameCheck(GEDCOMData entity, List<ValidationResult> results) {
        if (entity == null || results == null) {
            return;
        }

        entity.getFamilies().forEach((String k, FamilyEntity v) -> {
            if (v.Husband != null && v.Children != null) {
                v.Children.forEach((PersonEntity child) -> {
                    if (child != null && "M".equals(child.Gender)) {
                        if (!v.Husband.SurName.equals(child.SurName)) {
                            results.add(new ValidationResult("Male child `" + child.FullName + "` surname should have the same family `" + v.Husband.SurName + "` last name.", child, "US16"));
                        }
                    }
                });
            }
        });
    }

    // US22: Unique IDs
    public static void uniqueIDsCheck(GEDCOMData entity, List<ValidationResult> results) {
        if (entity == null || results == null) {
            return;
        }

        entity.getFamiliesDuplicates().forEach((k, v) -> {
            results.add(new ValidationResult("This is a duplicate ID. Please check entry.", v, "US22"));
        });

        entity.getIndividualsDuplicates().forEach((k, v) -> {
            results.add(new ValidationResult("This is a duplicate ID. Please check entry.", v, "US22"));

        });

    }

    // US23: Unique name and birth date
    public static void uniqueNameAndBirthDateCheck(GEDCOMData entity, List<ValidationResult> results) {
        if (entity == null || results == null) {
            return;
        }

        Set<String> set = new HashSet<String>();//Hold non-duplicate items
        entity.getIndividuals().forEach((k, v) -> {
            if (v.BirthDate != null) {
                String key = v.FullName + v.BirthDate.toString();
                if (set.contains(key)) {
                    results.add(new ValidationResult("Person has same name with birth date.", v, "US23"));
                } else {
                    set.add(key);
                }
            }
        });
    }
}
