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
                            results.add(new ValidationResult("All male members of a family should have the same last name.", child, "US16"));
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
            results.add(new ValidationResult("Error US22: This is a duplicate ID. Please check entry.", v));
        });

        entity.getIndividualsDuplicates().forEach((k, v) -> {
            results.add(new ValidationResult("Error US22: This is a duplicate ID. Please check entry.", v));

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
                    results.add(new ValidationResult("Error US23: Person has same name with birth date.", v));
                } else {
                    set.add(key);
                }
            }
        });
    }
}

