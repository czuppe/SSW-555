/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project03;

import java.text.*;
import java.util.*;

/**
 *
 * @author nraj39
 */
public class FamilyEntity implements IEntity {

    public static FamilyEntity create(String[] elements) throws ParseException {
        FamilyEntity entity = new FamilyEntity();

        entity.Id = elements[0].replace("0 @", "").replace("@ FAM", "").replace("@", "").trim();

        int index = 0;
        for (String var : elements) {
            if (var.contains("HUSB")) {
                entity.HusbandId = var.replace("1 HUSB", "").replace("@", "").trim();
            } else if (var.contains("WIFE")) {
                entity.WifeId = var.replace("1 WIFE", "").replace("@", "").trim();
            } else if (var.contains("CHIL")) {
                entity.ChildrenId.add(var.replace("1 CHIL ", "").replace("@", "").trim());
            } else if (var.contains("1 MARR")) {
                if (index + 1 < elements.length) {
                    DateFormat format = new SimpleDateFormat("DD MMM yyyy", Locale.ENGLISH);
                    FactEntity marriageFact = new FactEntity();
                    entity.MarriageDate = marriageFact.Date = format.parse(elements[index + 1].replace("2 DATE", "").trim());
                    marriageFact.Location = elements[index + 2].replace("2 PLAC", "").trim();
                    entity.Marriage = marriageFact;
                }
            } else if (var.contains("1 DIV")) {
                if (index + 1 < elements.length) {
                    DateFormat format = new SimpleDateFormat("DD MMM yyyy", Locale.ENGLISH);
                    FactEntity divorceFact = new FactEntity();
                    entity.DivorceDate = divorceFact.Date = format.parse(elements[index + 1].replace("2 DATE", "").trim());
                    divorceFact.Location = elements[index + 2].replace("2 PLAC", "").trim();
                    entity.Divorce = divorceFact;
                }
            }

            index++;
        }

        return entity;
    }

    public FamilyEntity() {
        ChildrenId = new ArrayList();
        Children = new ArrayList();
    }

    private String Id;
    public String HusbandId;
    public String WifeId;
    public List<String> ChildrenId;
    public FactEntity Marriage;
    public FactEntity Divorce;
    public Date MarriageDate;
    public Date DivorceDate;

    public PersonEntity Husband;
    public PersonEntity Wife;
    public List<PersonEntity> Children;

    @Override
    public String getId() {
        return Id;
    }

    private GEDCOMData GEDCOMDataObj;
    @Override
    public GEDCOMData getGEDCOMData() {
        return GEDCOMDataObj;
    }

    @Override
    public void setGEDCOMData(GEDCOMData value) {
        GEDCOMDataObj = value;
    }

    @Override
    public void validate(List<ValidationResult> results) {
        if (results == null) {
            return;
        }
        FamilyEntityValidator.datesBeforeCurrentDateCheck(this, results);
        FamilyEntityValidator.birthBeforeMarriageCheck(this, results);
        FamilyEntityValidator.divorceBeforeDeathCheck(this, results);
        FamilyEntityValidator.parentsNotTooOldCheck(this, results);
        FamilyEntityValidator.marriageBeforeDeathCheck(this, results);
        FamilyEntityValidator.fewerThan15SiblingsCheck(this, results);
        FamilyEntityValidator.marriageBeforeDivorceCheck(this, results);
        FamilyEntityValidator.birthBeforeMarriageParents(this, results);
        FamilyEntityValidator.birthBeforeDeathParents(this, results);
    }

    @Override
    public String getEntityName() {
        return "FAMILY";
    }
}
    