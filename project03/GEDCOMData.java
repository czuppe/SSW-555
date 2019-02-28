/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project03;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 * @author nraj39
 */
public class GEDCOMData {

    private Map<String, PersonEntity> Individuals;
    private Map<String, FamilyEntity> Families;

    private Map<String, PersonEntity> IndividualsDuplicates;
    private Map<String, FamilyEntity> FamiliesDuplicates;

    public Map<String, PersonEntity> getIndividualsDuplicates() {
        return IndividualsDuplicates;
    }

    public Map<String, FamilyEntity> getFamiliesDuplicates() {
        return FamiliesDuplicates;
    }

    public Map<String, PersonEntity> getIndividuals() {
        return Individuals;
    }
	
    public void setIndividuals(Map<String, PersonEntity> individuals){
        this.Individuals = individuals;
    }

    public Map<String, FamilyEntity> getFamilies() {
        return Families;
    }

    public GEDCOMData() {
        Individuals = new HashMap<String, PersonEntity>();
        Families = new HashMap<String, FamilyEntity>();

        IndividualsDuplicates = new HashMap<String, PersonEntity>();
        FamiliesDuplicates = new HashMap<String, FamilyEntity>();
    }

    public void addFamily(FamilyEntity entity) {
        if (entity == null) {
            return;
        }

        entity.setGEDCOMData(this);
        if (Families.containsKey(entity.getId())) {
            FamiliesDuplicates.put(entity.getId(), entity);
        }
        Families.put(entity.getId(), entity);
    }

    public void addIndividual(PersonEntity entity) {
        if (entity == null) {
            return;
        }

        entity.setGEDCOMData(this);
        if (Individuals.containsKey(entity.getId())) {
            IndividualsDuplicates.put(entity.getId(), entity);
        }
        Individuals.put(entity.getId(), entity);
    }

    public void Validate(List<ValidationResult> results) {
        if (results == null) {
            return;
        }

        loadReferenceEntitiesToFamily();

        //GEDCOM Validations
        //GEDCOMDataValidator.maleLastNameCheck(this, results);
        GEDCOMDataValidator.uniqueIDsCheck(this, results);
        GEDCOMDataValidator.uniqueNameAndBirthDateCheck(this, results);

        //Individual validations
        Individuals.forEach((k, entity) -> {
            entity.validate(results);
        });

        //Familty validations
        Families.forEach((k, entity) -> {
            entity.validate(results);
        });
    }

    private void loadReferenceEntitiesToFamily() {
        Families.forEach((k, family) -> {
            family.Husband = Individuals.get(family.HusbandId);
            family.Wife = Individuals.get(family.WifeId);

            for (String childId : family.ChildrenId) {
                PersonEntity child = Individuals.get(childId);
                if (child != null) {
                    family.Children.add(child);
                }
            }
        });

        Individuals.forEach((k, individual) -> {
            Map<String, IEntity> map = new HashMap<String, IEntity>();
            Map<String, FamilyEntity> x = Families.entrySet().stream().filter(fe -> fe.getValue().Husband != null && fe.getValue().Husband.getId().equals(k)).collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));

            Iterator i = x.entrySet().iterator();
            while (i.hasNext()) {
                Map.Entry<String, FamilyEntity> entry = (Map.Entry) i.next();
                map.put(entry.getKey(), IEntity.class.cast(entry.getValue()));
            }

            if (individual.Families == null) {
                Map<String, FamilyEntity> y = Families.entrySet().stream().filter(fe -> fe.getValue().Wife != null && fe.getValue().Wife.getId().equals(k)).collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));

                Iterator j = y.entrySet().iterator();
                while (j.hasNext()) {
                    Map.Entry<String, FamilyEntity> entry = (Map.Entry) j.next();
                    map.put(entry.getKey(), IEntity.class.cast(entry.getValue()));
                }
            }
            individual.Families = new ArrayList(map.values());
        });
    }

    @Override

    public String toString() {
        return null;
    }

    public String toFamiliesText() {
        StringBuilder msg = new StringBuilder();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        msg.append("ID, Married, Divorced, Husband ID, Husband Name, Wife ID, Wife Name, Children\n");
        Families.forEach((k, entity) -> {
            StringBuilder childMsg = new StringBuilder();
            if (entity.Children != null) {
                for (PersonEntity ce : entity.Children) {
                    if (childMsg.length() == 0) {
                        childMsg.append(ce.getId());
                    } else {
                        childMsg.append(", ").append(ce.getId());
                    }
                }
            }
            msg.append(entity.getId() + ", "
                    + (entity.MarriageDate != null ? format.format(entity.MarriageDate) : "NA") + ", "
                    + (entity.DivorceDate != null ? format.format(entity.DivorceDate) : "NA") + ", "
                    + (entity.HusbandId != null ? entity.HusbandId : "NA") + ", "
                    + (entity.Husband != null ? entity.Husband.FullName : "NA") + ", "
                    + (entity.WifeId != null ? entity.WifeId : "NA") + ", "
                    + (entity.Wife != null ? entity.Wife.FullName : "NA") + ", "
                    + (childMsg.length() == 0 ? "NA" : "{" + childMsg.toString() + "}") + "\n"
            );
        });
        return msg.toString();
    }

    public String toPersonsText() {
        StringBuilder msg = new StringBuilder();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        msg.append("ID, Name, Gender, Birthday, Age, Alive, Death, Child, Spouse\n");

        Individuals.forEach((k, entity) -> {
            StringBuilder childMsg = new StringBuilder();
            entity.Families.forEach(fe -> {
                if (fe != null && fe.Children != null && !fe.Children.isEmpty()) {
                    fe.Children.forEach(c -> {
                        if (childMsg.length() == 0) {
                            childMsg.append(c.getId());
                        } else {
                            childMsg.append(", ").append(c.getId());
                        }
                    });
                }
            });

            StringBuilder spouseMsg = new StringBuilder();
            entity.Families.forEach(fe -> {
                if (fe != null) {
                    if (fe.Husband != null && fe.Husband.getId().equals(entity.getId()) && fe.Wife != null) {
                        if (spouseMsg.length() == 0) {
                            spouseMsg.append(fe.Wife.getId());
                        } else {
                            spouseMsg.append(", ").append(fe.Wife.getId());
                        }
                    } else if (fe.Wife != null && fe.Wife.getId().equals(entity.getId()) && fe.Husband != null) {
                        if (spouseMsg.length() == 0) {
                            spouseMsg.append(fe.Husband.getId());
                        } else {
                            spouseMsg.append(", ").append(fe.Husband.getId());
                        }
                    }
                }
            });

            msg.append(entity.getId() + ", " + entity.FullName + ", " + entity.Gender + ", "
                    + format.format(entity.BirthDate) + ", " + entity.Age + ", " + (entity.DeathDate == null ? true : false) + ", "
                    + (entity.DeathDate != null ? format.format(entity.DeathDate) : "NA") + ", "
                    + (childMsg.length() == 0 ? "NA" : "{" + childMsg.toString() + "}") + ", "
                    + (spouseMsg.length() == 0 ? "NA" : "{" + spouseMsg.toString() + "}") + "\n");
        });

        return msg.toString();
    }
}