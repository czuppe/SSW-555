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
public class GEDCOMData {

    public List<PersonEntity> Individuals;
    public List<FamilyEntity> Families;

    public GEDCOMData() {
        Individuals = new ArrayList<PersonEntity>();
        Families = new ArrayList<FamilyEntity>();
    }

    public void Validate(List<ValidationResult> results) {
        if (results == null) {
            return;
        }

        loadReferenceEntitiesToFamily();

        GEDCOMDataValidator.uniqueIDsCheck(this, results);
        GEDCOMDataValidator.uniqueNameAndBirthDateCheck(this, results);

        Individuals.forEach((entity) -> {
            entity.validate(results);
        });

        Families.forEach((entity) -> {
            entity.validate(results);
        });
    }

    private void loadReferenceEntitiesToFamily() {
        Families.forEach((fe) -> {
            fe.Husband = Individuals.stream().filter(pe -> pe.getId().equals(fe.HusbandId)).findFirst().orElse(null);
            fe.Wife = Individuals.stream().filter(pe -> pe.getId().equals(fe.WifeId)).findFirst().orElse(null);

            for (String childId : fe.ChildrenId) {
                PersonEntity ce = Individuals.stream().filter(pe -> pe.getId().equals(childId)).findFirst().orElse(null);
                if (ce != null) {
                    fe.Children.add(ce);
                }
            }
        });
    }
}
