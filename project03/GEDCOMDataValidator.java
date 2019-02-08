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

    //US22: Unique IDs
    public static void uniqueIDsCheck(GEDCOMData entity, List<ValidationResult> results) {
        if (entity == null || results == null)
            return;
    }    

    //US23: Unique name and birth date
    public static void uniqueNameAndBirthDateCheck(GEDCOMData entity, List<ValidationResult> results) {
        if (entity == null || results == null)
            return;
    }    
}
