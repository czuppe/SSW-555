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

    public static List<PersonEntity> getIndividuals(){
        return GEDCOMData.Individuals;
    }
    
    public static PersonEntity getPerson(String id){
        for(PersonEntity person : Individuals){
            if(person.getId().equals(id)){
                return person;
            }           
        }
        return null;
    }

    public static List<PersonEntity> Individuals;
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
        Individuals.forEach((pe) -> {
            Map<String, IEntity> map = new HashMap<String, IEntity>();
            Object[] x = Families.stream().filter(fe -> fe.Husband != null && fe.Husband.getId().equals(pe.getId())).toArray();
            for (Object entity : x) {
                map.put(IEntity.class.cast(entity).getId(), IEntity.class.cast(entity));
            }

            if (pe.Families == null) {
                Object[] y = Families.stream().filter(fe -> fe.Wife != null && fe.Wife.getId().equals(pe.getId())).toArray();
                for (Object entity : y) {
                    if (!map.containsKey(IEntity.class.cast(entity).getId())) {
                        map.put(IEntity.class.cast(entity).getId(), IEntity.class.cast(entity));
                    }
                }
            }
            pe.Families = new ArrayList(map.values());
            /*
            if (pe.Family == null) {
                pe.Family = Families.stream().filter(fe -> {
                    if (fe.ChildrenId != null) {
                        return fe.ChildrenId.stream().filter(ce -> ce.equals(pe.getId())).count() > 0;
                    }
                    return true;
                }).findFirst().orElse(null);
            }*/
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
        for (FamilyEntity entity : Families) {
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
        }
        return msg.toString();
    }

    public String toPersonsText() {
        StringBuilder msg = new StringBuilder();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        msg.append("ID, Name, Gender, Birthday, Age, Alive, Death, Child, Spouse\n");
        for (PersonEntity entity : Individuals) {
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
        }
        return msg.toString();
    }
}
