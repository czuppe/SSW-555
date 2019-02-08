/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project03;

import java.util.*;
import java.text.*;

/**
 *
 * @author nraj39
 */
public class PersonEntity implements IEntity {

    public static PersonEntity create(String[] elements) throws ParseException {
        PersonEntity entity = new PersonEntity();

        entity.Id = elements[0].replace("0 @", "").replace("@ INDI", "").replace("@", "").trim();

        int index = 0;
        for (String var : elements) {
            if (var.contains("SEX")) {
                entity.Gender = var.replace("1 SEX", "").replace("@", "").trim();
            } else if (var.contains("NAME")) {
                entity.FullName = var.replace("1 NAME", "").replace("@", "").trim();

                String[] nameParts = entity.FullName.split(" ");
                entity.FirstName = nameParts[0].replace(".", "").trim();

                for (int i = 1; i < nameParts.length - 1; i++) {
                    entity.MiddleName = "";
                    String firstPart = nameParts[i].contains("/") ? "" : nameParts[i].replace(".", "").trim();
                    String secondPart = entity.MiddleName.length() > 0 ? " " : "";

                    entity.MiddleName = entity.MiddleName + firstPart + secondPart;
                }
                for (String namePart : nameParts) {
                    if (namePart.contains("/")) {
                        entity.SurName = namePart.replace("/", "");
                        break;
                    }
                }
                if (!nameParts[nameParts.length - 1].contains("/")) {
                    entity.Suffix = nameParts[nameParts.length - 1].replace(".", "").trim();
                }
            } else if (var.contains("_PHOTO")) {
                entity.PictureFileId = var.replace("1 _PHOTO ", "").replace("@", "").trim();
            } else if (var.contains("OBJE")) {
                entity.FileIds.add(var.split(" ")[2].replace("@", ""));
            } else if (var.contains("FAMC")) {
                entity.ChildhoodFamilyIds.add(var.replace("1 FAMC", "").replace("@", "").trim());
            } else if (var.contains("FAMS")) {
                entity.MarriedFamilyIds.add(var.replace("1 FAMS", "").replace("@", "").trim());
            } else if (var.contains("1 BIRT")) {
                if (index + 1 < elements.length) {
                    DateFormat format = new SimpleDateFormat("DD MMM yyyy", Locale.ENGLISH);
                    String bdText = elements[index + 1].replace("2 DATE", "").trim();
                    entity.BirthDate = format.parse(bdText);
                    if (bdText.length() > 0) {
                        String[] dateParts = bdText.split(" ");
                        entity.BirthYear = Integer.parseInt(dateParts[dateParts.length - 1]);
                        entity.Age = Calendar.getInstance().get(Calendar.YEAR) - entity.BirthYear;
                    }
                }
            } else if (var.contains("1 DEAT")) {
                if (index + 1 < elements.length) {
                    DateFormat format = new SimpleDateFormat("DD MMM yyyy", Locale.ENGLISH);
                    entity.DeathDate = format.parse(elements[index + 1].replace("2 DATE", "").trim());
                }
            }

            index++;
        }

        return entity;
    }

    public PersonEntity() {
        Facts = new ArrayList();
        ChildhoodFamilyIds = new ArrayList();
        MarriedFamilyIds = new ArrayList();
        FileIds = new ArrayList();
        MiddleName = "";
        Suffix = "";
    }

    private String Id;
    public String SurName;
    public String FirstName;
    public String MiddleName;
    public String Suffix;
    public String FullName;
    public String PictureFileId;
    public String Gender;
    public List<FactEntity> Facts;
    public List<String> ChildhoodFamilyIds;
    public List<String> MarriedFamilyIds;
    public List<String> FileIds;
    public Date BirthDate;
    public Date DeathDate;
    public int BirthYear;
    public int Age;

    public List<FamilyEntity> Families;
    
    @Override
    public String getId() {
        return Id;
    }

    @Override
    public void validate(List<ValidationResult> results) {
        if (results == null) {
            return;
        }

        PersonEntityValidator.datesBeforeCurrentDateCheck(this, results);
        PersonEntityValidator.birthBeforeDeathDateCheck(this, results);
    }
}
